# Contributing to Dansen


Thanks for your interest! Please follow the workflow below.


## Development Quickstart
1. Install JDK 21 (Temurin recommended)
2. `./gradlew build`
3. Run demo: `./gradlew :dansen-cli:run`


## Code Style
- Java 21, Lombok required.
- Spotless (google-java-format) enforced: `./gradlew spotlessApply`
- Keep real-time paths allocation-free (no boxing, streams, logging in the hot loop)


## Commit Messages
- Conventional Commits style (`feat:`, `fix:`, `docs:`, etc.)


## Pull Requests
- Include tests where sensible
- Ensure CI passes


## Code of Conduct
Be kind and constructive. Harassment or discrimination are not tolerated.
