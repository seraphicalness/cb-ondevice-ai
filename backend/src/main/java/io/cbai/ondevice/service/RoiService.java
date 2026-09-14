package io.cbai.ondevice.service;

import io.cbai.ondevice.domain.RegionOfInterest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ROI 저장소. 웹에서 그린 구역을 카메라별로 보관한다.
 * TODO: 현재는 인메모리. 영속화 필요 시 JPA Repository 로 교체.
 */
@Service
public class RoiService {

    // cameraId -> ROI 목록
    private final ConcurrentHashMap<String, List<RegionOfInterest>> store = new ConcurrentHashMap<>();

    public List<RegionOfInterest> findByCamera(String cameraId) {
        return store.getOrDefault(cameraId, List.of());
    }

    public void save(String cameraId, RegionOfInterest roi) {
        store.computeIfAbsent(cameraId, k -> new CopyOnWriteArrayList<>()).add(roi);
    }

    public void deleteAll(String cameraId) {
        store.remove(cameraId);
    }
}
