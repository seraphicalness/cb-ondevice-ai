package io.cbai.ondevice.domain;

/**
 * 3단계 경보 체계. 관제 클라이언트의 팝업/알림음 강도를 결정한다.
 */
public enum AlertLevel {
    NONE,     // 정상
    CAUTION,  // 주의 — 관심 구역 진입 등
    WARNING,  // 경고 — 위험 구역 침범 지속 등
    DANGER    // 위험 — 낙상/질식 등 즉시 대응 필요
}
