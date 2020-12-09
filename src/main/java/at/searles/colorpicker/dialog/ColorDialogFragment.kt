package at.searles.colorpicker.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import at.searles.colorpicker.ColorIconView
import at.searles.colorpicker.ColorWheelView
import at.searles.colorpicker.R
import at.searles.colorpicker.utils.ColorEntriesAdapter
import at.searles.colorpicker.utils.DefaultColors

class ColorDialogFragment : DialogFragment() {

    private var color: Int = 0
    private lateinit var colorMap: Map<String, Int>
    private lateinit var wheelView: ColorWheelView
    private lateinit var colorPreview: ColorIconView
    private lateinit var colorEditText: AutoCompleteTextView

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        colorMap = DefaultColors(context!!).list.map { it.name to it.rgb}.toMap()

        val view = LayoutInflater.from(context!!).inflate(R.layout.color_combined_fragment, null)

        wheelView = view.findViewById(R.id.wheel)!!
        colorPreview = view.findViewById(R.id.colorPreview)!!
        colorEditText = view.findViewById(R.id.colorEditText)!!

        if(savedInstanceState == null) {
            color = arguments!!.getInt(colorKey)
            wheelView.color = color
            colorPreview.color = color
            colorEditText.setText("#${toColorString(color)}")
        }

        val mediator = Mediator()

        wheelView.listener = mediator
        colorEditText.addTextChangedListener(mediator)

        colorEditText.setAdapter(ColorEntriesAdapter(context!!))
        colorEditText.threshold = 1

        return AlertDialog.Builder(context!!).apply {
            setView(view)
            setPositiveButton(android.R.string.ok) { _, _ ->
                (activity as ColorDialogCallback).setColor(this@ColorDialogFragment, color)
                dismiss()
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                dismiss()
            }
        }.show()
    }

    private inner class Mediator : TextWatcher, ColorWheelView.Listener {
        private var textInputListenerEnabled = true

        override fun afterTextChanged(s: Editable) {
            if(!textInputListenerEnabled) return

            if(s.isEmpty()) {
                return
            }

            val colorString = s.toString()

            color = if (colorMap.containsKey(colorString)) {
                colorMap.getValue(colorString)
            } else {
                try {
                    Color.parseColor(colorString)
                } catch(e: IllegalArgumentException) {
                    colorEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning, 0)
                    return
                }
            }

            colorEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0)

            wheelView.color = color
            colorPreview.color = color
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // ignore
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // ignore
        }

        @SuppressLint("SetTextI18n")
        override fun colorChanged(color: Int) {
            this@ColorDialogFragment.color = color
            colorPreview.color = color
            textInputListenerEnabled = false
            colorEditText.setText("#${toColorString(color)}")
            textInputListenerEnabled = true

            colorEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    companion object {
        private const val colorKey = "color"

        fun newInstance(color: Int, bundle: Bundle = Bundle()): ColorDialogFragment {
            return ColorDialogFragment().apply {
                bundle.putInt(colorKey, color)
                arguments = bundle
            }
        }

        private fun toColorString(color: Int): String {
            val alpha = Color.alpha(color)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            return if(alpha != 0xff) {
                String.format("%02x%02x%02x%02x", alpha, r, g, b)
            } else {
                String.format("%02x%02x%02x", r, g, b)
            }
        }
    }
}