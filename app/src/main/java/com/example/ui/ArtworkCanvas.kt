package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

/**
 * Renders the distinctive horned "H" logo from the top of the Untitled layout
 */
@Composable
fun HornedLogo(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_horned_h_logo),
            contentDescription = "Horned H Neon Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f)
        )
    }
}

private fun createHornedPath(w: Float, h: Float): Path {
    val path = Path()

    // Reference coordinates relative to w, h
    val leftOuter = w * 0.22f
    val leftInner = w * 0.38f
    val rightInner = w * 0.62f
    val rightOuter = w * 0.78f

    val topHornY = h * 0.12f
    val topInnerHornY = h * 0.22f
    val bridgeTopY = h * 0.44f
    val bridgeBottomY = h * 0.62f
    val bottomY = h * 0.88f

    // Start at bottom-left corner of left leg
    path.moveTo(leftOuter, bottomY)
    
    // Left outer vertical edge going up
    path.lineTo(leftOuter, h * 0.35f)
    
    // Left horn flare outward & upward
    path.quadraticTo(leftOuter * 0.8f, h * 0.2f, leftOuter * 0.85f, topHornY)
    
    // Horn tip to inner left dip
    path.quadraticTo(leftOuter * 1.1f, h * 0.24f, leftInner, topInnerHornY)
    
    // Left inner edge down to bridge top
    path.lineTo(leftInner, bridgeTopY)
    
    // Arch over the bridge center
    path.quadraticTo(w * 0.5f, bridgeTopY * 0.82f, rightInner, bridgeTopY)
    
    // Right inner edge up to right horn
    path.lineTo(rightInner, topInnerHornY)
    
    // Right horn tip
    path.quadraticTo(w - (leftOuter * 1.1f), h * 0.24f, w - (leftOuter * 0.85f), topHornY)
    
    // Right outer horn edge going down
    path.quadraticTo(w - (leftOuter * 0.8f), h * 0.2f, rightOuter, h * 0.35f)
    
    // Right outer vertical edge going down to bottom
    path.lineTo(rightOuter, bottomY)
    
    // Right bottom leg curve
    path.quadraticTo(rightOuter, bottomY + h * 0.04f, rightInner + (rightOuter - rightInner) * 0.5f, bottomY + h * 0.04f)
    path.lineTo(rightInner, bottomY)
    
    // Right inner leg going up to bridge bottom
    path.lineTo(rightInner, bridgeBottomY)
    
    // Arch under the bridge
    path.quadraticTo(w * 0.5f, bridgeBottomY * 0.92f, leftInner, bridgeBottomY)
    
    // Left inner leg going down to bottom
    path.lineTo(leftInner, bottomY)
    
    // Left bottom leg curve
    path.quadraticTo(leftOuter + (leftInner - leftOuter) * 0.5f, bottomY + h * 0.04f, leftOuter, bottomY)

    path.close()
    return path
}

/**
 * Faithfully reproduces the bottom cityscape / abstract shapes from the Untitled image
 */
