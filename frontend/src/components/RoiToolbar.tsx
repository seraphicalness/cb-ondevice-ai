import { useState } from 'react'
import type { RoiType } from '../types'
import { createRoi, clearRois } from '../api/roi'

interface Props {
  cameraId: string
  onChanged: () => void
}

/**
 * 동적 ROI 드로잉 툴바 (뼈대).
 * TODO: 캔버스 위 마우스 클릭으로 다각형 꼭짓점을 찍어 polygon 을 구성하는
 *       인터랙션을 SkeletonCanvas 와 연동해 구현.
 *       지금은 데모용 사각형을 저장하는 버튼만 제공.
 */
export function RoiToolbar({ cameraId, onChanged }: Props) {
  const [type, setType] = useState<RoiType>('DANGER')
  const [name, setName] = useState('위험구역')

  async function handleAddDemo() {
    // 데모: 화면 좌상단 사각형 (정규화 좌표)
    const demoPolygon: [number, number][] = [
      [0.1, 0.1], [0.4, 0.1], [0.4, 0.4], [0.1, 0.4],
    ]
    await createRoi(cameraId, name, type, demoPolygon)
    onChanged()
  }

  async function handleClear() {
    await clearRois(cameraId)
    onChanged()
  }

  return (
    <div style={{ display: 'flex', gap: 8, alignItems: 'center', padding: 8 }}>
      <input value={name} onChange={(e) => setName(e.target.value)} placeholder="구역 이름" />
      <select value={type} onChange={(e) => setType(e.target.value as RoiType)}>
        <option value="DANGER">위험(DANGER)</option>
        <option value="WATCH">관심(WATCH)</option>
      </select>
      <button onClick={handleAddDemo}>구역 추가(데모)</button>
      <button onClick={handleClear}>전체 삭제</button>
    </div>
  )
}
