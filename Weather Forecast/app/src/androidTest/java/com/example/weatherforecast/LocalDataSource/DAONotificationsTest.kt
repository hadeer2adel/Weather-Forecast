package com.example.weatherforecast.LocalDataSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast.Model.NotificationData
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DAONotificationsTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: DataBase
    lateinit var dao: DAONotifications

    val date = "28 Mar, 2024"
    val time = "05:05 PM"
    lateinit var notification: NotificationData

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DataBase::class.java
        ).build()
        dao = database.getDAONotifications()

        notification = NotificationData(
            date,
            time,
            10.5,
            15.6,
            "notification"
        )
    }

    @After
    fun TearDown(){
        database.close()
    }


    @Test
    fun insertNotificationTest_TakeNotificationData_RequestSameNotification() = runBlockingTest {
        //When
        dao.insertNotification(notification)

        //Then
        val result = dao.getNotificationById(date, time)

        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteNotificationTest_TakeNotificationData_RequestNull() = runBlockingTest {
        //Given
        dao.insertNotification(notification)

        //When
        dao.deleteNotification(notification)

        //Then
        val result = dao.getNotificationById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun deleteNotificationByIdTest_TakeDataAndTime_RequestNull() = runBlockingTest {
        //Given
        dao.insertNotification(notification)

        //When
        dao.deleteNotificationById(date, time)

        //Then
        val result = dao.getNotificationById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun getAllNotificationsTest_RequestSameNotificationList() = runBlockingTest {
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
        val notificationList = listOf(notification1, notification2)
        dao.insertNotification(notification1)
        dao.insertNotification(notification2)


        //When
        val resultFlow = dao.getAllNotifications()
        var result: List<NotificationData> = emptyList()
        val job = launch {
            resultFlow.collect {
                result = it
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(notificationList))
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
        dao.insertNotification(notification1)
        dao.insertNotification(notification2)

        //When
        dao.deleteAllNotifications()

        //Then
        val result1 = dao.getNotificationById("1 Mar, 2024", "01:00")
        assertThat(result1 , nullValue())
        val result2 = dao.getNotificationById("2 Mar, 2024", "02:00")
        assertThat(result2 , nullValue())
    }


}