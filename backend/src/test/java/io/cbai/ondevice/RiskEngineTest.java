package io.cbai.ondevice;

import io.cbai.ondevice.domain.RegionOfInterest;
import io.cbai.ondevice.engine.FallDetector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiskEngineTest {

    @Test
    void roi_다각형_내부_판정() {
        // 사각형 (0,0)-(10,0)-(10,10)-(0,10)
        RegionOfInterest roi = new RegionOfInterest(
                "r1", "테스트구역", RegionOfInterest.RoiType.DANGER,
                List.of(new double[]{0, 0}, new double[]{10, 0},
                        new double[]{10, 10}, new double[]{0, 10}));

        assertTrue(roi.contains(5, 5), "내부 점은 true");
        assertFalse(roi.contains(15, 5), "외부 점은 false");
    }

    @Test
    void 낙상_수직가속도_임계초과시_감지() {
        // 임계값 5.0, 연속 1프레임이면 감지
        FallDetector detector = new FallDetector(5.0, 1);

        // y가 급강하: 0 -> 1 -> 10 (accel = 10 - 2*1 + 0 = 8 >= 5)
        assertFalse(detector.accept(frameWithHipY(0)));
        assertFalse(detector.accept(frameWithHipY(1)));
        assertTrue(detector.accept(frameWithHipY(10)), "급강하 시 낙상 감지");
    }

    private io.cbai.ondevice.domain.SkeletonFrame frameWithHipY(double hipY) {
        // 17개 관절 중 hip(11,12)만 의미있게 채우고 나머지는 0
        io.cbai.ondevice.domain.Keypoint[] kps = new io.cbai.ondevice.domain.Keypoint[17];
        for (int i = 0; i < 17; i++) kps[i] = new io.cbai.ondevice.domain.Keypoint(0, 0, 1);
        kps[11] = new io.cbai.ondevice.domain.Keypoint(5, hipY, 1); // LEFT_HIP
        kps[12] = new io.cbai.ondevice.domain.Keypoint(5, hipY, 1); // RIGHT_HIP
        return new io.cbai.ondevice.domain.SkeletonFrame("cam", 0, 0, List.of(kps));
    }
}
