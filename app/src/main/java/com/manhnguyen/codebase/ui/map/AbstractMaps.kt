package com.manhnguyen.codebase.ui.map

import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.movie.MovieActivity
import com.manhnguyen.codebase.util.LocationUtils
import com.manhnguyen.codebase.util.PermissionUtils

interface AbstractMaps : GoogleMap.OnMarkerClickListener {
    var mMap: GoogleMap
    var locationButton: View?
    val fusedLocationProviderClient: FusedLocationProviderClient
    var mapFragment: SupportMapFragment
    var activity: ActivityBase
    var currentMarker: Marker?
    var currentLocaion: LatLng?

    @SuppressLint("ResourceType")
    fun initializeMap(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        if (PermissionUtils.locationPermission(activity)) {
            mMap.isMyLocationEnabled = true
            locationButton = mapFragment.requireView().findViewById(0x2)
            locationButton?.visibility = View.GONE
        }
        currentLocation()
    }

    fun currentLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let { loc ->
                currentLocaion = LatLng(loc.latitude, loc.longitude)
                currentMarker?.remove()
                currentMarker = mMap.addMarker(MarkerOptions().position(currentLocaion!!))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocaion, 17f))
            }
        }
    }

    fun drawPolygon(latLngs: List<LatLng>) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLngs[0]))
        mMap.addMarker(MarkerOptions().position(latLngs[latLngs.size - 1]))
        mMap.addPolygon(PolygonOptions().addAll(latLngs))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        activity.startActivity(MovieActivity.newIntent(activity))
        println(p0?.position.toString())
        return true
    }
}