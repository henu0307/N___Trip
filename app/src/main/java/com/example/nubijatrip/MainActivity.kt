package com.example.nubijatrip


import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
        } else {
            // Permission is not granted
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), // 1
                MainActivity.PERMISSION_REQUEST_CODE
            ) // 2
        }
        locationSource =
            FusedLocationSource(this, MainActivity.PERMISSION_REQUEST_CODE)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isNumber(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = true
        uiSettings.isLocationButtonEnabled = true

        naverMap.locationTrackingMode = LocationTrackingMode.Face
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true

        val assetManager: AssetManager = this.assets
        val inputStream: InputStream = assetManager.open("경상남도 창원시_누비자 터미널_20230407_1.csv")
        val reader = CSVReader(InputStreamReader(inputStream))
        val allContent = reader.readAll().toList()
        for (content in allContent) {
            if(isNumber(content[0])) {
                val marker = Marker()
                marker.position = LatLng(content[7].toDouble(), content[8].toDouble())
                marker.captionText = content[1]
                marker.captionColor = Color.BLUE
                marker.captionTextSize = 16f
                marker.isHideCollidedSymbols = true
                marker.isHideCollidedCaptions = true
                marker.map = naverMap
            }
        }
    }
}

//코드 참고 : https://shwoghk14.blogspot.com/2022/06/android-read-csv-file.html
//누비자 터미널 데이터 출처 : https://www.data.go.kr/data/15000545/fileData.do