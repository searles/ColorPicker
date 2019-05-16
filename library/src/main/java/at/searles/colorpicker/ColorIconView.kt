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

    override fun onDraw(canvas: Canvas) {
        paint.color = color or 0xff000000.toInt() // fixme ignore alpha
        // TODO rounded rectangle with border.
        canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), width.toFloat() / 8, height.toFloat() / 8, paint)
    }
}