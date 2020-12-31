package com.kdaydin.sixtcars.ui.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
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
import com.google.android.gms.maps.model.LatLngBounds
import com.kdaydin.sixtcars.R
import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.databinding.ActivityMapsBinding
import com.kdaydin.sixtcars.ui.adapter.CarListAdapter
import com.kdaydin.sixtcars.ui.base.BaseActivity
import com.kdaydin.sixtcars.ui.base.VMState
import com.kdaydin.sixtcars.ui.car_detail.CarDetailBottomSheet
import com.kdaydin.sixtcars.ui.listener.CarListButtonListener
import com.kdaydin.sixtcars.utils.extentions.dp
import org.koin.android.ext.android.get


class MapsActivity : OnMapReadyCallback, BaseActivity<MapsViewModel, ActivityMapsBinding>() {

    private var permissionDenied = false
    private lateinit var mMap: GoogleMap
    private lateinit var carDetailBottomSheet: CarDetailBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel?.carData?.observe(this) {
            it?.let {
                binding?.rvCarList?.apply {
                    adapter = CarListAdapter(it, object : CarListButtonListener {
                        override fun onGetDirectionClicked(item: SixtCar) {
                            val uri =
                                Uri.parse("google.navigation:q=${item.latitude},${item.longitude}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            startActivity(mapIntent)
                        }

                        override fun onGetInfoClicked(item: SixtCar) {
                            carDetailBottomSheet = CarDetailBottomSheet.create(item)
                            supportFragmentManager.executePendingTransactions()
                            carDetailBottomSheet.show(supportFragmentManager, item.modelIdentifier)
                        }

                    })
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

                                val marker = viewModel?.markers?.value?.get(pos ?: 0)
                                mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        marker?.position, 15f
                                    )
                                )
                                marker?.showInfoWindow()
                            }
                        }
                    })
                }
            }

        }
        viewModel?.markerOptions?.observe(this) { options ->
            if (options.isNullOrEmpty().not()) {
                val builder = LatLngBounds.Builder()
                options.forEach { opt ->
                    val marker = mMap.addMarker(opt)
                    builder.include(marker.position)
                    viewModel?.markers?.value?.add(marker)
                }
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 12.dp)
                mMap.animateCamera(cu)
                setCarImages()
            }
        }
    }

    private fun setCarImages() {
        viewModel?.markers?.value?.forEachIndexed { index, marker ->
            Glide.with(this)
                .asBitmap()
                .load(viewModel?.carData?.value?.get(index)?.carImageUrl)
                .circleCrop()
                .dontTransform()
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val ratio =
                            (resource.height.toFloat() / resource.width.toFloat())
                        val width = 48.dp
                        val height = (48.dp * ratio).toInt()
                        val bitmap = Bitmap.createScaledBitmap(
                            resource,
                            width,
                            height,
                            true
                        )
                        marker.setIcon(
                            BitmapDescriptorFactory.fromBitmap(bitmap)
                        )
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        marker.setIcon(
                            BitmapDescriptorFactory.fromBitmap(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_car_48dp,
                                    applicationContext.theme
                                )?.toBitmap(32.dp, 32.dp)
                            )
                        )
                    }
                })
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
        mMap.setPadding(16.dp, 16.dp, 16.dp, 240.dp)
        mMap.setOnMarkerClickListener { marker ->
            val index = viewModel?.markers?.value?.indexOfFirst { mrk ->
                mrk.position == marker.position
            }
            binding?.rvCarList?.smoothScrollToPosition(index ?: 0)
            false
        }
        viewModel?.getCars()
    }


    override fun getLayoutRes(): Int = R.layout.activity_maps

    override fun getViewModelType(): MapsViewModel = get()

    override fun onStateChanged(state: VMState?) {
    }

}