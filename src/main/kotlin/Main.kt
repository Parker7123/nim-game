import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
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
    var selectedStack by remember { mutableIntStateOf(-1) }
    MaterialTheme {
        if (showMenuScreen) {
            MenuScreen(modifier = Modifier.fillMaxSize()) { level, stacksCount, candiesCount ->
                state.initGame(level, stacksCount, candiesCount)
                showMenuScreen = false
            }
            return@MaterialTheme
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            state.stacks.forEachIndexed { i, stack ->
                NimTile(count = stack,
                    inputVisible = selectedStack == i,
                    modifier = Modifier.heightIn(min = 200.dp, max = 200.dp).padding(start = 15.dp, end = 15.dp),
                    onClick = {
                        selectedStack = i
                    },
                    onAccept = {
                        makeMove(state.stacks, Move(stackNumber = i, size = it))
                        selectedStack = -1
                    },
                    onDismiss = { selectedStack = -1 })
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
fun NimTile(
    count: Int,
    inputVisible: Boolean,
    onClick: () -> Unit,
    onAccept: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) = Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
    Card(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PileOfDots(
                numCircles = count,
                diameter = 7,
                modifier = Modifier.widthIn(150.dp, 150.dp).heightIn(80.dp, 80.dp).padding(10.dp)
            )
            Text(count.toString(), fontSize = 30.sp)
        }
    }
    if (inputVisible) {
        TextInputWithTwoIcons(onAccept, onDismiss)
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun TextInputWithTwoIcons(onAccept: (Int) -> Unit, onDismiss: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = inputValue,
        onValueChange = { inputValue = it },
        Modifier.width(150.dp).height(35.dp),
        singleLine = true,
        interactionSource = interactionSource
    ) { innerTextField ->
        TextFieldDefaults.OutlinedTextFieldDecorationBox(
            value = inputValue,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            trailingIcon = {
                Row {
                    Icon(Icons.Filled.Done, "contentDescription", modifier = Modifier.clickable {
                        onAccept(inputValue.toInt())
                        inputValue = ""
                    }.padding(end = 5.dp))
                    Icon(Icons.Filled.Close, "contentDescription", modifier = Modifier.clickable {
                        onDismiss()
                        inputValue = ""
                    })
                }
            },
            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                top = 0.dp, bottom = 0.dp
            )
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
