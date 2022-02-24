package com.manhnguyen.codebase.system.locations

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.manhnguyen.codebase.util.KalmanLatLong
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.android.ext.android.inject
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
fun FusedLocationProviderClient.getLocationFlow() = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(results: LocationResult) {
            for (location: Location in results.locations) {
                trySend(location).isSuccess
            }
        }
    }
    requestLocationUpdates(
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 2500
            smallestDisplacement = 0f
        },
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        close(e)
    }
    awaitClose {
        removeLocationUpdates(callback)
    }
}

@ExperimentalCoroutinesApi
suspend fun FusedLocationProviderClient.awaitLastLocation(): Location? =
    suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location) { e -> continuation.resumeWithException(e) }
        }.addOnFailureListener { e ->
            continuation.resumeWithException(e)
        }
    }


class FusedLocationService : LifecycleService() {
    private val locationViewModel: LocationViewModel by inject()
    private val fusedLocationProviderClient: FusedLocationProviderClient by inject()
    private val NOTIFICATION_ID = 12345678
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var notification: Notification
    private var lastLocation: Location? = null
    private var currentSpeed = 0.0f
    private val real: KalmanLatLong = KalmanLatLong(3f)
    private var isIgnored = false

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

    override fun onCreate() {
        super.onCreate()
        startForeground()

        fusedLocationProviderClient.getLocationFlow().conflate().asLiveData()
            .observe(lifecycleScope as LifecycleOwner, { newLoc ->

                if (lastLocation == null) { // first time
                    locationViewModel.getLocationData()
                        .postValue(LocationChangeEvent(newLoc))
                    lastLocation = newLoc
                    return@observe
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
            })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("FusedLocationService", "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopForeground(true)
        stopSelf()
        Log.i("FusedLocationService", "onTaskRemoved")
    }


    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
        Log.i("FusedLocationService", "Location services is destroyed")
    }

}