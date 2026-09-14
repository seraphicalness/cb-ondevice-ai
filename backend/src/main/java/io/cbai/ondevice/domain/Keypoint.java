package io.cbai.ondevice.domain;

/**
 * 단일 관절 좌표 + 신뢰도.
 * @param x          정규화 또는 픽셀 x 좌표
 * @param y          정규화 또는 픽셀 y 좌표
 * @param confidence 관절 검출 신뢰도 (0.0 ~ 1.0)
 */
public record Keypoint(double x, double y, double confidence) {

    /** 신뢰도가 임계값 이상인 유효한 관절인지 */
    public boolean isValid(double minConfidence) {
        return confidence >= minConfidence;
    }
}
