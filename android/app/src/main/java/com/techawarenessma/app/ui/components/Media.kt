package com.techawarenessma.app.ui.components

import android.content.Context
import android.provider.Settings
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.techawarenessma.app.ui.theme.TaaColors

/** True when the user turned on "Remove animations" (animator scale 0). */
fun animationsDisabled(context: Context): Boolean =
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

/**
 * A muted, looping clip bundled in `res/raw`, like the site's autoplaying timelapses.
 * It pauses whenever the app is in the background, starts paused if the user has turned
 * off animations, and always has a visible pause control.
 */
@OptIn(UnstableApi::class)
@Composable
fun LoopingVideo(
    @RawRes res: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var playing by rememberSaveable { mutableStateOf(!animationsDisabled(context)) }
    val player = remember(res) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("android.resource://${context.packageName}/$res"))
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 0f
            prepare()
        }
    }
    DisposableEffect(player) {
        onDispose { player.release() }
    }
    LifecycleResumeEffect(player, playing) {
        player.playWhenReady = playing
        onPauseOrDispose { player.playWhenReady = false }
    }

    Box(modifier) {
        AndroidView(
            factory = { viewContext -> TextureView(viewContext).also(player::setVideoTextureView) },
            modifier = Modifier
                .fillMaxSize()
                .semantics { this.contentDescription = contentDescription },
        )
        IconButton(
            onClick = { playing = !playing },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .background(TaaColors.Ink.copy(alpha = 0.65f), CircleShape),
        ) {
            Icon(
                imageVector = if (playing) TaaIcons.Pause else TaaIcons.Play,
                contentDescription = if (playing) "Pause video" else "Play video",
                tint = TaaColors.Cream,
            )
        }
    }
}

/**
 * The CSS phone mock-up from the site: ink bezel, side buttons and a pill notch,
 * wrapped around a 9:16 screen.
 */
@Composable
fun PhoneFrame(
    modifier: Modifier = Modifier,
    width: Dp = 250.dp,
    screen: @Composable () -> Unit,
) {
    val bezel = 12.dp
    Box(modifier.width(width + 8.dp), contentAlignment = Alignment.Center) {
        // Side buttons sit just outside the body, like the site's absolutely positioned bars.
        Box(Modifier.align(Alignment.TopStart).offset(y = 96.dp).size(4.dp, 28.dp).background(TaaColors.Ink, RoundedCornerShape(4.dp)))
        Box(Modifier.align(Alignment.TopStart).offset(y = 140.dp).size(4.dp, 48.dp).background(TaaColors.Ink, RoundedCornerShape(4.dp)))
        Box(Modifier.align(Alignment.TopEnd).offset(y = 150.dp).size(4.dp, 72.dp).background(TaaColors.Ink, RoundedCornerShape(4.dp)))
        Box(
            Modifier
                .width(width)
                .shadow(18.dp, RoundedCornerShape(44.dp))
                .background(TaaColors.Cream, RoundedCornerShape(44.dp))
                .border(5.dp, TaaColors.Ink, RoundedCornerShape(44.dp))
                .padding(bezel),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(34.dp)),
            ) {
                screen()
            }
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp)
                    .size(76.dp, 22.dp)
                    .background(TaaColors.Ink, CircleShape),
            )
        }
    }
}
