package nim

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import nim.Player.*
import java.time.Instant
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class NimViewModel(val computation: NimComputation = NimComputationImpl()) {
    val stacks: SnapshotStateList<Int> = mutableStateListOf(10, 20, 30)
    private var _player: MutableStateFlow<Player> = MutableStateFlow(PLAYER_ONE)
    val player = _player.asStateFlow()
    private var _level: MutableStateFlow<Level?> = MutableStateFlow(null)
    val level = _level.asStateFlow()
    private val _gameOver:  MutableStateFlow<Boolean> = MutableStateFlow(false)
    val gameOver = _gameOver.asStateFlow()
    var initialCandiesCount: Int = 0
    var initialStacksCount: Int = 0

    fun initGame(level: Level, stacksCount: Int, candiesCount: Int) {
        initialCandiesCount = candiesCount
        initialStacksCount = stacksCount
        _level.value = level
        _player.value = PLAYER_ONE
        stacks.clear()
        stacks.addAll(generateStacks(stacksCount, candiesCount))
        _gameOver.value = false
    }

    private fun generateStacks(stacksCount: Int, candiesCount: Int): List<Int> {
        val random = Random(Instant.now().toEpochMilli())
        val tempStacks =
            (1..stacksCount).map { random.nextInt(candiesCount / stacksCount / 2, candiesCount / stacksCount) }
        println(tempStacks)
        val sum = tempStacks.sum()
        println(sum)
        return tempStacks
            .map { it + (candiesCount - sum) / stacksCount }
            .mapIndexed { i, stack ->
                if (i < (candiesCount - sum) % stacksCount) stack + 1 else stack
            }
    }

    fun makePlayerMove(move: Move) {
        makeMove(move)
        if (checkAndSetGameOver()) {
            return
        }
        if (level.value == Level.TWO_PLAYERS) {
            _player.getAndUpdate { if (it == PLAYER_TWO) PLAYER_ONE else PLAYER_TWO }
        }
        else {
            _player.value = COMPUTER
            makeComputerMove()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun makeComputerMove() {
        GlobalScope.launch {
            delay(500)
            level.value?.let {
                val move = computation.computeMove(stacks, it)
                makeMove(move)
                if(!checkAndSetGameOver()) {
                    _player.value = PLAYER_ONE
                }
            }
        }
    }

    fun restart() {
        level.value?.let { initGame(it, initialStacksCount, initialCandiesCount) }
    }

    private fun checkAndSetGameOver(): Boolean {
        if (stacks.isEmpty()) {
            _gameOver.compareAndSet(false, update = true)
            return true
        }
        return false
    }

    private fun makeMove(move: Move) {
        if (stacks[move.stackNumber] <= move.size) {
            stacks.removeAt(move.stackNumber)
        } else {
            stacks[move.stackNumber] -= move.size
        }
    }
}