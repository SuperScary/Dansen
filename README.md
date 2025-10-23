# Dansen


**Dansen** is an open-source, modular audio mixing platform written in **Java 21**. It aims to provide a clean node-graph engine, real-time playback, offline rendering, and a friendly plugin API for DSP.


### Features (0.1.0‑SNAPSHOT)
- Pull-based audio graph (Mixer → Tracks → Processors)
- Java Sound driver (portable) + WAV export
- Basic DSP blocks: Sine oscillator, Gain, Pan
- CLI demo to hear a tone and export `demo.wav`


### Getting Started
```bash
./gradlew :dansen-cli:run
```

The demo will play ~3 seconds of a sine tone and write demo.wav.

# Modules

- dansen-core – engine, DSP, drivers

- dansen-cli – small demo app

# Roadmap

See [docs/ROADMAP.md](docs/ROADMAP.md).

# Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md). By participating, you agree to the Code of Conduct.

# License

Apache-2.0. See [LICENSE](LICENSE.md).
