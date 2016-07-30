name := "medstreaming_demo"

version := "1.0"

scalaVersion := "2.11.8"

val PhantomVersion = "1.22.0"
val sparkVersion = "1.6.2"
val akkaVersion = "2.4.7"

resolvers ++= Seq(
  "spray repo"                       at "http://repo.spray.io",
  "Typesafe repository snapshots"    at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo"                    at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases"                at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots"               at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging"                 at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  "Spark Packages Repo"              at "http://dl.bintray.com/spark-packages/maven",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies ++= Seq(
  "com.websudos" %% "phantom-dsl" % PhantomVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M3",
  "com.typesafe.play" %% "play-json" % "2.5.4" exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.3.0",
  "org.ghost4j" % "ghost4j" % "1.0.1"
)

    