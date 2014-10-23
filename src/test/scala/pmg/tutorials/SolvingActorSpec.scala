package pmg.tutorials

import org.scalatest._
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.testkit.TestActorRef
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import scala.language.postfixOps
import scala.util.{Success => ScalaSuccess}

class SolvingActorSpec extends FlatSpec with Matchers {

    "A SolvingActor" should "return a Success message when the solution matches the input" in {     
        implicit val system = ActorSystem("BruteForceSystem")
        implicit val timeout = Timeout(5 seconds)

        val actorRef = TestActorRef(new SolvingActor("pass"))

        // Lets send a simlulated "Solution" to the actor
        val future = actorRef ? Solution("pass")
        
        // Now we retreive the response similar to the "receive" method in the actor
        val ScalaSuccess(Success(solution: String)) = future.value.get

        // Finally we make the assertion
        solution should be("pass")
        
        system.shutdown
    }
    
    it should "return a Failure message when the solution does not match the input" in {        
        implicit val system = ActorSystem("BruteForceSystem")
        implicit val timeout = Timeout(5 seconds)

        val actorRef = TestActorRef(new SolvingActor("wrong"))
        
        val future = actorRef ? Solution("isThisRight")
        
        future.value.get should be(ScalaSuccess(Failure))
        
        system.shutdown
    }
}

