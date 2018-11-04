import com.trueaccord.scalapb.compiler.Version.{grpcJavaVersion, scalapbVersion, protobufVersion}

name := "grpc-practice"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % grpcJavaVersion,
  "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
  "io.grpc" % "grpc-all" % grpcJavaVersion
)

PB.targets in Compile := Seq(scalapb.gen() -> ((sourceManaged in Compile).value / "protobuf-scala"))
PB.protoSources in Compile += (baseDirectory in LocalRootProject).value / "protocol"
