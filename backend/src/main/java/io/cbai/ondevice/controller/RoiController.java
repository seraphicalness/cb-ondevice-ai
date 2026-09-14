package io.cbai.ondevice.controller;

import io.cbai.ondevice.domain.RegionOfInterest;
import io.cbai.ondevice.dto.RoiRequest;
import io.cbai.ondevice.service.RoiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 동적 ROI 관리 API. 관제 웹에서 마우스로 그린 위험/관심 구역을 CRUD 한다.
 */
@RestController
@RequestMapping("/api/cameras/{cameraId}/rois")
@CrossOrigin(origins = "*")
public class RoiController {

    private final RoiService roiService;

    public RoiController(RoiService roiService) {
        this.roiService = roiService;
    }

    @GetMapping
    public List<RegionOfInterest> list(@PathVariable String cameraId) {
        return roiService.findByCamera(cameraId);
    }

    @PostMapping
    public RegionOfInterest create(@PathVariable String cameraId, @RequestBody RoiRequest request) {
        RegionOfInterest roi = request.toDomain();
        roiService.save(cameraId, roi);
        return roi;
    }

    @DeleteMapping
    public void clear(@PathVariable String cameraId) {
        roiService.deleteAll(cameraId);
    }
}
