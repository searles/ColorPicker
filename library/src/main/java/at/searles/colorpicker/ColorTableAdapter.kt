package at.searles.colorpicker

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Context
import android.graphics.Color
import android.support.v7.recyclerview.extensions.ListAdapter
import android.view.LayoutInflater

class ColorTableAdapter(context: Context) : ListAdapter<ColorEntry, ColorTableAdapter.ColorViewHolder>(ColorEntry.DiffCallback) {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    var listener: ((ColorEntry) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val mItemView = mInflater.inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(this, mItemView)
    }

    override fun onBindViewHolder(viewHolder: ColorViewHolder, position: Int) {
        viewHolder.bindTo(getItem(position))
    }

    fun indexOf(color: Int): Int {
        for(i in 0 until itemCount) {
            val item = getItem(i)
            if (item.rgb == color) {
                return i
            }
        }

        // not found
        return -1
    }

    fun indexOf(colorString: String): Int {
        for(i in 0 until itemCount) {
            val item = getItem(i)
            if (item.name == colorString) {
                return i
            }
        }

        // not found
        return -1
    }

    fun getColorEntry(position: Int): ColorEntry = getItem(position)

    class ColorViewHolder(private val parent: ColorTableAdapter, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val colorTextView = itemView.findViewById<TextView>(R.id.colorTextView)
        private val rgbTextView = itemView.findViewById<TextView>(R.id.rgbTextView)
        private val colorPreview = itemView.findViewById<ColorIconView>(R.id.colorPreview)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Use that to access the affected item in mWordList.
            parent.listener?.invoke(parent.getItem(layoutPosition))
        }

        fun bindTo(item: ColorEntry) {
            colorTextView.text = item.name
            colorPreview.color = item.rgb
            rgbTextView.text = String.format("#%06x", item.rgb and 0x00ffffff)
        }
    }
}
