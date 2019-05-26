package at.searles.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ColorIconView(context: Context, attrs: AttributeSet?): View(context, attrs) {
    var color: Int = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint()
    private val paintStroke = Paint()

    init {
        // 2dp
        paintStroke.strokeWidth = context.resources.displayMetrics.density * 2
        paintStroke.style = Paint.Style.STROKE

        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = color

        canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), width.toFloat() / 8, height.toFloat() / 8, paint)

        paintStroke.color = if(Color.luminance(color) < 0.1f) Color.WHITE else Color.BLACK
        canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), width.toFloat() / 8, height.toFloat() / 8, paintStroke)
    }
}