"""
AI 가상 이상행동 시나리오 기반 관절 데이터 생성기.

실제 영유아 CCTV 수집 한계(개인정보/안전)를 극복하기 위해
낙상/질식 등 이상행동 시나리오의 COCO 17 관절 시퀀스를 합성한다.

좌표계: 정규화 (0.0 ~ 1.0), 화면 y는 아래로 증가.
"""

import numpy as np

from coco import NUM_KEYPOINTS, IDX


def _base_pose(cx: float = 0.5, cy: float = 0.5, scale: float = 0.25):
    """서 있는 기본 자세의 17 관절 좌표 생성."""
    kp = np.zeros((NUM_KEYPOINTS, 2), dtype=float)
    # 대략적인 인체 비율 배치 (머리 위, 발 아래)
    layout = {
        "nose": (0.0, -1.0), "left_eye": (-0.05, -1.05), "right_eye": (0.05, -1.05),
        "left_ear": (-0.1, -1.0), "right_ear": (0.1, -1.0),
        "left_shoulder": (-0.2, -0.7), "right_shoulder": (0.2, -0.7),
        "left_elbow": (-0.25, -0.4), "right_elbow": (0.25, -0.4),
        "left_wrist": (-0.28, -0.1), "right_wrist": (0.28, -0.1),
        "left_hip": (-0.15, 0.0), "right_hip": (0.15, 0.0),
        "left_knee": (-0.15, 0.5), "right_knee": (0.15, 0.5),
        "left_ankle": (-0.15, 1.0), "right_ankle": (0.15, 1.0),
    }
    for name, (dx, dy) in layout.items():
        kp[IDX[name]] = [cx + dx * scale, cy + dy * scale]
    return kp


def _with_confidence(kp: np.ndarray, conf: float = 0.9):
    """[x, y] -> [x, y, confidence] 로 변환."""
    return [[float(x), float(y), conf] for x, y in kp]


def normal_walk(frames: int = 150):
    """정상 보행: 좌우로 천천히 이동."""
    for f in range(frames):
        cx = 0.5 + 0.15 * np.sin(f / 20.0)
        kp = _base_pose(cx=cx)
        # 관절 미세 흔들림
        kp += np.random.normal(0, 0.002, kp.shape)
        yield _with_confidence(kp)


def fall(frames: int = 60, fall_at: int = 30):
    """낙상: fall_at 프레임부터 골반이 급강하하며 쓰러짐."""
    for f in range(frames):
        kp = _base_pose()
        if f >= fall_at:
            # 급격한 수직 하강 + 몸이 눕는 형태 (y 증가)
            drop = min((f - fall_at) * 0.06, 0.35)
            kp[:, 1] += drop
            # 상체가 앞으로 무너지는 효과
            kp[IDX["nose"], 1] += drop * 0.5
        kp += np.random.normal(0, 0.002, kp.shape)
        yield _with_confidence(kp)


def suffocation(frames: int = 120, still_after: int = 20):
    """질식: 특정 시점 이후 거의 미동 없이 정지 (움직임 소실)."""
    base = _base_pose(cy=0.6)
    for f in range(frames):
        if f < still_after:
            kp = base + np.random.normal(0, 0.004, base.shape)
        else:
            # 미동 거의 없음 (움직임 소실 = 질식 의심 신호)
            kp = base + np.random.normal(0, 0.0003, base.shape)
        yield _with_confidence(kp)


SCENARIOS = {
    "normal": normal_walk,
    "fall": fall,
    "suffocation": suffocation,
}
