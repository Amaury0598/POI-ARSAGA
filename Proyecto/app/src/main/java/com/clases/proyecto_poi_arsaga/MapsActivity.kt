package com.clases.proyecto_poi_arsaga

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.clases.proyecto_poi_arsaga.Modelos.GlobalPositioningSystem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var BTN_Ubi : Button
    private lateinit var CoordenadaSelec : LatLng

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val BTN_Back = findViewById<ImageButton>(R.id.BT_back_Ubi)
        BTN_Ubi = findViewById<Button>(R.id.BTN_C_Ubi)

        BTN_Back.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        BTN_Ubi.setOnClickListener {
            TraducirCoordenadas()
            Toast.makeText(this, "Compartida", Toast.LENGTH_SHORT).show()
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }


    private fun TraducirCoordenadas(){

        val geoCode = Geocoder(this, Locale.getDefault())

        Thread{
            val direcciones = geoCode.getFromLocation(CoordenadaSelec.latitude, CoordenadaSelec.longitude , 1)

            if(direcciones.size > 0){
                val direccion = GlobalPositioningSystem(direcciones[0].getAddressLine(0), direcciones[0].latitude, direcciones[0].longitude)
                val intent_OK = Intent()
                intent_OK.putExtra("Ubicacion", direccion)
                setResult(RESULT_OK, intent_OK)
                finish()
            }
        }.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        activarMiUbi()

        mMap.setOnMapClickListener { coodenadas ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(coodenadas).title("Marcador"))
            CoordenadaSelec = coodenadas
            BTN_Ubi.isEnabled = true
            BTN_Ubi.setTextColor(Color.parseColor("#FFFFFF"))
        }

        // Add a marker in Sydney and move the camera
        val Monterrey = LatLng(26.670, -100.31)
        mMap.addMarker(MarkerOptions().position(Monterrey).title("Marcador"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Monterrey, 9F))
    }

    private fun  activarMiUbi(){


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        mMap.isMyLocationEnabled = true
    }
}