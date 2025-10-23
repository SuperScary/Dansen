package io.dansen.io;

/**
 * Single-producer/single-consumer ring buffer for interleaved stereo frames.
 */
public final class RingBuffer {
  private final float[] left, right;
  private final int capacity;
  private volatile int writeIdx = 0;
  private volatile int readIdx = 0;


  public RingBuffer(int capacityFrames) {
    this.capacity = capacityFrames;
    this.left = new float[capacityFrames];
    this.right = new float[capacityFrames];
  }


  public int availableToRead() {
    return (writeIdx - readIdx + capacity) % capacity;
  }

  public int availableToWrite() {
    return capacity - 1 - availableToRead();
  }


  public int write(float[] l, float[] r, int frames) {
    int toWrite = Math.min(frames, availableToWrite());
    for (int i = 0; i < toWrite; i++) {
      left[writeIdx] = l[i];
      right[writeIdx] = r[i];
      writeIdx = (writeIdx + 1) % capacity;
    }
    return toWrite;
  }


  public int read(float[] l, float[] r, int frames) {
    int toRead = Math.min(frames, availableToRead());
    for (int i = 0; i < toRead; i++) {
      l[i] = left[readIdx];
      r[i] = right[readIdx];
      readIdx = (readIdx + 1) % capacity;
    }
    // zero remainder if underflow
    for (int i = toRead; i < frames; i++) {
      l[i] = 0f;
      r[i] = 0f;
    }
    return toRead;
  }
}
