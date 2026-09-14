package io.cbai.ondevice.domain;

/**
 * 위험 판정 엔진이 생성하는 경보 이벤트. WebSocket 으로 관제 클라이언트에 Push.
 *
 * @param cameraId  발생 카메라
 * @param level     경보 단계
 * @param type      이벤트 유형 (예: FALL, ROI_INTRUSION, SUFFOCATION)
 * @param message   관제자용 설명
 * @param frameId   이벤트가 감지된 프레임
 * @param timestamp 서버 판정 시각 (epoch ms)
 */
public record AlertEvent(
        String cameraId,
        AlertLevel level,
        String type,
        String message,
        long frameId,
        long timestamp
) {
    public static AlertEvent fall(String cameraId, long frameId) {
        return new AlertEvent(cameraId, AlertLevel.DANGER, "FALL",
                "낙상 감지: 수직 가속도 임계 초과", frameId, System.currentTimeMillis());
    }

    public static AlertEvent roiIntrusion(String cameraId, long frameId, String roiName) {
        return new AlertEvent(cameraId, AlertLevel.WARNING, "ROI_INTRUSION",
                "위험 구역 침범: " + roiName, frameId, System.currentTimeMillis());
    }
}
