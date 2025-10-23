package io.dansen.core.dsp;

import io.dansen.core.audio.AudioProcessor;
import lombok.RequiredArgsConstructor;

/**
 * Constant-power panner: pan in [-1..1], -1=L, 0=C, 1=R
 */
@RequiredArgsConstructor
public class Pan implements AudioProcessor {
    private final AudioProcessor input;
    private final float pan; // -1..1

    @Override
    public void process(float[] left, float[] right, int frames) {
        input.process(left, right, frames);
        float lGain = (float) Math.cos((pan + 1) * Math.PI / 4);
        float rGain = (float) Math.sin((pan + 1) * Math.PI / 4);
        for (int i = 0; i < frames; i++) {
            left[i] *= lGain;
            right[i] *= rGain;
        }
    }

}
