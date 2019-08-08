package com.example.android.trackmysleepquality.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.databinding.NightItemBinding

class TrackerAdapter : ListAdapter<Night, ViewHolder>(DiffNightCallback()) {

    var onItemClick : View.OnClickListener? = null

    init {
        setHasStableIds(true)
    }
    override fun getItemId(position: Int) = getItem(position).id!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    public override fun getItem(position: Int) : Night = super.getItem(position)

}

class ViewHolder(private val binding: NightItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Night, onClickListener: View.OnClickListener?) = binding.apply {
        night = item
        clicklistener = onClickListener
        itemView.tag = item
        executePendingBindings()
    }

}

class DiffNightCallback: DiffUtil.ItemCallback<Night>() {

    override fun areItemsTheSame   (oldItem: Night, newItem: Night) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Night, newItem: Night) = oldItem == newItem

}
