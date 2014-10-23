package pmg.tutorials

import akka.actor.{ActorSystem, Props}

object Application {
    
    /**
     * Main entry point
     *
     * $ app [password] [startingLength] [maxLength]
     */
    def main(args: Array[String]) {
        val system = ActorSystem("BruteForceSystem")
        
        val bruteActor = system.actorOf(Props[BruteForceActor], name = "generator")
        
        val solvingActor = system.actorOf(SolvingActor.props(args(0)), name = "solver")
        
        bruteActor ! Crack(args(1).toInt, args(2).toInt)
    }

}
