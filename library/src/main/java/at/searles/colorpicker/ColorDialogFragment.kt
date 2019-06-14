package at.searles.colorpicker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ColorDialogFragment: DialogFragment() {
//    @SuppressLint("InflateParams")
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(context)
//        val inflater = LayoutInflater.from(context)
//
//        val view = inflater.inflate(R.layout.color_view, null)
//
//        // set up tabs. I use the childFragmentManager to easily access the parent.
//        val mAdapter = ColorPickerPageAdapter(this)
//
//        val pager = view.findViewById<ViewPager>(R.id.pager)
//        pager.adapter = mAdapter
//
//        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
//        tabLayout.setupWithViewPager(pager)
//
//        return view
//
//        builder.setView(dialogView)
//        builder.show()
//
//        builder.setPositiveButton("Ok") { _, _ ->
//            dismiss() // ignore
//        }
//
//        return builder.create()
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val colorViewFragment = view.findViewById<CombinedColorView>(R.id.colorView)
//
//
//    }
}