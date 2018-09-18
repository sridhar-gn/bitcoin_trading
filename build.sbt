name := "Test"

version := "1.0"

scalaVersion := "2.11.8"

parallelExecution in Test := true

val akkaV = "10.0.9"

lazy val testDependencies = Seq("org.scalatest" % "scalatest_2.11" % "3.0.1" % Test,
  "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.5.0" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV)

lazy val deliveryDependencies =   Seq(
  "com.typesafe.akka" %% "akka-http" % akkaV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaV)

val injectionDependencies = Seq("org.scaldi" %  "scaldi_2.11" % "0.5.7" )

lazy val logDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3")


libraryDependencies ++= testDependencies ++ deliveryDependencies ++
  injectionDependencies ++ logDependencies

mainClass := Some("com.test.tookitaki.MainApp")
    