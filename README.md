# Dansen


**Dansen** is an open-source, modular audio mixing platform written in **Java 21**. It aims to provide a clean node-graph engine, real-time playback, offline rendering, and a plugin-friendly Decoder SPI.


### Features (0.1.0‑SNAPSHOT)
- Pull-based audio graph (Mixer → Tracks → Processors)
- Java Sound driver + WAV export
- **Decoder SPI** (WAV/AIFF via JavaSound; FLAC via third-party provider)
- **Parameters** with linear ramps (automatable Gain/Pan)
- CLI demo: play full song, mix mic, export `demo_mix.wav`


### Getting Started
```bash
# play a file + mix mic, then export full-length demo_mix.wav
./gradlew :dansen-cli:run --args="/path/to/song.wav"
# or no args → 5s tone + mic
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
