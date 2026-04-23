package com.codewithdipesh.ticketbooking.booking.seatBooking.gl

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CinemaRenderer(
    private val context: Context,
    private val videoUri: String,
    private val requestRender: () -> Unit
) : GLSurfaceView.Renderer {

    private lateinit var mesh: CurvedMesh
    private var videoTexture: VideoTexture? = null

    private var program = 0
    private var aPositionHandle = 0
    private var aTexCoordHandle = 0
    private var uMVPMatrixHandle = 0
    private var uTexMatrixHandle = 0
    private var uTextureHandle = 0
    private var uReflectionHandle = 0

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mvMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val texMatrix = FloatArray(16).also { Matrix.setIdentityM(it, 0) }

    @Volatile private var frameAvailable = false
    private val frameLock = Any()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        program = ShaderUtils.createProgram(Shaders.VERTEX_SRC, Shaders.FRAGMENT_SRC)
        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        aTexCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        uTexMatrixHandle = GLES20.glGetUniformLocation(program, "uTexMatrix")
        uTextureHandle = GLES20.glGetUniformLocation(program, "uTexture")
        uReflectionHandle = GLES20.glGetUniformLocation(program, "uReflection")

        mesh = CurvedMesh()
        videoTexture = VideoTexture(context, videoUri).apply {
            setOnFrameAvailableListener {
                synchronized(frameLock) { frameAvailable = true }
                requestRender()
            }
        }

        // Elevated camera looking slightly down at the screen, like viewing a
        // cinema hall from the mid/upper rows. This reveals the top face of the
        // curved screen and leaves room below it for the floor reflection.
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 1.45f, 2.35f,   // eye: raised higher, pulled back a touch
            0f, 0.28f, 0f,      // target: lower portion of screen for steeper tilt
            0f, 1f, 0f
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val aspect = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, 42f, aspect, 0.1f, 10f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        val tex = videoTexture ?: return
        synchronized(frameLock) {
            if (frameAvailable) {
                tex.update()
                tex.getTransformMatrix(texMatrix)
                frameAvailable = false
            }
        }

        GLES20.glUseProgram(program)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex.textureId)
        GLES20.glUniform1i(uTextureHandle, 0)
        GLES20.glUniformMatrix4fv(uTexMatrixHandle, 1, false, texMatrix, 0)

        // --- Main screen pass ---
        Matrix.setIdentityM(modelMatrix, 0)
        buildMvp()
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniform1f(uReflectionHandle, 0f)
        GLES20.glDepthMask(true)
        mesh.draw(aPositionHandle, aTexCoordHandle)

        // --- Reflection pass: mirrored across floor (y=0), faded + darkened ---
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 1f, -1f, 1f)
        Matrix.translateM(modelMatrix, 0, 0f, -0.03f, 0f)
        buildMvp()
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniform1f(uReflectionHandle, 1f)
        GLES20.glDepthMask(false)
        mesh.draw(aPositionHandle, aTexCoordHandle)
        GLES20.glDepthMask(true)
    }

    private fun buildMvp() {
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0)
    }

    fun release() {
        videoTexture?.release()
        videoTexture = null
        if (program != 0) {
            GLES20.glDeleteProgram(program)
            program = 0
        }
    }
}
