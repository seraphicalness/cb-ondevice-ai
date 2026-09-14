package io.cbai.ondevice.service;

import io.cbai.ondevice.domain.AlertEvent;
import io.cbai.ondevice.domain.RegionOfInterest;
import io.cbai.ondevice.domain.SkeletonFrame;
import io.cbai.ondevice.engine.FallDetector;
import io.cbai.ondevice.engine.ReplayRingBuffer;
import io.cbai.ondevice.engine.RoiStateMachine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 복합 위험 판정 엔진 (오케스트레이터).
 * 온디바이스 단말에서 들어온 스켈레톤 프레임을 받아
 *  1) 링버퍼 적재(리플레이용)
 *  2) 낙상 판정
 *  3) 동적 ROI 침범 판정
 * 을 수행하고 경보 이벤트 목록을 반환한다.
 *
 * 카메라별로 감지기 상태를 분리 관리한다.
 */
@Service
public class RiskEngineService {

    private final RoiService roiService;

    private final double fallAccelThreshold;
    private final int fallMinFrames;
    private final int roiDwellFrames;
    private final int replayBufferFrames;

    // cameraId -> 감지기 상태
    private final ConcurrentHashMap<String, FallDetector> fallDetectors = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RoiStateMachine> roiMachines = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReplayRingBuffer> replayBuffers = new ConcurrentHashMap<>();

    public RiskEngineService(
            RoiService roiService,
            @Value("${risk.fall.vertical-accel-threshold}") double fallAccelThreshold,
            @Value("${risk.fall.min-consecutive-frames}") int fallMinFrames,
            @Value("${risk.roi.dwell-frames}") int roiDwellFrames,
            @Value("${risk.replay.buffer-frames}") int replayBufferFrames
    ) {
        this.roiService = roiService;
        this.fallAccelThreshold = fallAccelThreshold;
        this.fallMinFrames = fallMinFrames;
        this.roiDwellFrames = roiDwellFrames;
        this.replayBufferFrames = replayBufferFrames;
    }

    /**
     * 프레임 1개를 처리하고 발생한 경보 이벤트들을 반환.
     */
    public List<AlertEvent> process(SkeletonFrame frame) {
        String cam = frame.cameraId();
        List<AlertEvent> events = new ArrayList<>();

        // 1) 리플레이 버퍼 적재
        replayBuffers
                .computeIfAbsent(cam, k -> new ReplayRingBuffer(replayBufferFrames))
                .push(frame);

        // 2) 낙상 판정
        FallDetector fall = fallDetectors.computeIfAbsent(
                cam, k -> new FallDetector(fallAccelThreshold, fallMinFrames));
        if (fall.accept(frame)) {
            events.add(AlertEvent.fall(cam, frame.frameId()));
        }

        // 3) ROI 침범 판정
        RoiStateMachine roiMachine = roiMachines.computeIfAbsent(
                cam, k -> new RoiStateMachine(roiDwellFrames));
        List<RegionOfInterest> rois = roiService.findByCamera(cam);
        for (RegionOfInterest intruded : roiMachine.accept(frame, rois)) {
            events.add(AlertEvent.roiIntrusion(cam, frame.frameId(), intruded.name()));
        }

        return events;
    }

    /** 사고 리플레이 스냅샷 (전후 5초 스켈레톤 궤적). */
    public List<SkeletonFrame> replaySnapshot(String cameraId) {
        ReplayRingBuffer buf = replayBuffers.get(cameraId);
        return buf == null ? List.of() : buf.snapshot();
    }
}
