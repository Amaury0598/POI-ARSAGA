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

    lateinit private var userActual : Usuario
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")
    private val tareasRef = database.getReference("Tareas")
    private val tareasUsuariosRef = database.getReference("TareasUsuarios")
    val listaTareasPendientes = mutableListOf<Tareas>()
    val listaTareasEntregadas = mutableListOf<Tareas>()
    val listaTareasNoEntregadas = mutableListOf<Tareas>()
    private lateinit var loading : LoadingDialogFragment

    val adaptador_tareas_Pendientes = Adaptador_Estatus_tarea_grupo(0, this , listaTareasPendientes, this)
    val adaptador_tareas_Entregadas = Adaptador_Estatus_tarea_grupo(1, this , listaTareasEntregadas,  this)
    val adaptador_tareas_Vencidas = Adaptador_Estatus_tarea_grupo(2, this , listaTareasNoEntregadas, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val miViewGrupos =  inflater.inflate(R.layout.fragmento_lista_tareas_grupo, container, false)
        loading = LoadingDialogFragment(this)
        loading.startLoading("Cargando tareas")

        var recycler_Pendientes = miViewGrupos.findViewById<RecyclerView>(R.id.Recy_lista_tareasPendientes_grupo)
        var recycler_Entregadas = miViewGrupos.findViewById<RecyclerView>(R.id.Recylista_tareasEntregadas_grupo)
        var recycler_Vencidas = miViewGrupos.findViewById<RecyclerView>(R.id.Recylista_tareasVencidas_grupo)

        recycler_Pendientes.adapter = adaptador_tareas_Pendientes
        recycler_Entregadas.adapter = adaptador_tareas_Entregadas
        recycler_Vencidas.adapter = adaptador_tareas_Vencidas
        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener {
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    cargarLista()
                }
        return miViewGrupos
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun cargarLista(){

        tareasRef.child(General_Grupos.grupoActual.nombre)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listaTareasNoEntregadas.clear()
                            listaTareasEntregadas.clear()
                            listaTareasPendientes.clear()
                            for (t in snapshot.children) {
                                val tarea = t.getValue(Tareas::class.java) as Tareas
                                tareasUsuariosRef.child(tarea.id).addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists()){
                                            for(ltu in snapshot.children){
                                                val listaTareasUsuario = ltu.getValue(lTareaUsuarios::class.java) as lTareaUsuarios
                                                for (l in listaTareasUsuario.listaUsuarios) {
                                                    if (l.correo == userActual.correo) {
                                                        when (l.status) {
                                                            "Pendiente" -> {
                                                                listaTareasPendientes.add(tarea)
                                                                adaptador_tareas_Pendientes.notifyDataSetChanged()
                                                            }
                                                            "Entregada" -> {
                                                                listaTareasEntregadas.add(tarea)
                                                                adaptador_tareas_Entregadas.notifyDataSetChanged()
                                                            }
                                                            "No Entregada" -> {
                                                                listaTareasNoEntregadas.add(tarea)
                                                                adaptador_tareas_Vencidas.notifyDataSetChanged()
                                                            }
                                                        }
                                                        break
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

                            }
                        }
                        loading.isDismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


        /*for(tarea in General_Grupos.tareasActual) {
            tareasUsuariosRef.child(tarea.id).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(ltu in snapshot.children){
                            val listaTareasUsuario = ltu.getValue(lTareaUsuarios::class.java) as lTareaUsuarios
                            for (l in listaTareasUsuario.listaUsuarios) {
                                if (l.correo == userActual.correo) {
                                    when (l.status) {
                                        "Pendiente" -> {
                                            listaTareasPendientes.add(tarea)
                                            adaptador_tareas_Pendientes.notifyDataSetChanged()
                                        }
                                        "Entregada" -> {
                                            listaTareasEntregadas.add(tarea)
                                            adaptador_tareas_Entregadas.notifyDataSetChanged()
                                        }
                                        "No Entregada" -> {
                                            listaTareasNoEntregadas.add(tarea)
                                            adaptador_tareas_Vencidas.notifyDataSetChanged()
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }*/

        /*listaTareasEstatus_tarea.add(Tareas("Videojuegos I","Modelos_2", "0", "05/05/2021", "No", 0))
        listaTareasEstatus_tarea.add(Tareas("Videojuegos I","Texturas", "0", "10/05/2021", "Si", 1))
        listaTareasEstatus_tarea.add(Tareas("Videojuegos I","Luces", "0", "12/05/2021","Si", 1))*/

        //listaTareasEntregadas_tarea.add(TareaEntregada("1","Audios", "Videojuegos I", "0", "16/05/2021"))
    }

    override fun onitemClick(tarea:Tareas, estatus: Int) {
        val intent = Intent(activity, Entregar_Tarea::class.java)
        intent.putExtra("tareaActual", tarea)
        intent.putExtra("estatus", estatus)
        activity?.startActivity(intent)
    }
}