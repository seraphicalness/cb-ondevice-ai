import type { RegionOfInterest, RoiType } from '../types'

// ROI REST API 클라이언트

export async function listRois(cameraId: string): Promise<RegionOfInterest[]> {
  const res = await fetch(`/api/cameras/${cameraId}/rois`)
  return res.json()
}

export async function createRoi(
  cameraId: string,
  name: string,
  type: RoiType,
  polygon: [number, number][],
): Promise<RegionOfInterest> {
  const res = await fetch(`/api/cameras/${cameraId}/rois`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, type, polygon }),
  })
  return res.json()
}

export async function clearRois(cameraId: string): Promise<void> {
  await fetch(`/api/cameras/${cameraId}/rois`, { method: 'DELETE' })
}
