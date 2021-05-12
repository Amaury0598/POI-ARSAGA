package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.*
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Estatus_tarea_grupo
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Grupos_Carreras
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.FieldPosition

class Frag_Estatus_tarea_grupo : Fragment(), Adaptador_Estatus_tarea_grupo.OnPubliClickListen {

    val listaTareasEstatus_tarea = mutableListOf<Tareas>()
    val listaTareasEntregadas_tarea = mutableListOf<TareaEntregada>()
    val Estatus = 0
    val adaptador_tareas_Pendientes = Adaptador_Estatus_tarea_grupo(0, this , listaTareasEstatus_tarea, listaTareasEntregadas_tarea, this)
    val adaptador_tareas_Entregadas = Adaptador_Estatus_tarea_grupo(1, this , listaTareasEstatus_tarea, listaTareasEntregadas_tarea, this)
    val adaptador_tareas_Vencidas = Adaptador_Estatus_tarea_grupo(2, this , listaTareasEstatus_tarea, listaTareasEntregadas_tarea, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val miViewGrupos =  inflater.inflate(R.layout.fragmento_lista_tareas_grupo, container, false)

        cargarLista()
        var recycler_Pendientes = miViewGrupos.findViewById<RecyclerView>(R.id.Recy_lista_tareasPendientes_grupo)
        var recycler_Entregadas = miViewGrupos.findViewById<RecyclerView>(R.id.Recylista_tareasEntregadas_grupo)
        var recycler_Vencidas = miViewGrupos.findViewById<RecyclerView>(R.id.Recylista_tareasVencidas_grupo)

        recycler_Pendientes.adapter = adaptador_tareas_Pendientes
        recycler_Entregadas.adapter = adaptador_tareas_Entregadas
        recycler_Vencidas.adapter = adaptador_tareas_Vencidas

        return miViewGrupos
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun cargarLista(){

        listaTareasEstatus_tarea.add(Tareas("Videojuegos I","Modelos_2", "0", "05/05/2021", "No", 0))
        listaTareasEstatus_tarea.add(Tareas("Videojuegos I","Texturas", "0", "10/05/2021", "Si", 1))
        listaTareasEstatus_tarea.add(Tareas("Videojuegos I","Luces", "0", "12/05/2021","Si", 1))

        listaTareasEntregadas_tarea.add(TareaEntregada("1","Audios", "Videojuegos I", "0", "16/05/2021"))
    }

    override fun onitemClick(Nombre_Tarea: String, Nombre_Grupo: String) {
        val intent = Intent(activity, Entregar_Tarea::class.java)
        intent.putExtra("Nombre_GRUPO", Nombre_Grupo)
        intent.putExtra("Nombre_TAREA", Nombre_Tarea)
        activity?.startActivity(intent)
    }
}