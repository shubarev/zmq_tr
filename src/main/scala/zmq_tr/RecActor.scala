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
class RecActor extends Actor {
  val name = "RECEIVER => "

  def preRestart() = {
  }

  val handler = context.actorOf(Props(classOf[RecHandler]))

  val repSocket = ZeroMQExtension(context.system).newRepSocket(Array(Connect("tcp://127.0.0.1:2553"), Listener(handler)))

  def receive: Receive = {
    case m: Any =>
      println(name + m)
    case Connecting =>
      println("RecActor Connected")

  }
}
