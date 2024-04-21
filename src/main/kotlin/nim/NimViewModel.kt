package nim

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import nim.Player.PLAYER_ONE

class NimViewModel(val computation: NimComputation = NimComputationImpl()) {
    val stacks: SnapshotStateList<Int>  = mutableStateListOf(10, 20, 30)
    var _player: MutableStateFlow<Player> = MutableStateFlow(PLAYER_ONE)
    val player = _player.asStateFlow()
    var _level: MutableStateFlow<Level?> = MutableStateFlow(null)
    val level = _level.asStateFlow()

    fun initGame(level: Level, stacksCount: Int, candiesCount: Int) {

    }

}