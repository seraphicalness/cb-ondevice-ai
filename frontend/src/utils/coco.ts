// COCO 17 골격 연결 (백엔드/시뮬레이터와 동일 순서)

export const SKELETON_EDGES: [number, number][] = [
  [5, 6], [5, 7], [7, 9], [6, 8], [8, 10], // 팔
  [5, 11], [6, 12], [11, 12],              // 몸통
  [11, 13], [13, 15], [12, 14], [14, 16],  // 다리
  [0, 5], [0, 6],                          // 머리-어깨
]

export const KEYPOINT_NAMES = [
  'nose', 'left_eye', 'right_eye', 'left_ear', 'right_ear',
  'left_shoulder', 'right_shoulder', 'left_elbow', 'right_elbow',
  'left_wrist', 'right_wrist', 'left_hip', 'right_hip',
  'left_knee', 'right_knee', 'left_ankle', 'right_ankle',
]
