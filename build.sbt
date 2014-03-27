name := "angular-marvel"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.ee" %% "assets-loader" % "0.12.1"
)


resolvers += "ed eustace" at "http://edeustace.com/repository/releases"

resolvers += "ed eustace" at "http://edeustace.com/repository/snapshots"


play.Project.playScalaSettings
