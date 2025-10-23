package io.dansen.core.dsp;

import io.dansen.core.audio.AudioProcessor;
import io.dansen.core.param.FloatParam;
import lombok.RequiredArgsConstructor;

/**
 * Scalar linear gain.
 */
@RequiredArgsConstructor
public class Gain implements AudioProcessor {

    private final AudioProcessor input;
    private final FloatParam gain;

    @Override
    public void process(float[] left, float[] right, int frames) {
        input.process(left, right, frames);
        for (int i = 0; i < frames; i++) {
            float g = gain.step();
            left[i] *= g;
            right[i] *= g;
        }
    }
}
