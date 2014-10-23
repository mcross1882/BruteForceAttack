package pmg.tutorials

class BruteForceActor(actor: Actor) extends Actor with ActorLogging {

    private val Alphabet = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    
    def crack(startingLength: Int, maxLength: Int) {
        val builder = new StringBuilder(maxLength)

        for (index <- startingLength to maxLength) {
            builder.clear
            solve(builder, 0, index)
        }
    }
    
    protected def solve(builder: StringBuilder, index: Int, maxDepth: Int) {
        val trueDepth = maxDepth - 1
        
        for (letter <- Alphabet) {
            builder.replace(index, index + 1, letter.toString)
            
            if (index == trueDepth) {
                actor ! Solution(builder.toString)
            } else {          
                solve(builder, index + 1, maxDepth)
            }
        }
    }
    
    def receive = {
        case Success(solution) => println("Found password: %s".format(solution))
        case Failure => println("Failure")
        case Booted => throw new Exception("Oops! Looks like you were booted!")
    }
}