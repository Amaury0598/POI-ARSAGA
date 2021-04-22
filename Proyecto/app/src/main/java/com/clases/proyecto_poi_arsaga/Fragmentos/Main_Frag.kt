package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R


class Main_Frag :  Fragment(), Adaptador_Lista_Chats.OnGrupoClickListen {


    val listaChats = mutableListOf<Grupos>()
    val adaptadorChatlistadechats = Adaptador_Lista_Chats(this, listaChats, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cargarLista()
        val miView =  inflater.inflate(R.layout.activity_lista__chats__grupos, container, false)
        var recycler_lista = miView.findViewById<RecyclerView>(R.id.RV_lista_chats_grupos)
        val milinear = LinearLayoutManager(context)

        recycler_lista.layoutManager = milinear
        recycler_lista.adapter = adaptadorChatlistadechats

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
        listaChats.add(Grupos("LMAD", 5, "Hola", 0))
        listaChats.add(Grupos("LCC", 3, "Holo", 1))
        listaChats.add(Grupos("LA", 8, "Halo", 2))
        listaChats.add(Grupos("LM", 10, "Hulu", 3))
        listaChats.add(Grupos("LMAD", 5, "Hola", 0))
        listaChats.add(Grupos("LCC", 3, "Holo", 1))
        listaChats.add(Grupos("LA", 8, "Halo", 2))
        listaChats.add(Grupos("LM", 10, "Hulu", 3))
        adaptadorChatlistadechats.notifyDataSetChanged()

    }

    override fun onitemHold(integrantes: Int) {
        Toast.makeText(activity, "Numero de integrantes $integrantes", Toast.LENGTH_SHORT).show()
    }

    override fun onitemClick(nombre: String) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        intent.putExtra("Nombre", nombre)
        activity?.startActivity(intent)
    }
}