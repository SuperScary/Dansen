package io.dansen.cli;

import io.dansen.core.audio.*;
import io.dansen.core.audio.drivers.*;
import io.dansen.core.dsp.*;

import java.io.File;


public class Demo {
  public static void main(String[] args) throws Exception {
    var fmt = AudioFormatEx.of44k1();
    var osc = new SineOsc(220f, fmt.sampleRate());
    var chain = new Pan(new Gain(osc, 0.2f), -0.2f);
    var mixer = new Mixer(fmt.blockSize()).add(new Track(chain));
    var engine = new AudioEngine(fmt, mixer);


    // 1) Play for 3 seconds via JavaSound
    try (var out = new JavaSoundOutput(fmt.sampleRate(), fmt.blockSize())) {
      engine.renderToDriver(out::write, 3);
    }


    // 2) Offline render to WAV (1 second)
    try (var writer = new WavWriter(new File("demo.wav"), fmt.sampleRate())) {
      engine.renderToDriver((l, r) -> {
        try {
          writer.writeBlock(l, r, fmt.blockSize());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }, 1);
    }
    System.out.println("Wrote demo.wav");
  }
}
