package com.example.weatherforecast.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.databinding.CardAlertBinding

class AlertAdapter (
    private val context: Context,
    private val onClick: (alert: AlertData)->Unit,
        ):ListAdapter<AlertData, AlertAdapter.AlertViewHolder>(AlertDiffUtil()){

    lateinit var binding: CardAlertBinding
    class AlertViewHolder (var binding: CardAlertBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardAlertBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alertData = getItem(position)
        holder.binding.apply {
            date.text = alertData.date
            time.text = alertData.time
            deleteBtn.setOnClickListener { onClick(alertData) }
        }
    }
}

class AlertDiffUtil : DiffUtil.ItemCallback<AlertData>(){
    override fun areItemsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
        return (oldItem.date == newItem.date && oldItem.time == newItem.time)
    }

    override fun areContentsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
        return oldItem == newItem
    }
}