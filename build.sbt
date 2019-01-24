import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

EclipseKeys.skipParents in ThisBuild := false

scalaJSStage in Global := FastOptStage

import Dependencies._

scalacOptions += "-P:scalajs:sjsDefinedByDefault"

lazy val server = (project in file("server"))
  .settings(commonSettings)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
        "com.vmunier" %% "scalajs-scripts" % "1.1.2",
        guice,
        specs2 % Test,
        "com.typesafe.play" %% "play-json" % "2.7.0-RC2",
        // "de.heikoseeberger" %% "akka-http-upickle" % "1.24.3"
    ),
    // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
    EclipseKeys.preTasks := Seq(compile in Compile)
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.6"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb, JSDependenciesPlugin)
  .dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
  .jsConfigure(_.enablePlugins(JSDependenciesPlugin))
  
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "com.example",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.7.1",
    "com.lihaoyi" %%% "upack" % "0.7.1"
  )
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
