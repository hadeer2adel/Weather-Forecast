package com.example.weatherforecast.LocalDataSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Services.Caching.DataBase
import com.example.weatherforecast.Services.Caching.LocalDataSource
import com.example.weatherforecast.Services.Caching.LocalDataSourceImpl
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
@MediumTest
class LocalDataSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: DataBase
    lateinit var localDataSource: LocalDataSource

    val date = "28 Mar, 2024"
    val time = "05:05 PM"
    lateinit var alert: AlertData

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DataBase::class.java
        ).allowMainThreadQueries()
            .build()

        localDataSource = LocalDataSourceImpl(database.getDAOLastWeather(), database.getDAOLocations(), database.getDAOAlerts())

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
        localDataSource.insertAlert(alert)

        //Then
        val result = localDataSource.getAlertById(date, time)

        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteAlertTest_TakeAlertData_RequestNull() = runBlockingTest {
        //Given
        localDataSource.insertAlert(alert)

        //When
        localDataSource.deleteAlert(alert)

        //Then
        val result = localDataSource.getAlertById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun deleteAlertByIdTest_TakeDataAndTime_RequestNull() = runBlockingTest {
        //Given
        localDataSource.insertAlert(alert)

        //When
        localDataSource.deleteAlertById(date, time)

        //Then
        val result = localDataSource.getAlertById(date, time)
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
        localDataSource.insertAlert(alert1)
        localDataSource.insertAlert(alert2)


        //When
        val resultFlow = localDataSource.getAllAlerts()
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
        localDataSource.insertAlert(alert1)
        localDataSource.insertAlert(alert2)

        //When
        localDataSource.deleteAllAlerts()

        //Then
        val result1 = localDataSource.getAlertById("1 Mar, 2024", "01:00")
        assertThat(result1 , nullValue())
        val result2 = localDataSource.getAlertById("2 Mar, 2024", "02:00")
        assertThat(result2 , nullValue())
    }

}