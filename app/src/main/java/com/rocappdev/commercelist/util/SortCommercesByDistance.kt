package com.rocappdev.commercelist.util

import com.rocappdev.commercelist.domain.Commerce
import kotlin.math.*

class SortCommercesByDistance(private val latitude: Double, private val longitude: Double) :
    Comparator<Commerce> {
    override fun compare(commerce1: Commerce, commerce2: Commerce): Int {
        val lat1: Double = commerce1.latitude
        val lon1: Double = commerce1.longitude
        val lat2: Double = commerce2.latitude
        val lon2: Double = commerce2.longitude
        val distanceToPlace1 = distance(latitude, longitude, lat1, lon1)
        val distanceToPlace2 = distance(latitude, longitude, lat2, lon2)
        return (distanceToPlace1 - distanceToPlace2).toInt()
    }

    private fun distance(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {
        val radius = 6378137.0 // approximate Earth radius, *in meters*
        val deltaLat = toLat - fromLat
        val deltaLon = toLon - fromLon
        val angle = 2 * asin(
            sqrt(
                sin(deltaLat / 2).pow(2.0) +
                        cos(fromLat) * cos(toLat) *
                        sin(deltaLon / 2).pow(2.0)
            )
        )
        return radius * angle
    }
}