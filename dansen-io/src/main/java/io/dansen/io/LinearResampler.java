package io.dansen.io;

/**
 * Simple linear resampler for stereo; not band-limited.
 */
public final class LinearResampler {
  private double pos = 0.0;
  private final double ratio; // sourceSR / destSR


  public LinearResampler(int srcRate, int dstRate) {
    this.ratio = (double) srcRate / dstRate;
  }


  public void process(float[] inL, float[] inR, int inFrames, float[] outL, float[] outR, int outFrames) {
    for (int i = 0; i < outFrames; i++) {
      double srcPos = pos * ratio;
      int i0 = (int) Math.floor(srcPos);
      int i1 = Math.min(i0 + 1, inFrames - 1);
      double t = srcPos - i0;
      outL[i] = (float) (inL[i0] * (1 - t) + inL[i1] * t);
      outR[i] = (float) (inR[i0] * (1 - t) + inR[i1] * t);
      pos += 1.0;
    }
    if (pos * ratio >= inFrames - 1) pos = 0.0; // simple chunk reset
  }
}
