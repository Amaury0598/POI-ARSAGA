package com.clases.proyecto_poi_arsaga.Adaptadores

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Estatus_tarea_grupo
import com.clases.proyecto_poi_arsaga.Modelos.TareaEntregada
import com.clases.proyecto_poi_arsaga.Modelos.Tareas
import com.clases.proyecto_poi_arsaga.R

class Adaptador_Estatus_tarea_grupo(private val Estatus: Int,
                                    private val context: Frag_Estatus_tarea_grupo,
                                    var listaTareasEstatus_tarea: MutableList<Tareas>,
                                    var listaTareasEntregadas_tarea: MutableList<TareaEntregada>,
                                    private val itemClickListener: OnPubliClickListen)
                                    :RecyclerView.Adapter<Adaptador_Estatus_tarea_grupo.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){

    }

    interface OnPubliClickListen{
        //fun onitemHold(toString: String)
        fun onitemClick(Nombre_Tarea: String, Nombre_Grupo: String)
    }

    override fun getItemCount(): Int {

        var Size = 1
        if(Estatus == 0) {
            Size = listaTareasEstatus_tarea.size
        }
        if(Estatus == 2) {
            Size = listaTareasEstatus_tarea.size
        }
        if(Estatus == 1) {
            Size = listaTareasEntregadas_tarea.size
        }
        return Size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {
        val vistaTareas = LayoutInflater.from(parent.context).inflate(R.layout.drawer_tareas, parent, false)

        return TLosChatViewHolder(vistaTareas)
    }

    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        val ETarea_Nombre_Grupo = holder.itemView.findViewById<TextView>(R.id.TV_tarea_nombre_grupo)
        val ETarea_Nombre_Tarea = holder.itemView.findViewById<TextView>(R.id.TV_tarea_nombre)
        val ETarea_Fecha = holder.itemView.findViewById<TextView>(R.id.TV_tarea_vencimiento)



        if(Estatus == 0){ //Estatus = ¿Ya entrego la tarea? coins = ¿Ya vencio?
            holder.itemView.setOnClickListener {
                itemClickListener.onitemClick(listaTareasEstatus_tarea[position].nombre, listaTareasEstatus_tarea[position].desc)
            }
            if(listaTareasEstatus_tarea[position].coins == 1) { //Pendientes
                ETarea_Nombre_Grupo.text = listaTareasEstatus_tarea[position].nombre
                ETarea_Nombre_Tarea.text = listaTareasEstatus_tarea[position].desc
                ETarea_Fecha.text = "Vencimiento " + listaTareasEstatus_tarea[position].fecha
                ETarea_Fecha.setTextColor(Color.parseColor("#2ECC71"))
            }
            else{
                val params = holder.itemView.layoutParams
                params.height = 0
                holder.itemView.layoutParams = params
            }
        }

        if(Estatus == 2){
            holder.itemView.setOnClickListener {
                itemClickListener.onitemClick(listaTareasEstatus_tarea[position].nombre, listaTareasEstatus_tarea[position].desc)
            }
            if(listaTareasEstatus_tarea[position].coins == 0){
                ETarea_Nombre_Grupo.text = listaTareasEstatus_tarea[position].nombre
                ETarea_Nombre_Tarea.text = listaTareasEstatus_tarea[position].desc
                ETarea_Fecha.text = "Vencimiento " + listaTareasEstatus_tarea[position].fecha
                ETarea_Fecha.setTextColor(Color.parseColor("#C70039"))
            } else {
                val params = holder.itemView.layoutParams
                params.height = 0
                holder.itemView.layoutParams = params
            }
        }

        if(Estatus == 1){
            holder.itemView.setOnClickListener {
                itemClickListener.onitemClick(listaTareasEstatus_tarea[position].nombre, listaTareasEstatus_tarea[position].desc)
            }
            if(listaTareasEntregadas_tarea[position].idTarea == "1") {
                ETarea_Nombre_Grupo.text = listaTareasEntregadas_tarea[position].nombre
                ETarea_Nombre_Tarea.text = listaTareasEntregadas_tarea[position].correo
                ETarea_Fecha.text = "Vencimiento " + listaTareasEntregadas_tarea[position].nombreArchivo
                ETarea_Fecha.setTextColor(Color.parseColor("#2ECC71"))
            }
        }
    }
}