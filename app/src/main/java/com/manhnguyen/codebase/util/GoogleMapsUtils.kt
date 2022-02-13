package com.manhnguyen.codebase.util

import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsRoute
import com.manhnguyen.codebase.common.toShortString
import com.manhnguyen.codebase.ui.map.DirectionsJSONParser
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class GoogleMapsUtils {

    companion object {

        fun getDirections(origin: LatLng, destination: LatLng): List<LatLng> {
            try {
                val context = GeoApiContext()
                    .setApiKey("")
                val request = DirectionsApi.getDirections(
                    context,
                    origin.toShortString(),
                    destination.toShortString()
                )
                var latlongList: List<com.google.maps.model.LatLng>? = null
                var resultList: List<LatLng> = ArrayList()
                try {
                    val routes: Array<DirectionsRoute> = request.await().routes
                    for (route in routes) {
                        latlongList = route.overviewPolyline.decodePath()
                    }
                    latlongList?.forEach { latLang ->
                        resultList.also {
                            it.plus(latLang)
                        }
                    }
                } catch (e: Exception) {
                    throw IllegalStateException(e)
                }
                return resultList
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return emptyList()
        }

        fun getDirectionsUrl(
            origin: LatLng,
            dest: LatLng
        ): String {

            // Origin of route
            val str_origin = "origin=" + origin.latitude + "," + origin.longitude

            // Destination of route
            val str_dest = "destination=" + dest.latitude + "," + dest.longitude

            // Key
            val key = "key="

            // Building the parameters to the web service
            val parameters = "$str_origin&amp;$str_dest&amp;$key"

            // Output format
            val output = "json"

            // Building the url to the web service
            return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
        }

        /** A method to download json data from url  */
        @Throws(IOException::class)
        fun downloadUrl(strUrl: String): String {
            var data = ""
            var iStream: InputStream? = null
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(strUrl)

                // Creating an http connection to communicate with url
                urlConnection = url.openConnection() as HttpURLConnection

                // Connecting to url
                urlConnection!!.connect()

                // Reading data from url
                iStream = urlConnection.inputStream
                val br = BufferedReader(InputStreamReader(iStream))
                val sb = StringBuffer()
                var line: String? = ""
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                data = sb.toString()
                br.close()
            } catch (e: java.lang.Exception) {
                Log.d("Exception on download", e.toString())
            } finally {
                iStream!!.close()
                urlConnection!!.disconnect()
            }
            return data
        }
    }

    /** A class to download data from Google Directions URL  */
    class DownloadTask : AsyncTask<String, Void, String>() {
        // Downloading data in non-ui thread
        override fun doInBackground(vararg url: String): String {

            // For storing data from web service
            var data = ""
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0])
                Log.d("DownloadTask", "DownloadTask : $data")
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)
        }
    }

    /** A class to parse the Google Directions in JSON format  */
    private class ParserTask :
        AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()

                // Starts parsing data
                routes = parser.parse(jObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return routes
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            var points: ArrayList<LatLng?>? = null
            var lineOptions: PolylineOptions? = null

            // Traversing through all the routes
            for (i in result!!.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.RED)
            }
            // Drawing polyline in the Google Map for the i-th route
        }
    }

}