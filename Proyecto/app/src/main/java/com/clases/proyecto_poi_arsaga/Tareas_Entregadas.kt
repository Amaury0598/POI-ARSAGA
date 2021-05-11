package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Tareas_Entregadas
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Ver_Tareas_Asignadas
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tarea_entregada.*
import kotlinx.android.synthetic.main.drawer_tarea_alumno.*
import kotlinx.android.synthetic.main.drawer_tareas.*

class Tareas_Entregadas : AppCompatActivity(), Adaptador_Tareas_Entregadas.OnPubliClickListen {

    val listatareasentregadas = mutableListOf<Usuario>()
    val adaptadortareasentregadas = Adaptador_Tareas_Entregadas(this, listatareasentregadas, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea_entregada)

        CargarLista()

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
    }

    fun CargarLista(){

        listatareasentregadas.add(Usuario("Javier","Hola, como estas", "https://cdn.icon-icons.com/icons2/2620/PNG/512/among_us_player_pink_icon_156938.png", "Hola@outloock.com", ""))
        listatareasentregadas.add(Usuario("El Rasho MacQueen la Machina mas veloz del mundo","Kuchau", "https://i.pinimg.com/236x/8c/5a/bb/8c5abb828846c4f963d84592197c6268.jpg", "Adios@outloock.com", ""))
        listatareasentregadas.add(Usuario("Oliver","Mis piernas !!!", "https://i.pinimg.com/236x/62/c6/7b/62c67bbea0fc21565df13b5b1998b56a.jpg", "Hola@outloock.com", ""))
        listatareasentregadas.add(Usuario("Benito","yipi yipi yipiy kejeje asdwajdjwa dwajdwadaw", "https://cdn.icon-icons.com/icons2/2620/PNG/512/among_us_player_pink_icon_156938.png", "Adios@outloock.com", ""))

    }
}