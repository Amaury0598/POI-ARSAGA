package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R
import kotlinx.android.synthetic.main.activity_lista__chats__grupos.*
import kotlinx.android.synthetic.main.activity_main.*

class Main_Frag :  Fragment() {


    val listaChats = mutableListOf<Grupos>()
    val adaptadorChatlistadechats = Adaptador_Lista_Chats(this, listaChats)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        RV_lista_chats_grupos.adapter = adaptadorChatlistadechats
        BTN_agregar.setOnClickListener {
            Toast.makeText(context, "Sii", Toast.LENGTH_SHORT).show()
        }
        return inflater.inflate(R.layout.activity_lista__chats__grupos, Contenedor, false)


    }

    fun cargarLista(){

        listaChats.add(Grupos("LMAD", 5,"Hola", 0))
        listaChats.add(Grupos("LCC", 3,"Holo", 1))
        listaChats.add(Grupos("LA", 8,"Halo", 2))
        listaChats.add(Grupos("LM", 10,"Hulu", 3))
        adaptadorChatlistadechats.notifyDataSetChanged()
    }
}