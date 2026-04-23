package com.codewithdipesh.ticketbooking.booking.seatBooking.gl

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.os.Handler
import android.os.Looper
import android.view.Surface
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class VideoTexture(context: Context, videoUri: String) {

    val textureId: Int = createExternalTexture()
    private val surfaceTexture = SurfaceTexture(textureId)
    private val surface = Surface(surfaceTexture)
    private val mainHandler = Handler(Looper.getMainLooper())

    // ExoPlayer must be constructed and driven on a thread with a Looper.
    // onSurfaceCreated runs on the GL thread (no Looper), so we build and
    // touch the player exclusively on the main thread.
    private var player: ExoPlayer? = null

    init {
        mainHandler.post {
            val p = ExoPlayer.Builder(context).build().also { player = it }
            p.setVideoSurface(surface)
            p.setMediaItem(MediaItem.fromUri(videoUri))
            p.repeatMode = Player.REPEAT_MODE_ALL
            p.prepare()
            p.volume = 0f
            p.playWhenReady = true
        }
    }

    fun setOnFrameAvailableListener(listener: SurfaceTexture.OnFrameAvailableListener) {
        surfaceTexture.setOnFrameAvailableListener(listener)
    }

    fun update() {
        surfaceTexture.updateTexImage()
    }

    fun getTransformMatrix(matrix: FloatArray) {
        surfaceTexture.getTransformMatrix(matrix)
    }

    fun release() {
        // Player owns the Surface while playing — tear those down together on main.
        mainHandler.post {
            player?.release()
            player = null
            surface.release()
            surfaceTexture.release()
        }
        GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
    }

    private fun createExternalTexture(): Int {
        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        val id = ids[0]
        val target = GLES11Ext.GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(target, id)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        return id
    }
}