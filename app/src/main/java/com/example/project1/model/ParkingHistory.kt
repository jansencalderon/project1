package com.example.project1.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

class ParkingHistory (val id: Int,
                      val lotName: String,
                      val date: Date,
                      val level: String,
                      val duration: String,
                      val charged: Int)