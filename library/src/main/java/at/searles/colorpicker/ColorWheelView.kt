package at.searles.colorpicker

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import at.searles.colorpicker.utils.RgbCommons
import at.searles.colorpicker.utils.RybColors
import kotlin.math.min

/**
 * Plain view showing a color wheel.
 */
class ColorWheelView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val colorKey = "color"

    private val desiredSizeDp: Float = 320F
    private var dotRadiusDp: Float = 8F

    private val dotRadius: Float
    private val dotPaint: Paint

    private val triangle: Triangle
    private val circle: HueCircle

    var listener: ((View, Int) -> Unit)? = null

    // color is calculated as comp colorFraction + whiteFraction.
    // colorFraction is related to saturation while whiteFraction
    // corresponds to brightness. This model avoids divisions
    // and is easier to calculate since it directly corresponds to
    // barymetric coordinates in the triangle.
    private var hue: Float = 0F
    private var colorFraction: Float = 1F
    private var whiteFraction: Float = 0F

    var color: Int
        get() {
            var r = RybColors.red(hue)
            var g = RybColors.green(hue)
            var b = RybColors.blue(hue)

            r = r * colorFraction + whiteFraction
            g = g * colorFraction + whiteFraction
            b = b * colorFraction + whiteFraction

            return RgbCommons.rgb2int(r, g, b)
        }
        set(rgb) {
            val rgbf = RgbCommons.int2rgb(rgb, FloatArray(3))

            this.hue = RybColors.hue(rgbf[0], rgbf[1], rgbf[2])
            this.whiteFraction = Math.min(Math.min(rgbf[0], rgbf[1]), rgbf[2])
            this.colorFraction = Math.max(Math.max(rgbf[0], rgbf[1]), rgbf[2]) - whiteFraction

            triangle.hueUpdated()

            invalidate()
        }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null) // for composedshader

        this.triangle = Triangle()
        this.circle = HueCircle()

        this.dotPaint = Paint()
        this.dotPaint.style = Paint.Style.STROKE

        this.dotRadius = dotRadiusDp * context.resources.displayMetrics.density
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // thanks to http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
        val desiredSize = (desiredSizeDp * context.resources.displayMetrics.density).toInt()

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var width: Int = -1
        var height: Int = -1

        // just an extensive case distinction
        when(widthMode) {
            View.MeasureSpec.EXACTLY -> {
                when(heightMode) {
                    View.MeasureSpec.EXACTLY -> {
                        width = widthSize
                        height = heightSize
                    }
                    View.MeasureSpec.AT_MOST -> {
                        width = widthSize
                        height = min(widthSize, heightSize)
                    }
                    View.MeasureSpec.UNSPECIFIED -> {
                        width = widthSize
                        height = widthSize
                    }
                }
            }
            View.MeasureSpec.AT_MOST -> {
                when(heightMode) {
                    View.MeasureSpec.EXACTLY -> {
                        width = min(widthSize, heightSize)
                        height = heightSize
                    }
                    View.MeasureSpec.AT_MOST -> {
                        width = min(min(widthSize, heightSize), desiredSize)
                        height = width
                    }
                    View.MeasureSpec.UNSPECIFIED -> {
                        width = min(desiredSize, widthSize)
                        height = width
                    }
                }
            }
            View.MeasureSpec.UNSPECIFIED -> {
                when(heightMode) {
                    View.MeasureSpec.EXACTLY -> {
                        width = heightSize
                        height = heightSize
                    }
                    View.MeasureSpec.AT_MOST -> {
                        width = min(desiredSize, heightSize)
                        height = width
                    }
                    View.MeasureSpec.UNSPECIFIED -> {
                        width = desiredSize
                        height = desiredSize
                    }
                }
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.circle.updateDimensions()
        this.triangle.updateDimensions()
    }

    override fun onDraw(canvas: Canvas) {
        circle.draw(canvas)
        triangle.draw(canvas)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked

        for (i in 0 until event.pointerCount) {
            val id = event.getPointerId(i)
            when (action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> if (!circle.select(
                        event.getX(i),
                        event.getY(i),
                        id
                    )
                ) {
                    // both should be mutually exclusive
                    triangle.select(event.getX(i), event.getY(i), id)
                }
                MotionEvent.ACTION_MOVE -> {
                    circle.dragTo(event.getX(i), event.getY(i), id)
                    triangle.dragTo(event.getX(i), event.getY(i), id)
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    circle.unselect(id)
                    triangle.unselect(id)
                }
                MotionEvent.ACTION_UP -> {
                    circle.unselect(id)
                    triangle.unselect(id)

                    performClick()
                }
                else -> super.onTouchEvent(event)
            }
        }

        return true // fixme
    }

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState() // ignore. Parent is a View.

        val bundle = Bundle()
        bundle.putInt(colorKey, color)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null) // ignore, parent is a View.

        if (state is Bundle) {
            this.color = state.getInt(colorKey)
        }
    }

    private fun onColorChanged(color: Int) {
        listener?.invoke(this, color)
    }

    private fun drawDot(canvas: Canvas, x: Float, y: Float) {
        dotPaint.color = Color.BLACK
        dotPaint.strokeWidth = dotRadius / 4f

        canvas.drawCircle(x, y, dotRadius, dotPaint)

        dotPaint.color = Color.WHITE
        dotPaint.strokeWidth = dotRadius / 4f

        canvas.drawCircle(x, y, dotRadius * 0.75f, dotPaint)
    }

    internal inner class HueCircle {
        internal var innerRadius: Float = 0F
        private var outerRadius: Float = 0F

        private lateinit var huePaint: Paint
        private lateinit var hueCircle: Path

        private var selectedId = -1

        fun updateDimensions() {
            val size = min(height, width)

            this.outerRadius = size * 0.5F
            this.innerRadius = size * 0.4F

            initGradients()

            invalidate()
        }

        fun select(x: Float, y: Float, id: Int): Boolean {
            return setSelectedPoint(x, y, id, true)
        }

        fun dragTo(x: Float, y: Float, id: Int): Boolean {
            return setSelectedPoint(x, y, id, false)
        }

        fun unselect(id: Int): Boolean {
            if (selectedId != id) {
                return false
            }

            selectedId = -1
            return true
        }

        fun setSelectedPoint(x: Float, y: Float, id: Int, init: Boolean): Boolean {
            if (!init && selectedId != id) {
                return false
            }

            val dx = x - width / 2f
            val dy = y - height / 2f

            if (init) {
                val rad2 = dx * dx + dy * dy

                if (rad2 < innerRadius * innerRadius || outerRadius * outerRadius < rad2) {
                    return false
                }

                selectedId = id
            }

            // selected is true, update hue.
            val h = (Math.atan2(dy.toDouble(), dx.toDouble()) / (2 * Math.PI)).toFloat()

            hue = if (h < 0f) h + 1 else h

            triangle.hueUpdated()
            invalidate()

            // hue was changed
            onColorChanged(color)

            return true
        }

        fun draw(canvas: Canvas) {
            canvas.drawPath(hueCircle, huePaint)

            val co = Math.cos(hue * 2.0 * Math.PI)
            val si = Math.sin(hue * 2.0 * Math.PI)

            val rad = outerRadius - dotRadius * 1.5f

            val x0 = (co * rad).toFloat() + width / 2f
            val y0 = (si * rad).toFloat() + height / 2f

            drawDot(canvas, x0, y0)
        }

        fun initGradients() {
            val colors = IntArray(RybColors.colorSegmentsCount() + 1)

            for (i in 0 until RybColors.colorSegmentsCount()) {
                val hue = i.toFloat() / RybColors.colorSegmentsCount().toFloat()
                val r = RybColors.red(hue)
                val g = RybColors.green(hue)
                val b = RybColors.blue(hue)

                colors[i] = RgbCommons.rgb2int(r, g, b)
            }

            colors[RybColors.colorSegmentsCount()] = colors[0]

            val hueGradient = SweepGradient(width / 2f, height / 2f, colors, null)

            huePaint = Paint()
            huePaint.style = Paint.Style.FILL
            huePaint.shader = hueGradient

            hueCircle = Path()
            hueCircle.addCircle(width / 2f, height / 2f, outerRadius, Path.Direction.CW)
            hueCircle.addCircle(width / 2f, height / 2f, innerRadius, Path.Direction.CW)
            hueCircle.fillType = Path.FillType.EVEN_ODD
        }
    }

    internal inner class Triangle {
        val sin120 = (Math.sqrt(3.0) / 2.0).toFloat()
        val cos120 = -0.5f

        // the following coordinates are in pixels
        var centerX: Float = 0F
        var centerY: Float = 0F
        var radius: Float = 0F

        // The triangle. Initially hue is assumed to be 0.
        private var ax: Float = 1F // corner of color
        private var ay: Float = 0F

        private var bx: Float = cos120 // corner of white
        private var by: Float = -sin120

        private var cx: Float = cos120 // corner of white
        private var cy: Float = sin120

        private val path: Path = Path()
        private val paint: Paint = Paint()
        private var selectedId = -1
        private var draggedCorner = -1

        init {
            paint.style = Paint.Style.FILL
        }

        private fun x(x: Float): Float {
            return x * radius + centerX
        }

        private fun y(y: Float): Float {
            return y * radius + centerY
        }

        private fun ix(x: Float): Float {
            return (x - centerX) / radius
        }

        private fun iy(y: Float): Float {
            return (y - centerY) / radius
        }

        fun draw(canvas: Canvas) {
            canvas.drawPath(path, paint)

            val blackFraction = 1f - colorFraction - whiteFraction
            val px = ax * colorFraction + bx * whiteFraction + cx * blackFraction
            val py = ay * colorFraction + by * whiteFraction + cy * blackFraction

            drawDot(canvas, x(px), y(py))
        }

        fun select(x: Float, y: Float, id: Int): Boolean {
            return setSelectedPoint(x, y, id, true)
        }

        fun dragTo(x: Float, y: Float, id: Int): Boolean {
            return setSelectedPoint(x, y, id, false)
        }

        fun unselect(id: Int): Boolean {
            if (selectedId == id) {
                selectedId = -1
                return true
            }

            return false
        }

        fun setSelectedPoint(vx: Float, vy: Float, id: Int, init: Boolean): Boolean {
            if (!init && selectedId != id) {
                // id is not the finger that selected this triangle initially.
                return false
            }

            val x = ix(vx)
            val y = iy(vy)

            val det = (by - cy) * (ax - cx) + (cx - bx) * (ay - cy)

            var s = ((by - cy) * (x - cx) + (cx - bx) * (y - cy)) / det // color
            var t = ((cy - ay) * (x - cx) + (ax - cx) * (y - cy)) / det // white
            var u = 1f - s - t // black

            if (init) {
                if (!(s >= 0 && t >= 0 && u >= 0)) {
                    // outside triangle
                    return false
                }

                // inside triangle
                selectedId = id
            }

            if (s >= 0 && t >= 0 && u >= 0) {
                // inside triangle
                draggedCorner = -1 // no dragged corner.
            }

            if (t <= 0 && u <= 0) {
                draggedCorner = 0 // A
                s = 1f
                t = 0f
                u = 0f
            }

            if (s <= 0 && u <= 0) {
                draggedCorner = 1 // B
                s = 0f
                t = 1f
                u = 0f
            }

            if (s <= 0 && t <= 0) {
                draggedCorner = 2 // C
                s = 0f
                t = 0f
                u = 1f
            }

            // now, at most one of them is negative

            // check if left/right of one side.
            if (u < 0) {
                s += u / 2f
                t += u / 2f
                u = 0f // for debugging
            }

            if (t < 0) {
                s += t / 2f
                u += t / 2f
                t = 0f
            }

            if (s < 0) {
                t += s / 2f
                u += s / 2f
                s = 0f
            }

            // set color coordinates
            colorFraction = Math.max(0f, Math.min(1f, s))
            whiteFraction = Math.max(0f, Math.min(1f, t))

            // is there a drag? If yes, update hue.
            if (draggedCorner != -1) { // A is dragged
                // adapt hue
                hue = ((Math.atan2(
                    y.toDouble(),
                    x.toDouble()
                ) + draggedCorner.toDouble() * Math.PI * 2.0 / 3.0) / (2 * Math.PI)).toFloat()

                hue = (hue - Math.floor(hue.toDouble())).toFloat()

                hueUpdated()
            }

            // color was changed.
            onColorChanged(color)

            invalidate()

            return true
        }

        /**
         * Called to update the triangle to a new value of 'hue'
         */
        fun hueUpdated() {
            // corners of the triangle

            ax = Math.cos(hue * 2.0 * Math.PI).toFloat()
            ay = Math.sin(hue * 2.0 * Math.PI).toFloat()

            cx = ax * cos120 - ay * sin120
            cy = ax * sin120 + ay * cos120

            bx = cx * cos120 - cy * sin120
            by = cx * sin120 + cy * cos120

            updateTriange()
        }

        fun updateDimensions() {
            this.centerX = width / 2f
            this.centerY = height / 2f

            this.radius = circle.innerRadius

            this.updateTriange()
        }

        fun updateTriange() {
            // Triangle
            path.rewind()

            path.moveTo(x(ax), y(ay))
            path.lineTo(x(bx), y(by))
            path.lineTo(x(cx), y(cy))
            path.close()

            // Color of triangle
            val cbGradient = LinearGradient(
                x(ax),
                y(ay),
                x(-ax / 2f),
                y(-ay / 2f),
                RybColors.color(hue),
                Color.BLACK,
                Shader.TileMode.CLAMP
            )
            val wbGradient =
                LinearGradient(x(bx), y(by), x(-bx / 2f), y(-by / 2f), Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP)

            val shader = ComposeShader(cbGradient, wbGradient, PorterDuff.Mode.ADD)

            paint.shader = shader
        }
    }
}