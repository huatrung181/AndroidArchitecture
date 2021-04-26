package com.example.filterphoto.java

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.filterphoto.R
import com.example.filterphoto.kotlin.FilterData

class ListFilterAdapter(var listFilter: List<FilterData>) :
    RecyclerView.Adapter<ListFilterAdapter.FilterViewHolder>() {
    var currentPosition = 0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FilterViewHolder,
        position: Int
    ) {
        holder.setData(listFilter[position], position)
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    inner class FilterViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        @BindView(R.id.ll_filter)
        lateinit var llFilter: View

        @BindView(R.id.iv_filter_image)
        lateinit var ivFilterImage: ImageView

        @BindView(R.id.tv_filter_name)
        lateinit  var tvFilterName: TextView

        fun setData(filterData: FilterData?, position: Int) {
            //  ivFilterImage.setImageResource(filterData.getImageId());
            // tvFilterName.setText(filterData.getName());
            itemView.setOnClickListener {
                if (onFilterSelect != null) {
                    val oldFocusPosition = currentPosition
                    currentPosition = position
                    notifyItemChanged(oldFocusPosition)
                    notifyItemChanged(position)
                    onFilterSelect!!.onSelect(filterData)
                }
            }
            if (position == currentPosition) {
                llFilter!!.setBackgroundResource(R.drawable.bg_item_filter_selected)
            } else {
                llFilter!!.setBackgroundResource(R.drawable.bg_item_filter_unselected)
            }
        }

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    interface OnFilterSelect {
        fun onSelect(filterData: FilterData?)
    }

    var onFilterSelect: OnFilterSelect? = null

}