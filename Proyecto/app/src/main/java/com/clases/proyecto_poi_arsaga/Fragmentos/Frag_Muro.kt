package com.clases.proyecto_poi_arsaga.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Muro_General
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.clases.proyecto_poi_arsaga.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class Frag_Muro : Fragment(), Adaptador_Muro_General.OnPubliClickListen {

    val listaPublicacion = mutableListOf<Usuario>()
    val adaptadorMuro = Adaptador_Muro_General(this, listaPublicacion,this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        CargarLista()
        val miViewGeneral =  inflater.inflate(R.layout.fragmento_muro_grupos, container, false)
        val btnCrearGrupo = miViewGeneral.findViewById<FloatingActionButton>(R.id.FAB_crearGrupo)

        val MuroRecycler = miViewGeneral.findViewById<RecyclerView>(R.id.Recy_Muro)
        val milinear = LinearLayoutManager(context)

        MuroRecycler.layoutManager = milinear
        MuroRecycler.adapter = adaptadorMuro

        return miViewGeneral
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun CargarLista(){

        listaPublicacion.add(Usuario("Javier","Hola, como estas", "https://cdn.icon-icons.com/icons2/2620/PNG/512/among_us_player_pink_icon_156938.png", "Hola@outloock.com", ""))
        listaPublicacion.add(Usuario("El Rasho MacQueen la Machina mas veloz del mundo","Kuchau", "https://i.pinimg.com/236x/8c/5a/bb/8c5abb828846c4f963d84592197c6268.jpg", "Adios@outloock.com", ""))
        listaPublicacion.add(Usuario("Oliver","Mis piernas !!!", "https://i.pinimg.com/236x/62/c6/7b/62c67bbea0fc21565df13b5b1998b56a.jpg", "Hola@outloock.com", ""))
        listaPublicacion.add(Usuario("Benito","yipi yipi yipiy kejeje asdwajdjwa dwajdwadaw", "https://cdn.icon-icons.com/icons2/2620/PNG/512/among_us_player_pink_icon_156938.png", "Adios@outloock.com", ""))
        listaPublicacion.add(Usuario("José José","Hola, como estas", "https://i.pinimg.com/236x/62/c6/7b/62c67bbea0fc21565df13b5b1998b56a.jpg", "Hola@outloock.com", ""))

    }
}