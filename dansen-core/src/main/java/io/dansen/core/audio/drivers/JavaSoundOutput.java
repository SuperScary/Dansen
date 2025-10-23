package io.dansen.core.audio.drivers;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * Interleaves float buffers -> 16-bit PCM to SourceDataLine.
 */
public class JavaSoundOutput implements AutoCloseable {
  private final SourceDataLine line;
  private final int framesPerBlock;


  public JavaSoundOutput(int sampleRate, int framesPerBlock) {
    try {
      AudioFormat fmt = new AudioFormat(sampleRate, 16, 2, true, false);
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
      line = (SourceDataLine) AudioSystem.getLine(info);
      line.open(fmt, framesPerBlock * 4 * 4);
      line.start();
      this.framesPerBlock = framesPerBlock;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public void write(float[] left, float[] right) {
    byte[] buf = new byte[framesPerBlock * 4]; // 2ch * 16-bit
    int bi = 0;
    for (int i = 0; i < framesPerBlock; i++) {
      int l = (int) (Math.max(-1f, Math.min(1f, left[i])) * 32767);
      int r = (int) (Math.max(-1f, Math.min(1f, right[i])) * 32767);
      buf[bi++] = (byte) (l & 0xFF);
      buf[bi++] = (byte) ((l >> 8) & 0xFF);
      buf[bi++] = (byte) (r & 0xFF);
      buf[bi++] = (byte) ((r >> 8) & 0xFF);
    }
    line.write(buf, 0, buf.length);
  }


  @Override
  public void close() {
    line.drain();
    line.stop();
    line.close();
  }
}
