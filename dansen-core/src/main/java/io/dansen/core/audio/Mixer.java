package io.dansen.core.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sums multiple tracks into the master bus.
 */
public class Mixer implements GraphNode {

    private final List<Track> tracks = new ArrayList<>();
    private final BufferPool scratch;

    public Mixer(int frames) {
        this.scratch = new BufferPool(frames);
    }

    public Mixer add(Track track) {
        tracks.add(track);
        return this;
    }

    @Override
    public void process(float[] left, float[] right, int frames) {
        Arrays.fill(left, 0, frames, 0f);
        Arrays.fill(right, 0, frames, 0f);

        for (var track : tracks) {
            track.process(scratch.L(), scratch.R(), frames);
            for (int i = 0; i < frames; i++) {
                left[i] += scratch.L()[i];
                right[i] += scratch.R()[i];
            }
        }
    }

}
