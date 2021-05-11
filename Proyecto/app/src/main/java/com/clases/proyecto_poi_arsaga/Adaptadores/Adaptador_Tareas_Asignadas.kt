package com.clases.proyecto_poi_arsaga.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Muro
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Ver_Tareas_Asignadas
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.General_Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Tareas
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chats_grupos.view.*
import org.w3c.dom.Text
import java.util.*

class Adaptador_Tareas_Asignadas(private val context: Frag_Ver_Tareas_Asignadas,
                                 var listaTareasAsignadas:  MutableList<Tareas>,
                                 private val itemClickListener: OnPubliClickListen) : RecyclerView.Adapter<Adaptador_Tareas_Asignadas.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){

    }

    interface OnPubliClickListen{
        //fun onitemHold(toString: String)
        fun onitemClick(tareaSeleccionada: Tareas)
    }

    override fun getItemCount(): Int {
        return listaTareasAsignadas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {
        val vistaTareas = LayoutInflater.from(parent.context).inflate(R.layout.drawer_tareas, parent, false)

        return TLosChatViewHolder(vistaTareas)
    }

    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        val Tarea_Nombre = holder.itemView.findViewById<TextView>(R.id.TV_tarea_nombre)
        val Tarea_Nombre_Grupo = holder.itemView.findViewById<TextView>(R.id.TV_tarea_nombre_grupo)
        val Tarea_Vencimiento = holder.itemView.findViewById<TextView>(R.id.TV_tarea_vencimiento)
        val Tarea_Img_Perfil = holder.itemView.findViewById<ImageView>(R.id.IMG_tareas_imagen)


        Tarea_Nombre.text = listaTareasAsignadas[position].nombre
        Tarea_Nombre_Grupo.text = General_Grupos.grupoActual.nombre
        Tarea_Vencimiento.text = listaTareasAsignadas[position].fecha
        Picasso.get().load(listaTareasAsignadas[position].imagen).into(Tarea_Img_Perfil)

        holder.itemView.setOnClickListener {
            itemClickListener.onitemClick(
                listaTareasAsignadas[position]
                )
        }

    }
}