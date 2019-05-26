package at.searles.colorpicker

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ColorViewFragment: Fragment() {

    var color: Int = 0xffff0000.toInt() // red as initial color

    private lateinit var mAdapter: ColorPickerPageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.color_view_fragment, container)

        // set up tabs. I use the childFragmentManager to easily access the parent.
        mAdapter = ColorPickerPageAdapter(this)

        val pager = view.findViewById<ViewPager>(R.id.pager)
        pager.adapter = mAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(pager)

        return view
    }
}