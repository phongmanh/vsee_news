package com.manhnguyen.codebase.system.locations

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.manhnguyen.codebase.di.AppModule
import com.manhnguyen.codebase.util.KalmanLatLong
import org.koin.android.ext.android.inject


class FusedLocationService constructor(appModule: AppModule) : Service() {

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val locationViewModel: LocationViewModel by inject()
    private val fusedLocationProviderClient: FusedLocationProviderClient by inject()
    private val NOTIFICATION_ID = 12345678
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var notification: Notification
    private var lastLocation: Location? = null

    private fun startForeground() {
        try {
            if (Build.VERSION.SDK_INT >= 26) {

                mNotificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                val CHANNEL_ID = "my_channel_01"
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                mNotificationManager.createNotificationChannel(channel)
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build()
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private var currentSpeed = 0.0f
    private val real: KalmanLatLong = KalmanLatLong(3f)
    private var isIgnored = false

    override fun onCreate() {
        super.onCreate()
        startForeground()
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 2500
            smallestDisplacement = 0f
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { newLoc ->
                    try {
                        if (lastLocation == null) { // first time
                            locationViewModel.getLocationData()
                                .postValue(LocationChangeEvent(newLoc))
                            lastLocation = newLoc
                            return
                        }
                        lastLocation?.let { last ->

                            if (currentSpeed == 0.0f)
                                real.Process(
                                    newLoc.latitude,
                                    newLoc.longitude,
                                    newLoc.accuracy,
                                    newLoc.time,
                                    3f
                                )
                            else
                                real.Process(
                                    newLoc.latitude,
                                    newLoc.longitude,
                                    newLoc.accuracy,
                                    newLoc.time,
                                    newLoc.speed
                                )


                            val filterLocation = Location("").apply {
                                latitude = Location.convert(real.get_lat(), Location.FORMAT_DEGREES)
                                    .toDouble()
                                longitude =
                                    Location.convert(real.get_lng(), Location.FORMAT_DEGREES)
                                        .toDouble()
                            }
                            val time: Long = real.get_TimeStamp() - last.time
                            val distance: Double = last.distanceTo(
                                filterLocation
                            ).toDouble()

                            if (distance > 3f) {
                                if (isIgnored) { // case user not move
                                    locationViewModel.getLocationData()
                                        .postValue(LocationChangeEvent(lastLocation))
                                }
                                locationViewModel.getLocationData()
                                    .postValue(LocationChangeEvent(filterLocation))
                                lastLocation = filterLocation
                                currentSpeed = newLoc.speed
                                isIgnored = false
                            } else {
                                isIgnored = true
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("FusedLocationService", "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        Log.i("FusedLocationService", "onBind")
        return null
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
        Log.i("FusedLocationService", "onTaskRemoved")
    }


    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
        Log.i("FusedLocationService", "Location services is destroyed")
    }

}