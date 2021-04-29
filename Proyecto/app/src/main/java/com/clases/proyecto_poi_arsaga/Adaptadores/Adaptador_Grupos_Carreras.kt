package com.clases.proyecto_poi_arsaga.Adaptadores

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Grupos_Carreras
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chats_grupos.view.*
import kotlinx.android.synthetic.main.drawer_grupos_carreras.view.*

class Adaptador_Grupos_Carreras (private val context: Frag_Grupos_Carreras,
                                 val listaChatsGrupos:  MutableList<Grupos>,
                                 private val itemClickListener: OnGrupoClickListen) : RecyclerView.Adapter<Adaptador_Grupos_Carreras.Grupos_Carreras_ViewHolder>() {

    class Grupos_Carreras_ViewHolder(View: View) : RecyclerView.ViewHolder(View) {

    }

    interface OnGrupoClickListen {
        fun onitemHold(integrantes: Int)
        fun onitemClick(nombre: String)
    }

    override fun getItemCount(): Int {
        return listaChatsGrupos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Grupos_Carreras_ViewHolder {

        val vistagrupos_carreras = LayoutInflater.from(parent.context).inflate(R.layout.drawer_grupos_carreras, parent, false)
        return Grupos_Carreras_ViewHolder(vistagrupos_carreras)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Grupos_Carreras_ViewHolder, position: Int) {

        holder.itemView.setOnClickListener { itemClickListener.onitemClick((listaChatsGrupos[position].nombre)) }
        //holder.itemView.TV_nombre_grupos_carreras.setOnClickListener { itemClickListener.onitemHold(listaChatsGrupos[position].integrantes) }

        val CG_Nombre = holder.itemView.findViewById<TextView>(R.id.TV_nombre_grupos_carreras)
        val CG_Imagen = holder.itemView.findViewById<ImageView>(R.id.IMG_grupos_carreras)

        CG_Nombre.text = listaChatsGrupos[position].nombre
        Picasso.get().load(listaChatsGrupos[position].foto).into(CG_Imagen)

    }
}