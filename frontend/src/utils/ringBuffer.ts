// 클라이언트측 5초 리플레이 링버퍼 (스켈레톤 궤적 재구성용)

export class RingBuffer<T> {
  private buffer: T[] = []
  constructor(private capacity: number) {}

  push(item: T) {
    this.buffer.push(item)
    if (this.buffer.length > this.capacity) {
      this.buffer.shift()
    }
  }

  /** 오래된 → 최신 순서 스냅샷 */
  snapshot(): T[] {
    return [...this.buffer]
  }

  clear() {
    this.buffer = []
  }
}
