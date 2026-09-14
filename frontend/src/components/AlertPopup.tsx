import { useEffect } from 'react'
import type { AlertEvent } from '../types'

interface Props {
  alert: AlertEvent | null
  onClose: () => void
}

const LEVEL_COLOR: Record<string, string> = {
  CAUTION: '#faad14',
  WARNING: '#fa8c16',
  DANGER: '#ff4d4f',
}

/**
 * 비상 긴급 팝업. 위험 감지 시 화면에 표시하고 경보음을 재생한다.
 * 1초 미만 초저지연 대응(골든타임 사수)이 목표.
 */
export function AlertPopup({ alert, onClose }: Props) {
  useEffect(() => {
    if (alert && alert.level === 'DANGER') {
      // TODO: 실제 경보음 파일 연결 (public/alarm.mp3)
      // new Audio('/alarm.mp3').play().catch(() => {})
    }
  }, [alert])

  if (!alert || alert.level === 'NONE') return null

  return (
    <div
      style={{
        position: 'fixed',
        top: 24,
        right: 24,
        minWidth: 280,
        padding: 16,
        borderRadius: 8,
        color: '#fff',
        background: LEVEL_COLOR[alert.level] ?? '#333',
        boxShadow: '0 4px 16px rgba(0,0,0,0.3)',
        animation: alert.level === 'DANGER' ? 'blink 0.6s step-end infinite' : undefined,
      }}
    >
      <strong style={{ fontSize: 18 }}>🚨 {alert.level} — {alert.type}</strong>
      <p style={{ margin: '8px 0' }}>{alert.message}</p>
      <small>
        {alert.cameraId} · frame {alert.frameId} ·{' '}
        {new Date(alert.timestamp).toLocaleTimeString()}
      </small>
      <button onClick={onClose} style={{ display: 'block', marginTop: 8 }}>
        확인
      </button>
      <style>{`@keyframes blink { 50% { opacity: 0.4; } }`}</style>
    </div>
  )
}
