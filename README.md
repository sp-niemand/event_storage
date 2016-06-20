# Event storage

## What it needs
* `JRE 8`
* `sbt`
* `MongoDB`

## What is this
This is a simple prototype of event storing Web-application.
 
## How to run in dev mode
To run just `sbt run`. You can find some pretty self-explanatory
parameters in `src/main/resources/application.conf`.
Textual logs will be produced in `logs` directory.

Application has a simple HTTP REST interface with following points:
* `POST /event` - you can send an event JSON here
* `GET /event/counter` - you can get a counters of event types here
Event JSON format:
```javascript
{
    "event_type": "test", // event type
    "params": {"a": 1, "b": 2, "c": {"t": 0.001}}, // plain old javascript object
    "ts": 123454678 // timestamp
}
```

The easiest way to run MongoDB for this app is to use Docker:
`docker run -p 54321:27017 -d mongo:2.6`
I had some strange behavior with my dockerized MongoDB though. Maybe
you'd prefer to just install Mongo. Don't forget to change the port
in `application.conf` then. It's non-standard there.