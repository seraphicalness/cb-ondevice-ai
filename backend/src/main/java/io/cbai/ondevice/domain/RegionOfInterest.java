package io.cbai.ondevice.domain;

import java.util.List;

/**
 * 동적 ROI(관심/위험 구역). 웹에서 마우스 드래그로 그린 다각형.
 *
 * @param id      ROI 식별자
 * @param name    구역 이름 (예: "계단 앞", "창가")
 * @param type    구역 유형 (위험/관심)
 * @param polygon 다각형 꼭짓점 목록 (시계 or 반시계 순서)
 */
public record RegionOfInterest(
        String id,
        String name,
        RoiType type,
        List<double[]> polygon
) {
    public enum RoiType { DANGER, WATCH }

    /**
     * 점(px, py)이 이 다각형 내부에 있는지 (Ray Casting 알고리즘).
     */
    public boolean contains(double px, double py) {
        boolean inside = false;
        int n = polygon.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = polygon.get(i)[0], yi = polygon.get(i)[1];
            double xj = polygon.get(j)[0], yj = polygon.get(j)[1];
            boolean intersect = ((yi > py) != (yj > py))
                    && (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
            if (intersect) inside = !inside;
        }
        return inside;
    }
}
