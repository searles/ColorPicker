package at.searles.colorpicker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ColorWheelFragment: Fragment() {

    var listener: ((Int) -> Unit)? = null

    private var mColorWheelView: ColorWheelView? = null
    private var mColorPreview: ColorIconView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.color_wheel_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mColorWheelView = view.findViewById(R.id.wheel)
        mColorPreview = view.findViewById(R.id.wheelColorPreview)

        // the color wheel handles orientation changes.
        mColorPreview!!.color = mColorWheelView!!.color

        mColorWheelView!!.setOnColorChangedListener {
            _, newColor -> run {
                mColorPreview!!.color = newColor
                listener?.invoke(newColor)
            }
        }
    }

    /**
     * Set color from outside
     */
    fun setColor(color: Int) {
        mColorWheelView!!.color = color
        mColorPreview!!.color = color
    }
}