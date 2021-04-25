package com.example.filterphoto.kotlin

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.logging.Filter

class ListFilterAdapter(var listFi: List<FilterData>): RecyclerView.Adapter<ListFilterAdapter.FilterViewHolder>() {
    lateinit var listFilter: List<FilterData>
    var currentPosition=0


    class FilterViewHolder {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFilterAdapter.FilterViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ListFilterAdapter.FilterViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}