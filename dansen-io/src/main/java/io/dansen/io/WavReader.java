package io.dansen.io;

import javax.sound.sampled.*;
import java.io.File;


/**
 * Decodes WAV via JavaSound into float stereo at the file's sample rate.
 */
public final class WavReader implements AutoCloseable {
  private final AudioInputStream stream;
  private final AudioFormat fmt; // decoded PCM_SIGNED 16-bit stereo


  public WavReader(File file) {
    try {
      AudioInputStream in = AudioSystem.getAudioInputStream(file);
      AudioFormat base = in.getFormat();
      AudioFormat target = new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        base.getSampleRate(),
        16,
        2,
        4,
        base.getSampleRate(),
        false);
      this.stream = AudioSystem.getAudioInputStream(target, in);
      this.fmt = stream.getFormat();
    } catch (Exception e) {
      throw new RuntimeException("Failed to open WAV: " + file, e);
    }
  }


  public int sampleRate() {
    return (int) fmt.getSampleRate();
  }


  /**
   * Reads up to frames into left/right; returns frames actually read, 0 on EOF.
   */
  public int readBlock(float[] left, float[] right, int frames) {
    try {
      byte[] buf = new byte[frames * 4];
      int n = stream.read(buf);
      if (n <= 0) return 0;
      int f = n / 4;
      int bi = 0;
      for (int i = 0; i < f; i++) {
        int l = (buf[bi++] & 0xFF) | (buf[bi++] << 8);
        int r = (buf[bi++] & 0xFF) | (buf[bi++] << 8);
        left[i] = l / 32768f;
        right[i] = r / 32768f;
      }
      // zero tail if short
      for (int i = f; i < frames; i++) {
        left[i] = 0f;
        right[i] = 0f;
      }
      return f;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void close() {
    try {
      stream.close();
    } catch (Exception ignored) {
    }
  }
}
