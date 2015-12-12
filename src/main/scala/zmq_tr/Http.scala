package zmq_tr

/**
 * @author nex
 *
 */


//import akka.
//import org.apache.camel._
import akka.actor.Actor._
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Status.Failure
import akka.actor.actorRef2Scala
import akka.camel._

import org.apache.camel.Endpoint
import akka.remote.EndpointActor
import org.apache.camel.component.jetty.CamelHttpClient

object Http {

  //startCamelService
  
  
  
  class Serv extends Consumer {
    def endpointUri = "jetty:http://127.0.0.1:8877"
    //val service = 
    
    def receive = {
      
      case msg: CamelMessage => sender() ! ("Hello %s" format msg.bodyAs[String])
     
    }
  }
  
  
}