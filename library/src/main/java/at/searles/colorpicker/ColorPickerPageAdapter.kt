package at.searles.colorpicker

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ColorPickerPageAdapter(val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(index: Int): Fragment = when(index) {
        0 -> ColorWheelFragment()
        else -> ColorTableFragment()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int) = when(position) {
        0 -> context.getString(R.string.tab_color_wheel)
        else -> context.getString(R.string.tab_color_table)
    }
}