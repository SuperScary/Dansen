package io.dansen.core.dsp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class SineOscTest {
    @Test
    void generatesSamples() {
        SineOsc osc = new SineOsc(440f, 44_100);
        float[] l = new float[128], r = new float[128];
        osc.process(l, r, 128);
        assertThat(l).hasSize(128);
        assertThat(l[0]).isBetween(-1f, 1f);
    }
}
