package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        BT_registrarse.setOnClickListener {
            Toast.makeText(this, "Gracias por Registrarte", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}