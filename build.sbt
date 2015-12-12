name := "zmq_tr"

version := "2.4-SNAPSHOT"

scalaVersion := "2.10.0"

libraryDependencies ++= {
val testVersion = "1.9.2"
val akkaVersion = "2.3.9"

Seq(
"org.zeromq" %% "zeromq-scala-binding" % "0.0.6",
"com.typesafe.akka" % "akka-actor_2.10" % akkaVersion,
"com.typesafe.akka" % "akka-remote_2.10" % akkaVersion,
"com.typesafe.akka" % "akka-zeromq_2.10" % akkaVersion,
"com.typesafe.akka" % "akka-camel_2.10" % "2.3.14",
"org.apache.camel" % "camel-jetty" % "2.10.3",
"org.apache.camel" % "camel-quartz" % "2.10.3",
"com.typesafe.akka" % "akka-slf4j_2.10" % akkaVersion,
"ch.qos.logback" % "logback-classic" % "1.0.13",
"org.scalatest" % "scalatest_2.10" % testVersion,
"com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
)
}
