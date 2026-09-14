package io.cbai.ondevice.config;

import io.cbai.ondevice.websocket.IngestHandler;
import io.cbai.ondevice.websocket.MonitorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 두 개의 WebSocket 채널을 등록한다.
 *  - /ws/ingest  : 온디바이스 단말(시뮬레이터)이 스켈레톤 프레임을 밀어넣는 채널
 *  - /ws/monitor : 관제 웹 클라이언트가 경보/프레임을 수신하는 채널
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final IngestHandler ingestHandler;
    private final MonitorHandler monitorHandler;

    public WebSocketConfig(IngestHandler ingestHandler, MonitorHandler monitorHandler) {
        this.ingestHandler = ingestHandler;
        this.monitorHandler = monitorHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ingestHandler, "/ws/ingest").setAllowedOrigins("*");
        registry.addHandler(monitorHandler, "/ws/monitor").setAllowedOrigins("*");
    }
}
