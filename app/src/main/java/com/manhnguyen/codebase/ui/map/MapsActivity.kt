package com.manhnguyen.codebase.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.data.repository.ConfigRepository
import com.manhnguyen.codebase.databinding.ActivityMapsBinding
import com.manhnguyen.codebase.system.networking.NetworkViewModel
import com.manhnguyen.codebase.ui.DialogBuilder
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.progressbar.ProgressDialog
import com.manhnguyen.codebase.ui.progressbar.ProgressHelper
import com.manhnguyen.codebase.ui.viewmodels.MapsViewModel
import com.manhnguyen.codebase.util.LocationUtils
import com.manhnguyen.codebase.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import android.view.Gravity
import com.google.android.gms.maps.model.LatLng
import com.manhnguyen.codebase.util.GoogleMapsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


class MapsActivity : ActivityBase(), OnMapReadyCallback, ProgressHelper, AbstractMaps {


    override fun getActivityDataBinding(): ViewDataBinding {
        return binding
    }

    override fun getActivityWindow(): Window {
        return window
    }

    override fun getProgressDialog(): ProgressDialog {
        return progress_main
    }

    override lateinit var mMap: GoogleMap
    override lateinit var mapFragment: SupportMapFragment
    override var locationButton: View? = null
    override val fusedLocationProviderClient: FusedLocationProviderClient by inject()
    override var activity: ActivityBase = this
    override var currentMarker: Marker? = null
    override var currentLocaion: LatLng? = null

    private lateinit var binding: ActivityMapsBinding
    private val LOCATION_RQ_RESULT_CODE = 11
    private val mapsViewModel: MapsViewModel by inject()
    private val configRepo: ConfigRepository by inject()
    private val locationManager: LocationManager by inject()
    private val networkViewModel: NetworkViewModel by inject()
    private var snackbarNetStatus: Snackbar? = null
    private var isPermissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ButterKnife.bind(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        requestPermission()
        initializeSearch()
        snackbarNetStatus =
            Snackbar.make(binding.root, "There no network connect", LENGTH_INDEFINITE)
                .setBackgroundTint(resources.getColor(R.color.colorRed))
        networkViewModel.getInternetState().observe(this, { networkState ->
            if (!networkState) {
                snackbarNetStatus?.show()
            } else {
                snackbarNetStatus?.dismiss()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        /**
         * After user allow permission and back to app
         */
        if (isPermissionDenied) {
            requestPermission()
        }
    }

    private fun showHideMapsFeatures(show: Boolean) {
        if (show) {
            binding.idSearchView.visibility = View.VISIBLE
            binding.currentLocationBtn.visibility = View.VISIBLE
        } else {
            binding.idSearchView.visibility = View.GONE
            binding.currentLocationBtn.visibility = View.GONE
        }
    }

    private fun requestPermission() {
        val permissionArrayList: Array<String> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }

        PermissionUtils.requestPermission(this,
            permissionArrayList,
            object : PermissionUtils.PermissionResultHandler {
                override fun onGranted(results: Array<PermissionUtils.PermissionRequestResult?>) {
                    showHideMapsFeatures(true)
                    locationEnabledChecking()
                }

                override fun onDenied(results: Array<PermissionUtils.PermissionRequestResult?>) {
                    handlePermissionDenied()
                }

                override fun onDeniedForever(results: Array<PermissionUtils.PermissionRequestResult?>) {
                    handlePermissionDenied()
                }
            })
    }

    /**
     * Permission denied : Still show maps but hide maps features
     */
    private fun handlePermissionDenied() {
        val snackbar = Snackbar.make(
            binding.coordinatorLayout,
            resources.getString(R.string.permission_location_warning),
            Snackbar.LENGTH_INDEFINITE
        )
            .setBackgroundTint(resources.getColor(R.color.backgroundGrey2))
            .setTextColor(resources.getColor(R.color.black_color))
        snackbar.show()
        showHideMapsFeatures(false)
        initialize()
        lifecycleScope.launch {
            delay(7000)
            snackbar.dismiss()
            isPermissionDenied = true
        }
    }

    private fun locationEnabledChecking() {
        isPermissionDenied = false
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            DialogBuilder.alertYesDialog(
                this,
                "Location disabled",
                "Yes",
                getString(R.string.location_setting_enable_message)
            ) { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, LOCATION_RQ_RESULT_CODE)

            }.show()
        } else {
            initialize()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_RQ_RESULT_CODE) {
            initialize()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initializeMap(googleMap)
    }

    private fun initialize() {
        showProgressBar()
        mapFragment.getMapAsync(this)
        lifecycleScope.launch {
            when (val result = configRepo.fetchConfig()) {
                is Result.Loading -> showProgressBar()
                is Result.Success -> {
                    mapsViewModel.loadConfig().observe(this@MapsActivity, {
                        mapsViewModel.api.initializeImageUrl(it)
                        hideProgressBar()
                    })
                }
                is Result.Error -> {
                    hideProgressBar()
                }
            }
        }

    }

    private fun initializeSearch() {
        binding.idSearchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (query.isNotEmpty()) {
                        val latLng = LocationUtils.getFromLocationName(this@MapsActivity, query)
                        latLng?.let {
                            // Getting URL to the Google Directions API
                            drawPolygon(arrayListOf(currentLocaion!!, latLng))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                        }
                    }

                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    @OnClick(R.id.currentLocationBtn)
    fun currentLocationClick() {
        currentLocation()
    }


    companion object {
        fun newIntent(context: Context) = Intent(context, MapsActivity::class.java)
    }

}