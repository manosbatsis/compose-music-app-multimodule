import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.guru.composecookbook.ui.demoui.spotify.data.Album
import com.guru.composecookbook.ui.demoui.spotify.data.SpotifyDataProvider
import ui.SpotifyHome
import ui.SpotifyLibrary
import ui.SpotifyNavType
import ui.SpotifySearchScreen
import ui.detail.SpotifyDetailScreen
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.inputStream


fun main() {
    singleWindowApplication(
        title = "Sample app showing Conveyor with JetPack Compose (version ${System.getProperty("app.version")})",
        state = WindowState(placement = WindowPlacement.Maximized),
        icon = loadIcon()
    ) {
        val darkTheme = remember { mutableStateOf(true) }
        val roboto = remember { Font("Roboto-Light.ttf").toFontFamily() }
        MaterialTheme(
            colors = if (darkTheme.value) DarkGreenColorPalette else LightGreenColorPalette,
            typography = typography.copy(
                h4 = typography.h4.copy(fontFamily = roboto),
                h5 = typography.h5.copy(fontFamily = roboto),
                h6 = typography.h6.copy(fontFamily = roboto),
                body1 = typography.body1.copy(fontFamily = roboto),
                body2 = typography.body2.copy(fontFamily = roboto),
                button = typography.button.copy(fontFamily = roboto),
                caption = typography.caption.copy(fontFamily = roboto),
                overline = typography.overline.copy(fontFamily = roboto)
            )
        ) {
            SpotifyApp(darkTheme)
        }
    }
}

fun loadIcon(): Painter? {
    // app.dir is set when packaged to point at our collected inputs.
    val appDirProp = System.getProperty("app.dir")
    val appDir = appDirProp?.let { Path.of(it) } ?: return null
    val iconPath = appDir.resolve("icon-256.png")
    return if (iconPath.exists()) {
        BitmapPainter(iconPath.inputStream().buffered().use { loadImageBitmap(it) })
    } else {
        null
    }
}

@Composable
fun <T> savedInstanceState(body: () -> T) = remember { mutableStateOf(body()) }

