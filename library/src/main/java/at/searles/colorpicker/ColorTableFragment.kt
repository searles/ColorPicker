package at.searles.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class ColorTableFragment: Fragment() {

    lateinit var mAdapter: ColorTableAdapter
    lateinit var mColorEditText: EditText
    lateinit var mColorPreview: ColorIconView

    var listener: ((Int) -> Unit)? = null

    private var mTextListenerActive: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.color_table_fragment, container, false)

        mColorEditText = view.findViewById(R.id.colorEditText)
        mColorPreview = view.findViewById(R.id.colorPreview)

        mAdapter = ColorTableAdapter(context!!)
        mAdapter.submitList(createDefaultColorList())

        // set adapter listener
        mAdapter.listener = { colorEntry ->
            mColorEditText.setText(colorEntry.name)
            mColorPreview.color = colorEntry.rgb
            listener?.invoke(colorEntry.rgb)
        }

        // Connect the adapter with the RecyclerView.
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.colorTable)
        mRecyclerView.adapter = mAdapter

        // Give the RecyclerView a default layout manager.
        mRecyclerView.layoutManager = LinearLayoutManager(context!!)

        mColorEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(string: Editable) {
                if(!mTextListenerActive || string.isEmpty()) {
                    return
                }

                val item = mAdapter.findColor(string.toString())

                try {
                    val color: Int = item?.rgb ?: Color.parseColor(string.toString())
                    mColorPreview.color = color

                    listener?.invoke(color)
                } catch(_: IllegalArgumentException) {
                    // ignore. It is a parse error.
                }
            }
        })

        return view
    }

    /**
     * Calling this method will not trigger the listener
     */
    fun setColor(color: Int) {
        val colorString = mAdapter.findColor(color)?.name ?: String.format("#%06x", color and 0xffffff)

        mTextListenerActive = false
        mColorEditText.setText(colorString)
        mTextListenerActive = true

        mColorPreview.color = color
    }

    private fun createDefaultColorList(): MutableList<ColorEntry>? {
        val list = ArrayList<ColorEntry>()

        list.add(ColorEntry("white", 0xffffffff.toInt()))
        list.add(ColorEntry("black", 0xff000000.toInt()))
        list.add(ColorEntry("gray", 0xffaaaaaa.toInt()))
        list.add(ColorEntry("red", 0xffff0000.toInt()))
        list.add(ColorEntry("orange", 0xffffaa00.toInt()))
        list.add(ColorEntry("yellow", 0xffffff00.toInt()))
        list.add(ColorEntry("green", 0xff00ff00.toInt()))
        list.add(ColorEntry("cyan", 0xff00ffff.toInt()))
        list.add(ColorEntry("blue", 0xff0000ff.toInt()))
        list.add(ColorEntry("indigo", 0xff4400ff.toInt()))
        list.add(ColorEntry("violet", 0xffaa00ff.toInt()))

        return list
    }
}