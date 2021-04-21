package com.clases.proyecto_poi_arsaga.Adaptadores

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R

class Adaptador_Lista_Chats(private val context: Main_Frag, private val listaChatsGrupos:  MutableList<Grupos>) : RecyclerView.Adapter<Adaptador_Lista_Chats.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {

        val vistalista = LayoutInflater.from(parent.context).inflate(R.layout.chats_grupos, parent, false)
        return TLosChatViewHolder(vistalista)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        val CNombre = holder.itemView.findViewById<TextView>(R.id.TV_Nombre)
        val CMensaje = holder.itemView.findViewById<TextView>(R.id.TV_Contenido)
        val CHora = holder.itemView.findViewById<TextView>(R.id.TV_Hora)

        CNombre.text = listaChatsGrupos[position].nombre
        CMensaje.text = listaChatsGrupos[position].foto
        CHora.text = listaChatsGrupos[position].integrantes.toString()
    }

    override fun getItemCount(): Int {
        return listaChatsGrupos.size
    }
}