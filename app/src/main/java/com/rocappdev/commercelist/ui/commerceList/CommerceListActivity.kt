package com.rocappdev.commercelist.ui.commerceList

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.rocappdev.commercelist.R
import com.rocappdev.commercelist.domain.Categories
import com.rocappdev.commercelist.domain.Commerce
import com.rocappdev.commercelist.ui.commerceDetail.CommerceDetailActivity
import com.rocappdev.commercelist.ui.commerceList.viewmodel.CommerceListViewModel
import com.rocappdev.commercelist.util.PERMISSION_ID
import com.rocappdev.commercelist.util.checkPermission
import com.rocappdev.commercelist.util.isLocationEnabled
import com.rocappdev.commercelist.util.toast
import kotlinx.android.synthetic.main.activity_commerce_list.*

class CommerceListActivity : AppCompatActivity() {
    private val commerceListViewModel: CommerceListViewModel by viewModels()
    private lateinit var commerceAdapter: CommerceListAdapter
    private var checkedItem = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commerce_list)

        setViews()
        setObservers()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setViews() {
        fab_location.imageTintList = ContextCompat.getColorStateList(this, R.color.colorPrimary)
        fab_location.backgroundTintList =
            ContextCompat.getColorStateList(this, android.R.color.white)

        commerceAdapter = CommerceListAdapter { commerce ->
            openCommerceDetail(commerce)
        }
        commerceList.adapter = commerceAdapter

        val mLayoutManager = LinearLayoutManager(applicationContext)
        commerceList.layoutManager = mLayoutManager

        fab_location.setOnClickListener {
            getLocation()
        }
    }

    private fun openCommerceDetail(commerce: Commerce) {
        val intent = Intent(this, CommerceDetailActivity::class.java)
        intent.putExtra(SELECTED_COMMERCE, commerce)
        startActivity(intent)
    }

    private fun setObservers() {
        commerceListViewModel.commerceList.observe(this, {
            error.visibility = View.GONE
            commerceList.visibility = View.VISIBLE
            commerceAdapter.setData(it.list)
            fab_location.visibility = View.VISIBLE
        })
        commerceListViewModel.error.observe(this, {
            error.visibility = View.VISIBLE
            commerceList.visibility = View.GONE
            fab_location.visibility = View.GONE
            error.text = it.message
        })
        commerceListViewModel.loading.observe(this, {
            if (it) {
                loading.visibility = View.VISIBLE
                commerceList.visibility = View.GONE
                fab_location.visibility = View.GONE
                error.visibility = View.GONE
            } else
                loading.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            showCategoryList()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showCategoryList() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.filter_category))

        val categories = arrayListOf<String>()
        Categories.values().forEach {
            categories.add(it.category_name)
        }

        builder.setSingleChoiceItems(categories.toTypedArray(), checkedItem) { dialog, selected ->
            commerceListViewModel.getFilteredCommerces(Categories.valueOf(categories[selected]))
            commerceList.smoothScrollToPosition(0)
            dialog.dismiss()
            checkedItem = selected
        }
        builder.setNegativeButton(getString(android.R.string.cancel), null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun getLocation() {
        if (checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        sortByLocation(location)
                    }
                }
            } else {
                toast(getString(R.string.enable_location))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ID -> {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val lastLocation = locationResult?.lastLocation
            if (lastLocation != null) {
                sortByLocation(lastLocation)
            }
        }
    }

    private fun sortByLocation(location: Location) {
        fab_location.imageTintList = ContextCompat.getColorStateList(this, android.R.color.white)
        fab_location.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.colorPrimary)
        commerceListViewModel.getSortCommercesByDistance(
            location.latitude,
            location.longitude
        )
        commerceList.smoothScrollToPosition(0)
    }

    companion object {
        const val SELECTED_COMMERCE = "selectedCommerce"
    }
}