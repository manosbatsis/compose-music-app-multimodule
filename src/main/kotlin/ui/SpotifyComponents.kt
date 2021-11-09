package ui

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.composecookbook.ui.demoui.spotify.data.Album
import graySurface
import org.jetbrains.skia.Bitmap
import spotifyGreen
import utils.horizontalGradientBackground

@Composable
fun SpotifyHomeGridItem(album: Album) {
    val cardColor = graySurface
    Card(
        elevation = 4.dp,
        backgroundColor = cardColor,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable(onClick = {})
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(album.imageId), "Item",
                modifier = Modifier.size(55.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = album.song,
                style = typography.h6.copy(fontSize = 14.sp),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpotifyLaneItem(album: Album, onclick: () -> Unit) {
    val album = remember { album }
    var showPlayButton by remember { mutableStateOf(false) }
    var scalePlayButton by remember { mutableStateOf(false) }

    Box(modifier = Modifier.pointerMoveFilter(
        onEnter = {
            showPlayButton = true
            false
        },
        onExit = {
            showPlayButton = false
            false
        })
        .clickable { onclick.invoke() }
    ) {
        Column(
            modifier = Modifier.width(240.dp).padding(24.dp)
        ) {
            Image(
                painterResource(album.imageId), "Album",
                modifier = Modifier.width(240.dp).height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "${album.song}: ${album.descriptions}",
                style = typography.body2,
                maxLines = 2,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        val alpha by animateFloatAsState(if (showPlayButton) 1f else 0f)
        val scale by animateFloatAsState(if (scalePlayButton) 1.3f else 1f)
        IconButton(
            onClick = {},
            modifier = Modifier
                .pointerMoveFilter(
                    onEnter = {
                        scalePlayButton = true
                        false
                    },
                    onExit = {
                        scalePlayButton = false
                        false
                    }
                )
                .alpha(alpha)
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 30.dp)
                .clip(CircleShape)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                .background(spotifyGreen)
        ) {
            Icon(rememberVectorPainter(Icons.Default.PlayArrow), "Play", tint = MaterialTheme.colors.onSurface)
        }
    }
}

@Composable
fun SpotifySearchGridItem(album: Album, modifier: Modifier = Modifier, onclick: () -> Unit) {
    val dominantColor = remember(album.id) {
        getDominantColor(useResource(album.imageId) { loadImageBitmap(it).asSkiaBitmap() })
    }
    val dominantGradient = remember(album.id) { listOf(dominantColor, dominantColor.copy(alpha = 0.6f)) }

    Row(
        modifier = modifier
            .padding(12.dp)
            .clickable(onClick = { onclick.invoke() })
            .height(220.dp)
            .clip(RoundedCornerShape(8.dp))
            .horizontalGradientBackground(dominantGradient),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = album.song,
            style = typography.h6,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painterResource(album.imageId), album.descriptions,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
                .align(Alignment.Bottom)
                .graphicsLayer(translationX = 40f, rotationZ = 32f, shadowElevation = 16f)
        )
    }
}


fun getDominantColor(bitmap: Bitmap): Color {
    val colorCode = bitmap.getColor(10, 10)
    return Color(colorCode)
}
