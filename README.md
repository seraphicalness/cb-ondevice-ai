# cb-ondevice-ai

온디바이스 AI 기반 생활밀착 공공서비스
**영유아 실시간 이상행동 감지 및 스마트 돌봄 관제 플랫폼**

(재)충북과학기술혁신원 생활밀착 공공서비스 실증 연계 과제 — 김나희, 임수정

---

## 프로젝트 개요

CCTV 현장 단말에서 **온디바이스 AI**로 COCO 17개 관절 좌표만 추출·전송하여
개인정보(원본 영상)를 전송하지 않고도 영유아의 이상행동(낙상·질식 등)을
실시간으로 감지·관제하는 플랫폼.

## 아키텍처

```
[온디바이스 AI 단말(수집)]        [백엔드 위험 판정 엔진(분석)]      [웹 관제 클라이언트(시각화)]
 simulator (Python)          →     backend (Spring Boot)        →    frontend (React + Canvas)
 - COCO 17 keypoint 추출            - 낙상 수직가속도 판정              - Canvas 2D 스켈레톤
 - 낙상/질식 시나리오 생성           - 동적 ROI 침범 상태머신            - 동적 ROI 드로잉
 - JSON 메타데이터만 전송            - 3단계 경보(주의/경고/위험)         - 5초 리플레이(Ring Buffer)
 (원본 영상 미전송)                 - WebSocket <1s Push               - 비상 팝업 & 경보음
```

### 데이터 파이프라인
1. **simulator** → 관절 좌표 JSON 을 WebSocket 으로 backend 에 전송 (초당 30프레임)
2. **backend** → 위험 판정 엔진이 낙상/ROI 침범 분석 → 경보 이벤트 생성 → 관제 클라이언트로 Push
3. **frontend** → 스켈레톤 실시간 렌더링, ROI 설정, 경보 팝업/리플레이

## 핵심 기능 요구사항
| 기능 | 설명 |
|------|------|
| 프라이버시 보장 | 원본 비디오 미전송, 관절 좌표 메타데이터만 전송 |
| 1초 미만 초저지연 | WebSocket 양방향 통신, 긴급 팝업/알림음 |
| 동적 ROI 드로잉 | 마우스 다각형 위험구역 지정, 좌표 실시간 저장 |
| 5초 사고 리플레이 | Ring Buffer 기반 이상감지 전후 5초 스켈레톤 궤적 |

## 디렉터리 구조
```
cb-ondevice-ai/
├── backend/     Spring Boot — WebSocket + 위험 판정 엔진
├── frontend/    React + Canvas — 관제 웹 클라이언트
├── simulator/   Python — COCO 관절 시뮬레이터 (온디바이스 단말 대체)
└── docs/        요구사항/설계 문서
```

## 실행 방법
각 모듈 README 참고:
- [backend/](./backend) — `./gradlew bootRun` (포트 8080)
- [frontend/](./frontend) — `npm install && npm run dev` (포트 5173)
- [simulator/](./simulator) — `python -m simulator.sender`

## 기술 스택
- **Backend**: Spring Boot 3, Spring WebSocket(STOMP)
- **Frontend**: React 18, TypeScript, Vite, Canvas 2D
- **Simulator**: Python 3.11, websockets
