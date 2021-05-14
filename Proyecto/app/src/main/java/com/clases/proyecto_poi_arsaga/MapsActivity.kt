package com.clases.proyecto_poi_arsaga

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.clases.proyecto_poi_arsaga.Modelos.GlobalPositioningSystem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_entregar_tarea.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*
import kotlin.properties.Delegates

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var BTN_Ubi : Button
    private lateinit var CoordenadaSelec : LatLng
    private var opc : Int = 0
    private val STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        opc = intent.getIntExtra("Opcion", 0)

        when(opc){
            0 -> {
                finish()
            }
            1 -> {
                f_google_maps.visibility = View.VISIBLE
                image.visibility = View.GONE
                download_image.visibility = View.GONE
                BTN_C_Ubi.visibility = View.VISIBLE
                header_gps_image.text = "Mapa"
                seleccionarUbicacion()
            }
            2 -> {
                f_google_maps.visibility = View.VISIBLE
                image.visibility = View.GONE
                download_image.visibility = View.GONE
                BTN_C_Ubi.visibility = View.GONE
                header_gps_image.text = "Mapa"
                verUbicacion()
            }
            3 -> {
                image.visibility = View.VISIBLE
                download_image.visibility = View.VISIBLE
                f_google_maps.visibility = View.GONE
                BTN_C_Ubi.visibility = View.GONE
                header_gps_image.text = "Imagen"

                verImagen()
            }

        }


    }

    private fun verImagen() {

        val BTN_Back = findViewById<ImageButton>(R.id.BT_back_Ubi)
        val imagenURL = intent.getStringExtra("Imagen")


        Picasso.get().load(imagenURL).into(image)

        BTN_Back.setOnClickListener {

            finish()
        }

        download_image.setOnClickListener {
            permisosDescarga()

        }
    }

    private fun permisosDescarga() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }else{
                startDownloading()
            }
        }else{
            startDownloading()
        }
    }

    private fun startDownloading() {
        val url =  intent.getStringExtra("Imagen")
        val nombre =  intent.getStringExtra("ImagenNombre")

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(nombre)
        request.setDescription("Descargando...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        Toast.makeText(this@MapsActivity, "Se ha iniciado la descarga de la imagen", Toast.LENGTH_SHORT).show()
    }

    private fun verUbicacion() {

        BT_back_Ubi.setOnClickListener {

            finish()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.f_google_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun seleccionarUbicacion(){
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
            .findFragmentById(R.id.f_google_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }


    private fun TraducirCoordenadas(){

        val geoCode = Geocoder(this, Locale.getDefault())

        Thread{
            val direcciones = geoCode.getFromLocation(
                CoordenadaSelec.latitude,
                CoordenadaSelec.longitude,
                1
            )

            if(direcciones.size > 0){
                val direccion = GlobalPositioningSystem(
                    direcciones[0].getAddressLine(0),
                    direcciones[0].latitude,
                    direcciones[0].longitude
                )
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
        if(opc == 1) {
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
        }else if (opc == 2){

            val verLocacion = LatLng(intent.getDoubleExtra("latitud", 0.0), intent.getDoubleExtra("longitud", 0.0))
            mMap.addMarker(MarkerOptions().position(verLocacion).title("Marcador"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(verLocacion, 9F))
        }
    }

    private fun  activarMiUbi(){


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)

        mMap.isMyLocationEnabled = true
    }



}