package io.cbai.ondevice.controller;

import io.cbai.ondevice.domain.SkeletonFrame;
import io.cbai.ondevice.service.RiskEngineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사고 리플레이 API. 이상 감지 순간의 전후 5초 스켈레톤 궤적을 반환한다.
 */
@RestController
@RequestMapping("/api/cameras/{cameraId}/replay")
@CrossOrigin(origins = "*")
public class ReplayController {

    private final RiskEngineService riskEngine;

    public ReplayController(RiskEngineService riskEngine) {
        this.riskEngine = riskEngine;
    }

    @GetMapping
    public List<SkeletonFrame> replay(@PathVariable String cameraId) {
        return riskEngine.replaySnapshot(cameraId);
    }
}
