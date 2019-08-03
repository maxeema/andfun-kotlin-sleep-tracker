package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.database.date
import com.example.android.trackmysleepquality.database.how
import com.example.android.trackmysleepquality.database.icon
import com.example.android.trackmysleepquality.databinding.SleepItemViewBinding

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
        return TextItemViewHolder(SleepItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemId(position: Int) = data[position].nightId

}

class TextItemViewHolder(private val binding: SleepItemViewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SleepNight) = binding.apply {
        how.text = item.how
        date.text= item.date
        icon.setImageDrawable(item.icon)
        itemView.tag = item
        executePendingBindings()
    }

}
