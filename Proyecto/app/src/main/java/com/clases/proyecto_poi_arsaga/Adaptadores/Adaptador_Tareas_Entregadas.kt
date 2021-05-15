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
import com.clases.proyecto_poi_arsaga.Modelos.TareaEntregada
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.clases.proyecto_poi_arsaga.Tareas_Entregadas
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chats_grupos.view.*
import java.util.*

class Adaptador_Tareas_Entregadas(private val context: Tareas_Entregadas,
                                  var listaTareasEntregadas:  MutableList<TareaEntregada>,
                                  private val itemClickListener: OnPubliClickListen) : RecyclerView.Adapter<Adaptador_Tareas_Entregadas.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){

    }

    interface OnPubliClickListen{
        //fun onitemHold(toString: String)
        fun onitemClick(tareaEnt: TareaEntregada)
    }

    override fun getItemCount(): Int {
        return listaTareasEntregadas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {
        val vistaTareas = LayoutInflater.from(context).inflate(R.layout.drawer_tarea_alumno, parent, false)

        return TLosChatViewHolder(vistaTareas)
    }

    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        val TareaA_Nombre = holder.itemView.findViewById<TextView>(R.id.TV_TA_nomre_alumno)
        val TareaA_Nombre_Grupo = holder.itemView.findViewById<TextView>(R.id.TV_TA_archivo)
        val TareaA_Img_Perfil = holder.itemView.findViewById<ImageView>(R.id.IMG_TE_TA_imagen)

        TareaA_Nombre.text = listaTareasEntregadas[position].nombre
        TareaA_Nombre_Grupo.text = listaTareasEntregadas[position].multimedia.nombreArchivo
        Picasso.get().load(listaTareasEntregadas[position].imagen).into(TareaA_Img_Perfil)

        holder.itemView.setOnClickListener {
            itemClickListener.onitemClick(
                    listaTareasEntregadas[position]
            )
        }

    }
}