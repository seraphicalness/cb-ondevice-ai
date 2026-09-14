package io.cbai.ondevice.engine;

import io.cbai.ondevice.domain.Keypoint;
import io.cbai.ondevice.domain.RegionOfInterest;
import io.cbai.ondevice.domain.SkeletonFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동적 ROI 침범 상태머신.
 * 대상(골반 중심)이 ROI 안에 dwellFrames 이상 머무르면 '침범(INTRUDING)'으로 확정.
 * 순간적으로 스치는 것은 무시한다.
 *
 * 상태 전이: OUTSIDE → ENTERING(누적) → INTRUDING → (이탈 시) OUTSIDE
 */
public class RoiStateMachine {

    public enum State { OUTSIDE, ENTERING, INTRUDING }

    private final int dwellFrames;
    // ROI id -> 현재 상태 / 체류 카운트
    private final Map<String, State> states = new HashMap<>();
    private final Map<String, Integer> dwellCount = new HashMap<>();

    public RoiStateMachine(int dwellFrames) {
        this.dwellFrames = dwellFrames;
    }

    /**
     * 프레임을 흘려넣고, 이번 프레임에서 '새로 침범 확정된' ROI 목록을 반환.
     */
    public List<RegionOfInterest> accept(SkeletonFrame frame, List<RegionOfInterest> rois) {
        Keypoint hip = frame.hipCenter();
        java.util.List<RegionOfInterest> newlyIntruding = new java.util.ArrayList<>();

        for (RegionOfInterest roi : rois) {
            boolean inside = roi.contains(hip.x(), hip.y());
            State prev = states.getOrDefault(roi.id(), State.OUTSIDE);

            if (inside) {
                int count = dwellCount.merge(roi.id(), 1, Integer::sum);
                if (count >= dwellFrames && prev != State.INTRUDING) {
                    states.put(roi.id(), State.INTRUDING);
                    newlyIntruding.add(roi);
                } else if (prev == State.OUTSIDE) {
                    states.put(roi.id(), State.ENTERING);
                }
            } else {
                dwellCount.put(roi.id(), 0);
                states.put(roi.id(), State.OUTSIDE);
            }
        }
        return newlyIntruding;
    }

    public State stateOf(String roiId) {
        return states.getOrDefault(roiId, State.OUTSIDE);
    }
}
