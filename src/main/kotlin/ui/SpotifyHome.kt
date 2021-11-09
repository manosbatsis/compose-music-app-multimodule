package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guru.composecookbook.ui.demoui.spotify.data.Album
import com.guru.composecookbook.ui.demoui.spotify.data.SpotifyDataProvider
import spotifyGradient
import utils.horizontalGradientBackground

@Composable
fun SpotifyHome(onAlbumSelected: (Album) -> Unit) {
    val scrollState = rememberScrollState(0)
    val surfaceGradient = listOf(MaterialTheme.colors.secondary, MaterialTheme.colors.surface)

    Surface {
        SpotifyHomeContent(scrollState = scrollState, surfaceGradient = surfaceGradient, onAlbumSelected)
    }
}

@Composable
fun SpotifyTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = MaterialTheme.colors.onSurface,
        style = typography.h5.copy(fontWeight = FontWeight.ExtraBold),
        modifier = modifier.padding(start = 24.dp, end = 4.dp, bottom = 8.dp, top = 24.dp)
    )
}


@Composable
fun SpotifyHomeContent(scrollState: ScrollState, surfaceGradient: List<Color>, onAlbumSelected: (Album) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState()).horizontalGradientBackground(surfaceGradient).padding(8.dp)) {
        Spacer(modifier = Modifier.height(50.dp))
        SpotifyTitle("Spotify Stories")
        SpotifyStories()
        HomeLanesSection(onAlbumSelected)
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SpotifyStories() {
    val i = remember { SpotifyDataProvider.albums }
    LazyRow {
        items(i) {
            SpotifyStoryItem(it)
        }
    }
}

@Composable
fun SpotifyStoryItem(album: Album) {
    Image(
        painterResource(album.imageId), "Album Image",
        modifier = Modifier.padding(16.dp)
            .size(100.dp)
            .clip(CircleShape)
            .border(shape = CircleShape, border = BorderStroke(3.dp, brush = spotifyGradient()))
    )
}

@Composable
fun HomeLanesSection(onAlbumSelected: (Album) -> Unit) {
    val categories = remember { SpotifyDataProvider.listOfSpotifyHomeLanes }
    categories.forEachIndexed { index, lane ->
        SpotifyTitle(text = lane)
        SpotifyLane(index, onAlbumSelected)
    }
}

@Composable
fun SpotifyLane(index: Int, onAlbumSelected: (Album) -> Unit) {
    val itemsEven = remember { SpotifyDataProvider.albums }
    val itemsOdd = remember { SpotifyDataProvider.albums.asReversed() }

    LazyRow {
        items(itemsEven) {
            SpotifyLaneItem(it) { onAlbumSelected.invoke(it) }
        }
        items(itemsOdd) {
            SpotifyLaneItem(it) { onAlbumSelected.invoke(it) }
        }
    }
    // TODO: not quite right.
}

@Composable
fun spotifyGradient() = Brush.Companion.verticalGradient(colors = spotifyGradient, startY = 0f, endY = 100f)
