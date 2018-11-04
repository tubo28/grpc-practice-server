package mygrpc

import java.util.logging.Logger

import io.grpc.{Server, ServerBuilder, Status}
import proto.customer_service.{User, UserRequest, UsersServiceGrpc}
import proto.customer_service.UsersServiceGrpc.UsersService

import scala.concurrent.{ExecutionContext, Future}

class GrpcServer(executionContext: ExecutionContext) {
  self =>
  private val logger = Logger.getLogger(classOf[GrpcServer].getName)
  private val port = sys.env.getOrElse("SERVER_PORT", "50001").toInt
  private[this] var server: Server = _

  def start(): Unit = {
    server = ServerBuilder.forPort(port).addService(
      UsersServiceGrpc.bindService(new UsersServiceImpl, executionContext)
    ).build().start()
  }

  def stop(): Unit = {
    if (server != null) {
      logger.info("*** gRPC server shutdown ***")
      server.shutdown()
    }
  }

  def blockUnitShutdown() : Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }
}

object GrpcServer {
  def main(args: Array[String]): Unit = {
    val server = new GrpcServer(ExecutionContext.global)
    server.start()
    server.blockUnitShutdown()
  }
}

class UsersServiceImpl extends UsersService {
  var counter = 0
  override def getUsers(request: UserRequest): Future[User] = {
    counter += 1
    if (counter % 3 == 0) {
      Future.successful(new User(0, "taro"))
    } else if (counter % 3 == 1) {
      Future.successful(new User(1, "hanako"))
    } else {
      throw Status.NOT_FOUND.asRuntimeException()
    }
  }
}
