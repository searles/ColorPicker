package at.searles.colorpicker

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.searles.colorpicker.utils.DefaultColors

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
        mAdapter.submitList(DefaultColors.list)

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

                    mColorPreview.color = color
                    listener?.invoke(color)
                } catch(_: IllegalArgumentException) {
                    // ignore. It is a parse error.
                    // set marks to indicate that the color is invalid
                    mColorEditText.error = context.getString(R.string.invalidColorMessage)
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
}