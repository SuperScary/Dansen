package io.dansen.io.spi;

import java.io.Closeable;

/**
 * Pull decoder that delivers float stereo at its native sample rate.
 */
public interface PcmDecoder extends Closeable {

  int sampleRate();

  int channels();

  /**
   * Reads up to frames
   * @param left left channel
   * @param right right channel
   * @param frames number of frames to read
   * @return frames actually read, 0 on EOF
   */
  int read(float[] left, float[] right, int frames);

}
