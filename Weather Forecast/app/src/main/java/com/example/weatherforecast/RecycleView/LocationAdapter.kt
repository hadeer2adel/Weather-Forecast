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
import com.example.weatherforecast.Helpers.getCountryFlagUrl
import com.example.weatherforecast.Helpers.getWeatherIconUrl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.databinding.CardLocationBinding
import com.example.weatherforecast.databinding.CardTodayWeatherBinding
import com.example.weatherforecast.databinding.CardWeekWeatherBinding

class LocationAdapter (
    private val context: Context, private val onClick:(location: WeatherData)->Unit
        ):ListAdapter<WeatherData, LocationAdapter.LocationViewHolder>(LocationDiffUtil()){

    lateinit var binding: CardLocationBinding
    class LocationViewHolder (var binding: CardLocationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val locationData = getItem(position)
        holder.binding.apply {
            val imgUrl = getCountryFlagUrl(locationData.countryCode)
            Glide.with(context).load(imgUrl).into(flagImg)
            location.text = locationData.cityName
            deleteBtn.setOnClickListener { onClick(locationData) }
        }
    }
}

class LocationDiffUtil : DiffUtil.ItemCallback<WeatherData>(){
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem == newItem
    }
}