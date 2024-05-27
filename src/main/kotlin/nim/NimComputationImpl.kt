package nim

import nim.Level.*
import kotlin.random.Random


class NimComputationImpl : NimComputation {
    private val random: Random = Random(System.currentTimeMillis())
    private fun makeRandomMove(stacks: List<Int>): Move {
        println("Making random move")
        val stack = random.nextInt(0, stacks.size)
        return Move(stack, random.nextInt(0, stacks[stack] + 1))
    }

    private fun makePerfectMove(stacks: List<Int>, nimSum: Int): Move {
        println("Making perfect move")
        val highestBitSet = nimSum.takeHighestOneBit()
        val stackToModify = stacks.indexOfFirst { stack -> (stack and highestBitSet) != 0 }
        val stackSizeToDecrease = stacks[stackToModify] - (stacks[stackToModify] xor nimSum)
        return Move(stackToModify, stackSizeToDecrease)
    }

    override fun computeMove(stacks: List<Int>, level: Level): Move {
        val nimSum = stacks.nimSum()
        println("Nim sum = $nimSum")
        if (nimSum == 0) {
            return makeRandomMove(stacks)
        }
        if (stacks.size <= 2) {
            return makePerfectMove(stacks, nimSum)
        }
        val perfectMoveRatio = random.nextDouble()
        return when {
            level == EASY && perfectMoveRatio < 0.3 -> makePerfectMove(stacks, nimSum)
            level == MEDIUM && perfectMoveRatio < 0.6 -> makePerfectMove(stacks, nimSum)
            level == HARD && perfectMoveRatio < 1.0 -> makePerfectMove(stacks, nimSum)
            else -> makeRandomMove(stacks)
        }
    }
}

private fun Int.nimSum(b: Int): Int = this xor b
private fun List<Int>.nimSum(): Int = this.fold(0, Int::nimSum)
