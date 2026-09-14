package io.cbai.ondevice.dto;

import io.cbai.ondevice.domain.RegionOfInterest;

import java.util.List;
import java.util.UUID;

/**
 * 웹에서 그린 ROI 저장 요청.
 * polygon: [[x,y], [x,y], ...] 다각형 꼭짓점.
 */
public record RoiRequest(
        String name,
        String type,          // "DANGER" | "WATCH"
        List<double[]> polygon
) {
    public RegionOfInterest toDomain() {
        return new RegionOfInterest(
                UUID.randomUUID().toString(),
                name,
                RegionOfInterest.RoiType.valueOf(type),
                polygon
        );
    }
}