@Composable
fun SpotifyApp(darkTheme: MutableState<Boolean>) {
    val spotifyNavItemState = savedInstanceState { SpotifyNavType.HOME }
    val showAlbumDetailState = savedInstanceState<Album?> { null }
    Box {
        Row {
            SpotifySideBar(spotifyNavItemState, showAlbumDetailState, darkTheme)
            SpotifyBodyContent(spotifyNavItemState.value, showAlbumDetailState)
        }
        PlayerBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun SpotifyBodyContent(spotifyNavType: SpotifyNavType, album: MutableState<Album?>) {
    if (album.value != null) {
        SpotifyDetailScreen(album.value!!) {
            album.value = null
        }
    } else {
        Crossfade(targetState = spotifyNavType) { state ->
            when (state) {
                SpotifyNavType.HOME -> SpotifyHome { onAlbumSelected ->
                    album.value = onAlbumSelected
                }
                SpotifyNavType.SEARCH -> SpotifySearchScreen { onAlbumSelected ->
                    album.value = onAlbumSelected
                }
                SpotifyNavType.LIBRARY -> SpotifyLibrary()
            }
        }
    }
}

@Composable
fun SpotifySideBar(
    spotifyNavItemState: MutableState<SpotifyNavType>,
    showAlbumDetailState: MutableState<Album?>,
    darkTheme: MutableState<Boolean>
) {
    val selectedIndex = remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier.fillMaxHeight().width(250.dp).background(MaterialTheme.colors.surface)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Logo(darkTheme.value)

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable { darkTheme.value = !darkTheme.value }.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Toggle Theme", style = typography.h6.copy(fontSize = 14.sp), color = MaterialTheme.colors.onSurface)
            if (darkTheme.value) {
                Icon(rememberVectorPainter(Icons.Default.Star), "Star", tint = Color.Yellow)
            } else {
                Icon(rememberVectorPainter(Icons.Default.Star), "Star", tint = MaterialTheme.colors.onSurface)
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        SideBarNavItem("Home", Icons.Default.Home, spotifyNavItemState.value == SpotifyNavType.HOME) {
            spotifyNavItemState.value = SpotifyNavType.HOME
            showAlbumDetailState.value = null
            selectedIndex.value = -1
        }
        SideBarNavItem("Search", Icons.Default.Search, spotifyNavItemState.value == SpotifyNavType.SEARCH) {
            spotifyNavItemState.value = SpotifyNavType.SEARCH
            showAlbumDetailState.value = null
            selectedIndex.value = -1
        }
        SideBarNavItem("Your Library", Icons.Default.List, spotifyNavItemState.value == SpotifyNavType.LIBRARY) {
            spotifyNavItemState.value = SpotifyNavType.LIBRARY
            showAlbumDetailState.value = null
            selectedIndex.value = -1
        }
        Spacer(modifier = Modifier.height(20.dp))
        PlayListsSideBar(selectedIndex.value) {
            showAlbumDetailState.value = SpotifyDataProvider.albums[it]
            selectedIndex.value = it
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun Logo(darkTheme: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Load the headphones from FontAwesome.
        val fontAwesome = remember { Font("fa_solid_900.ttf").toFontFamily() }
        Text(
            0xf58f.toChar().toString(),
            Modifier.padding(16.dp),
            fontFamily = fontAwesome,
            fontSize = 3.em,
            color = if (darkTheme) Color.White else Color.Black
        )
        Text(
            "Music",
            color = if (darkTheme) Color.White else Color.Black,
            fontSize = 3.em
        )
    }
}

@Composable
fun PlayerBottomBar(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.secondary)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painterResource("adele.jpeg"), "Adele",
            modifier = Modifier.size(75.dp).padding(8.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Someone Like you by Adele",
            style = typography.h6.copy(fontSize = 14.sp),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(16.dp),
        )
        Icon(
            rememberVectorPainter(Icons.Default.AddCircle),
            "Add",
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colors.onSurface
        )
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Icon(
                rememberVectorPainter(Icons.Default.Refresh),
                "Refresh",
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Icon(
                rememberVectorPainter(Icons.Default.PlayArrow),
                "Play",
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Icon(rememberVectorPainter(Icons.Default.Favorite), "Favorite", modifier = Modifier.padding(8.dp), tint = spotifyGreen)
        }

        Icon(rememberVectorPainter(Icons.Default.List), "List", modifier = Modifier.padding(8.dp), tint = MaterialTheme.colors.onSurface)
        Icon(
            rememberVectorPainter(Icons.Default.Share), "Share",
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun PlayListsSideBar(selectedIndex: Int, onPlayListSelected: (Int) -> Unit) {
    Text(
        "PLAYLISTS",
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        color = MaterialTheme.colors.onSurface
    )
    LazyColumn {
        itemsIndexed(SpotifyDataProvider.playLits) { index, playlist ->
            Text(
                playlist,
                modifier = Modifier.padding(8.dp).clickable { onPlayListSelected.invoke(index) },
                color =
                if (index == selectedIndex) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSecondary.copy(
                    alpha = 0.7f
                ),
                style = if (index == selectedIndex) typography.h6 else typography.body1
            )
        }
    }
}


@Composable
fun SideBarNavItem(title: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val animatedBackgroundColor =
        animateColorAsState(if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
    val animatedContentColor =
        animateColorAsState(if (selected) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSecondary)
    Row(
        modifier = Modifier
            .fillMaxWidth().background(animatedBackgroundColor.value).clip(RoundedCornerShape(4.dp)).padding(16.dp)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(rememberVectorPainter(icon), title, tint = animatedContentColor.value)
        Text(
            title,
            style = typography.body1,
            color = animatedContentColor.value,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
