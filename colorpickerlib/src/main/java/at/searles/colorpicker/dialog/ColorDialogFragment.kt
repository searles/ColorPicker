package at.searles.colorpicker.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import at.searles.colorpicker.CombinedColorView
import at.searles.colorpicker.R

class ColorDialogFragment : DialogFragment() {

    private var color: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        builder.setView(R.layout.color_combined_fragment)

        builder.setPositiveButton("Ok") { _, _ ->
            (activity as ColorDialogCallback).setColor(this@ColorDialogFragment, color)
            dismiss()
        }

        val dialog = builder.show()

        val view = dialog.findViewById<CombinedColorView>(
            R.id.colorView
        )!!

        view.listener = { color = it }

        if(savedInstanceState == null) {
            view.pageItem = 0
            color = arguments!!.getInt(colorKey)
            view.color = color
        }

        return dialog
    }

    companion object {
        private const val colorKey = "color"

        fun newInstance(color: Int): ColorDialogFragment {
            val bundle = Bundle().apply {
                putInt(colorKey, color)
            }

            return ColorDialogFragment().apply {
                arguments = bundle
            }
        }
    }
}