package at.searles.colorpicker

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ColorViewFragment: Fragment() {

    private lateinit var wheelFragment: ColorWheelFragment
    private lateinit var tableFragment: ColorTableFragment

    var color: Int = 0xffff0000.toInt() // red as initial color

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.color_view, container)

        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)

        viewPager.adapter = ColorPagerAdapter(this, context!!)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO

    }

    class ColorPagerAdapter(val parent: ColorViewFragment, private val context: Context) : FragmentPagerAdapter(parent.childFragmentManager) {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position)

            return when(position) {
                0 -> {
                    parent.wheelFragment = fragment as ColorWheelFragment

                    parent.wheelFragment.listener = { color ->
                        run {
                            parent.tableFragment.setColor(color)
                            parent.color = color
                        }
                    }

                    parent.wheelFragment
                }
                else -> {
                    parent.tableFragment = fragment as ColorTableFragment

                    parent.tableFragment.listener = { color ->
                        run {
                            parent.wheelFragment.setColor(color)
                            parent.color = color
                        }
                    }

                    parent.tableFragment
                }
            }
        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            super.destroyItem(container, position, `object`)
//        }

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> ColorWheelFragment()
                else -> ColorTableFragment()
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> context.getString(R.string.colorWheelLabel)
                else -> context.getString(R.string.colorTableLabel)
            }
        }
    }
}