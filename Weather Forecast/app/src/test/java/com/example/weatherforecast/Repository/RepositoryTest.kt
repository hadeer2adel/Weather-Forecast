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
        //Given
        repository.insertNotification(notification)

        //When
        val result = repository.getNotificationById(date, time)

        //Given
        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteNotificationTest_TakeNotificationData_RequestNull() = runBlockingTest {
        //Given
        repository.insertNotification(notification)
        repository.deleteNotification(notification)

        //When
        val result = repository.getNotificationById(date, time)

        //Given
        assertThat(result , nullValue())
    }

    @Test
    fun deleteNotificationByIdTest_TakeNotificationData_RequestNull() = runBlockingTest {
        //Given
        repository.insertNotification(notification)
        repository.deleteNotificationById(date, time)

        //When
        val result = repository.getNotificationById(date, time)

        //Given
        assertThat(result , nullValue())
    }

    @Test
    fun deleteAllNotificationsTest_TakeNotificationData_RequestSameNotification() = runBlockingTest {
        //Given
        repository.insertNotification(notification)
        repository.deleteAllNotifications()

        //When
        val result = repository.getNotificationById(date, time)

        //Given
        assertThat(result , nullValue())
    }

    @Test
    fun getAllNotificationsTest_TakeNotificationData_RequestSameNotification() = runBlockingTest {
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

        assertThat(result, IsEqual(localList))
    }
}