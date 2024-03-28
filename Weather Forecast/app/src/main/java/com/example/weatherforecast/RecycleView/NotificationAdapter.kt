package com.example.weatherforecast.RecycleView

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
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
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.databinding.CardLocationBinding
import com.example.weatherforecast.databinding.CardNotificationBinding
import com.example.weatherforecast.databinding.CardTodayWeatherBinding
import com.example.weatherforecast.databinding.CardWeekWeatherBinding

class NotificationAdapter (
    private val context: Context,
    private val onClick: (notification: NotificationData)->Unit,
        ):ListAdapter<NotificationData, NotificationAdapter.NotificationViewHolder>(NotificationDiffUtil()){

    lateinit var binding: CardNotificationBinding
    class NotificationViewHolder (var binding: CardNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardNotificationBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notificationData = getItem(position)
        holder.binding.apply {
            date.text = notificationData.date
            time.text = notificationData.time
            deleteBtn.setOnClickListener { onClick(notificationData) }
        }
    }
}

class NotificationDiffUtil : DiffUtil.ItemCallback<NotificationData>(){
    override fun areItemsTheSame(oldItem: NotificationData, newItem: NotificationData): Boolean {
        return (oldItem.date == newItem.date && oldItem.time == newItem.time)
    }

    override fun areContentsTheSame(oldItem: NotificationData, newItem: NotificationData): Boolean {
        return oldItem == newItem
    }
}