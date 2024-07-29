package com.example.weatherforecast.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.Services.ResponseState
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Repository.FakeRepositoryImpl
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class LocationViewModelTest {

    @get:Rule
    val myRule = InstantTaskExecutorRule()

    private val alert1 = AlertData(
        "1 Mar, 2024",
        "01:00",
        10.5,
        15.6,
        "notification"
    )
    private val alert2 = AlertData(
        "2 Mar, 2024",
        "02:00",
        10.5,
        15.6,
        "alarm"
    )
    private val location1 = LocationData(
        1,
        10.5,
        15.6,
    )
    private val location2 = LocationData(
        2,
        202.0,
        185.7,
    )

    private val alertList = listOf(alert1, alert2)
    private val locationList = listOf(location1, location2)

    private lateinit var viewModel: LocationViewModel
    private lateinit var repository: FakeRepositoryImpl

    private val latitude = 123.4
    private val longitude = 567.8
    private lateinit var location: LocationData

    @Before
    fun CreateRepository(){
        repository = FakeRepositoryImpl(alertList.toMutableList(), locationList.toMutableList())
        viewModel = LocationViewModel(repository)

        location = LocationData(
            0,
            latitude,
            longitude,
        )
    }

    @Test
    fun getAllLocationsTest_RequestSameLocationList() = runBlockingTest {

        //When
        viewModel.getAllLocations()
        var result: List<LocationData> = emptyList()

        val job = launch {
            viewModel.locationList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { }
                    is ResponseState.Success ->{
                        result = response.data
                    }
                    is ResponseState.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(locationList))
    }

    @Test
    fun insertLocationTest_RequestNewLocationList() = runBlockingTest {
        //When
        viewModel.insertLocation(location)
        val newLocationList = locationList.toMutableList()
        newLocationList.add(location)

        //Then
        viewModel.getAllLocations()
        var result: List<LocationData> = emptyList()

        val job = launch {
            viewModel.locationList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { }
                    is ResponseState.Success ->{
                        result = response.data
                    }
                    is ResponseState.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newLocationList))
    }

    @Test
    fun deleteLocationTest_RequestNewLocationList() = runBlockingTest {
        //When
        viewModel.insertLocation(location2)
        val newLocationList = locationList.toMutableList()
        newLocationList.add(location2)

        //Then
        viewModel.getAllLocations()
        var result: List<LocationData> = emptyList()

        val job = launch {
            viewModel.locationList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { }
                    is ResponseState.Success ->{
                        result = response.data
                    }
                    is ResponseState.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newLocationList))
    }

}