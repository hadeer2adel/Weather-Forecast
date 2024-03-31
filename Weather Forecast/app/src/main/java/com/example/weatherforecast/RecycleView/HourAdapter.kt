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
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.databinding.CardTodayWeatherBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HourAdapter (
    private val context: Context,
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
        holder.binding.apply {

            val sdf = SimpleDateFormat("h a", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(hourWeather.time)
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val isMorning = (currentHour in 6..17)
            val imgUrl = getWeatherIcon(hourWeather.weatherIcon, isMorning)
            image.setImageResource(imgUrl)

            time.text = hourWeather.time
            val unit = AppSettings.getInstance(context).temperatureUnit
            temperature.text = hourWeather.temperature.toString() + " º$unit"
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