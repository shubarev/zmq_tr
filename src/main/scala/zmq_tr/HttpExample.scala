package zmq_tr

/**
 * @author nex
 */
import org.apache.camel.Exchange
import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.actor.Status.Failure
import akka.camel.{ CamelMessage, CamelExtension }
import akka.camel._
import akka.camel.Consumer
import akka.camel.Producer
import akka.util.ByteString
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.jetty.CamelHttpClient
import org.apache.camel.component.jetty.JettyHttpBinding
import org.apache.camel.component.http.HttpBinding
import org.apache.commons.codec.StringEncoder
//import org.apache.camel.component.http.HttpBinding

object HttpExample {

  val producerEndpoint = "jetty:http://127.0.0.1:8080"
  val consumerEndpoint = "jetty:http://0.0.0.0:8080"

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system")
    val httpTransformer = system.actorOf(Props[HttpTransformer])
    // val httpProducer = system.actorOf(Props(classOf[HttpProducer], httpTransformer))

    //val httpConsumer = system.actorOf(Props(classOf[HttpConsumer], httpProducer))
    //new v

    val responder = system.actorOf(Props(classOf[TestServer], consumerEndpoint), name = "TestResponder")

//    val httpProducer = system.actorOf(Props(classOf[HttpProducer], httpTransformer, producerEndpoint), name = "TestProducer")
//    val camel = CamelExtension(system)
//    camel.context.addRoutes(new CustomRouteBuilder(producerEndpoint, consumerEndpoint))

    //    val responder = system.actorOf(Props[TestServer], name = "TestResponder")
    //    camel.context.addRoutes(new CustomRouteBuilder(system))

    //httpProducer ! CamelMessage("hi", Map(CamelMessage.MessageExchangeId -> "header1"))
    //httpProducer ! CamelMessage("world", Map(CamelMessage.MessageExchangeId -> "header2"))
    //httpProducer ! "akka" match {
    //  case m: Any => println(m)
    // }
  }

  class HttpConsumer(producer: ActorRef) extends Consumer {
    def endpointUri = "jetty:http://0.0.0.0:8875"

    // producer ! CamelMessage("Akka", Map(CamelMessage.MessageExchangeId -> "header1"))
    def receive = {
      case msg: CamelMessage =>
        println("FROM SERVER: " + msg.body.toString())
        //sender() ! CamelMessage("hi", Map(CamelMessage.MessageExchangeId -> "header2"))
      case msg => sender() forward msg
      // CamelMessage()
    }
  }

  class HttpProducer(transformer: ActorRef, endpoint: String) extends Actor with Producer {
   
    def endpointUri = endpoint
    override def transformOutgoingMessage(msg: Any) = msg match {
      case camelMsg: CamelMessage => camelMsg.copy(headers =
        camelMsg.headers(Set(Exchange.HTTP_PATH)))
    }

    // instead of replying to the initial sender(), producer actors can implement custom
    // response processing by overriding the routeResponse method
     override def routeResponse(msg: Any) { sender() forward msg }
  }

  class HttpTransformer extends Actor {
    def receive = {
      case msg: CamelMessage =>
        sender() ! (msg.mapBody { body: Array[Byte] =>
          new String(body).replaceAll("Akka ", "AKKA ")
        })
      case msg: Failure => sender() ! msg
    }
  }

  class CustomRouteBuilder(fromEndpoint: String, toEndpoint: String)
      extends RouteBuilder {
    def configure {
      from(fromEndpoint).to(toEndpoint)
    }
  }
  
  class TestServer(endpoint: String) extends Actor with Consumer {
   
    def endpointUri = endpoint
    def receive = {
      case msg: CamelMessage =>
        println("TEST_SERVER RECEIVE: " + msg)
        println(msg.body.formatted("ByteString"))
    }
  }

}