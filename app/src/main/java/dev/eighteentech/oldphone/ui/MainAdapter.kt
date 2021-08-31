package dev.eighteentech.oldphone.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.eighteentech.oldphone.databinding.ItemBinding

class MainAdapter:RecyclerView.Adapter<MainAdapter.VH>(){

    private var list = listOf<String>()

    inner class VH(val binding: ItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = with(holder.binding){
        word.text = list[position]
    }

    override fun getItemCount() = list.size

    fun updateList(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }
}