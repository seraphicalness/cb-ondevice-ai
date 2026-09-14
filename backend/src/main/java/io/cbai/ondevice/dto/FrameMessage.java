package io.cbai.ondevice.dto;

import io.cbai.ondevice.domain.Keypoint;
import io.cbai.ondevice.domain.SkeletonFrame;

import java.util.List;

/**
 * 온디바이스 단말이 /ws/ingest 로 보내는 프레임 메시지(경량 JSON).
 * keypoints 는 [x, y, confidence] 배열 17개로 전송된다.
 *
 * 예:
 * {
 *   "cameraId": "cam-01",
 *   "frameId": 42,
 *   "timestamp": 1726300000000,
 *   "keypoints": [[0.5,0.2,0.9], ... 총 17개]
 * }
 */
public record FrameMessage(
        String cameraId,
        long frameId,
        long timestamp,
        List<double[]> keypoints
) {
    public SkeletonFrame toDomain() {
        List<Keypoint> kps = keypoints.stream()
                .map(a -> new Keypoint(a[0], a[1], a.length > 2 ? a[2] : 1.0))
                .toList();
        return new SkeletonFrame(cameraId, frameId, timestamp, kps);
    }
}
