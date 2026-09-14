# backend — 위험 판정 엔진 (Spring Boot)

## 역할
온디바이스 단말에서 들어온 COCO 17 관절 프레임을 받아 위험을 판정하고,
관제 클라이언트에 경보/프레임을 실시간 Push 한다.

## 구조
```
io.cbai.ondevice
├── config/       WebSocketConfig — /ws/ingest, /ws/monitor 등록
├── websocket/
│   ├── IngestHandler   단말 프레임 수신 → 엔진 통과 → 브로드캐스트
│   └── MonitorHandler  관제 세션 관리 + 브로드캐스트
├── engine/
│   ├── FallDetector      낙상(수직 가속도) 판정
│   ├── RoiStateMachine   동적 ROI 침범 상태머신
│   └── ReplayRingBuffer  5초 리플레이 링버퍼
├── service/
│   ├── RiskEngineService 복합 판정 오케스트레이터
│   └── RoiService        ROI 저장소(인메모리)
├── controller/   RoiController(ROI CRUD), ReplayController(리플레이)
├── domain/       CocoKeypoint, Keypoint, SkeletonFrame, RegionOfInterest, AlertLevel, AlertEvent
└── dto/          FrameMessage, RoiRequest
```

## 실행
```bash
# gradle wrapper 가 없다면 최초 1회 생성 (시스템에 gradle 필요)
gradle wrapper --gradle-version 8.8

./gradlew bootRun     # http://localhost:8080
./gradlew test        # 단위 테스트
```

## WebSocket 채널
- `ws://localhost:8080/ws/ingest`  — 시뮬레이터가 프레임을 밀어넣음
- `ws://localhost:8080/ws/monitor` — 관제 웹이 구독 (frame/alert 수신)

## REST API
| Method | Path | 설명 |
|--------|------|------|
| GET | /api/cameras/{cameraId}/rois | ROI 목록 |
| POST | /api/cameras/{cameraId}/rois | ROI 생성 |
| DELETE | /api/cameras/{cameraId}/rois | ROI 전체 삭제 |
| GET | /api/cameras/{cameraId}/replay | 리플레이 스냅샷 |

## TODO
- [ ] 질식(suffocation) 판정 로직 (FallDetector 확장)
- [ ] ROI 영속화 (JPA 전환)
- [ ] 경보 이벤트 이력 저장/조회
- [ ] 인증/인가 (관제자 로그인)
