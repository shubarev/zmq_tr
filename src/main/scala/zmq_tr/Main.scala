package zmq_tr
import akka.zeromq.ZeroMQExtension
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.zeromq.SocketType
import akka.zeromq.Subscribe
import akka.actor.Props
import akka.actor.ActorRef
import sun.nio.ch.SelChImpl
import ch.qos.logback.core.util.TimeUtil
import java.util.concurrent.TimeUnit
import scalax.file.Path

/**
 * @author nex
 */
object Main extends App {

  //@Override
  def main() {
    val sys = ActorSystem.create("sys")
    println("main")

    //  val path: Path = Path.fromString("/home/nex/")
    //    path.createFile()
    //    path.createFile(failIfExists=false)
    //    path.createDirectory()
    //    path.createDirectory(failIfExists=false)
    //    

    // TimeUnit.SECONDS.sleep(1);
    val receiver = sys.actorOf(Props[RecActor])
    receiver.tell("START", ActorRef.noSender)

    val sender = sys.actorOf(Props[SendActor])

    sender.tell("START", ActorRef.noSender)
   
  }
  main()
}