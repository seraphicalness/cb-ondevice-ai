// 백엔드 도메인과 1:1 대응하는 타입 정의

export type Keypoint = [x: number, y: number, confidence: number]

export interface SkeletonFrame {
  cameraId: string
  frameId: number
  timestamp: number
  keypoints: Keypoint[] // 17개
}

export type AlertLevel = 'NONE' | 'CAUTION' | 'WARNING' | 'DANGER'

export interface AlertEvent {
  cameraId: string
  level: AlertLevel
  type: string // 'FALL' | 'ROI_INTRUSION' | ...
  message: string
  frameId: number
  timestamp: number
}

export type RoiType = 'DANGER' | 'WATCH'

export interface RegionOfInterest {
  id: string
  name: string
  type: RoiType
  polygon: [number, number][]
}

// /ws/monitor 로 들어오는 메시지 봉투
export type MonitorMessage =
  | { type: 'frame'; data: SkeletonFrame }
  | { type: 'alert'; data: AlertEvent }
