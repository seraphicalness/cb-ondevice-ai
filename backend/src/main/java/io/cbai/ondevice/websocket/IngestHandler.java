package io.cbai.ondevice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cbai.ondevice.domain.AlertEvent;
import io.cbai.ondevice.domain.SkeletonFrame;
import io.cbai.ondevice.dto.FrameMessage;
import io.cbai.ondevice.service.RiskEngineService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;

/**
 * 온디바이스 단말 수집 채널(/ws/ingest).
 * 프레임 수신 → 위험 판정 엔진 통과 → 관제 클라이언트로 프레임/경보 브로드캐스트.
 */
@Component
public class IngestHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RiskEngineService riskEngine;
    private final MonitorHandler monitorHandler;

    public IngestHandler(RiskEngineService riskEngine, MonitorHandler monitorHandler) {
        this.riskEngine = riskEngine;
        this.monitorHandler = monitorHandler;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        FrameMessage msg = objectMapper.readValue(message.getPayload(), FrameMessage.class);
        SkeletonFrame frame = msg.toDomain();

        // 1) 스켈레톤 프레임을 관제 화면으로 실시간 전달 (시각화용)
        monitorHandler.broadcast(Map.of("type", "frame", "data", msg));

        // 2) 위험 판정 → 경보가 있으면 즉시 Push
        List<AlertEvent> alerts = riskEngine.process(frame);
        for (AlertEvent alert : alerts) {
            monitorHandler.broadcast(Map.of("type", "alert", "data", alert));
        }
    }
}
