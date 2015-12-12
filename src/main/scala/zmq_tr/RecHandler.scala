package zmq_tr

import akka.zeromq._

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorLogging
import akka.serialization.SerializationExtension
import java.lang.management.ManagementFactory
import akka.actor.ActorRef
import akka.util.ByteString
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption;

/**
 * @author nex
 */
class RecHandler extends Actor {
  val name = "Wilhelm: "
  val brother = "Jacob: "
  val mayI = "May I send you a file?"
  val uCan = "Yes, you can"
  var FILE_LENGTH: Long = 0
  val PATH = java.nio.file.Paths.get("/home/nex/Actor_receiver/FILE_1.JPG")
  var mCounter: Int = 0

  var OFFSET: Int = 0
  var currentChunkSize: Int = 0
  var CHUNK: Int = 500000
  var CURRENT_OFFSET: Int = 0

  def receive = {

    case Connecting =>
      println(name + "Wilhelm on receive")

    case m: ZMQMessage if m.frame(0) == ByteString(mayI) =>
      sender() ! ZMQMessage(ByteString(uCan))
      println(brother + mayI)

      FILE_LENGTH = m.frame(1).utf8String.toLong
      //println(FILE_LENGTH)

      val fileChannel = FileChannel.open(PATH,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND)
      context become {
        case m: ZMQMessage =>
          println(brother + mCounter)
    
          writeData(m.frame(0), PATH, fileChannel)
          
          sender() ! ZMQMessage(ByteString(mCounter))
          mCounter += 1
      }

    case m: Any =>
      println(name + m)
  }

  def writeData(data: ByteString, PATH: java.nio.file.Path, fileChannel: FileChannel) {

    CURRENT_OFFSET = data.length
    if (CURRENT_OFFSET > 0) {
      fileChannel.write(data.asByteBuffer, OFFSET);
      OFFSET += CURRENT_OFFSET;
    }
  }
}