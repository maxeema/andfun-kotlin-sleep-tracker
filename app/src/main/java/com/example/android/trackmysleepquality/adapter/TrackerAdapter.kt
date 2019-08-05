package com.example.android.trackmysleepquality.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.databinding.NightItemBinding

class TrackerAdapter(private val onClickListener: View.OnClickListener) : ListAdapter<Night, ViewHolder>(DiffNightCallback()) {

    init {
        setHasStableIds(true)
    }
    override fun getItemId(position: Int) = getItem(position).nightId

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClickListener)
    }

    public override fun getItem(position: Int) = super.getItem(position)

}

class ViewHolder(private val binding: NightItemBinding, private val onClickListener: View.OnClickListener)
        : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Night) = binding.apply {
        night = item
        clicklistener = onClickListener
        itemView.tag = item
        executePendingBindings()
    }

}

class DiffNightCallback: DiffUtil.ItemCallback<Night>() {

    override fun areItemsTheSame   (oldItem: Night, newItem: Night) = oldItem.nightId == newItem.nightId
    override fun areContentsTheSame(oldItem: Night, newItem: Night) = oldItem == newItem

}
