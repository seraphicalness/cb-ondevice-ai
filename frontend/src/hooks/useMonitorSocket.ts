import { useEffect, useRef, useState } from 'react'
import type { AlertEvent, MonitorMessage, SkeletonFrame } from '../types'

/**
 * 관제 WebSocket(/ws/monitor) 구독 훅.
 * 최신 스켈레톤 프레임과 경보 이벤트를 상태로 노출한다.
 */
export function useMonitorSocket(url = '/ws/monitor') {
  const [frame, setFrame] = useState<SkeletonFrame | null>(null)
  const [alert, setAlert] = useState<AlertEvent | null>(null)
  const [connected, setConnected] = useState(false)
  const wsRef = useRef<WebSocket | null>(null)

  useEffect(() => {
    // vite 프록시를 태우기 위해 상대경로 → 절대 ws URL 변환
    const wsUrl =
      url.startsWith('ws') ? url : `ws://${window.location.host}${url}`
    const ws = new WebSocket(wsUrl)
    wsRef.current = ws

    ws.onopen = () => setConnected(true)
    ws.onclose = () => setConnected(false)
    ws.onmessage = (ev) => {
      const msg: MonitorMessage = JSON.parse(ev.data)
      if (msg.type === 'frame') setFrame(msg.data)
      else if (msg.type === 'alert') setAlert(msg.data)
    }

    return () => ws.close()
  }, [url])

  return { frame, alert, connected }
}
