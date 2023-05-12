package com.example.nubijatrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Transformations.map
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

class MapFragmentActivity : FragmentActivity(), OnMapReadyCallback {
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
        // ...
    }
}