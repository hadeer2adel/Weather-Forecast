package com.example.weatherforecast.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Helpers.getWeatherIcon
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.databinding.CardWeekWeatherBinding

class DayAdapter (
    private val context: Context,
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
        holder.binding.apply {
            val imgUrl = getWeatherIcon(dayWeather.weatherIcon)
            image.setImageResource(imgUrl)
            day.text = dayWeather.date
            val unit = AppSettings.getInstance(context).temperatureUnit
            temperature.text = dayWeather.minTemperature.toString() + " / " + dayWeather.maxTemperature.toString() + "  º$unit"
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