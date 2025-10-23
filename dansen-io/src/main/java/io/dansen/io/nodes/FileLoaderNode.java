package io.dansen.io.nodes;

import io.dansen.core.audio.AudioProcessor;
import io.dansen.io.spi.AudioDecoderProvider;
import io.dansen.io.spi.PcmDecoder;

import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Steams PCM from a file using SPI. Optionally loops when EOF hit.
 */
public final class FileLoaderNode implements AudioProcessor, AutoCloseable {
    private final File file;
    private final boolean loop;
    private PcmDecoder decoder;


    public FileLoaderNode(File file, boolean loop) {
        this.file = file;
        this.loop = loop;
        this.decoder = open(file);
    }


    public int sourceSampleRate() {
        return decoder.sampleRate();
    }


    @Override
    public void process(float[] left, float[] right, int frames) {
        int read = decoder.read(left, right, frames);
        if (read < frames) {
            if (loop) {
                try {
                    decoder.close();
                } catch (Exception ignored) {
                }

                decoder = open(file);
                int rem = frames - read;
                float[] l2 = new float[rem];
                float[] r2 = new float[rem];
                int r = decoder.read(l2, r2, rem);
                System.arraycopy(l2, 0, left, read, rem);
                System.arraycopy(r2, 0, right, read, rem);
            } else {
                for (int i = read; i < frames; i++) {
                    left[i] = 0f;
                    right[i] = 0f;
                }
            }
        }
    }


    private static PcmDecoder open(File f) {
        for (AudioDecoderProvider p : ServiceLoader.load(AudioDecoderProvider.class)) {
            if (p.supports(f)) {
                try {
                    return p.open(f);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new IllegalStateException("No decoder provider for " + f);
    }


    @Override
    public void close() {
        try {
            decoder.close();
        } catch (Exception ignored) {
        }
    }
}
