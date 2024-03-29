package com.example.weatherforecast.Repository

import com.example.weatherforecast.LocalDataSource.FakeLocalDataSourceImpl
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.RemoteDataSource.FakeRemoteDataSourceImpl
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

class RepositoryTest {
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
    private val notification3 = NotificationData(
        "3 Mar, 2024",
        "03:00",
        10.5,
        15.6,
        "notification"
    )
    private val notification4 = NotificationData(
        "4 Mar, 2024",
        "04:00",
        10.5,
        15.6,
        "alarm"
    )

    private val localList = listOf(notification1, notification2, notification3, notification4)

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: Repository

    val date = "28 Mar, 2024"
    val time = "05:05 PM"
    lateinit var notification: NotificationData

    @Before
    fun CreateRepository(){
        remoteDataSource = FakeRemoteDataSourceImpl()
        localDataSource = FakeLocalDataSourceImpl(localList.toMutableList())
        repository = RepositoryImpl(remoteDataSource, localDataSource)

        notification = NotificationData(
            date,
            time,
            10.5,
            15.6,
            "notification"
        )
    }

    @Test
    fun insertNotificationTest_TakeNotificationData_RequestSameNotification() = runBlockingTest {
        //When
        repository.insertNotification(notification)

        //Then
        val result = repository.getNotificationById(date, time)

        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteNotificationTest_TakeNotificationData_RequestNull() = runBlockingTest {
        //Given
        repository.insertNotification(notification)

        //When
        repository.deleteNotification(notification)

        //Then
        val result = repository.getNotificationById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun deleteNotificationByIdTest_TakeDataAndTime_RequestNull() = runBlockingTest {
        //Given
        repository.insertNotification(notification)

        //When
        repository.deleteNotificationById(date, time)

        //Then
        val result = repository.getNotificationById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun getAllNotificationsTest_RequestSameNotificationList() = runBlockingTest {

        //When
        val resultFlow = repository.getAllNotifications()
        var result: List<NotificationData> = emptyList()
        val job = launch {
            resultFlow.collect {
                result = it
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(localList))
    }

    @Test
    fun deleteAllNotificationsTest_RequestNull() = runBlockingTest {
        //Given
        val notification1 = NotificationData(
            "1 Mar, 2024",
            "01:00",
            10.5,
            15.6,
            "notification"
        )
        val notification2 = NotificationData(
            "2 Mar, 2024",
            "02:00",
            10.5,
            15.6,
            "alarm"
        )
        repository.insertNotification(notification1)
        repository.insertNotification(notification2)

        //When
        repository.deleteAllNotifications()

        //Then
        val result1 = repository.getNotificationById("1 Mar, 2024", "01:00")
        assertThat(result1 , nullValue())
        val result2 = repository.getNotificationById("2 Mar, 2024", "02:00")
        assertThat(result2 , nullValue())
    }
}