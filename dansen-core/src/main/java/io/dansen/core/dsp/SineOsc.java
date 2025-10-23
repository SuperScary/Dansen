package io.dansen.core.dsp;

import io.dansen.core.audio.AudioProcessor;
import lombok.RequiredArgsConstructor;

/**
 * Simple sine oscillator (stereo identical).
 */
@RequiredArgsConstructor
public class SineOsc implements AudioProcessor {

    private final float freqHz;
    private final int sampleRate;
    private float phase = 0f;
    private static final float TAU = (float) (Math.PI * 2);

    @Override
    public void process(float[] left, float[] right, int frames) {
        float inc = TAU * freqHz / sampleRate;
        for (int i = 0; i < frames; i++) {
            float s = (float) Math.sin(phase);
            phase += inc;
            if (phase > TAU) phase -= TAU;
            left[i] = s;
            right[i] = s;
        }
    }

}
