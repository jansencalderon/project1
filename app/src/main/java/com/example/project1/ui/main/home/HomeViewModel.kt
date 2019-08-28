package com.example.project1.ui.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1.model.ParkingLot
import com.google.android.gms.maps.model.LatLng

class HomeViewModel : ViewModel() {

    private val _parkingList: MutableLiveData<List<ParkingLot>> = MutableLiveData()
    private val _filterParkingList: MutableLiveData<List<ParkingLot>> = MutableLiveData()

    fun getParkingList() = _parkingList
    fun getFilteredParkingList() = _filterParkingList

    init {
        loadParkingLots()
    }

    private fun loadParkingLots() {
        val parkingList: MutableList<ParkingLot> = arrayListOf()
        parkingList.add(ParkingLot(1, "Megamall", LatLng(14.5850352, 121.0541626)))
        parkingList.add(ParkingLot(2, "St. Francis Square Mall", LatLng(14.5845264, 121.0568556)))
        parkingList.add(ParkingLot(3, "The Linden Suites", LatLng(14.5846244, 121.0561067)))

        _parkingList.value = parkingList

    }

    fun filterParkingLots(query: String) =
        (_parkingList.value ?: emptyList()).let { parkingList ->
            Log.d("Parking Lot Query: ", query)
            _filterParkingList.value = when (query) {
                "" -> parkingList
                else -> parkingList.filter { parkingLot ->
                    parkingLot.name.toLowerCase().contains(query.toLowerCase())
                }
            }
            Log.d("Parking Lot Filtered: ", _filterParkingList.value.toString())
        }

}