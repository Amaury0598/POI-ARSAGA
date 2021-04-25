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
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R
import java.util.*


class Main_Frag :  Fragment(), Adaptador_Lista_Chats.OnGrupoClickListen {


    val listaChats = mutableListOf<ChatMensaje>()
    val adaptadorChatlistadechats = Adaptador_Lista_Chats(this, listaChats, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cargarLista()
        val miView =  inflater.inflate(R.layout.activity_lista__chats__grupos, container, false)
        var recycler_lista = miView.findViewById<RecyclerView>(R.id.RV_lista_chats_grupos)
        val milinear = LinearLayoutManager(context)

        recycler_lista.layoutManager = milinear
        recycler_lista.adapter = adaptadorChatlistadechats

        return miView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun cargarLista(){

        listaChats.add(ChatMensaje("Javier","Hola, como estas", Date(), false, 0, true))
        listaChats.add(ChatMensaje("Rasho MacQueen","Kuchau", Date(), true, 1, false))
        listaChats.add(ChatMensaje("Oliver","Mis piernas !!!", Date(), false, 2, true))
        listaChats.add(ChatMensaje("Benito","yipi yipi yipiy kejeje asdwajdjwa dwajdwadaw", Date(), true, 4, false))
        listaChats.add(ChatMensaje("José José","Hola, como estas", Date(), true, 5, true))
        listaChats.add(ChatMensaje("Messi","lalalalaa lalalalaa allalaala lalalala alallala alala", Date(), false, 6, true))


        adaptadorChatlistadechats.notifyDataSetChanged()

    }

    override fun onitemHold(toString: String) {
        Toast.makeText(activity, "El id es: $toString", Toast.LENGTH_SHORT).show()
    }


    override fun onitemClick(usuario: String) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        intent.putExtra("Nombre", usuario)
        activity?.startActivity(intent)
    }
}