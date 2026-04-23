package com.codewithdipesh.ticketbooking.booking.seatBooking.gl

object Shaders {

    const val VERTEX_SRC = """
        uniform mat4 uMVPMatrix;
        uniform mat4 uTexMatrix;
        attribute vec4 aPosition;
        attribute vec2 aTexCoord;
        varying vec2 vTexCoord;
        varying float vLocalY;
        void main() {
            gl_Position = uMVPMatrix * aPosition;
            vTexCoord = (uTexMatrix * vec4(aTexCoord, 0.0, 1.0)).xy;
            vLocalY = aPosition.y;
        }
    """

    const val FRAGMENT_SRC = """
        #extension GL_OES_EGL_image_external : require
        precision mediump float;
        uniform samplerExternalOES uTexture;
        uniform float uReflection;
        varying vec2 vTexCoord;
        varying float vLocalY;
        void main() {
            vec4 color = texture2D(uTexture, vTexCoord);
            if (uReflection > 0.5) {
                // Soft quadratic falloff: strongest at the screen's base,
                // fading to nothing at the far end of the floor reflection.
                float t = clamp(1.0 - vLocalY, 0.0, 1.0);
                float fade = t * t;
                // Desaturate slightly so the reflection reads as a glossy floor,
                // not a second screen.
                float luma = dot(color.rgb, vec3(0.299, 0.587, 0.114));
                vec3 desat = mix(color.rgb, vec3(luma), 0.35);
                color.rgb = desat * 0.65 * fade;
                color.a = fade;
            } else {
                color.a = 1.0;
            }
            gl_FragColor = color;
        }
    """
}