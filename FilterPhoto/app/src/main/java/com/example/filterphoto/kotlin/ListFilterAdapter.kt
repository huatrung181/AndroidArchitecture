package com.example.filterphoto.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.filterphoto.R

class ListFilterAdapter(var listFilter: List<FilterData>): RecyclerView.Adapter<ListFilterAdapter.FilterViewHolder>() {
    var currentPosition=0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFilterAdapter.FilterViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_filter, parent, false)
        return FilterViewHolder(view)
    }


    override fun getItemCount(): Int {
     return  listFilter.size
    }

    override fun onBindViewHolder(holder: ListFilterAdapter.FilterViewHolder, position: Int) {
        holder.setData(listFilter.get(position),position)
    }

   inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.ll_filter)
       lateinit var llFilter: View

        @BindView(R.id.iv_filter_image)
       lateinit var ivFilterImage: ImageView

        @BindView(R.id.tv_filter_name)
      lateinit  var tvFilterName: TextView

        init {
            ButterKnife.bind(this,itemView)
        }

        fun  setData(filterData: FilterData, position: Int){
            ivFilterImage.setImageResource(filterData.imageId)
            tvFilterName.text= filterData.name
            itemView.setOnClickListener {
                if (onFilterSelect!=null){
                    var oldFocusPosition = currentPosition
                    currentPosition = position
                    notifyItemChanged(oldFocusPosition)
                    notifyItemChanged(position)
                    onFilterSelect?.onSelect(filterData)
                }
            }

            if (position==currentPosition){
                llFilter.setBackgroundResource(R.drawable.bg_item_filter_selected)

            }else{
                llFilter.setBackgroundResource(R.drawable.bg_item_filter_unselected)
            }


        }

    }

   public interface OnFilterSelect {
        fun onSelect(filterData: FilterData?)
    }

    var onFilterSelect: OnFilterSelect? = null



}