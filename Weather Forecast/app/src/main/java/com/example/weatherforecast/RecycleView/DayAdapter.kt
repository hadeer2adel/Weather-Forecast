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
import com.example.weatherforecast.Helpers.getFromSharedPreferences
import com.example.weatherforecast.Helpers.getWeatherIconUrl
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.databinding.CardTodayWeatherBinding
import com.example.weatherforecast.databinding.CardWeekWeatherBinding

class DayAdapter (
    private val context: Context?,
        ):ListAdapter<DailyWeatherData, DayAdapter.DayViewHolder>(DayDiffUtil()){

    lateinit var binding: CardWeekWeatherBinding
    class DayViewHolder (var binding: CardWeekWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardWeekWeatherBinding.inflate(inflater, parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayWeather = getItem(position)
        if (context != null) {
            val imgUrl = getWeatherIconUrl(dayWeather.weatherIcon)
            Glide.with(context).load(imgUrl).into(holder.binding.image)
        }
        holder.binding.apply {
            day.text = dayWeather.date
            temperature.text = dayWeather.minTemperature.toString() + " / " + dayWeather.maxTemperature.toString()
            val unit = context?.let { getFromSharedPreferences(it, "temperatureUnit", "K") }
            temperatureUnit.text = "º$unit"
        }
    }
}

class DayDiffUtil : DiffUtil.ItemCallback<DailyWeatherData>(){
    override fun areItemsTheSame(oldItem: DailyWeatherData, newItem: DailyWeatherData): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: DailyWeatherData, newItem: DailyWeatherData): Boolean {
        return oldItem == newItem
    }
}