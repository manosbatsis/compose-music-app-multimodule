package ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import utils.horizontalGradientBackground

@Composable
fun SpotifyLibrary() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalGradientBackground(listOf(MaterialTheme.colors.secondary, MaterialTheme.colors.surface))
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var draw2 by remember { mutableStateOf(false) }
        var draw4 by remember { mutableStateOf(false) }
        val imageSize = 300.dp

        SpotifyTitle("My Library")
        Spacer(modifier = Modifier.padding(30.dp))
        Box(modifier = Modifier.clickable(onClick = {
            draw2 = !draw2
            draw4 = false
        })) {
            Image(
                painterResource("bp.jpg"), "BP",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw2) 30f else 5f).value,
                    translationX = animateFloatAsState(if (draw2) 520f else 0f).value,
                    translationY = 0f,
                )
            )
            Image(
                painterResource("dualipa.jpeg"), "Dualipa",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw2) 30f else 10f).value,
                    translationX = animateFloatAsState(if (draw2) -520f else 0f).value,
                    translationY = animateFloatAsState(if (draw2) 0f else 30f).value,
                ).clickable(onClick = { draw2 = !draw2 })
            )
            Image(
                painterResource("tylor.jpeg"), "Tylor",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw2) 30f else 5f).value,
                    translationY = animateFloatAsState(if (draw2) 0f else 50f).value
                ).clickable(onClick = { draw2 = !draw2 })
            )
        }

        Spacer(modifier = Modifier.padding(50.dp))

        Box(modifier = Modifier.clickable {
            draw4 = !draw4
            draw2 = false
        }) {
            Image(
                painterResource("katy.jpg"), "Katy",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw4) 30f else 5f).value,
                    translationX = animateFloatAsState(if (draw4) 320f else 0f).value,
                    rotationZ = animateFloatAsState(if (draw4) 45f else 0f).value,
                    translationY = 0f
                )
            )
            Image(
                painterResource("ed2.jpg"), "Ed2",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw4) 30f else 10f).value,
                    translationX = animateFloatAsState(if (draw4) -320f else 0f).value,
                    rotationZ = animateFloatAsState(if (draw4) 45f else 0f).value,
                    translationY = animateFloatAsState(if (draw4) 0f else 30f).value
                )
            )
            Image(
                painterResource("camelia.jpeg"), "Camelia",
                modifier = Modifier.size(imageSize).graphicsLayer (
                    shadowElevation = animateFloatAsState(if (draw4) 30f else 5f).value,
                    translationY = animateFloatAsState(if (draw4) 0f else 50f).value,
                    rotationZ = animateFloatAsState(if (draw4) 45f else 0f).value
                )
            )
        }
    }
}
