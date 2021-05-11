package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Muro_General
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Tareas_Asignadas
import com.clases.proyecto_poi_arsaga.General_Grupos
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.clases.proyecto_poi_arsaga.R
import com.clases.proyecto_poi_arsaga.Tareas_Entregadas
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class Frag_Ver_Tareas_Asignadas : Fragment(), Adaptador_Tareas_Asignadas.OnPubliClickListen {

    val listaTareasAsignadas = mutableListOf<Usuario>()
    val adaptadorMuro = Adaptador_Tareas_Asignadas(this, listaTareasAsignadas,this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        CargarLista()
        val miViewGeneral =  inflater.inflate(R.layout.fragmento_tareas_asignadas, container, false)

        val MuroRecycler = miViewGeneral.findViewById<RecyclerView>(R.id.Recy_tareas_asignadas)
        val milinear = LinearLayoutManager(context)

        MuroRecycler.layoutManager = milinear
        MuroRecycler.adapter = adaptadorMuro

        return miViewGeneral
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun CargarLista(){

        listaTareasAsignadas.add(Usuario("Biblia de Arte","Optimización de Videojuegos", "https://developer.nvidia.com/sites/default/files/akamai/gamedev/UnrealEngineLogo.jpg", "15/05/2021",""))
        listaTareasAsignadas.add(Usuario("Análisis","Optimización de Videojuegos", "https://developer.nvidia.com/sites/default/files/akamai/gamedev/UnrealEngineLogo.jpg", "17/05/2021",""))
        listaTareasAsignadas.add(Usuario("Modélos","Optimización de Videojuegos", "https://developer.nvidia.com/sites/default/files/akamai/gamedev/UnrealEngineLogo.jpg", "19/05/2021",""))

    }

    override fun onitemClick(Nombre_Tarea: String, Nombre_Grupo: String, Imagen: String) {
        val intent = Intent(activity, Tareas_Entregadas::class.java)
        intent.putExtra("Nombre_Tarea", Nombre_Tarea)
        intent.putExtra("Nombre_Grupo", Nombre_Grupo)
        intent.putExtra("Imagen_TA", Imagen)
        activity?.startActivity(intent)
    }
}