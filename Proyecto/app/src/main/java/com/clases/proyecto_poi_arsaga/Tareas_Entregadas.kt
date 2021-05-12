package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Tareas_Entregadas
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Ver_Tareas_Asignadas
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialog
import com.clases.proyecto_poi_arsaga.Modelos.TareaEntregada
import com.clases.proyecto_poi_arsaga.Modelos.Tareas
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tarea_entregada.*
import kotlinx.android.synthetic.main.drawer_tarea_alumno.*
import kotlinx.android.synthetic.main.drawer_tareas.*

class Tareas_Entregadas : AppCompatActivity(), Adaptador_Tareas_Entregadas.OnPubliClickListen {

    val listatareasentregadas = mutableListOf<TareaEntregada>()
    val adaptadortareasentregadas = Adaptador_Tareas_Entregadas(this, listatareasentregadas, this)
    private lateinit var tarea : Tareas
    private val database = FirebaseDatabase.getInstance();
    private val tareasEntregadasRef = database.getReference("TareasEntregadas");
    private lateinit var loading : LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea_entregada)
        loading = LoadingDialog(this)
        loading.startLoading("Cargando tareas")

        tarea = intent.getSerializableExtra("tarea") as Tareas


        val RecyTareasEntregadas = findViewById<RecyclerView>(R.id.Recy_Tareas_entregadas)
        RecyTareasEntregadas.adapter = adaptadortareasentregadas
        val Back_TE = findViewById<ImageButton>(R.id.BT_back_TE)

        val Nombre_grupo = findViewById<TextView>(R.id.TV_TE_nombre_grupo)
        val Nombre_tarea = findViewById<TextView>(R.id.TV_TE_nombre_tarea)
        val Imagen_Grupo = findViewById<ImageView>(R.id.IMG_TE_imagen)
        val Fecha_Vencimiento = findViewById<TextView>(R.id.TV_FechaVencimiento)
        val ARSAGA_Coins = findViewById<TextView>(R.id.TV_COINS)
        val coins = Frag_Ver_Tareas_Asignadas.tareaSel.coins.toString()
        val text = ARSAGA_Coins.text.toString()
        val text_coins = "$text: $coins"

                Nombre_grupo.text = General_Grupos.grupoActual.nombre
        Nombre_tarea.text = Frag_Ver_Tareas_Asignadas.tareaSel.nombre
        Fecha_Vencimiento.text = Frag_Ver_Tareas_Asignadas.tareaSel.fecha
        ARSAGA_Coins.text = text_coins
        Picasso.get().load(Frag_Ver_Tareas_Asignadas.tareaSel.imagen).into(Imagen_Grupo)

        Back_TE.setOnClickListener {
            finish()
        }
        CargarLista()
    }

    fun CargarLista(){

        tareasEntregadasRef.child(tarea.id).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    listatareasentregadas.clear()
                    for (te in snapshot.children){
                        val tareaEnt = te.getValue(TareaEntregada::class.java) as TareaEntregada
                        listatareasentregadas.add(tareaEnt)
                    }
                }
                adaptadortareasentregadas.notifyDataSetChanged()
                loading.isDismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onitemClick(tareaEnt: TareaEntregada) {
        val intent = Intent(this, Entregar_Tarea::class.java)
        intent.putExtra("tareaActual", tarea)
        intent.putExtra("correo", tareaEnt.correo)
        intent.putExtra("estatus", 1)
        startActivity(intent)
    }
}