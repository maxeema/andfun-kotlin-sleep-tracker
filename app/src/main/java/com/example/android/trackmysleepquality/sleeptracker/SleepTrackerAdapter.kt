package com.example.android.trackmysleepquality.sleeptracker

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

class SleepTrackerAdapter : RecyclerView.Adapter<TextItemViewHolder>() {

    private val data = mutableListOf<SleepNight>()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.textView.text = data[position].sleepQuality.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)
