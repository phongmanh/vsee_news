package com.manhnguyen.codebase.common

import com.google.android.gms.maps.model.LatLng

fun Float.toString() = "$this"

fun LatLng.toShortString() = "${this.latitude},${this.longitude}"

