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
        //Given
        dao.insertNotification(notification)

        //When
        val result = dao.getNotificationById(date, time)

        //Given
        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteNotificationTest_TakeNotificationData_RequestNull() = runBlockingTest {
        //Given
        dao.insertNotification(notification)
        dao.deleteNotification(notification)

        //When
        val result = dao.getNotificationById(date, time)

        //Given
        assertThat(result , nullValue())
    }

    @Test
    fun deleteNotificationByIdTest_TakeNotificationData_RequestNull() = runBlockingTest {
        //Given
        dao.insertNotification(notification)
        dao.deleteNotificationById(date, time)

        //When
        val result = dao.getNotificationById(date, time)

        //Given
        assertThat(result , nullValue())
    }

    @Test
    fun deleteAllNotificationsTest_TakeNotificationData_RequestSameNotification() = runBlockingTest {
        //Given
        dao.insertNotification(notification)
        dao.deleteAllNotifications()

        //When
        val result = dao.getNotificationById(date, time)

        //Given
        assertThat(result , nullValue())
    }

    @Test
    fun getAllNotificationsTest_TakeNotificationData_RequestSameNotification() = runBlockingTest {
        //Given
        dao.getAllNotifications()
        dao.insertNotification(notification)

        //When
        val resultFlow = dao.getAllNotifications()
        var result: NotificationData? = null
        val job = launch {
            resultFlow.collect {
                result = it.get(0)
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Given
        assertThat(result, not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))

    }


}