package zmq_tr
import akka.zeromq._
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorLogging
import akka.serialization.SerializationExtension
import java.lang.management.ManagementFactory
import akka.actor.ActorRef
import akka.util.ByteString

/**
 * @author nex
 */
class SendActor extends Actor {
  val name = "SENDER => "
  val mayI = "May I send you a file?"

  val PATH = java.nio.file.Paths.get("/home/nex/FILE.JPG")
  val FILE_LENGTH: Long = PATH.toFile().length()

  def preRestart() = {
  }

  val handler = context.actorOf(Props(classOf[SendHandler]))
  val reqSocket = ZeroMQExtension(context.system).newReqSocket(Array(Bind("tcp://127.0.0.1:2553"), Listener(handler)))

  def receive: Receive = {

    case m: String if m == "START" =>
      println(name + m)
      reqSocket ! ZMQMessage(ByteString(mayI),ByteString(FILE_LENGTH.toString()))

    case m: Any =>
      println(name + m)
  }
}