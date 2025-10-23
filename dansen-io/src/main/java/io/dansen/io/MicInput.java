package io.dansen.io;

import javax.sound.sampled.*;


/**
 * Captures stereo mic (or mono duplicated) at given sampleRate/16-bit → RingBuffer.
 */
public final class MicInput implements AutoCloseable {
  private final TargetDataLine line;
  private final RingBuffer rb;
  private final int framesPerBlock;
  private volatile boolean running = false;
  private Thread thread;


  public MicInput(int sampleRate, int framesPerBlock, RingBuffer rb) {
    this.framesPerBlock = framesPerBlock;
    this.rb = rb;
    try {
      AudioFormat fmt = new AudioFormat(sampleRate, 16, 2, true, false);
      DataLine.Info info = new DataLine.Info(TargetDataLine.class, fmt);
      line = (TargetDataLine) AudioSystem.getLine(info);
      line.open(fmt, framesPerBlock * 4 * 4);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public void start() {
    if (running) return;
    running = true;
    line.start();
    thread = new Thread(this::loop, "MicInput");
    thread.setDaemon(true);
    thread.start();
  }


  private void loop() {
    byte[] buf = new byte[framesPerBlock * 4];
    float[] l = new float[framesPerBlock];
    float[] r = new float[framesPerBlock];
    while (running) {
      int read = line.read(buf, 0, buf.length);
      if (read <= 0) continue;
      int frames = read / 4;
      int bi = 0;
      for (int i = 0; i < frames; i++) {
        int li = (buf[bi++] & 0xFF) | (buf[bi++] << 8);
        int ri = (buf[bi++] & 0xFF) | (buf[bi++] << 8);
        l[i] = li / 32768f;
        r[i] = ri / 32768f;
      }
      rb.write(l, r, frames);
    }
  }


  public int read(float[] left, float[] right, int frames) {
    return rb.read(left, right, frames);
  }


  @Override
  public void close() {
    running = false;
    if (line != null) {
      line.stop();
      line.flush();
      line.close();
    }
    if (thread != null) try {
      thread.join(100);
    } catch (InterruptedException ignored) {
    }
  }
}
