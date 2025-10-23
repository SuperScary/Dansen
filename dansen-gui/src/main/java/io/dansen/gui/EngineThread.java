package io.dansen.gui;

import io.dansen.core.audio.*;
import io.dansen.core.audio.drivers.JavaSoundOutput;
import io.dansen.core.dsp.Gain;
import io.dansen.core.dsp.Pan;
import io.dansen.core.dsp.SineOsc;
import io.dansen.core.param.FloatParam;
import io.dansen.io.nodes.FileLoaderNode;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple background engine runner controlling playback.
 */
public final class EngineThread {
    private final AudioFormatEx fmt = AudioFormatEx.of44k1();
    private final FloatParam gain = new FloatParam(0.8f);
    private final FloatParam pan = new FloatParam(0f);

    private GraphNode graph;
    private Thread thread;
    private final AtomicBoolean playing = new AtomicBoolean(false);

    public EngineThread() {
        setSource(null); // default tone
    }

    public void setSource(File file) {
        AudioProcessor src = (file == null)
                ? new SineOsc(220f, fmt.sampleRate())
                : new FileLoaderNode(file, false);
        graph = new Mixer(fmt.blockSize())
                .add(new Track(new Pan(new Gain(src, gain), pan)));
    }


    public FloatParam gainParam() {
        return gain;
    }

    public FloatParam panParam() {
        return pan;
    }


    public void play() {
        if (playing.get()) return;
        playing.set(true);
        thread = new Thread(() -> {
            try (var out = new JavaSoundOutput(fmt.sampleRate(), fmt.blockSize())) {
                var engine = new AudioEngine(fmt, (l, r, n) -> graph.process(l, r, n));
// run for a long time; stopping flips the flag
                while (playing.get()) {
                    engine.renderToDriver(out::write, 1);
                }
            } finally {
                playing.set(false);
            }
        }, "Dansen-Engine");
        thread.setDaemon(true);
        thread.start();
    }


    public void stop() {
        playing.set(false);
        if (thread != null) try {
            thread.join(200);
        } catch (InterruptedException ignored) {
        }
    }
}
