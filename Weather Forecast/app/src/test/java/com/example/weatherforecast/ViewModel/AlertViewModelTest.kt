package com.example.weatherforecast.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.LocalDataSource.DaoAlertResponse
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
class AlertViewModelTest {

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
        0,
        10.5,
        15.6,
        "Cairo",
        "EG"
    )
    private val location2 = LocationData(
        0,
        202.0,
        185.7,
        "Roma",
        "IT"
    )

    private val alertList = listOf(alert1, alert2)
    private val locationList = listOf(location1, location2)

    private lateinit var viewModel: AlertViewModel
    private lateinit var repository: FakeRepositoryImpl

    private val date = "28 Mar, 2024"
    private val time = "05:05 PM"
    private lateinit var alert: AlertData

    @Before
    fun CreateRepository(){
        repository = FakeRepositoryImpl(alertList.toMutableList(), locationList.toMutableList())
        viewModel = AlertViewModel(repository)

        alert = AlertData(
            date,
            time,
            10.5,
            15.6,
            "notification"
        )
    }

    @Test
    fun getAllAlertsTest_RequestSameAlertList() = runBlockingTest {

        //When
        viewModel.getAllAlerts()
        var result: List<AlertData> = emptyList()

        val job = launch {
            viewModel.alertList.collectLatest { response ->
                when(response){
                    is DaoAlertResponse.Loading -> { }
                    is DaoAlertResponse.Success ->{
                        result = response.data
                    }
                    is DaoAlertResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(alertList))
    }

    @Test
    fun insertAlertTest_RequestNewAlertList() = runBlockingTest {
        //When
        viewModel.insertAlert(alert)
        val newAlertList = alertList.toMutableList()
        newAlertList.add(alert)

        //Then
        viewModel.getAllAlerts()
        var result: List<AlertData> = emptyList()

        val job = launch {
            viewModel.alertList.collectLatest { response ->
                when(response){
                    is DaoAlertResponse.Loading -> { }
                    is DaoAlertResponse.Success ->{
                        result = response.data
                    }
                    is DaoAlertResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newAlertList))
    }

    @Test
    fun deleteAlertTest_RequestNewAlertList() = runBlockingTest {
        //When
        viewModel.deleteAlert(alert2)
        val newAlertList = alertList.toMutableList()
        newAlertList.remove(alert2)

        //Then
        viewModel.getAllAlerts()
        var result: List<AlertData> = emptyList()

        val job = launch {
            viewModel.alertList.collectLatest { response ->
                when(response){
                    is DaoAlertResponse.Loading -> { }
                    is DaoAlertResponse.Success ->{
                        result = response.data
                    }
                    is DaoAlertResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newAlertList))
    }

}