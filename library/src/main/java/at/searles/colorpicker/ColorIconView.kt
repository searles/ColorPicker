package at.searles.colorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

class ColorIconView(context: Context, attrs: AttributeSet?): View(context, attrs) {

    private val RADIUS = 8 // dp
    private val radiusPixel: Float

    var color: Int = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint()
    private val paintStroke = Paint()

    init {
        paintStroke.strokeWidth = context.resources.displayMetrics.density * 2
        paintStroke.style = Paint.Style.STROKE

        paint.style = Paint.Style.FILL

        radiusPixel = context.resources.displayMetrics.density * RADIUS
    }

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState() // return value is null

        return Bundle().also {
            it.putInt(COLOR_LABEL, color)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)

        if(state is Bundle) {
            color = state.getInt(COLOR_LABEL)
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = color

        canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), radiusPixel, radiusPixel, paint)

        paintStroke.color = Color.WHITE
        canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), radiusPixel, radiusPixel, paintStroke)

        paintStroke.color = Color.BLACK
        canvas.drawRoundRect(paintStroke.strokeWidth, paintStroke.strokeWidth,
            width.toFloat() - paintStroke.strokeWidth, height.toFloat() - paintStroke.strokeWidth,
            radiusPixel, radiusPixel, paintStroke)
    }

    companion object {
        private const val COLOR_LABEL = "COLOR"
    }
}