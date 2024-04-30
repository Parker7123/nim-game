package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

/**
 *   *
 *  * *
 * * * *
 */
@Composable
fun PileOfDots(
    numCircles: Int,
    diameter: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val n = ((sqrt((8 * numCircles + 1).toDouble()) - 1) / 2).toInt()
        val xOffset = (maxWidth - ((2 * n - 1) * diameter).dp).div(2)
        println(n)
        println(xOffset)
        var count = 0
        for (i in 0..numCircles) {
            for (j in 0..i) {
                if (count == numCircles) {
                    break
                }
                Box(
                    modifier = Modifier
                        .size(diameter.dp)
                        .absoluteOffset(x = xOffset + ((2 * i) * diameter - (j * diameter)).dp, y = maxHeight - (j * diameter).dp)
                        .background(Color.Red, shape = CircleShape)
                )
                count++
            }
        }
    }
}