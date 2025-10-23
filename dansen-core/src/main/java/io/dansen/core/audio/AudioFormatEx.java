package io.dansen.core.audio;

/**
 * Simple format carrier; interleaved stereo float32 @ sampleRate.
 */
public record AudioFormatEx(int sampleRate, int blockSize) {

    public static AudioFormatEx of44k1() {
        return new AudioFormatEx(44100, 512);
    }

}
