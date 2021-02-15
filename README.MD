## Assetto Corsa Telemetry Client
Assetto Corsa Telemetry Client is a Java library to listen telemetry over UPD.  
[AC Remote Telemetry Documentation](https://docs.google.com/document/d/1KfkZiIluXZ6mMhLWfDX1qAGbvhGRC3ZUzjVIt5FQpp4/pub)

![](https://github.com/iimetra/assetto-corsa-telemetry-4j/workflows/tests/badge.svg)

## Installation
Pull the code and install it locally with maven.
```bash
git clone git@github.com:iimetra/assetto-corsa-telemetry-4j.git
cd assetto-corsa-telemetry-4j
mvn clean install
```
Add dependency to your project.
```xml
<dependency>
  <groupId>dev.iimetra</groupId>
  <artifactId>assetto-corsa-telemetry-4j</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage
1. Start Assetto Corsa on your device and get to a track.
2. Run the following snippet which will log you speed to the console.
```java
@Slf4j
public class DemoApplication {
    private final static String PS4_LOCAL_IP_ADDRESS = "192.168.1.5";

    public static void main(String[] args) throws IOException {
        Writer writer = new Writer();
        Reader reader = new Reader();
        PoJoToBinarySerializer serializer = new PoJoToBinarySerializer(writer, reader);
        DatagramSocket socket = new DatagramSocket();
        ACClient client = ACClient.of(PS4_LOCAL_IP_ADDRESS, socket, serializer);

        HandshakeResponse connection = client.connect();
        log.info("Hello, {}", connection.getDriverName());
        log.info("Good choice of {} car", connection.getCarName());
        client.subscribeCarTelemetry();

        CarTelemetry prev = null;
        while (true) {
            CarTelemetry carTelemetry = client.getCarTelemetry();
            if (!carTelemetry.equals(prev)) {
                prev = carTelemetry;
                log.info("Car speed is {} Km/H", carTelemetry.getSpeedKmh());
                log.info("Engine RPM is {}", carTelemetry.getEngineRPM());
                log.info("Steer: {}", carTelemetry.getSteer());
            }
        }
    }

}
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
MIT License

Copyright (c) 2021 Artemii Morozov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.