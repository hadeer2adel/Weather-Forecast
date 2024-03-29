package com.example.weatherforecast.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.lifecycleScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.LocalDataSource.DaoLocationResponse
import com.example.weatherforecast.LocalDataSource.DaoNotificationResponse
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Repository.FakeRepositoryImpl
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class LocationViewModelTest {

    @get:Rule
    val myRule = InstantTaskExecutorRule()

    private val notification1 = NotificationData(
        "1 Mar, 2024",
        "01:00",
        10.5,
        15.6,
        "notification"
    )
    private val notification2 = NotificationData(
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
        "Cairo",
        "EG"
    )
    private val location2 = LocationData(
        2,
        202.0,
        185.7,
        "Roma",
        "IT"
    )

    private val notificationList = listOf(notification1, notification2)
    private val locationList = listOf(location1, location2)

    private lateinit var viewModel: LocationViewModel
    private lateinit var repository: FakeRepositoryImpl

    private val latitude = 123.4
    private val longitude = 567.8
    private lateinit var location: LocationData

    @Before
    fun CreateRepository(){
        repository = FakeRepositoryImpl(notificationList.toMutableList(), locationList.toMutableList())
        viewModel = LocationViewModel(repository)

        location = LocationData(
            0,
            latitude,
            longitude,
            "Tokyo",
            "JP"
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
                    is DaoLocationResponse.Loading -> { }
                    is DaoLocationResponse.Success ->{
                        result = response.data
                    }
                    is DaoLocationResponse.Failure ->{  }
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
                    is DaoLocationResponse.Loading -> { }
                    is DaoLocationResponse.Success ->{
                        result = response.data
                    }
                    is DaoLocationResponse.Failure ->{  }
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
                    is DaoLocationResponse.Loading -> { }
                    is DaoLocationResponse.Success ->{
                        result = response.data
                    }
                    is DaoLocationResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newLocationList))
    }

}