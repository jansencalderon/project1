package com.example.project1.ui.main.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1.enums.DateTimeFormat
import com.example.project1.model.ParkingHistory
import com.example.project1.utils.toReadableString
import java.util.*
import kotlin.collections.ArrayList

class HistoryViewModel : ViewModel() {

    private val _historyList: MutableLiveData<List<ParkingHistory>> = MutableLiveData()

    fun getHistoryList() = _historyList

    init {
        loadParkingData()
    }

    /*
    * Load Parking Data
    */
    fun loadParkingData() {
        val list: MutableList<ParkingHistory> = ArrayList()
        list.add(ParkingHistory(1, "Megamall", Date(), "Parking A", "3 hours", 100))
        list.add(ParkingHistory(2, "St. Francis Square", Date(), "Parking F", "6 hours", 600))
        list.add(ParkingHistory(3, "The Linden Suites", Date(), "Parking D", "7 hours", 700))
        list.add(ParkingHistory(4, "The Podium", Date(), "Parking C", "5 hours", 500))
        list.add(ParkingHistory(5, "The Linden Suites", Date(), "Parking G", "2 hours", 100))

        _historyList.value = list
    }

    fun filterHistory(dateQuery: Date) {
        loadParkingData()
        _historyList.value = _historyList.value?.filter { parkingHistory ->
            parkingHistory.date.toReadableString(DateTimeFormat.DATE_LONG) == dateQuery.toReadableString(DateTimeFormat.DATE_LONG)
        }
    }


}