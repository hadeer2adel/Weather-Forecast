package com.example.weatherforecast.Repository

import com.example.weatherforecast.LocalDataSource.FakeLocalDataSourceImpl
import com.example.weatherforecast.Services.Caching.LocalDataSource
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Networking.FakeNetworkManagerImpl
import com.example.weatherforecast.Services.Networking.NetworkManager
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

class RepositoryTest {
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
    private val alert3 = AlertData(
        "3 Mar, 2024",
        "03:00",
        10.5,
        15.6,
        "notification"
    )
    private val alert4 = AlertData(
        "4 Mar, 2024",
        "04:00",
        10.5,
        15.6,
        "alarm"
    )

    private val localList = listOf(alert1, alert2, alert3, alert4)

    private lateinit var networkManager: NetworkManager
    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: Repository

    val date = "28 Mar, 2024"
    val time = "05:05 PM"
    lateinit var alert: AlertData

    @Before
    fun CreateRepository(){
        networkManager = FakeNetworkManagerImpl()
        localDataSource = FakeLocalDataSourceImpl(localList.toMutableList())
        repository = RepositoryImpl(networkManager, localDataSource)

        alert = AlertData(
            date,
            time,
            10.5,
            15.6,
            "notification"
        )
    }

    @Test
    fun insertAlertTest_TakeAlertData_RequestSameAlert() = runBlockingTest {
        //When
        repository.insertAlert(alert)

        //Then
        val result = repository.getAlertById(date, time)

        assertThat(result , not(nullValue()))
        assertThat(result?.latitude, `is`(10.5))
        assertThat(result?.longitude, `is`(15.6))
        assertThat(result?.notificationType, `is`("notification"))
    }

    @Test
    fun deleteAlertTest_TakeAlertData_RequestNull() = runBlockingTest {
        //Given
        repository.insertAlert(alert)

        //When
        repository.deleteAlert(alert)

        //Then
        val result = repository.getAlertById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun deleteAlertByIdTest_TakeDataAndTime_RequestNull() = runBlockingTest {
        //Given
        repository.insertAlert(alert)

        //When
        repository.deleteAlertById(date, time)

        //Then
        val result = repository.getAlertById(date, time)
        assertThat(result , nullValue())
    }

    @Test
    fun getAllAlertsTest_RequestSameAlertList() = runBlockingTest {

        //When
        val resultFlow = repository.getAllAlerts()
        var result: List<AlertData> = emptyList()
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
        repository.insertAlert(alert1)
        repository.insertAlert(alert2)

        //When
        repository.deleteAllAlerts()

        //Then
        val result1 = repository.getAlertById("1 Mar, 2024", "01:00")
        assertThat(result1 , nullValue())
        val result2 = repository.getAlertById("2 Mar, 2024", "02:00")
        assertThat(result2 , nullValue())
    }
}