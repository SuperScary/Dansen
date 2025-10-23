package io.dansen.core.audio;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class Track implements GraphNode {

    @Getter
    @Setter
    private AudioProcessor processor;

    public Track(AudioProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void process(float[] left, float[] right, int frames) {
        if (processor != null) {
            processor.process(left, right, frames);
        } else {
            Arrays.fill(left, 0, frames, 0f);
            // right will be filled by processor; if null, it's already zeroed by caller
        }
    }

}
