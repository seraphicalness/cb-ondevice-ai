import { useEffect, useState } from 'react'
import { SkeletonCanvas } from './components/SkeletonCanvas'
import { AlertPopup } from './components/AlertPopup'
import { RoiToolbar } from './components/RoiToolbar'
import { useMonitorSocket } from './hooks/useMonitorSocket'
import { listRois } from './api/roi'
import type { AlertEvent, RegionOfInterest } from './types'

const CAMERA_ID = 'cam-01'

export default function App() {
  const { frame, alert, connected } = useMonitorSocket()
  const [rois, setRois] = useState<RegionOfInterest[]>([])
  const [activeAlert, setActiveAlert] = useState<AlertEvent | null>(null)

  const refreshRois = () => listRois(CAMERA_ID).then(setRois)
  useEffect(() => { refreshRois() }, [])
  useEffect(() => { if (alert) setActiveAlert(alert) }, [alert])

  return (
    <div style={{ fontFamily: 'sans-serif', color: '#e6edf3', background: '#010409', minHeight: '100vh', padding: 24 }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1 style={{ fontSize: 20 }}>스마트 돌봄 관제 · {CAMERA_ID}</h1>
        <span style={{ color: connected ? '#39d353' : '#ff4d4f' }}>
          ● {connected ? '수신중' : '연결 끊김'}
        </span>
      </header>

      <RoiToolbar cameraId={CAMERA_ID} onChanged={refreshRois} />
      <SkeletonCanvas frame={frame} rois={rois} />

      <AlertPopup alert={activeAlert} onClose={() => setActiveAlert(null)} />
    </div>
  )
}
