package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R
import kotlinx.android.synthetic.main.activity_main.*


class Main_Frag :  Fragment() {

    val listaChats = mutableListOf<Grupos>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cargarLista()
        val context: Context = requireContext()
        val adaptadorChatlistadechats = Adaptador_Lista_Chats(context , listaChats)

        val miView =  inflater.inflate(R.layout.activity_lista__chats__grupos, Contenedor, false)
        val recycler_lista = miView.findViewById<RecyclerView>(R.id.lista_Chats_Grupos)




        val Boton = miView.findViewById<Button>(R.id.BTN_agregar)
        Boton.setOnClickListener {
            val intent = Intent(activity, Chat_Grupal::class.java)
            activity?.startActivity(intent)
        }
        return miView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun cargarLista(){

        listaChats.add(Grupos("LMAD", 5, "Hola", 0))
        listaChats.add(Grupos("LCC", 3, "Holo", 1))
        listaChats.add(Grupos("LA", 8, "Halo", 2))
        listaChats.add(Grupos("LM", 10, "Hulu", 3))

    }
}