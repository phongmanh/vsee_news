package com.manhnguyen.codebase.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class PermissionUtils {

    companion object {

        val LOCATION_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            arrayListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        fun checkSelfPermission(context: Context, type: String): Boolean {
            if (ContextCompat.checkSelfPermission(
                    context,
                    type
                ) != PackageManager.PERMISSION_GRANTED
            )
                return false
            return true
        }

        fun locationPermission(context: Context): Boolean {
            try {
                val locationStatus = checkSelfPermission(
                    context,Manifest.permission.ACCESS_FINE_LOCATION
                ) && checkSelfPermission(
                    context,Manifest.permission.ACCESS_COARSE_LOCATION
                )
                val backgroundLocation: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    checkSelfPermission(
                        context,Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                } else {
                    /*Auto grant*/
                    true
                }

                return locationStatus && backgroundLocation

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }


        fun checkActivityRecognition(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACTIVITY_RECOGNITION
                )
            else
                true
        }

        fun requestPermission(
            activity: FragmentActivity,
            permissions: ArrayList<String>,
            resultHandler: PermissionResultHandler
        ) {

            if (permissions.isNotEmpty()) {
                val i = intArrayOf(0)
                val granted = booleanArrayOf(true)
                val hasRationale = booleanArrayOf(false)
                val returnedPermissions: Array<PermissionRequestResult?> =
                    arrayOfNulls(permissions.size)

                val rxPermissions = RxPermissions(activity)
                rxPermissions
                    .requestEach(*permissions.toTypedArray())
                    .subscribe(object : Observer<Permission> {
                        override fun onNext(@NonNull permission: Permission) {
                            if (!permission.granted) {
                                granted[0] = false
                            }

                            if (!permission.shouldShowRequestPermissionRationale) {
                                hasRationale[0] = true
                            }

                            returnedPermissions[i[0]] = PermissionRequestResult(
                                permission.name,
                                permission.granted,
                                !permission.shouldShowRequestPermissionRationale
                            )
                            i[0]++

                            if (i[0] == permissions.size) {
                                if (granted[0]) {
                                    resultHandler.onGranted(returnedPermissions)
                                } else {
                                    if (!hasRationale[0]) {
                                        resultHandler.onDenied(returnedPermissions)
                                    } else {
                                        resultHandler.onDeniedForever(returnedPermissions)

                                    }
                                }
                            }
                        }

                        override fun onError(@NonNull e: Throwable) {
                            e.printStackTrace()

                        }

                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable?) {

                        }
                    })
            }
        }

        @ExperimentalCoroutinesApi
        fun dexterRequestPermissions(context: Context, permissions: List<String>) = callbackFlow {
            val listener = object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            this@callbackFlow.trySend(true).isSuccess
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    requests: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
            Dexter.withContext(context).withPermissions(permissions)
                .withListener(listener).check()

            awaitClose()
        }

    }

    class PermissionRequestResult(
        var permissionName: String,
        var isGranted: Boolean,
        var isWontAskAgain: Boolean
    )

    interface PermissionResultHandler {
        fun onGranted(results: Array<PermissionRequestResult?>)
        fun onDenied(results: Array<PermissionRequestResult?>)
        fun onDeniedForever(results: Array<PermissionRequestResult?>)
    }


}