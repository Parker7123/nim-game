package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nim.Level

@Composable
fun MenuScreen(modifier: Modifier = Modifier, onLevelSelected: (level: Level, stacksCount: Int, candiesCount: Int) -> Unit) {
    var showSettings by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf(Level.EASY) }
    CenterColumn(modifier) {
        Title()
        if (showSettings) {
            SettingsSection(modifier = Modifier.width(150.dp)) { stacksCount, numOfCandies ->
                onLevelSelected(selectedLevel, stacksCount, numOfCandies)
            }
        } else {
            LevelSelection(Modifier.width(150.dp)) {
                selectedLevel = it
                showSettings = true
            }
        }
    }
}

@Composable
fun LevelTile(item: Level, onClick: (level: Level) -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = { onClick(item) }) {
        Text(item.text, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

@Composable
fun Title(modifier: Modifier = Modifier) {
    Text("NIM", style = MaterialTheme.typography.h1)
}

@Composable
fun SettingsSection(modifier: Modifier = Modifier, onPlay: (stacksCount: Int, numOfCandies: Int) -> Unit) {
    var stacksCount by remember { mutableIntStateOf(3) }
    var numberOfCandies by remember { mutableIntStateOf(30) }
    CenterColumn(modifier = modifier) {
        OutlinedTextField(
            stacksCount.toString(),
            onValueChange = { stacksCount = it.toInt() },
            label = { Text("Number of stacks") })
        OutlinedTextField(
            numberOfCandies.toString(),
            onValueChange = { numberOfCandies = it.toInt() },
            label = { Text("Number of candies") },
            modifier = Modifier.padding(bottom = 20.dp))
        Button(onClick = { onPlay(stacksCount, numberOfCandies) }, modifier = Modifier.fillMaxWidth()) {
            Text("Play")
        }
    }
}

@Composable
fun LevelSelection(modifier: Modifier = Modifier, onLevelSelected: (level: Level) -> Unit) {
    CenterColumn(modifier = modifier) {
        Level.entries.forEach {
            LevelTile(it, onLevelSelected)
        }
    }
}