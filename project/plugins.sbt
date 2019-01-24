// Comment to get more information during initialization
logLevel := Level.Warn

//import Dependencies._
lazy val playVersion = "2.7.0-M4"
//lazy val scalajsVersion = "1.0.0-M6"
lazy val scalajsVersion = "0.6.26"


// Resolvers
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// Sbt plugins
// for Scala.js 1.x.
// addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.8")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.8-0.6")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalajsVersion)

// for Scala.js 1.x.
//addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % scalajsVersion)
//libraryDependencies += "org.scala-js" %% "scalajs-env-nodejs" % scalajsVersion

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % playVersion)
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.5.0")
