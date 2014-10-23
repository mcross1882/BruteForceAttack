package pmg.tutorials

import akka.actor.{Actor, ActorLogging, PoisonPill}

/**
 * Messaging classes for communitcating between actors
 * these primarily enforce a typehint and small amount of
 * meta data that acts as a payload
 */
case class Crack(startingLength: Int, maxLength: Int)

case class Solution(attemptedValue: String)

case class Success(solution: String)

case object Failure

class BruteForceActor extends Actor with ActorLogging {
    /**
     * Here we concatenate "ranges" of valid characters to form a
     * single sequnce
     */
    private val Alphabet = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    
    /**
     * We lookup our SolvingActor and store it for messaging
     */
    private val solver = context.actorSelection("/user/solver")
    
    /**
     * This is the receiver function that is overriden
     * from the Actor class. Here we listen for messages
     * and act accordingly
     */
    def receive = {
        case Crack(startingLength, maxLength) => crack(startingLength, maxLength)
        case Success(solution) => {
            println("Found password: %s".format(solution))
            context.system.shutdown
        }
        case Failure => // noop
    }

    /**
     * This is the entry point for the brute force algorithm
     */
    protected def crack(startingLength: Int, maxLength: Int) {
        val builder = new StringBuilder(maxLength)

        for (index <- startingLength to maxLength) {
            builder.clear
            solve(builder, 0, index)
        }
    }
    
    /**
     * Recursive solver method that sends possible solutions to our
     * SolvingActor reference
     */
    protected def solve(builder: StringBuilder, index: Int, maxDepth: Int) {
        val trueDepth = maxDepth - 1
        
        for (letter <- Alphabet) {
            builder.replace(index, index + 1, letter.toString)
            
            if (index == trueDepth) {
                solver ! Solution(builder.toString)
            } else {          
                solve(builder, index + 1, maxDepth)
            }
        }
    }
}

