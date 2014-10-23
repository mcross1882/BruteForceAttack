package pmg.tutorials

import akka.actor.{Actor, ActorLogging, Props}

/**
 * Companion object to SolvingActor in this example we put our 
 * factory method for creating the actor here
 */
object SolvingActor {
    
    /**
     * Its best practice to use a factory method for creating actors
     * with constructor arguments.
     */
    def props(solution: String): Props = Props(new SolvingActor(solution))
}

/**
 * This actor stores a solution and validates possibilities. If there
 * is a match it returns  Success message with the solution. Otherwise
 * it returns a case object represting a Failure
 */
class SolvingActor(solution: String) extends Actor with ActorLogging {

    def receive = {
        case Solution(attemptedValue: String) => {
            if (attemptedValue equals solution) {
                sender ! Success(solution)
            } else {
                sender ! Failure
            }
        }
    }
}
