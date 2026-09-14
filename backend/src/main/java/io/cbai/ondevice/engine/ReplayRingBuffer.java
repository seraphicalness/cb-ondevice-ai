package io.cbai.ondevice.engine;

import io.cbai.ondevice.domain.SkeletonFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * 사고 전후 리플레이용 링버퍼.
 * 최근 N개 프레임을 순환 저장하여, 이상 감지 순간 즉시 스켈레톤 궤적을 재구성한다.
 * (5초 * 30fps = 150 프레임 기본)
 *
 * 주의: 단일 카메라 기준. 다중 카메라면 cameraId 별로 인스턴스를 둔다.
 */
public class ReplayRingBuffer {

    private final SkeletonFrame[] buffer;
    private int head = 0;      // 다음 쓸 위치
    private int size = 0;      // 현재 채워진 개수

    public ReplayRingBuffer(int capacity) {
        this.buffer = new SkeletonFrame[capacity];
    }

    public synchronized void push(SkeletonFrame frame) {
        buffer[head] = frame;
        head = (head + 1) % buffer.length;
        if (size < buffer.length) size++;
    }

    /**
     * 오래된 → 최신 순서로 정렬된 스냅샷 반환 (사고 리플레이용).
     */
    public synchronized List<SkeletonFrame> snapshot() {
        List<SkeletonFrame> result = new ArrayList<>(size);
        int start = (head - size + buffer.length) % buffer.length;
        for (int i = 0; i < size; i++) {
            result.add(buffer[(start + i) % buffer.length]);
        }
        return result;
    }
}
