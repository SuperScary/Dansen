package io.dansen.core.audio;

/**
 * Pull-based processor; fill left/right buffers with frames of audio.
 */
public interface AudioProcessor {

    void process(float[] left, float[] right, int frames);

}
