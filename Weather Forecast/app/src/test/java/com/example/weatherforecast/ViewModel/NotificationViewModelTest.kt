package com.example.weatherforecast.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.lifecycleScope
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class NotificationViewModelTest {

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

    private val notificationList = listOf(notification1, notification2)
    private val locationList = listOf(location1, location2)

    private lateinit var viewModel: NotificationViewModel
    private lateinit var repository: FakeRepositoryImpl

    private val date = "28 Mar, 2024"
    private val time = "05:05 PM"
    private lateinit var notification: NotificationData

    @Before
    fun CreateRepository(){
        repository = FakeRepositoryImpl(notificationList.toMutableList(), locationList.toMutableList())
        viewModel = NotificationViewModel(repository)

        notification = NotificationData(
            date,
            time,
            10.5,
            15.6,
            "notification"
        )
    }

    @Test
    fun getAllNotificationsTest_RequestSameNotificationList() = runBlockingTest {

        //When
        viewModel.getAllNotifications()
        var result: List<NotificationData> = emptyList()

        val job = launch {
            viewModel.notificationList.collectLatest { response ->
                when(response){
                    is DaoNotificationResponse.Loading -> { }
                    is DaoNotificationResponse.Success ->{
                        result = response.data
                    }
                    is DaoNotificationResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(notificationList))
    }

    @Test
    fun insertNotificationTest_RequestNewNotificationList() = runBlockingTest {
        //When
        viewModel.insertNotification(notification)
        val newNotificationList = notificationList.toMutableList()
        newNotificationList.add(notification)

        //Then
        viewModel.getAllNotifications()
        var result: List<NotificationData> = emptyList()

        val job = launch {
            viewModel.notificationList.collectLatest { response ->
                when(response){
                    is DaoNotificationResponse.Loading -> { }
                    is DaoNotificationResponse.Success ->{
                        result = response.data
                    }
                    is DaoNotificationResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newNotificationList))
    }

    @Test
    fun deleteNotificationTest_RequestNewNotificationList() = runBlockingTest {
        //When
        viewModel.deleteNotification(notification2)
        val newNotificationList = notificationList.toMutableList()
        newNotificationList.remove(notification2)

        //Then
        viewModel.getAllNotifications()
        var result: List<NotificationData> = emptyList()

        val job = launch {
            viewModel.notificationList.collectLatest { response ->
                when(response){
                    is DaoNotificationResponse.Loading -> { }
                    is DaoNotificationResponse.Success ->{
                        result = response.data
                    }
                    is DaoNotificationResponse.Failure ->{  }
                }
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(newNotificationList))
    }

}