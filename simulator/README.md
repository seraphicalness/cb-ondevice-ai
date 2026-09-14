# simulator — 온디바이스 AI 단말 (Python)

실제 영유아 CCTV 수집의 한계(개인정보/안전)를 극복하기 위해
낙상·질식 등 **AI 가상 이상행동 시나리오**의 COCO 17 관절 데이터를 합성해
백엔드로 전송하는 온디바이스 단말 대체 시뮬레이터.

## 설치
```bash
cd simulator
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
```

## 실행
```bash
# 낙상 시나리오 1회 재생
python sender.py --scenario fall

# 정상 보행 반복
python sender.py --scenario normal --loop

# 질식 시나리오, 카메라 2번, 15fps
python sender.py --scenario suffocation --camera cam-02 --fps 15
```

## 시나리오
| 이름 | 설명 | 기대 경보 |
|------|------|-----------|
| normal | 정상 보행 (좌우 이동) | 없음 |
| fall | 낙상 (골반 급강하) | DANGER (FALL) |
| suffocation | 질식 (움직임 소실) | (백엔드 로직 구현 후) |

## 파일
- `coco.py` — COCO 17 관절 정의 + 골격 연결
- `scenario.py` — 이상행동 시나리오 관절 시퀀스 생성
- `sender.py` — WebSocket 전송 클라이언트

## 전송 포맷
```json
{
  "cameraId": "cam-01",
  "frameId": 42,
  "timestamp": 1726300000000,
  "keypoints": [[0.5, 0.2, 0.9], "... 총 17개 [x, y, confidence]"]
}
```
