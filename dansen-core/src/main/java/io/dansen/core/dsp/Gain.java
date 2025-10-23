package io.dansen.core.dsp;

import io.dansen.core.audio.AudioProcessor;
import lombok.RequiredArgsConstructor;

/**
 * Scalar linear gain.
 */
@RequiredArgsConstructor
public class Gain implements AudioProcessor {

    private final AudioProcessor input;
    private final float gain;

    @Override
    public void process(float[] left, float[] right, int frames) {
        input.process(left, right, frames);
        for (int i = 0; i < frames; i++) {
            left[i] *= gain;
            right[i] *= gain;
        }
    }
}
