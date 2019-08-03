package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.TextItemViewBinding

class SleepTrackerAdapter : RecyclerView.Adapter<TextItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        return TextItemViewHolder(TextItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemId(position: Int) = data[position].nightId

}

class TextItemViewHolder(private val binding: TextItemViewBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SleepNight) {
        binding.apply {
            night = item
            itemView.tag = item
            executePendingBindings()
        }
    }
}
