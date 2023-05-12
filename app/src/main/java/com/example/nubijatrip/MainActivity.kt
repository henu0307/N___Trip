package com.example.nubijatrip

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Transformations.map
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker

class MainActivity : FragmentActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = true

        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        val marker = Marker()
        marker.position = LatLng(35.21663989, 128.6942419)
        marker.captionText = "성원중앙상가"
        marker.captionColor = Color.BLUE
        marker.captionTextSize = 16f
        marker.isHideCollidedSymbols = true
        marker.isHideCollidedCaptions = true
        marker.map = naverMap
    }
}