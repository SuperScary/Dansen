package io.dansen.core.audio;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * Drives the graph block-by-block.
 */
@RequiredArgsConstructor
public class AudioEngine {

    private final AudioFormatEx format;
    private final GraphNode root;

    public void renderToDriver(BiConsumer<float[], float[]> blockConsumer, int seconds) {
        int frames = format.blockSize();
        float[] l = new float[frames];
        float[] r = new float[frames];
        long totalBlocks = (long) Math.ceil((double) (seconds * format.sampleRate()) / frames);

        for (long b = 0; b < totalBlocks; b++) {
            Arrays.fill(l, 0f);
            Arrays.fill(r, 0f);
            root.process(l, r, frames);
            blockConsumer.accept(l, r);
        }
    }

}
