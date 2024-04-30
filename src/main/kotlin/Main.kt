import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import nim.Move
import nim.NimViewModel
import ui.MenuScreen
import ui.PileOfDots

@Composable
@Preview
fun App() {
    val state = remember { NimViewModel() }
    var showMenuScreen by remember { mutableStateOf(true) }
    var moveDialogVisible by remember { mutableStateOf(false) }
    var selectedStack by remember { mutableIntStateOf(0) }
    MaterialTheme {
        if (showMenuScreen) {
            MenuScreen(modifier = Modifier.fillMaxSize()) { level, stacksCount, candiesCount ->
                state.initGame(level, stacksCount, candiesCount)
                showMenuScreen = false
            }
            return@MaterialTheme
        }
        if (moveDialogVisible) {
            MoveDialog(onMoveMade = { value ->
                makeMove(state.stacks, Move(stackNumber = selectedStack, size = value))
                moveDialogVisible = false
            }, onDismiss = {
                moveDialogVisible = false
            })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            state.stacks.forEachIndexed { i, stack ->
                NimTile(stack, modifier = Modifier.padding(start = 15.dp, end = 15.dp), onClick = {
                    moveDialogVisible = true
                    selectedStack = i
                })
            }
        }
    }
}

fun makeMove(stacks: MutableList<Int>, move: Move) {
    if (stacks[move.stackNumber] == move.size) {
        stacks.removeAt(move.stackNumber)
    } else {
        stacks[move.stackNumber] -= move.size
    }
}

@Composable
fun MoveDialog(onMoveMade: (value: Int) -> Unit, onDismiss: () -> Unit) {
    var value by remember { mutableStateOf("") }
    AlertDialog(title = {
        Text("How many items do you want to remove?")
    }, text = {
        TextField(value, onValueChange = { value = it })
    }, confirmButton = {
        Button(onClick = {
            onMoveMade(value.toInt())
        }) {
            Text("OK")
        }
    }, dismissButton = {
        Button(onClick = onDismiss) {
            Text("Cancel")
        }
    }, onDismissRequest = onDismiss
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NimTile(count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier, onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PileOfDots(
                numCircles = count,
                diameter = 7,
                modifier = Modifier
                    .widthIn(150.dp, 150.dp)
                    .heightIn(80.dp, 80.dp)
                    .padding(10.dp)
            )
            Text(count.toString(), fontSize = 30.sp)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
