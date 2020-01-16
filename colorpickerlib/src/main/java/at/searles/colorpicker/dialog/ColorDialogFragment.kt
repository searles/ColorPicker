package at.searles.colorpicker.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import at.searles.colorpicker.ColorIconView
import at.searles.colorpicker.ColorWheelView
import at.searles.colorpicker.R
import com.google.android.material.textfield.TextInputLayout
import java.lang.IllegalArgumentException

class ColorDialogFragment : DialogFragment() {

    private var color: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        builder.setView(R.layout.color_combined_fragment)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            (activity as ColorDialogCallback).setColor(this@ColorDialogFragment, color)
            dismiss()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            dismiss()
        }

        val dialog = builder.show()

        // set up views
        val wheelView: ColorWheelView = dialog.findViewById(R.id.wheel)!!
        val colorPreview: ColorIconView = dialog.findViewById(R.id.colorPreview)!!
        val colorEditText: EditText = dialog.findViewById(R.id.colorEditText)!!
        val colorInputLayout: TextInputLayout = dialog.findViewById(R.id.colorInputLayout)!!

        if(savedInstanceState == null) {
            color = arguments!!.getInt(colorKey)
            wheelView.color = color
            colorPreview.color = color
            colorEditText.setText("#${toColorString(color)}")
        }

        val listener = Listener(wheelView, colorEditText, colorPreview, colorInputLayout)

        wheelView.listener = listener
        colorEditText.addTextChangedListener(listener)

        return dialog
    }

    private inner class Listener(val wheelView: ColorWheelView, val colorEditText: EditText,
                                 val colorPreview: ColorIconView, val colorInputLayout: TextInputLayout
    ) : TextWatcher, ColorWheelView.Listener {
        private var textInputListenerEnabled = true

        override fun afterTextChanged(s: Editable) {
            if(!textInputListenerEnabled) return

            if(s.isEmpty()) {
                return
            }

            try {
                color = Color.parseColor(s.toString())
                wheelView.color = color
                colorPreview.color = color
                colorInputLayout.error = null
            } catch(e: IllegalArgumentException) {
                colorInputLayout.error = getString(R.string.badColorFormatError)
            }
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
            colorInputLayout.error = null
            textInputListenerEnabled = true
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

            if(alpha != 0xff) {
                return String.format("%02x%02x%02x%02x", alpha, r, g, b)
            } else {
                return String.format("%02x%02x%02x", r, g, b)
            }
        }
    }
}