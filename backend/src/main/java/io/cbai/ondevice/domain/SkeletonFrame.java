package io.cbai.ondevice.domain;

import java.util.List;

/**
 * 한 프레임의 스켈레톤 데이터. 온디바이스 단말이 보내는 최소 단위.
 *
 * @param cameraId  카메라(현장 단말) 식별자
 * @param frameId   프레임 시퀀스 번호
 * @param timestamp 단말 기준 밀리초 타임스탬프
 * @param keypoints COCO 17개 관절 (index 순서 = {@link CocoKeypoint} 순서)
 */
public record SkeletonFrame(
        String cameraId,
        long frameId,
        long timestamp,
        List<Keypoint> keypoints
) {
    public Keypoint keypoint(CocoKeypoint kp) {
        return keypoints.get(kp.index());
    }

    /** 골반 중심점(두 hip의 평균). 낙상 판정의 기준점으로 사용. */
    public Keypoint hipCenter() {
        Keypoint left = keypoint(CocoKeypoint.LEFT_HIP);
        Keypoint right = keypoint(CocoKeypoint.RIGHT_HIP);
        return new Keypoint(
                (left.x() + right.x()) / 2.0,
                (left.y() + right.y()) / 2.0,
                Math.min(left.confidence(), right.confidence())
        );
    }
}
