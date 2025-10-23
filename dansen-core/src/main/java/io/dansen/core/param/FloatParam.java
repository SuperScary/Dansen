package io.dansen.core.param;

/**
 * Automatable float parameter with per-block linear ramps.
 */
public final class FloatParam {

  private volatile float current;
  private volatile float target;
  private volatile int rampSamples = 0;

  public FloatParam(float initial) {
    this.current = this.target = initial;
  }

  public float value() {
    return current;
  }

  public void set(float v) {
    current = target = v;
    rampSamples = 0;
  }

  /**
   * Lines ramp over given samples
   */
  public void rampTo(float v, int samples) {
    this.target = v;
    this.rampSamples = Math.max(0, samples);
  }

  public float step() {
    if (rampSamples <= 0) {
      current = target;
      return current;
    }

    float diff = target - current;
    current += diff / rampSamples;
    rampSamples--;
    return current;
  }

}
