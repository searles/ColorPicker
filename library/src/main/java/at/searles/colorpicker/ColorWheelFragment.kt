package at.searles.colorpicker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.color_wheel_fragment.*

class ColorWheelFragment: Fragment() {

    var listener: ((Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.color_wheel_fragment, container, false)

        val wheel = view.findViewById<at.searles.colorpicker.ColorWheelView>(R.id.wheel)
        val wheelColorPreview = view.findViewById<View>(R.id.wheelColorPreview)

        wheelColorPreview.setBackgroundColor(wheel.color)

        wheel.setOnColorChangedListener { _, color -> run {
            wheelColorPreview.setBackgroundColor(color)
            listener?.invoke(color)
        } }

        return view
    }

    /**
     * Calling this method will not trigger the listener.
     */
    fun setColor(color: Int) {
        wheel.color = color
        wheelColorPreview.setBackgroundColor(color)
    }
}