package pmg.tutorials

case class Solution(attemptedValue: String)
case class Success(solution: String)
case object Failure
case object Booted

class SolvingActor(solution: String) with Actor with ActorLogging {

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
