# durationtext

Parse durations like 1h30m into java.time.Duration and format them back.

## Usage

```java
DurationText.parse("1h 30m");              // PT1H30M
DurationText.format(Duration.ofSeconds(93784)); // "1d 2h 3m 4s"
```

## Test

```bash
mvn test
```

## License

MIT
