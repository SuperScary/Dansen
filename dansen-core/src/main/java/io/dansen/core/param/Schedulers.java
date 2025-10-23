package io.dansen.core.param;

/**
 * Helpers for scheduling time-based ramps.
 */
public final class Schedulers {

  private Schedulers() {}

  public static int secondsToSamples(double seconds, int sampleRate) {
    return (int) Math.max(1, Math.round(seconds * sampleRate));
  }

}
