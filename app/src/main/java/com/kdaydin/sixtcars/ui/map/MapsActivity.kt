package com.kdaydin.sixtcars.ui.map

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kdaydin.sixtcars.R
import com.kdaydin.sixtcars.databinding.ActivityMapsBinding
import com.kdaydin.sixtcars.ui.adapter.CarListAdapter
import com.kdaydin.sixtcars.ui.base.BaseActivity
import com.kdaydin.sixtcars.ui.base.VMState
import com.kdaydin.sixtcars.utils.extentions.dp
import org.koin.android.ext.android.get


class MapsActivity : OnMapReadyCallback, BaseActivity<MapsViewModel, ActivityMapsBinding>() {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel?.carData?.observe(this) {
            val builder = LatLngBounds.Builder()
            it?.let {
                it.forEach { car ->
                    Glide.with(this)
                        .asBitmap()
                        .load(car.carImageUrl)
                        .circleCrop()
                        .dontTransform()
                        .into(object : SimpleTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val marker = MarkerOptions().icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        Bitmap.createScaledBitmap(resource, 60.dp, 40.dp, true)
                                    )
                                ).title(car.name).position(
                                    LatLng(
                                        car.latitude?.toDouble() ?: 0.0,
                                        car.longitude?.toDouble() ?: 0.0
                                    )
                                )
                                mMap.addMarker(marker)
                                builder.include(marker.position)
                                val bounds = builder.build()
                                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 12.dp)
                                mMap.animateCamera(cu)
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                mMap.addMarker(
                                    MarkerOptions().icon(
                                        BitmapDescriptorFactory.defaultMarker()
                                    ).title(car.name).position(
                                        LatLng(
                                            car.latitude?.toDouble() ?: 0.0,
                                            car.longitude?.toDouble() ?: 0.0
                                        )
                                    )
                                )
                            }
                        })
                }
                binding?.rvCarList?.apply {
                    adapter = CarListAdapter(it)
                    val snapHelper: SnapHelper = LinearSnapHelper()
                    snapHelper.attachToRecyclerView(this)

                    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                val centerView: View =
                                    snapHelper.findSnapView(layoutManager) ?: View(context)
                                val pos: Int? = layoutManager?.getPosition(centerView)
                                Log.e("Snapped Item Position:", "" + pos)
                            }
                        }
                    })
                }
            }

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setPadding(16.dp, 16.dp, 16.dp, 200.dp)
        viewModel?.getCars()
    }

    override fun getLayoutRes(): Int = R.layout.activity_maps

    override fun getViewModelType(): MapsViewModel = get()

    override fun onStateChanged(state: VMState?) {
    }
}