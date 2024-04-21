package nim

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

class NimViewModel(val computation: NimComputation = NimComputationImpl()) {
    private var _counter = mutableStateOf(0)
    val counter: State<Int> = _counter.asIntState()
    val stacks: SnapshotStateList<Int>  = mutableStateListOf(10, 20, 30)

    fun incrementCounter() {
        var newValue = computation.incrementCounter(counter.value)
        _counter.value = newValue
    }

}