package at.searles.colorpicker

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class CombinedColorView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private val viewPager: ViewPager
    private var wheelView: ColorWheelView? = null
    private var wheelPreview: ColorIconView? = null
    private var tableView: ColorTableView? = null

    var listener: ((Int) -> Unit)? = null

    var color: Int // red as initial color
        get() = mColor
        set(value) {
            mColor = value
            wheelView?.color = value
            wheelPreview?.color = value
            tableView?.setColor(value)
        }

    var pageItem: Int
        get() = viewPager.currentItem
        set(value) { viewPager.currentItem = value }

    private var mColor = 0xffff0000.toInt()

    init {
        orientation = VERTICAL

        val view = LayoutInflater.from(context).inflate(R.layout.color_view, this, true)

        viewPager = view.findViewById(R.id.viewPager)

        viewPager.adapter = ColorPagerAdapter(this, context)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState() // return value is null

        return Bundle().also {
            it.putInt("color", color)
            it.putInt("pageItem", pageItem) // save tab index
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)

        if(state is Bundle) {
            // this also sets the colors in all views.
            color = state.getInt("color")
            pageItem = state.getInt("pageItem")
        }
    }

    class ColorPagerAdapter(private val parent: CombinedColorView, private val context: Context) : PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return when(position) {
                0 -> LayoutInflater.from(context).inflate(R.layout.color_wheel_compound_view, container, false).also {
                    parent.wheelView = it.findViewById(R.id.wheel)
                    parent.wheelPreview = it.findViewById(R.id.wheelColorPreview)

                    parent.wheelView!!.color = parent.color
                    parent.wheelPreview!!.color = parent.color

                    parent.wheelView!!.listener = { _, color ->
                        parent.mColor = color
                        parent.tableView?.setColor(color)
                        parent.wheelPreview?.color = color
                        parent.listener?.invoke(color)
                    }

                    container.addView(it)
                }
                else -> LayoutInflater.from(context).inflate(R.layout.color_table_fragment, container, false).also {
                    parent.tableView = it.findViewById(R.id.tableView)

                    parent.tableView!!.setColor(parent.color)

                    parent.tableView!!.listener = { color ->
                        parent.mColor = color
                        parent.wheelView?.color = color
                        parent.wheelPreview?.color = color
                        parent.listener?.invoke(color)
                    }

                    container.addView(it)
                }
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            super.destroyItem(container, position, obj)
            container.removeView(obj as View)
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