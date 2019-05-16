package at.searles.colorpicker

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater

class ColorTableAdapter(val context: Context) : RecyclerView.Adapter<ColorTableAdapter.ColorViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val colorList: ArrayList<Int> = ArrayList()
    private val colorNames: HashMap<Int, String> = HashMap()

    init {
        // fixme
        colorList.add(0xff0000)
        colorNames[0xff0000] = "red"

        colorList.add(0xffff00)
        colorNames[0xffff00] = "yellow"

        colorList.add(0x00ff00)
        colorNames[0x00ff00] = "green"

        colorList.add(0x0000ff)
        colorNames[0x0000ff] = "blue"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val mItemView = mInflater.inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(mItemView)
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onBindViewHolder(viewHolder: ColorViewHolder, position: Int) {
        val rgb: Int = colorList[position]

        viewHolder.textView.text = colorNames[rgb]
        viewHolder.colorPreview.color = rgb
    }

    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.colorTextView)
        val colorPreview = itemView.findViewById<ColorIconView>(R.id.colorPreview)
    }
}
