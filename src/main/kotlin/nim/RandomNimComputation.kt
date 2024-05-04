package nim

class RandomNimComputation: NimComputation {
    override fun computeMove(stacks: List<Int>, level: Level): Move {
        return Move(0, (1..stacks[0]).random())
    }
}