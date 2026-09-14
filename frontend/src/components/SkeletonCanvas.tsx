import { useEffect, useRef } from 'react'
import type { RegionOfInterest, SkeletonFrame } from '../types'
import { SKELETON_EDGES } from '../utils/coco'

interface Props {
  frame: SkeletonFrame | null
  rois: RegionOfInterest[]
  width?: number
  height?: number
}

/**
 * 실시간 2D 스켈레톤 시각화 캔버스.
 * 정규화 좌표(0~1)를 캔버스 픽셀로 변환해 관절/골격/ROI를 그린다.
 */
export function SkeletonCanvas({ frame, rois, width = 800, height = 600 }: Props) {
  const canvasRef = useRef<HTMLCanvasElement>(null)

  useEffect(() => {
    const ctx = canvasRef.current?.getContext('2d')
    if (!ctx) return

    ctx.clearRect(0, 0, width, height)
    ctx.fillStyle = '#0d1117'
    ctx.fillRect(0, 0, width, height)

    // ROI 다각형
    for (const roi of rois) {
      ctx.beginPath()
      roi.polygon.forEach(([x, y], i) => {
        const px = x * width
        const py = y * height
        i === 0 ? ctx.moveTo(px, py) : ctx.lineTo(px, py)
      })
      ctx.closePath()
      ctx.strokeStyle = roi.type === 'DANGER' ? '#ff4d4f' : '#faad14'
      ctx.lineWidth = 2
      ctx.fillStyle =
        roi.type === 'DANGER' ? 'rgba(255,77,79,0.12)' : 'rgba(250,173,20,0.12)'
      ctx.fill()
      ctx.stroke()
    }

    if (!frame) return
    const kp = frame.keypoints

    // 골격 라인
    ctx.strokeStyle = '#39d353'
    ctx.lineWidth = 3
    for (const [a, b] of SKELETON_EDGES) {
      ctx.beginPath()
      ctx.moveTo(kp[a][0] * width, kp[a][1] * height)
      ctx.lineTo(kp[b][0] * width, kp[b][1] * height)
      ctx.stroke()
    }

    // 관절 점
    ctx.fillStyle = '#58a6ff'
    for (const [x, y, c] of kp) {
      if (c < 0.3) continue // 신뢰도 낮은 관절 스킵
      ctx.beginPath()
      ctx.arc(x * width, y * height, 4, 0, Math.PI * 2)
      ctx.fill()
    }
  }, [frame, rois, width, height])

  return <canvas ref={canvasRef} width={width} height={height} style={{ borderRadius: 8 }} />
}
