package com.codewithdipesh.ticketbooking.booking.seatBooking.gl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class CurvedMesh(
    private val rows: Int = 20,
    private val cols: Int = 40,
    private val halfWidth: Float = 1.2f,
    private val height: Float = 1.0f,
    private val curveDepth: Float = 0.95f
) {

    private val vertexBuffer: FloatBuffer
    private val indexBuffer: ShortBuffer
    private val indexCount: Int

    init {
        val stride = FLOATS_PER_VERTEX
        val vertices = FloatArray((rows + 1) * (cols + 1) * stride)
        var vi = 0
        for (y in 0..rows) {
            val v = y.toFloat() / rows
            for (x in 0..cols) {
                val u = x.toFloat() / cols

                val posX = (u - 0.5f) * 2f * halfWidth
                val posY = v * height
                val curve = u - 0.5f
                // Concave from the viewer: edges curl forward (closer to camera),
                // center sits furthest back — like a real cinema screen wrapping
                // around the audience.
                val posZ = curve * curve * curveDepth

                vertices[vi++] = posX
                vertices[vi++] = posY
                vertices[vi++] = posZ
                vertices[vi++] = u
                vertices[vi++] = v
            }
        }

        val indices = ShortArray(rows * cols * 6)
        var ii = 0
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val tl = (y * (cols + 1) + x).toShort()
                val tr = (tl + 1).toShort()
                val bl = ((y + 1) * (cols + 1) + x).toShort()
                val br = (bl + 1).toShort()
                indices[ii++] = tl
                indices[ii++] = bl
                indices[ii++] = br
                indices[ii++] = tl
                indices[ii++] = br
                indices[ii++] = tr
            }
        }

        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply { put(vertices).position(0) }

        indexBuffer = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .apply { put(indices).position(0) }

        indexCount = indices.size
    }

    fun draw(positionHandle: Int, texCoordHandle: Int) {
        vertexBuffer.position(POS_OFFSET)
        GLES20.glVertexAttribPointer(
            positionHandle, 3, GLES20.GL_FLOAT, false, STRIDE_BYTES, vertexBuffer
        )
        GLES20.glEnableVertexAttribArray(positionHandle)

        vertexBuffer.position(UV_OFFSET)
        GLES20.glVertexAttribPointer(
            texCoordHandle, 2, GLES20.GL_FLOAT, false, STRIDE_BYTES, vertexBuffer
        )
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    companion object {
        private const val FLOATS_PER_VERTEX = 5
        private const val STRIDE_BYTES = FLOATS_PER_VERTEX * 4
        private const val POS_OFFSET = 0
        private const val UV_OFFSET = 3
    }
}
