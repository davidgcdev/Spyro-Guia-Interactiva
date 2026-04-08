package dam.pmdm.spyrothedragon.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.*

class BrilloCetroView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private var phase = 0f

    // Colores del diamante del cetro de Ripto: morado y violeta mágico
    private val diamondColors = intArrayOf(
        Color.parseColor("#9C27B0"),
        Color.parseColor("#E040FB"),
        Color.parseColor("#CE93D8"),
        Color.parseColor("#EA80FC"),
        Color.parseColor("#FFFFFF")
    )

    init {
        animator.duration = 2500
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            phase = it.animatedValue as Float
            invalidate()
        }
    }

    fun startBrillo() {
        animator.start()
    }

    fun stopBrillo() {
        animator.cancel()
        phase = 0f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width == 0 || height == 0) return

        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = minOf(width, height) * 0.28f

        // Intensidad pulsante: aumenta y disminuye progresivamente
        val intensity = (sin(phase * 2 * PI) * 0.5 + 0.5).toFloat()
        val radius = baseRadius * (0.85f + 0.15f * intensity)

        // 1. Ondas luminosas expansivas (efecto de energía mágica)
        val waveCount = 3
        for (i in 0 until waveCount) {
            val wavePhase = (phase + i.toFloat() / waveCount) % 1f
            val waveRadius = radius * (1f + wavePhase * 1.8f)
            val waveAlpha = ((1f - wavePhase) * 120 * intensity).toInt().coerceIn(0, 255)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 4f * (1f - wavePhase)
            paint.color = Color.parseColor("#E040FB")
            paint.alpha = waveAlpha
            canvas.drawCircle(cx, cy, waveRadius, paint)
        }

        // 2. Halo de brillo exterior (aura mágica)
        val haloRadius = radius * (1.3f + 0.2f * intensity)
        val haloAlpha = (80 * intensity).toInt().coerceIn(0, 255)
        val haloShader = RadialGradient(
            cx, cy, haloRadius,
            intArrayOf(
                Color.argb(haloAlpha, 234, 128, 252),
                Color.argb(0, 156, 39, 176)
            ),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )
        paint.style = Paint.Style.FILL
        paint.shader = haloShader
        paint.alpha = 255
        canvas.drawCircle(cx, cy, haloRadius, paint)
        paint.shader = null

        // 3. Diamante con forma de rombo (cetro de Ripto)
        val colorIndex =
            (phase * (diamondColors.size - 1)).toInt().coerceIn(0, diamondColors.size - 2)
        val colorFrac = (phase * (diamondColors.size - 1)) - colorIndex
        val color1 = diamondColors[colorIndex]
        val color2 = diamondColors[colorIndex + 1]
        val blendedColor = blendColors(color1, color2, colorFrac)

        val diamondPath = Path().apply {
            moveTo(cx, cy - radius)           // arriba
            lineTo(cx + radius * 0.65f, cy)   // derecha
            lineTo(cx, cy + radius)           // abajo
            lineTo(cx - radius * 0.65f, cy)   // izquierda
            close()
        }

        // Gradiente interno del diamante
        val diamondShader = RadialGradient(
            cx, cy - radius * 0.3f, radius,
            intArrayOf(Color.WHITE, blendedColor, darkenColor(blendedColor)),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        paint.style = Paint.Style.FILL
        paint.shader = diamondShader
        canvas.drawPath(diamondPath, paint)
        paint.shader = null

        // 4. Destellos en forma de cruz (intensidad variable)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = Color.WHITE
        val destelloCount = 6
        for (i in 0 until destelloCount) {
            val angle = (i.toFloat() / destelloCount) * 2 * PI + phase * PI
            val destelloLen = radius * (0.6f + 0.6f * intensity)
            val alpha = (180 * intensity * sin(phase * 2 * PI + i).toFloat()
                .absoluteValue).toInt().coerceIn(0, 255)
            paint.alpha = alpha
            val x1 = cx + (radius * 0.9f) * cos(angle).toFloat()
            val y1 = cy + (radius * 0.9f) * sin(angle).toFloat()
            val x2 = cx + (radius * 0.9f + destelloLen) * cos(angle).toFloat()
            val y2 = cy + (radius * 0.9f + destelloLen) * sin(angle).toFloat()
            canvas.drawLine(x1, y1, x2, y2, paint)
        }

        // 5. Centro brillante blanco
        paint.style = Paint.Style.FILL
        paint.alpha = (200 + 55 * intensity).toInt().coerceIn(0, 255)
        paint.color = Color.WHITE
        canvas.drawCircle(cx, cy - radius * 0.15f, radius * 0.18f, paint)
    }

    private fun blendColors(c1: Int, c2: Int, fraction: Float): Int {
        val r = (Color.red(c1) + (Color.red(c2) - Color.red(c1)) * fraction).toInt()
        val g = (Color.green(c1) + (Color.green(c2) - Color.green(c1)) * fraction).toInt()
        val b = (Color.blue(c1) + (Color.blue(c2) - Color.blue(c1)) * fraction).toInt()
        return Color.rgb(r, g, b)
    }

    private fun darkenColor(color: Int): Int {
        return Color.rgb(
            (Color.red(color) * 0.5f).toInt(),
            (Color.green(color) * 0.5f).toInt(),
            (Color.blue(color) * 0.5f).toInt()
        )
    }
}