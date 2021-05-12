package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_entregar_tarea.*

class Entregar_Tarea : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entregar_tarea)

        BT_back_EntregarT.setOnClickListener {
            finish()
        }

        TV_Entrega_Archivo.setOnClickListener {
            Toast.makeText(this, "Se subió el Archivaldo", Toast.LENGTH_SHORT).show()
        }

        TV_Entrega_nombre_tarea.text = intent.getStringExtra("Nombre_GRUPO")
        TV_Entrega_nombre_grupo.text = intent.getStringExtra("Nombre_TAREA")
    }
}