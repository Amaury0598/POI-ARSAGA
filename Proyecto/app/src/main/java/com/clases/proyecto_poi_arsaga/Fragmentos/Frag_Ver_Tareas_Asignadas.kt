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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class Frag_Ver_Tareas_Asignadas : Fragment(), Adaptador_Tareas_Asignadas.OnPubliClickListen {

    val listaTareasAsignadas = mutableListOf<Tareas>()
    private val database = FirebaseDatabase.getInstance();
    private val tareaRef = database.getReference("Tareas"); //crear "rama" (tabla)
    private val auth = FirebaseAuth.getInstance()
    val adaptadorMuro = Adaptador_Tareas_Asignadas(this, listaTareasAsignadas,this)
    private lateinit var loading: LoadingDialogFragment

    companion object{
        var tareaSel : Tareas = Tareas()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loading = LoadingDialogFragment(this)
        loading.startLoading("Cargando Tareas")

        val miViewGeneral =  inflater.inflate(R.layout.fragmento_tareas_asignadas, container, false)

        val MuroRecycler = miViewGeneral.findViewById<RecyclerView>(R.id.Recy_tareas_asignadas)
        val milinear = LinearLayoutManager(context)

        MuroRecycler.layoutManager = milinear
        MuroRecycler.adapter = adaptadorMuro
        CargarLista()
        return miViewGeneral
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun CargarLista(){

        tareaRef.child(General_Grupos.grupoActual.nombre).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    listaTareasAsignadas.clear()
                    for (t in snapshot.children) {
                        val tarea = t.getValue(Tareas::class.java) as Tareas
                        listaTareasAsignadas.add(tarea)
                    }
                }
                adaptadorMuro.notifyDataSetChanged()
                loading.isDismiss()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    override fun onitemClick(tareaSeleccionada: Tareas) {
        tareaSel = tareaSeleccionada
        val intent = Intent(activity, Tareas_Entregadas::class.java)
        intent.putExtra("tarea", tareaSel)

        activity?.startActivity(intent)
    }
}