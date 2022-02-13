package com.manhnguyen.codebase.common

import com.google.android.gms.maps.model.LatLng
import com.manhnguyen.codebase.data.model.ResponseInfo
import com.manhnguyen.codebase.data.model.TopRate

fun TopRate.toDataModel(): ResponseInfo = ResponseInfo(page, total_pages, total_results)

fun Float.toString() = "$this"

fun LatLng.toShortString() = "${this.latitude},${this.longitude}"

