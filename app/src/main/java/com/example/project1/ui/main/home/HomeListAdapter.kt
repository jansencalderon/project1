package com.example.project1.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.databinding.ItemParkingLotBinding
import com.example.project1.model.ParkingLot
import kotlinx.android.synthetic.main.item_parking_lot.view.*

class HomeListAdapter(private val onSelectItem: (ParkingLot) -> Unit) : RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {
    private val parkingLotList: MutableList<ParkingLot> = ArrayList()
    // val clickSubject = PublishSubject.create<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemParkingLotBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_parking_lot, parent, false)
        return ViewHolder(binding, onSelectItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ParkingLot = parkingLotList[position]
        holder.bind(item)
        /*Glide.with(holder.itemView.context)
            .load("${item.thumbnail.path}.${item.thumbnail.extension}")
            .centerCrop()
            .into(holder.binding.image)*/

    }

    override fun getItemCount(): Int {
        return parkingLotList.size
    }

    fun updateList(list: List<ParkingLot>) {
        this.parkingLotList.clear()
        this.parkingLotList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemParkingLotBinding, private val onSelectItem: (ParkingLot) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ParkingLot) {
            itemView.parkingLotName.text = item.name
            itemView.setOnClickListener {
                onSelectItem(item)
            }
        }
    }


}