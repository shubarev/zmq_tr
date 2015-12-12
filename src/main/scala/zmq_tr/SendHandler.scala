package zmq_tr
import akka.zeromq._
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorLogging
import akka.serialization.SerializationExtension
import java.lang.management.ManagementFactory
import akka.actor.ActorRef
import akka.util.ByteString
import scala.Int
import scalax.file.Path
import org.zeromq.zmq_msg_t
import java.nio.channels.FileChannel;

//import java.lang._
//import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption;
//import scala.util.control.Breaks.TryBlock

/**
 * @author nex
 */
class SendHandler extends Actor {

  val name = "Jacob: "
  val brother = "Wilhelm: "
  val uCan = "Yes, you can"

  val path: Path = Path.fromString("/home/nex/FILE")

  var mCounter: Int = 0

  //val PATH = path;
  var OFFSET: Int = 0
  var currentChunkSize: Int = 0
  var CHUNK: Int = 500000
  val PATH = java.nio.file.Paths.get("/home/nex/FILE.JPG")
  var FILE_LENGTH: Long = PATH.toFile().length()

  def receive: Receive = {
    //.utf8String
    case m: ZMQMessage if m.frame(0) == ByteString(uCan) =>
      println(brother + uCan)
      var connection = sender()
      val fileChannel: FileChannel = FileChannel.open(PATH, java.nio.file.StandardOpenOption.READ)
      transport(PATH, fileChannel, connection)
      //sender() ! ZMQMessage(ByteString("fly"))
      
      context become {
        case m: ZMQMessage if m.frame(0) == ByteString(mCounter) =>
          transport(PATH, fileChannel, connection)
          //sender() ! ZMQMessage(ByteString("fly"))
          println(mCounter)
          mCounter += 1
          if (OFFSET >= FILE_LENGTH)
            fileChannel.close();
            //connection ! ZMQMessage(ByteString("fly"))
      }
    // fileChannel.close();

    case m: Any =>
      println(brother + m)

  }

  def create(path: Path) {
    // import scalax.file.Path
    //    val path: Path = Path ("/tmp/file")
    //path.createFile()
    path.createFile(failIfExists = true)
    //path.createDirectory()
    //path.createDirectory(failIfExists = false)
  }

  def delete(path: Path) {
    //val path: Path = Path ("/tmp/file")
    // path.delete()
    path.deleteIfExists()
    //  path.deleteRecursively()
    // path.deleteRecursively(true)
    // path.deleteRecursively(continueOnFailure = true)
  }

  def transport(PATH: java.nio.file.Path, fileChannel: FileChannel, connection: ActorRef) {

    import java.nio.ByteBuffer;

    try {
      if (OFFSET < FILE_LENGTH
        && !Thread.currentThread().isInterrupted()) {

        currentChunkSize = CHUNK;

        if (PATH.toFile().length() < OFFSET
          + CHUNK) {
          currentChunkSize = (FILE_LENGTH - OFFSET).toInt;
        }

        if (currentChunkSize > 0) {

          var byteBuffer: ByteBuffer = ByteBuffer
            .allocate(currentChunkSize);
          fileChannel.read(byteBuffer, OFFSET);
          byteBuffer.flip();
          connection ! ZMQMessage(ByteString.fromByteBuffer(byteBuffer))
          byteBuffer.clear();
          OFFSET += currentChunkSize;
        }
      }
    } catch {
      case t: Throwable => t.printStackTrace() // TODO: handle error
    }
  }

}