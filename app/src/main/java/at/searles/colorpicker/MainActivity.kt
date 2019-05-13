package at.searles.colorpicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wheel = findViewById<ColorWheelView>(R.id.wheel)
        val colorView = findViewById<View>(R.id.colorView)

        wheel.setOnColorChangedListener { _, color -> colorView.setBackgroundColor(color) }

        wheel.color = 0xffffff00.toInt()
    }
}
