package io.cbai.ondevice.domain;

/**
 * COCO 17 keypoint 표준 관절 정의 (index 순서 고정).
 * 온디바이스 단말에서 이 순서대로 좌표를 추출/전송한다.
 */
public enum CocoKeypoint {
    NOSE,            // 0
    LEFT_EYE,        // 1
    RIGHT_EYE,       // 2
    LEFT_EAR,        // 3
    RIGHT_EAR,       // 4
    LEFT_SHOULDER,   // 5
    RIGHT_SHOULDER,  // 6
    LEFT_ELBOW,      // 7
    RIGHT_ELBOW,     // 8
    LEFT_WRIST,      // 9
    RIGHT_WRIST,     // 10
    LEFT_HIP,        // 11
    RIGHT_HIP,       // 12
    LEFT_KNEE,       // 13
    RIGHT_KNEE,      // 14
    LEFT_ANKLE,      // 15
    RIGHT_ANKLE;     // 16

    public int index() {
        return ordinal();
    }
}
