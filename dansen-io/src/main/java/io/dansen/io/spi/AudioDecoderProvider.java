package io.dansen.io.spi;

import java.io.File;
import java.io.IOException;

/**
 * Service provider interface to create decoders for files.
 */
public interface AudioDecoderProvider {

  boolean supports(File file);

  PcmDecoder open(File file) throws IOException;

}
