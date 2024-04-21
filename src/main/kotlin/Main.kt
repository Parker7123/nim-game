import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import nim.Move
import nim.NimViewModel

@Composable
@Preview
fun App() {
    val state = remember { NimViewModel() }
    var moveDialogVisible by remember { mutableStateOf(false) }
    var selectedStack by remember { mutableIntStateOf(0) }
    MaterialTheme {
        if (moveDialogVisible) {
            var value by remember { mutableStateOf("") }
            AlertDialog(
                title = {
                        Text("How many items do you want to remove?")
                },
                text = {
                    TextField(value.toString(), onValueChange = { value = it })
                },
                confirmButton = {
                    Button(onClick = {
                        makeMove(state.stacks, Move(stackNumber = selectedStack, size = value.toInt()))
                        moveDialogVisible = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { moveDialogVisible = false }) {
                        Text("Cancel")
                    }
                },
                onDismissRequest = { moveDialogVisible = false }
            )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NimTile(count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier, onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)
        ) {
            Text(count.toString(), fontSize = 30.sp)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
