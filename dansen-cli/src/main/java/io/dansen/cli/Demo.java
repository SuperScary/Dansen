package io.dansen.cli;

import io.dansen.core.audio.*;
import io.dansen.core.audio.drivers.JavaSoundOutput;
import io.dansen.core.audio.drivers.WavWriter;
import io.dansen.core.dsp.Gain;
import io.dansen.core.dsp.Pan;
import io.dansen.core.dsp.SineOsc;
import io.dansen.core.param.FloatParam;
import io.dansen.core.param.Schedulers;
import io.dansen.io.MicInput;
import io.dansen.io.RingBuffer;
import io.dansen.io.nodes.FileLoaderNode;

import java.io.File;

public class Demo {
    public static void main(String[] args) throws Exception {
        var fmt = AudioFormatEx.of44k1();


        // Source: file if provided, else tone
        AudioProcessor src;
        int seconds;
        if (args.length > 0) {
            File f = new File(args[0]);
            var fileNode = new FileLoaderNode(f, false);
            src = fileNode;
            // crude duration estimate: use file length / (sr*ch*bytesPerSample); fallback 30s
            long bytes = f.length();
            int sr = Math.max(1, fileNode.sourceSampleRate());
            int ch = 2;
            int bps = 2; // 16-bit stereo target stream
            seconds = (int) Math.max(1, bytes / ((long) sr * ch * bps));
        } else {
            src = new SineOsc(220f, fmt.sampleRate());
            seconds = 5;
        }

        // Parameters & automation
        var gainParam = new FloatParam(0.0f);
        var panParam = new FloatParam(0.0f);

        // Schedule ramps: 3s fade-in to 0.8, slow L→R pan over demo length
        gainParam.rampTo(0.8f, Schedulers.secondsToSamples(3.0, fmt.sampleRate()));
        panParam.rampTo(1.0f, Schedulers.secondsToSamples(seconds, fmt.sampleRate()));


        var chain = new Pan(new Gain(src, gainParam), panParam);

        // Optional mic mix at -6 dB
        var mic = new MicInput(fmt.sampleRate(), fmt.blockSize(), new RingBuffer(fmt.blockSize() * 8));
        mic.start();
        AudioProcessor micNode = (l, r, n) -> {
            float[] ml = new float[n];
            float[] mr = new float[n];
            mic.read(ml, mr, n);
            for (int i = 0; i < n; i++) {
                l[i] += ml[i] * 0.5f;
                r[i] += mr[i] * 0.5f;
            }
        };


        var mixer = new Mixer(fmt.blockSize()).add(new Track(chain)).add(new Track(micNode));
        var engine = new AudioEngine(fmt, mixer);


        // Realtime playback
        try (var out = new JavaSoundOutput(fmt.sampleRate(), fmt.blockSize())) {
            engine.renderToDriver(out::write, seconds);
        }
        mic.close();


        // Offline export of full length
        try (var writer = new WavWriter(new File("demo_mix.wav"), fmt.sampleRate())) {
            engine.renderToDriver((l, r) -> {
                try {
                    writer.writeBlock(l, r, fmt.blockSize());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, seconds);
        }
        System.out.println("Finished playback and wrote demo_mix.wav");
    }
}
