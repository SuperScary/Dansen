package io.dansen.core.dsp;

import io.dansen.core.audio.AudioProcessor;
import io.dansen.core.param.FloatParam;
import lombok.RequiredArgsConstructor;

/**
 * Constant-power panner: pan in [-1..1], -1=L, 0=C, 1=R
 */
@RequiredArgsConstructor
public class Pan implements AudioProcessor {
    private final AudioProcessor input;
    private final FloatParam pan; // -1..1

    @Override
    public void process(float[] left, float[] right, int frames) {
        input.process(left, right, frames);
        for (int i = 0; i < frames; i++) {
          float p = Math.max(-1f, Math.min(1f, pan.step()));
          float lGain = (float) Math.cos((p + 1) * Math.PI / 4);
          float rGain = (float) Math.cos((p - 1) * Math.PI / 4);
          left[i] *= lGain;
          right[i] *= rGain;
        }
    }

}
