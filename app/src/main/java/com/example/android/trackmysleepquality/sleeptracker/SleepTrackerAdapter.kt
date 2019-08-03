package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.SleepNightItemBinding
import com.example.android.trackmysleepquality.describeIt
import com.example.android.trackmysleepquality.logIt
import com.example.android.trackmysleepquality.presentIt

class SleepTrackerAdapter : ListAdapter<SleepNight, SleepNightViewHolder>(SleepNightItemCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: SleepNightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightViewHolder {
        return SleepNightViewHolder(SleepNightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemId(position: Int) = getItem(position).nightId

}

class SleepNightViewHolder(private val binding: SleepNightItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(night: SleepNight) = binding.apply {
        howStr = night.describeIt(); whenStr= night.logIt(); iconImg = night.presentIt()
        itemView.tag = night
        executePendingBindings()
    }

}

class SleepNightItemCallback: DiffUtil.ItemCallback<SleepNight>() {

    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight)    = oldItem.nightId == newItem.nightId
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight) = oldItem == newItem

}
