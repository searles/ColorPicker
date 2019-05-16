package at.searles.colorpicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color_view)

        // set up wheel
// fixme
//        val wheel = findViewById<at.searles.colorpicker.ColorWheelView>(R.id.wheel)
//        val colorView = findViewById<View>(R.id.colorView)
//
//        wheel.setOnColorChangedListener { _, color -> colorView.setBackgroundColor(color) }

        // set up tabs
        val adapter = ColorPickerPageAdapter(this, supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.pager)

        pager.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(pager)
    }
}
