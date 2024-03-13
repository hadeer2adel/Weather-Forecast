package com.example.weatherforecast.RecycleView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Helpers.getWeatherIconUrl
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.databinding.CardTodayWeatherBinding

class HourAdapter (
    private val context: Context?,
        ):ListAdapter<HourlyWeatherData, HourAdapter.HourViewHolder>(HourDiffUtil()){

    lateinit var binding: CardTodayWeatherBinding
    class HourViewHolder (var binding: CardTodayWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardTodayWeatherBinding.inflate(inflater, parent, false)
        return HourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hourWeather = getItem(position)
        if (context != null) {
            val imgUrl = getWeatherIconUrl(hourWeather.weatherIcon)
            Glide.with(context).load(imgUrl).into(holder.binding.image)
        }
        holder.binding.apply {
            time.text = hourWeather.time
            temperature.text = hourWeather.temperature.toString()
        }
    }
}

class HourDiffUtil : DiffUtil.ItemCallback<HourlyWeatherData>(){
    override fun areItemsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem == newItem
    }
}