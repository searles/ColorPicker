package at.searles.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ColorWheelFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.color_wheel_fragment, container, false)

        val wheel = view.findViewById<at.searles.colorpicker.ColorWheelView>(R.id.wheel)
        val colorView = view.findViewById<View>(R.id.colorFieldView)

        colorView.setBackgroundColor(wheel.color)

        wheel.setOnColorChangedListener { _, color -> colorView.setBackgroundColor(color) }

        return view
    }
}