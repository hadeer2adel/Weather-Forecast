package com.example.weatherforecast.LocalDataSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Services.Caching.DAOAlerts
import com.example.weatherforecast.Services.Caching.DataBase
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
class DAOAlertsTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: DataBase
    lateinit var dao: DAOAlerts

    val date = "28 Mar, 2024"
    val time = "05:05 PM"
    lateinit var alert: AlertData

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DataBase::class.java
        ).build()
        dao = database.getDAOAlerts()

        alert = AlertData(
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
    fun insertAlertTest_TakeAlertData_RequestSameAlert() = runBlockingTest {
        //When
        dao.insertAlert(alert)

        //Then
        val result = dao.getAlertById(date, time)

        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteAlertTest_TakeAlertData_RequestNull() = runBlockingTest {
        //Given
        dao.insertAlert(alert)

        //When
        dao.deleteAlert(alert)

        //Then
        val result = dao.getAlertById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun deleteAlertByIdTest_TakeDataAndTime_RequestNull() = runBlockingTest {
        //Given
        dao.insertAlert(alert)

        //When
        dao.deleteAlertById(date, time)

        //Then
        val result = dao.getAlertById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun getAllAlertsTest_RequestSameAlertList() = runBlockingTest {
        //Given
        val alert1 = AlertData(
            "1 Mar, 2024",
            "01:00",
            10.5,
            15.6,
            "notification"
        )
        val alert2 = AlertData(
            "2 Mar, 2024",
            "02:00",
            10.5,
            15.6,
            "alarm"
        )
        val alertList = listOf(alert1, alert2)
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)


        //When
        val resultFlow = dao.getAllAlerts()
        var result: List<AlertData> = emptyList()
        val job = launch {
            resultFlow.collect {
                result = it
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()

        //Then
        assertThat(result, IsEqual(alertList))
    }

    @Test
    fun deleteAllAlertsTest_RequestNull() = runBlockingTest {
        //Given
        val alert1 = AlertData(
            "1 Mar, 2024",
            "01:00",
            10.5,
            15.6,
            "notification"
        )
        val alert2 = AlertData(
            "2 Mar, 2024",
            "02:00",
            10.5,
            15.6,
            "alarm"
        )
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)

        //When
        dao.deleteAllAlerts()

        //Then
        val result1 = dao.getAlertById("1 Mar, 2024", "01:00")
        assertThat(result1 , nullValue())
        val result2 = dao.getAlertById("2 Mar, 2024", "02:00")
        assertThat(result2 , nullValue())
    }


}