@Composable
fun UntitledBottomArtwork(
    modifier: Modifier = Modifier,
    height: Dp = 190.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val w = size.width
        val h = size.height

        val strokeBlack = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val strokeThin = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // -------------------------------------------------------------
        // Shape 1: Left Gray Wedge Building (0% to 21%)
        // -------------------------------------------------------------
        val b1Left = 0f
        val b1Right = w * 0.21f
        val b1Top = h * 0.28f

        // Gray polygon
        val grayPath = Path().apply {
            moveTo(b1Left, b1Top)
            lineTo(b1Right, b1Top)
            lineTo(b1Right, h)
            lineTo(b1Left, h)
            close()
        }
        drawPath(grayPath, Color(0xFFA5A5A5), style = Fill)
        drawPath(grayPath, Color.Black, style = strokeBlack)

        // Bottom-left darker wedge
        val darkWedge = Path().apply {
            moveTo(b1Left, b1Top + (h - b1Top) * 0.35f)
            lineTo(b1Right * 0.85f, h)
            lineTo(b1Left, h)
            close()
        }
        drawPath(darkWedge, Color(0xFF6E6E6E), style = Fill)
        drawPath(darkWedge, Color.Black, style = strokeThin)

        // Curved black doodle road across Shape 1
        val roadDoodle = Path().apply {
            moveTo(b1Left, b1Top + 20f)
            cubicTo(
                b1Right * 0.4f, b1Top + 20f,
                b1Right * 0.45f, b1Top + (h - b1Top) * 0.55f,
                b1Right * 0.4f, b1Top + (h - b1Top) * 0.65f
            )
            cubicTo(
                b1Right * 0.3f, b1Top + (h - b1Top) * 0.78f,
                b1Right * 0.7f, h - 10f,
                b1Right * 0.95f, h
            )
        }
        drawPath(roadDoodle, Color.Black, style = Stroke(width = 4.5f, cap = StrokeCap.Round))

        // -------------------------------------------------------------
        // Shape 2: Red & Brown Vertical Striped Block (19% to 26%)
        // -------------------------------------------------------------
        val b2Left = w * 0.195f
        val b2Right = w * 0.265f
        val b2Top = h * 0.60f
        val b2Width = b2Right - b2Left
        val stripeW = b2Width / 3f

        // Stripe 1: Red
        drawRect(Color(0xFFCC0000), Offset(b2Left, b2Top), Size(stripeW, h - b2Top))
        // Stripe 2: Warm Brown
        drawRect(Color(0xFFA75D37), Offset(b2Left + stripeW, b2Top), Size(stripeW, h - b2Top))
        // Stripe 3: Red
        drawRect(Color(0xFFCC0000), Offset(b2Left + stripeW * 2, b2Top), Size(stripeW, h - b2Top))

        // Outlines for stripes
        drawRect(Color.Black, Offset(b2Left, b2Top), Size(b2Width, h - b2Top), style = strokeBlack)
        drawLine(Color.Black, Offset(b2Left + stripeW, b2Top), Offset(b2Left + stripeW, h), strokeWidth = 3f)
        drawLine(Color.Black, Offset(b2Left + stripeW * 2, b2Top), Offset(b2Left + stripeW * 2, h), strokeWidth = 3f)

        // -------------------------------------------------------------
        // Shape 3: Bright Pink Block with Curved U-Arc Doodle (24.5% to 35%)
        // -------------------------------------------------------------
        val b3Left = w * 0.245f
        val b3Right = w * 0.355f
        val b3Top = h * 0.15f
        val b3W = b3Right - b3Left

        drawRect(Color(0xFFFF9AA2), Offset(b3Left, b3Top), Size(b3W, h - b3Top))
        drawRect(Color.Black, Offset(b3Left, b3Top), Size(b3W, h - b3Top), style = strokeBlack)

        // Pink Block Doodle: looping curved arc
        val pinkArc = Path().apply {
            moveTo(b3Right - 10f, b3Top + 20f)
            cubicTo(
                b3Right + 10f, b3Top + (h - b3Top) * 0.5f,
                b3Left + 10f, b3Top + (h - b3Top) * 0.7f,
                b3Left + 5f, b3Top + (h - b3Top) * 0.45f
            )
        }
        drawPath(pinkArc, Color.Black, style = Stroke(width = 4f, cap = StrokeCap.Round))

        // -------------------------------------------------------------
        // Shape 4: Yellow Block with Beige Windows & Green Base (35% to 45.5%)
        // -------------------------------------------------------------
        val b4Left = w * 0.355f
        val b4Right = w * 0.455f
        val b4Top = h * 0.30f
        val b4W = b4Right - b4Left

        // Yellow main body
        drawRect(Color(0xFFFFC400), Offset(b4Left, b4Top), Size(b4W, h - b4Top))
        drawRect(Color.Black, Offset(b4Left, b4Top), Size(b4W, h - b4Top), style = strokeBlack)

        // Beige Window 1 (Horizontal)
        val w1Left = b4Left + 5f
        val w1Top = b4Top + (h - b4Top) * 0.35f
        val w1W = b4W * 0.42f
        val w1H = 22f
        drawRect(Color(0xFFFFF1A8), Offset(w1Left, w1Top), Size(w1W, w1H))
        drawRect(Color.Black, Offset(w1Left, w1Top), Size(w1W, w1H), style = strokeThin)

        // Beige Window 2 (Square)
        val w2Left = b4Left + b4W * 0.65f
        val w2Top = b4Top + (h - b4Top) * 0.22f
        val w2W = b4W * 0.3f
        val w2H = 45f
        drawRect(Color(0xFFFFF1A8), Offset(w2Left, w2Top), Size(w2W, w2H))
        drawRect(Color.Black, Offset(w2Left, w2Top), Size(w2W, w2H), style = strokeThin)

        // Green base divisions
        val greenTop = b4Top + (h - b4Top) * 0.55f
        val greenLeft = b4Left
        val greenW = b4W
        val greenH = h - greenTop
        drawRect(Color(0xFF00C853), Offset(greenLeft, greenTop), Size(greenW, greenH))
        drawRect(Color.Black, Offset(greenLeft, greenTop), Size(greenW, greenH), style = strokeThin)

        // Center green vertical divider
        val greenMid = greenLeft + greenW * 0.45f
        drawRect(Color(0xFF76FF03), Offset(greenLeft + 2f, greenTop + 2f), Size(greenMid - greenLeft - 4f, greenH - 4f))
        drawLine(Color.Black, Offset(greenMid, greenTop), Offset(greenMid, h), strokeWidth = 3f)

        // -------------------------------------------------------------
        // Shape 5: Cyan & Blue Block with Orange Swoop (45.5% to 57.5%)
        // -------------------------------------------------------------
        val b5Left = w * 0.455f
        val b5Right = w * 0.575f
        val b5Top = h * 0.52f
        val b5W = b5Right - b5Left

        // Cyan upper block
        drawRect(Color(0xFF00A0E9), Offset(b5Left, b5Top), Size(b5W, h - b5Top))
        drawRect(Color.Black, Offset(b5Left, b5Top), Size(b5W, h - b5Top), style = strokeBlack)

        // Dark blue lower-right block
        val blueLeft = b5Left + b5W * 0.42f
        val blueTop = b5Top + (h - b5Top) * 0.38f
        drawRect(Color(0xFF2932B4), Offset(blueLeft, blueTop), Size(b5Right - blueLeft, h - blueTop))
        drawRect(Color.Black, Offset(blueLeft, blueTop), Size(b5Right - blueLeft, h - blueTop), style = strokeThin)

        // Cyan bottom-left block
        drawRect(Color(0xFF00C8FF), Offset(b5Left, blueTop), Size(blueLeft - b5Left, h - blueTop))
        drawRect(Color.Black, Offset(b5Left, blueTop), Size(blueLeft - b5Left, h - blueTop), style = strokeThin)

        // Black curved arc on cyan top
        val cyanArc = Path().apply {
            moveTo(b5Right, b5Top + 5f)
            quadraticTo(b5Right - 15f, b5Top + 35f, b5Left + b5W * 0.7f, b5Top + 38f)
        }
        drawPath(cyanArc, Color.Black, style = Stroke(width = 3.5f, cap = StrokeCap.Round))

        // Orange swoop doodle in lower section extending left
        val orangeSwoop = Path().apply {
            moveTo(w * 0.31f, h * 0.87f)
            quadraticTo(w * 0.45f, h * 0.86f, b5Left + b5W * 0.95f, h * 0.87f)
            lineTo(b5Left + b5W * 0.95f, h * 0.96f)
            quadraticTo(w * 0.42f, h * 0.96f, w * 0.32f, h * 0.89f)
            close()
        }
        drawPath(orangeSwoop, Color(0xFFFF6600), style = Fill)
        drawPath(orangeSwoop, Color.Black, style = Stroke(width = 3f))

        // -------------------------------------------------------------
        // Shape 6: Slate Blue Skyscraper (57.5% to 66.5%)
        // -------------------------------------------------------------
        val b6Left = w * 0.575f
        val b6Right = w * 0.665f
        val b6Top = h * 0.42f
        val b6W = b6Right - b6Left

        drawRect(Color(0xFF708FA8), Offset(b6Left, b6Top), Size(b6W, h - b6Top))
        drawRect(Color.Black, Offset(b6Left, b6Top), Size(b6W, h - b6Top), style = strokeBlack)

        // -------------------------------------------------------------
        // Shape 7: Purple / Violet Blocks (62% to 70.5%)
        // -------------------------------------------------------------
        val b7Left = w * 0.625f
        val b7Right = w * 0.705f
        val b7Top = h * 0.68f
        val b7W = b7Right - b7Left

        val p1W = b7W * 0.5f
        drawRect(Color(0xFF6B8DB5), Offset(b7Left, b7Top), Size(p1W, h - b7Top))
        drawRect(Color.Black, Offset(b7Left, b7Top), Size(p1W, h - b7Top), style = strokeThin)

        drawRect(Color(0xFF9E2A8B), Offset(b7Left + p1W, b7Top), Size(b7W - p1W, h - b7Top))
        drawRect(Color.Black, Offset(b7Left + p1W, b7Top), Size(b7W - p1W, h - b7Top), style = strokeThin)

        // -------------------------------------------------------------
        // Shape 8: Soft Lavender Block with '2' Doodle (70.5% to 79%)
        // -------------------------------------------------------------
        val b8Left = w * 0.705f
        val b8Right = w * 0.79f
        val b8Top = h * 0.33f
        val b8W = b8Right - b8Left

        drawRect(Color(0xFFCCC5E8), Offset(b8Left, b8Top), Size(b8W, h - b8Top))
        drawRect(Color.Black, Offset(b8Left, b8Top), Size(b8W, h - b8Top), style = strokeBlack)

        // Lavender black '2' doodle
        val lavenderScribble = Path().apply {
            moveTo(b8Left + b8W * 0.45f, b8Top + 20f)
            cubicTo(
                b8Right + 5f, b8Top + 25f,
                b8Left - 10f, b8Top + (h - b8Top) * 0.55f,
                b8Left + b8W * 0.35f, b8Top + (h - b8Top) * 0.72f
            )
            cubicTo(
                b8Right + 20f, b8Top + (h - b8Top) * 0.72f,
                b8Right + 50f, h * 0.85f,
                b8Right + 80f, h * 0.85f
            )
        }
        drawPath(lavenderScribble, Color.Black, style = Stroke(width = 4.5f, cap = StrokeCap.Round))

        // -------------------------------------------------------------
        // Shape 9: Tall Grey Skyscraper with White Garage/Car (81.5% to 88.5%)
        // -------------------------------------------------------------
        val b9Left = w * 0.815f
        val b9Right = w * 0.885f
        val b9Top = h * 0.18f
        val b9W = b9Right - b9Left

        drawRect(Color(0xFF707070), Offset(b9Left, b9Top), Size(b9W, h - b9Top))
        drawRect(Color.Black, Offset(b9Left, b9Top), Size(b9W, h - b9Top), style = strokeBlack)

        // White garage/car box at the bottom
        val carLeft = b9Left - 35f
        val carTop = h - 25f
        val carW = 40f
        val carH = 25f
        drawRect(Color.White, Offset(carLeft, carTop), Size(carW, carH))
        drawRect(Color.Black, Offset(carLeft, carTop), Size(carW, carH), style = strokeThin)
        // Little black door/wheel detail
        drawRect(Color.Black, Offset(carLeft + 8f, carTop + 8f), Size(6f, 10f))

        // -------------------------------------------------------------
        // Shape 10: Right White Structure with Dark Grey/Black Steps (88.5% to 100%)
        // -------------------------------------------------------------
        val b10Left = w * 0.885f
        val b10Right = w
        val b10Top = h * 0.28f
        val b10W = b10Right - b10Left

        // White block
        drawRect(Color.White, Offset(b10Left, b10Top), Size(b10W, h - b10Top))
        drawRect(Color.Black, Offset(b10Left, b10Top), Size(b10W, h - b10Top), style = strokeBlack)

        // Grey stepped square 1
        val step1Left = b10Left
        val step1Top = h * 0.50f
        val step1W = b10W * 0.45f
        val step1H = h * 0.28f
        drawRect(Color(0xFF7E7E7E), Offset(step1Left, step1Top), Size(step1W, step1H))
        drawRect(Color.Black, Offset(step1Left, step1Top), Size(step1W, step1H), style = strokeThin)

        // Black step at bottom left of white block
        val step2Left = b10Left
        val step2Top = step1Top + step1H
        val step2W = step1W
        val step2H = h - step2Top
        drawRect(Color.Black, Offset(step2Left, step2Top), Size(step2W, step2H))
        drawRect(Color.Black, Offset(step2Left, step2Top), Size(step2W, step2H), style = strokeThin)

        // Grey step at bottom right
        val step3Left = b10Left + step1W
        val step3Top = h * 0.78f
        val step3W = b10W - step1W
        val step3H = h - step3Top
        drawRect(Color(0xFF636363), Offset(step3Left, step3Top), Size(step3W, step3H))
        drawRect(Color.Black, Offset(step3Left, step3Top), Size(step3W, step3H), style = strokeThin)

        // Horizontal baseline along very bottom
        drawLine(Color.Black, Offset(0f, h), Offset(w, h), strokeWidth = 4f)
    }
}
