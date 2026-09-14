# frontend — 스마트 관제 웹 클라이언트 (React + Canvas)

## 역할
백엔드에서 Push 되는 스켈레톤 프레임/경보를 실시간 시각화하고,
동적 ROI 드로잉·비상 팝업·리플레이를 제공하는 관제 화면.

## 설치 / 실행
```bash
cd frontend
npm install
npm run dev     # http://localhost:5173 (백엔드 8080 프록시)
```

## 구조
```
src/
├── components/
│   ├── SkeletonCanvas.tsx  실시간 2D 스켈레톤 + ROI 렌더링
│   ├── RoiToolbar.tsx      동적 ROI 드로잉 툴바
│   └── AlertPopup.tsx      비상 긴급 팝업 + 경보음
├── hooks/
│   └── useMonitorSocket.ts /ws/monitor 구독
├── api/
│   └── roi.ts              ROI REST 클라이언트
├── utils/
│   ├── coco.ts             골격 연결 정의
│   └── ringBuffer.ts       5초 리플레이 버퍼
└── types/index.ts          백엔드 도메인 타입
```

## 구현 완료 / TODO
- [x] WebSocket 실시간 스켈레톤 렌더링
- [x] 경보 팝업 (단계별 색상/점멸)
- [x] ROI 목록 표시
- [ ] 마우스 드래그로 ROI 다각형 직접 그리기 (현재 데모 버튼만)
- [ ] 5초 리플레이 재생 UI (RingBuffer 연동)
- [ ] 경보음 파일 연결 (public/alarm.mp3)
- [ ] GIS 지도 팝업 (카메라 위치/이벤트)
