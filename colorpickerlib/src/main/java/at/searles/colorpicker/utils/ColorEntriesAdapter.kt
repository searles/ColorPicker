package at.searles.colorpicker.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import at.searles.colorpicker.ColorIconView
import at.searles.colorpicker.R

class ColorEntriesAdapter(context: Context): ArrayAdapter<String>(context, R.layout.color_entry) {

    private val colorList = DefaultColors(context).list
    private val colorMap = colorList.map { it.name to it.rgb }.toMap()

    init {
        addAll(colorList.map { it.name })
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.color_entry, parent, false)

        val textView = view.findViewById<TextView>(R.id.textView)
        val colorIconView = view.findViewById<ColorIconView>(R.id.colorView)

        val name = getItem(position)!!

        textView.text = name
        colorIconView.color = colorMap.getValue(name)

        return view
    }
}