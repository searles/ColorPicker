package at.searles.colorpicker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ColorTableFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.color_table_fragment, container, false)

        val mAdapter = ColorTableAdapter(context!!)

        // Connect the adapter with the RecyclerView.
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.colorTable)
        mRecyclerView.adapter = mAdapter

        // Give the RecyclerView a default layout manager.
        mRecyclerView.layoutManager = LinearLayoutManager(context!!)

        return view
    }
}