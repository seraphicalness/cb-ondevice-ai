package io.cbai.ondevice.engine;

import io.cbai.ondevice.domain.Keypoint;
import io.cbai.ondevice.domain.SkeletonFrame;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 낙상 감지: 골반(hip) 중심의 수직 가속도를 계산해 임계값 초과가
 * 연속으로 발생하면 낙상으로 판정한다.
 *
 * 가속도 근사: a(t) ≈ y(t) - 2*y(t-1) + y(t-2)  (이산 2차 미분)
 */
public class FallDetector {

    private final double accelThreshold;
    private final int minConsecutiveFrames;

    // 최근 골반 y좌표 3개를 보관 (2차 미분 계산용)
    private final Deque<Double> recentHipY = new ArrayDeque<>(3);
    private int consecutive = 0;

    public FallDetector(double accelThreshold, int minConsecutiveFrames) {
        this.accelThreshold = accelThreshold;
        this.minConsecutiveFrames = minConsecutiveFrames;
    }

    /**
     * 프레임을 흘려넣고 낙상 여부를 반환.
     * @return true 면 이번 프레임에서 낙상 확정
     */
    public boolean accept(SkeletonFrame frame) {
        Keypoint hip = frame.hipCenter();
        recentHipY.addLast(hip.y());
        if (recentHipY.size() > 3) {
            recentHipY.removeFirst();
        }
        if (recentHipY.size() < 3) {
            return false;
        }

        Double[] y = recentHipY.toArray(new Double[0]);
        double accel = y[2] - 2 * y[1] + y[0]; // 아래로 급강하하면 양수(화면 y는 아래로 증가)

        if (accel >= accelThreshold) {
            consecutive++;
        } else {
            consecutive = 0;
        }
        return consecutive >= minConsecutiveFrames;
    }

    public void reset() {
        recentHipY.clear();
        consecutive = 0;
    }

    // TODO: 질식(suffocation) 판정 — 장시간 미동 + 특정 자세 패턴 분석 로직 추가
}
