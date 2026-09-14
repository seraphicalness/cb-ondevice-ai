package io.cbai.ondevice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 관제 클라이언트 채널(/ws/monitor).
 * 연결된 모든 관제 세션에게 경보 이벤트/프레임을 브로드캐스트한다.
 */
@Component
public class MonitorHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    /**
     * 임의 payload 를 JSON 으로 직렬화해 모든 관제 세션에 전송.
     * type 필드로 프론트가 frame/alert 를 구분한다.
     */
    public void broadcast(Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            TextMessage message = new TextMessage(json);
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(message);
                }
            }
        } catch (IOException e) {
            // TODO: 로깅 프레임워크로 교체
            System.err.println("[MonitorHandler] broadcast 실패: " + e.getMessage());
        }
    }
}
