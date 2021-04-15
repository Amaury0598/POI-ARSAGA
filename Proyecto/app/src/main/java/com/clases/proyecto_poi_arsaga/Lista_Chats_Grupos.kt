package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats

class Lista_Chats_Grupos : AppCompatActivity() {

    private val listMsgChats = Adaptador_Lista_Chats()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista__chats__grupos)

        val rvListChats = findViewById<RecyclerView>(R.id.RV_lista_chats_grupos)
        rvListChats.adapter = listMsgChats

    }
}