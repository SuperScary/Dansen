package io.dansen.io.javasound;

import io.dansen.io.spi.AudioDecoderProvider;
import io.dansen.io.spi.PcmDecoder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public final class JavaSoundDecoderProvider implements AudioDecoderProvider {

  @Override
  public boolean supports(File file) {
    String n = file.getName().toLowerCase();
    return n.endsWith(".wav") || n.endsWith(".aiff") || n.endsWith(".aif") || n.endsWith(".flac");
  }


  @Override
  public PcmDecoder open(File file) {
    try {
      AudioInputStream in = AudioSystem.getAudioInputStream(file);
      AudioFormat base = in.getFormat();
      int ch = Math.min(2, base.getChannels());
      AudioFormat target = new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        base.getSampleRate(),
        16,
        ch,
        ch * 2,
        base.getSampleRate(),
        false);
      AudioInputStream stream = AudioSystem.getAudioInputStream(target, in);
      return new PcmDecoder() {
        final AudioFormat f = stream.getFormat();
        final int channels = f.getChannels();

        @Override
        public int sampleRate() {
          return (int) f.getSampleRate();
        }

        @Override
        public int channels() {
          return channels;
        }

        @Override
        public int read(float[] left, float[] right, int frames) {
          try {
            byte[] buf = new byte[frames * channels * 2];
            int n = stream.read(buf);
            if (n <= 0) return 0;
            int samples = n / 2;
            int framesRead = samples / channels;
            int bi = 0;
            for (int i = 0; i < framesRead; i++) {
              int li = (buf[bi++] & 0xFF) | (buf[bi++] << 8);
              float l = li / 32768f;
              float r = l;
              if (channels == 2) {
                int ri = (buf[bi++] & 0xFF) | (buf[bi++] << 8);
                r = ri / 32768f;
              }
              left[i] = l;
              right[i] = r;
            }
            // zero tail
            for (int i = framesRead; i < frames; i++) {
              left[i] = 0f;
              right[i] = 0f;
            }
            return framesRead;
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }

        @Override
        public void close() throws IOException {
          stream.close();
        }
      };
    } catch (Exception e) {
      throw new RuntimeException("Cannot decode: " + file, e);
    }
  }

}
