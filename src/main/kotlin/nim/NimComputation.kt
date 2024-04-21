package nim

interface NimComputation {
    fun incrementCounter(counter: Int): Int {
        return counter + 1
    }

    fun computeMove(stacks: List<Int>, level: Level): Move
}

data class Move(val stackNumber: Int, val size: Int)

enum class Level { EASY, MEDIUM, HARD }
