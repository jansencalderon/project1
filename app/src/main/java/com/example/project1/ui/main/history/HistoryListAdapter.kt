package com.example.project1.ui.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.databinding.ItemParkingHistoryBinding
import com.example.project1.enums.DateTimeFormat
import com.example.project1.model.ParkingHistory
import com.example.project1.utils.toReadableString
import kotlinx.android.synthetic.main.item_parking_history.view.*
import kotlinx.android.synthetic.main.item_parking_lot.view.*

class HistoryListAdapter : RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {
    private lateinit var historyList: List<ParkingHistory>
    // val clickSubject = PublishSubject.create<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemParkingHistoryBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_parking_history, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ParkingHistory = historyList[position]
        holder.bind(item)
        /*Glide.with(holder.itemView.context)
            .load("${item.thumbnail.path}.${item.thumbnail.extension}")
            .centerCrop()
            .into(holder.binding.image)*/

    }

    override fun getItemCount(): Int {
        return if (::historyList.isInitialized) historyList.size else 0
    }

    fun updateList(list: List<ParkingHistory>) {
        this.historyList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemParkingHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ParkingHistory) {
            itemView.itemName.text = item.lotName
            itemView.itemDate.text = item.date.toReadableString(DateTimeFormat.DATE_LONG)
            itemView.itemLevel.text = item.level
            itemView.itemCharge.text = "PHP ${item.charged}.00"
            itemView.itemDuration.text = item.duration
        }
    }


}