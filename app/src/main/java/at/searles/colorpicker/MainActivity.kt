package at.searles.colorpicker

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import at.searles.colorpicker.dialog.ColorDialogCallback
import at.searles.colorpicker.dialog.ColorDialogFragment

class MainActivity : AppCompatActivity(), ColorDialogCallback {

    private var selectedColorPreview: ColorIconView? = null

    private var color: Int
        get() = selectedColorPreview!!.color
        set(value) { selectedColorPreview!!.color = value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color_view_layout)

        selectedColorPreview = findViewById(R.id.selectedColor)
    }

    fun onDialogStart(view: View) {
        when(view.id) {
            R.id.wheelButton -> {
                WheelDialogFragment().show(supportFragmentManager, "dialog")
            }
            R.id.tableButton -> {
                TableDialogFragment().show(supportFragmentManager, "dialog")
            }
            R.id.combinedButton -> {
                ColorDialogFragment.newInstance(color)
                    .show(supportFragmentManager, "dialog")
            }
        }
    }

    class WheelDialogFragment: DialogFragment() {

        private var color: Int = 0xffff0000.toInt() // save on rotate

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            color = savedInstanceState?.getInt("color") ?: (activity as MainActivity).color
        }

        override fun onSaveInstanceState(outState: Bundle) {
            outState.putInt("color", color)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(context!!)
            builder.setView(R.layout.color_wheel_compound_view)

            builder.setPositiveButton("Ok") { _, _ -> (activity as MainActivity).color = color }

            val dialog = builder.show()

            val colorWheelView = dialog.findViewById<ColorWheelView>(R.id.wheel)
            val colorPreview = dialog.findViewById<ColorIconView>(R.id.wheelColorPreview)

            colorWheelView!!.listener = {
                _, newColor -> run {
                    color = newColor
                    colorPreview!!.color = color
                }
            }

            colorWheelView.color = color
            colorPreview!!.color = color

            return dialog
        }
    }

    class TableDialogFragment: DialogFragment() {

        private var color: Int = 0xffff0000.toInt()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            color = savedInstanceState?.getInt("color") ?: (activity as MainActivity).color
        }

        override fun onSaveInstanceState(outState: Bundle) {
            outState.putInt("color", color)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(context!!)
            builder.setView(R.layout.color_table_fragment)

            builder.setPositiveButton("Ok") { _, _ -> (activity as MainActivity).color = color }

            val dialog = builder.show()

            val colorTableView = dialog.findViewById<ColorTableView>(R.id.tableView)

            colorTableView!!.listener = { color = it }

            colorTableView.setColor(color)

            return dialog
        }
    }

    override fun setColor(dialogFragment: ColorDialogFragment, color: Int) {
        this.color = color
    }

}
