name := "event_storage"

version := "1.0"

scalaVersion := "2.11.8"

val akkaStreamV = "2.0.3"
val akkaBaseV   = "2.4.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % akkaBaseV,
  "com.typesafe.akka" %% "akka-testkit" % akkaBaseV % Test,
  "com.typesafe.akka" %% "akka-remote" % akkaBaseV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaBaseV,

  "com.typesafe.akka" %% "akka-http-core" % akkaBaseV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaBaseV,

  "ch.qos.logback" % "logback-core" % "1.1.5",
  "ch.qos.logback" % "logback-classic" % "1.1.5"
)