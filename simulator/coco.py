"""COCO 17 keypoint 표준 정의 및 골격 연결(skeleton edges)."""

# 관절 index (백엔드 CocoKeypoint enum 과 순서 일치)
KEYPOINT_NAMES = [
    "nose", "left_eye", "right_eye", "left_ear", "right_ear",
    "left_shoulder", "right_shoulder", "left_elbow", "right_elbow",
    "left_wrist", "right_wrist", "left_hip", "right_hip",
    "left_knee", "right_knee", "left_ankle", "right_ankle",
]

NUM_KEYPOINTS = len(KEYPOINT_NAMES)  # 17

# 프론트 Canvas 렌더링과 동일한 골격 연결 (index 쌍)
SKELETON_EDGES = [
    (5, 6), (5, 7), (7, 9), (6, 8), (8, 10),   # 팔
    (5, 11), (6, 12), (11, 12),                # 몸통
    (11, 13), (13, 15), (12, 14), (14, 16),    # 다리
    (0, 5), (0, 6),                            # 머리-어깨
]

# 이름 -> index 헬퍼
IDX = {name: i for i, name in enumerate(KEYPOINT_NAMES)}
