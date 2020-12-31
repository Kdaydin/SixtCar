package com.kdaydin.sixtcars.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.databinding.ItemSixtCarBinding

class CarListAdapter(val cars: List<SixtCar>) :
    RecyclerView.Adapter<CarListAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemSixtCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[holder.adapterPosition])
    }

    override fun getItemCount(): Int = cars.size

    inner class CarViewHolder(val binding: ItemSixtCarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SixtCar) {
            with(binding) {
                this.sixtCar = item
                executePendingBindings()
            }
        }
    }
}