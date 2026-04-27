package com.codewithdipesh.ticketbooking.tickets.components

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.min

class CutCornerCircleShape(
    private val radius: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val r = radius.coerceAtMost(min(size.width, size.height) / 2)

        val path = Path().apply {

            // Start top-left (after cut)
            moveTo(r, 0f)

            // Top edge
            lineTo(size.width - r, 0f)

            // TOP-RIGHT concave (center outside)
            arcTo(
                rect = Rect(
                    size.width - r,
                    -r,
                    size.width + r,
                    r
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )

            // Right edge
            lineTo(size.width, size.height - r)

            // BOTTOM-RIGHT concave
            arcTo(
                rect = Rect(
                    size.width - r,
                    size.height - r,
                    size.width + r,
                    size.height + r
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )

            // Bottom edge
            lineTo(r, size.height)

            // BOTTOM-LEFT concave
            arcTo(
                rect = Rect(
                    -r,
                    size.height - r,
                    r,
                    size.height + r
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )

            // Left edge
            lineTo(0f, r)

            // TOP-LEFT concave
            arcTo(
                rect = Rect(
                    -r,
                    -r,
                    r,
                    r
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )

            close()
        }

        return Outline.Generic(path)
    }
}