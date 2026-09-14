"""
온디바이스 단말 시뮬레이터.
지정한 시나리오의 관절 프레임을 WebSocket 으로 백엔드(/ws/ingest)에 전송한다.

원본 영상은 전송하지 않고, COCO 17 관절 메타데이터(JSON)만 보낸다.

사용:
    python sender.py --scenario fall --camera cam-01 --fps 30
"""

import argparse
import asyncio
import json
import time

import websockets

from scenario import SCENARIOS


async def stream(uri: str, camera_id: str, scenario: str, fps: int, loop: bool):
    generator_fn = SCENARIOS[scenario]
    interval = 1.0 / fps

    async with websockets.connect(uri) as ws:
        print(f"[sim] connected → {uri} (scenario={scenario}, camera={camera_id}, fps={fps})")
        frame_id = 0
        while True:
            for keypoints in generator_fn():
                message = {
                    "cameraId": camera_id,
                    "frameId": frame_id,
                    "timestamp": int(time.time() * 1000),
                    "keypoints": keypoints,
                }
                await ws.send(json.dumps(message))
                frame_id += 1
                await asyncio.sleep(interval)
            if not loop:
                break
        print(f"[sim] done. sent {frame_id} frames.")


def main():
    parser = argparse.ArgumentParser(description="온디바이스 AI 단말 시뮬레이터")
    parser.add_argument("--uri", default="ws://localhost:8080/ws/ingest")
    parser.add_argument("--camera", default="cam-01")
    parser.add_argument("--scenario", default="fall", choices=list(SCENARIOS.keys()))
    parser.add_argument("--fps", type=int, default=30)
    parser.add_argument("--loop", action="store_true", help="시나리오 반복 재생")
    args = parser.parse_args()

    asyncio.run(stream(args.uri, args.camera, args.scenario, args.fps, args.loop))


if __name__ == "__main__":
    main()
