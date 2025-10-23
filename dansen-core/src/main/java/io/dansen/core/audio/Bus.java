package io.dansen.core.audio;

import lombok.Getter;

import java.util.Arrays;

/**
 * Stereo summing bus.
 */
public class Bus {

    @Getter
    private final float[] left;

    @Getter
    private final float[] right;

    public Bus(int frames) {
        left = new float[frames];
        right = new float[frames];
    }

    public void clear(int frames) {
        Arrays.fill(left, 0, frames, 0f);
        Arrays.fill(right, 0, frames, 0f);
    }

    public void sumFrom(float[] l, float[] r, int frames) {
        for (int i = 0; i < frames; i++) {
            left[i] += l[i];
            right[i] += r[i];
        }
    }

}
