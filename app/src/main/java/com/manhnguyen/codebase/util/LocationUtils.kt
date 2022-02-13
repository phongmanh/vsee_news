package com.manhnguyen.codebase.util

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocationUtils {

    companion object {

        fun getFromLocationName(context: Context, address: String): LatLng? {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(address, 5)

            var latLng: LatLng? = null
            if (!addresses.isNullOrEmpty()){
                latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
            }
            return latLng
        }

        fun getFromLocation(context: Context, lat: Double, lng: Double): String? {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(lat, lng, 30)
            if (address.size > 0)
                return address[0].getAddressLine(0)
            return null
        }

    }

}