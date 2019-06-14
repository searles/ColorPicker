package at.searles.colorpicker

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout

class ColorTableView: LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val mAdapter: ColorTableAdapter
    private val mColorTable: RecyclerView
    private val mColorEditText: EditText
    private val mColorPreview: ColorIconView

    var listener: ((Int) -> Unit)? = null

    // this is used to deactivate the text listener
    private var isSetExternally: Boolean = false

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.color_table_view, this, true)

        mColorEditText = view.findViewById(R.id.colorEditText)
        mColorPreview = view.findViewById(R.id.colorPreview)
        mColorTable = view.findViewById(R.id.colorTable)

        mAdapter = ColorTableAdapter(context)
        mAdapter.submitList(createDefaultColorList())

        // set adapter listener
        mAdapter.listener = { colorEntry ->
            mColorEditText.setText(colorEntry.name)
            mColorPreview.color = colorEntry.rgb
            listener?.invoke(colorEntry.rgb)
        }

        // Connect the adapter with the RecyclerView.
        mColorTable.adapter = mAdapter

        // Give the RecyclerView a default layout manager.
        mColorTable.layoutManager = LinearLayoutManager(context!!)

        mColorEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(string: Editable) {
                if(isSetExternally || string.isEmpty()) {
                    mColorEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    return
                }

                val position = mAdapter.indexOf(string.toString().trim())

                try {
                    val color: Int = if (position != -1) {
                        mColorTable.scrollToPosition(position)
                        mAdapter.getColorEntry(position).rgb
                    } else {
                        Color.parseColor(string.toString())
                    }

                    // clear error marks
                    mColorEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                    mColorPreview.color = color
                    listener?.invoke(color)
                } catch(_: IllegalArgumentException) {
                    // ignore. It is a parse error.
                    // set marks to indicate that the color is invalid
                    mColorEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.sym_action_call, 0)
                }
            }
        })
    }

    fun setColor(color: Int) {
        val position = mAdapter.indexOf(color)

        val colorString =
            when {
                position != -1 -> {
                    mColorTable.scrollToPosition(position)
                    mAdapter.getColorEntry(position).name
                }
                Color.alpha(color) != 0xff -> String.format("#%08x", color)
                else -> String.format("#%06x", color and 0xffffff)
            }

        isSetExternally = true
        mColorEditText.setText(colorString)
        isSetExternally = false

        mColorPreview.color = color
    }

    private fun createDefaultColorList(): MutableList<ColorEntry>? {
        val list = ArrayList<ColorEntry>()

        list.add(ColorEntry("maroon", 0xFF800000.toInt()))
        list.add(ColorEntry("dark red", 0xFF8B0000.toInt()))
        list.add(ColorEntry("brown", 0xFFA52A2A.toInt()))
        list.add(ColorEntry("firebrick", 0xFFB22222.toInt()))
        list.add(ColorEntry("crimson", 0xFFDC143C.toInt()))
        list.add(ColorEntry("red", 0xFFFF0000.toInt()))
        list.add(ColorEntry("tomato", 0xFFFF6347.toInt()))
        list.add(ColorEntry("coral", 0xFFFF7F50.toInt()))
        list.add(ColorEntry("indian red", 0xFFCD5C5C.toInt()))
        list.add(ColorEntry("light coral", 0xFFF08080.toInt()))
        list.add(ColorEntry("dark salmon", 0xFFE9967A.toInt()))
        list.add(ColorEntry("salmon", 0xFFFA8072.toInt()))
        list.add(ColorEntry("light salmon", 0xFFFFA07A.toInt()))
        list.add(ColorEntry("orange red", 0xFFFF4500.toInt()))
        list.add(ColorEntry("dark orange", 0xFFFF8C00.toInt()))
        list.add(ColorEntry("orange", 0xFFFFA500.toInt()))
        list.add(ColorEntry("gold", 0xFFFFD700.toInt()))
        list.add(ColorEntry("dark golden rod", 0xFFB8860B.toInt()))
        list.add(ColorEntry("golden rod", 0xFFDAA520.toInt()))
        list.add(ColorEntry("pale golden rod", 0xFFEEE8AA.toInt()))
        list.add(ColorEntry("dark khaki", 0xFFBDB76B.toInt()))
        list.add(ColorEntry("khaki", 0xFFF0E68C.toInt()))
        list.add(ColorEntry("olive", 0xFF808000.toInt()))
        list.add(ColorEntry("yellow", 0xFFFFFF00.toInt()))
        list.add(ColorEntry("yellow green", 0xFF9ACD32.toInt()))
        list.add(ColorEntry("dark olive green", 0xFF556B2F.toInt()))
        list.add(ColorEntry("olive drab", 0xFF6B8E23.toInt()))
        list.add(ColorEntry("lawn green", 0xFF7CFC00.toInt()))
        list.add(ColorEntry("chart reuse", 0xFF7FFF00.toInt()))
        list.add(ColorEntry("green yellow", 0xFFADFF2F.toInt()))
        list.add(ColorEntry("dark green", 0xFF006400.toInt()))
        list.add(ColorEntry("green", 0xFF008000.toInt()))
        list.add(ColorEntry("forest green", 0xFF228B22.toInt()))
        list.add(ColorEntry("lime", 0xFF00FF00.toInt()))
        list.add(ColorEntry("lime green", 0xFF32CD32.toInt()))
        list.add(ColorEntry("light green", 0xFF90EE90.toInt()))
        list.add(ColorEntry("pale green", 0xFF98FB98.toInt()))
        list.add(ColorEntry("dark sea green", 0xFF8FBC8F.toInt()))
        list.add(ColorEntry("medium spring green", 0xFF00FA9A.toInt()))
        list.add(ColorEntry("spring green", 0xFF00FF7F.toInt()))
        list.add(ColorEntry("sea green", 0xFF2E8B57.toInt()))
        list.add(ColorEntry("medium aqua marine", 0xFF66CDAA.toInt()))
        list.add(ColorEntry("medium sea green", 0xFF3CB371.toInt()))
        list.add(ColorEntry("light sea green", 0xFF20B2AA.toInt()))
        list.add(ColorEntry("dark slate gray", 0xFF2F4F4F.toInt()))
        list.add(ColorEntry("teal", 0xFF008080.toInt()))
        list.add(ColorEntry("dark cyan", 0xFF008B8B.toInt()))
        list.add(ColorEntry("aqua", 0xFF00FFFF.toInt()))
        list.add(ColorEntry("cyan", 0xFF00FFFF.toInt()))
        list.add(ColorEntry("light cyan", 0xFFE0FFFF.toInt()))
        list.add(ColorEntry("dark turquoise", 0xFF00CED1.toInt()))
        list.add(ColorEntry("turquoise", 0xFF40E0D0.toInt()))
        list.add(ColorEntry("medium turquoise", 0xFF48D1CC.toInt()))
        list.add(ColorEntry("pale turquoise", 0xFFAFEEEE.toInt()))
        list.add(ColorEntry("aqua marine", 0xFF7FFFD4.toInt()))
        list.add(ColorEntry("powder blue", 0xFFB0E0E6.toInt()))
        list.add(ColorEntry("cadet blue", 0xFF5F9EA0.toInt()))
        list.add(ColorEntry("steel blue", 0xFF4682B4.toInt()))
        list.add(ColorEntry("corn flower blue", 0xFF6495ED.toInt()))
        list.add(ColorEntry("deep sky blue", 0xFF00BFFF.toInt()))
        list.add(ColorEntry("dodger blue", 0xFF1E90FF.toInt()))
        list.add(ColorEntry("light blue", 0xFFADD8E6.toInt()))
        list.add(ColorEntry("sky blue", 0xFF87CEEB.toInt()))
        list.add(ColorEntry("light sky blue", 0xFF87CEFA.toInt()))
        list.add(ColorEntry("midnight blue", 0xFF191970.toInt()))
        list.add(ColorEntry("navy", 0xFF000080.toInt()))
        list.add(ColorEntry("dark blue", 0xFF00008B.toInt()))
        list.add(ColorEntry("medium blue", 0xFF0000CD.toInt()))
        list.add(ColorEntry("blue", 0xFF0000FF.toInt()))
        list.add(ColorEntry("royal blue", 0xFF4169E1.toInt()))
        list.add(ColorEntry("blue violet", 0xFF8A2BE2.toInt()))
        list.add(ColorEntry("indigo", 0xFF4B0082.toInt()))
        list.add(ColorEntry("dark slate blue", 0xFF483D8B.toInt()))
        list.add(ColorEntry("slate blue", 0xFF6A5ACD.toInt()))
        list.add(ColorEntry("medium slate blue", 0xFF7B68EE.toInt()))
        list.add(ColorEntry("medium purple", 0xFF9370DB.toInt()))
        list.add(ColorEntry("dark magenta", 0xFF8B008B.toInt()))
        list.add(ColorEntry("dark violet", 0xFF9400D3.toInt()))
        list.add(ColorEntry("dark orchid", 0xFF9932CC.toInt()))
        list.add(ColorEntry("medium orchid", 0xFFBA55D3.toInt()))
        list.add(ColorEntry("purple", 0xFF800080.toInt()))
        list.add(ColorEntry("thistle", 0xFFD8BFD8.toInt()))
        list.add(ColorEntry("plum", 0xFFDDA0DD.toInt()))
        list.add(ColorEntry("violet", 0xFFEE82EE.toInt()))
        list.add(ColorEntry("magenta / fuchsia", 0xFFFF00FF.toInt()))
        list.add(ColorEntry("orchid", 0xFFDA70D6.toInt()))
        list.add(ColorEntry("medium violet red", 0xFFC71585.toInt()))
        list.add(ColorEntry("pale violet red", 0xFFDB7093.toInt()))
        list.add(ColorEntry("deep pink", 0xFFFF1493.toInt()))
        list.add(ColorEntry("hot pink", 0xFFFF69B4.toInt()))
        list.add(ColorEntry("light pink", 0xFFFFB6C1.toInt()))
        list.add(ColorEntry("pink", 0xFFFFC0CB.toInt()))
        list.add(ColorEntry("antique white", 0xFFFAEBD7.toInt()))
        list.add(ColorEntry("beige", 0xFFF5F5DC.toInt()))
        list.add(ColorEntry("bisque", 0xFFFFE4C4.toInt()))
        list.add(ColorEntry("blanched almond", 0xFFFFEBCD.toInt()))
        list.add(ColorEntry("wheat", 0xFFF5DEB3.toInt()))
        list.add(ColorEntry("corn silk", 0xFFFFF8DC.toInt()))
        list.add(ColorEntry("lemon chiffon", 0xFFFFFACD.toInt()))
        list.add(ColorEntry("light golden rod yellow", 0xFFFAFAD2.toInt()))
        list.add(ColorEntry("light yellow", 0xFFFFFFE0.toInt()))
        list.add(ColorEntry("saddle brown", 0xFF8B4513.toInt()))
        list.add(ColorEntry("sienna", 0xFFA0522D.toInt()))
        list.add(ColorEntry("chocolate", 0xFFD2691E.toInt()))
        list.add(ColorEntry("peru", 0xFFCD853F.toInt()))
        list.add(ColorEntry("sandy brown", 0xFFF4A460.toInt()))
        list.add(ColorEntry("burly wood", 0xFFDEB887.toInt()))
        list.add(ColorEntry("tan", 0xFFD2B48C.toInt()))
        list.add(ColorEntry("rosy brown", 0xFFBC8F8F.toInt()))
        list.add(ColorEntry("moccasin", 0xFFFFE4B5.toInt()))
        list.add(ColorEntry("navajo white", 0xFFFFDEAD.toInt()))
        list.add(ColorEntry("peach puff", 0xFFFFDAB9.toInt()))
        list.add(ColorEntry("misty rose", 0xFFFFE4E1.toInt()))
        list.add(ColorEntry("lavender blush", 0xFFFFF0F5.toInt()))
        list.add(ColorEntry("linen", 0xFFFAF0E6.toInt()))
        list.add(ColorEntry("old lace", 0xFFFDF5E6.toInt()))
        list.add(ColorEntry("papaya whip", 0xFFFFEFD5.toInt()))
        list.add(ColorEntry("sea shell", 0xFFFFF5EE.toInt()))
        list.add(ColorEntry("mint cream", 0xFFF5FFFA.toInt()))
        list.add(ColorEntry("slate gray", 0xFF708090.toInt()))
        list.add(ColorEntry("light slate gray", 0xFF778899.toInt()))
        list.add(ColorEntry("light steel blue", 0xFFB0C4DE.toInt()))
        list.add(ColorEntry("lavender", 0xFFE6E6FA.toInt()))
        list.add(ColorEntry("floral white", 0xFFFFFAF0.toInt()))
        list.add(ColorEntry("alice blue", 0xFFF0F8FF.toInt()))
        list.add(ColorEntry("ghost white", 0xFFF8F8FF.toInt()))
        list.add(ColorEntry("honeydew", 0xFFF0FFF0.toInt()))
        list.add(ColorEntry("ivory", 0xFFFFFFF0.toInt()))
        list.add(ColorEntry("azure", 0xFFF0FFFF.toInt()))
        list.add(ColorEntry("snow", 0xFFFFFAFA.toInt()))
        list.add(ColorEntry("black", 0xFF000000.toInt()))
        list.add(ColorEntry("dim gray", 0xFF696969.toInt()))
        list.add(ColorEntry("gray", 0xFF808080.toInt()))
        list.add(ColorEntry("dark gray", 0xFFA9A9A9.toInt()))
        list.add(ColorEntry("silver", 0xFFC0C0C0.toInt()))
        list.add(ColorEntry("light gray", 0xFFD3D3D3.toInt()))
        list.add(ColorEntry("gainsboro", 0xFFDCDCDC.toInt()))
        list.add(ColorEntry("white smoke", 0xFFF5F5F5.toInt()))
        list.add(ColorEntry("white", 0xFFFFFFFF.toInt()))

        return list
    }
}