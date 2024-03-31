package com.example.weatherforecast.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.example.weatherforecast.Helpers.getCity
import com.example.weatherforecast.Helpers.getCountryFlagUrl
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.databinding.CardLocationBinding

class LocationAdapter (
    private val context: Context,
    private val visibility: Int,
    private val onClick: (location: LocationData)->Unit,
    private val onCardClick: (location: LocationData)->Unit
        ):ListAdapter<LocationData, LocationAdapter.LocationViewHolder>(LocationDiffUtil()){

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
            val city = getCity(context, locationData.latitude, locationData.longitude)
            val imgUrl = getCountryFlagUrl(city.countryCode)
            Glide.with(context).load(imgUrl).transform(Rotate(90)).into(flagImg)
                location.text = city.cityName
            deleteBtn.visibility = visibility
            deleteBtn.setOnClickListener { onClick(locationData) }
            card.setOnClickListener { onCardClick(locationData) }
        }
    }
}

class LocationDiffUtil : DiffUtil.ItemCallback<LocationData>(){
    override fun areItemsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
        return oldItem == newItem
    }
}