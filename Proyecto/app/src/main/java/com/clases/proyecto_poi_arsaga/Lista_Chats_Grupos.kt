package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats
import kotlinx.android.synthetic.main.activity_lista__chats__grupos.*
import kotlinx.android.synthetic.main.chats_grupos.*

class Lista_Chats_Grupos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista__chats__grupos)
    }
}