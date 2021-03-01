package com.clases.clase_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.clases.clase_2.adaptadores.ChatAdaptador

class MainActivity : AppCompatActivity() {

    private val chatAdaptador = ChatAdaptador()

    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Mensajes)
        rvMensajes.adapter = chatAdaptador
    }
}