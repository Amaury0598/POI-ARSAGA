package com.clases.proyecto_poi_arsaga.Fragmentos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.clases.proyecto_poi_arsaga.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class Dialog_Agregar_I: DialogFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val DialogView_A = inflater.inflate(R.layout.dialog_mapa, container, false)
        val Dialog_Elegir_I = android.app.AlertDialog.Builder(activity).setView(DialogView_A)

        DialogView_A.background = ColorDrawable(Color.TRANSPARENT)
        val BTN_Ubicacion = DialogView_A.findViewById<Button>(R.id.BTN_Compartit_Ubi)

        BTN_Ubicacion.setOnClickListener {
            Toast.makeText(activity, "Gracias", Toast.LENGTH_SHORT).show()
        }

        val mapDetail = (requireActivity().supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment?)
        mapDetail?.getMapAsync(this)


        return DialogView_A
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.setOnMapClickListener { coodenadas ->

            mMap.addMarker(MarkerOptions().position(coodenadas).title("Marker in Sydney"))
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }
}