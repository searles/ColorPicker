package at.searles.colorpicker

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class ColorPickerPageAdapter(private val parent: ColorViewFragment) : FragmentPagerAdapter(parent.fragmentManager) {

    private val context = parent.context!!

    private lateinit var colorWheelFragment: ColorWheelFragment
    private lateinit var colorTableFragment: ColorTableFragment

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        super.instantiateItem(container, position).let {
            when(position) {
                0 -> colorWheelFragment = (it as ColorWheelFragment).apply { it.listener = { color -> parent.color = color; colorTableFragment.setColor(color) } }
                else -> colorTableFragment = (it as ColorTableFragment).apply { it.listener = { color -> parent.color = color; colorWheelFragment.setColor(color) } }
            }

            it
        }

    override fun getItem(index: Int): Fragment = when(index) {
        0 -> ColorWheelFragment()
        else -> ColorTableFragment()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): String = when(position) {
        0 -> context.getString(R.string.tab_color_wheel)
        else -> context.getString(R.string.tab_color_table)
    }
}