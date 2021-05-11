package com.clases.proyecto_poi_arsaga.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Muro
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chats_grupos.view.*
import java.util.*

class Adaptador_Muro_General(private val context: Frag_Muro,
                             var listaPublicacion:  MutableList<Usuario>,
                             private val itemClickListener: OnPubliClickListen) : RecyclerView.Adapter<Adaptador_Muro_General.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){

    }

    interface OnPubliClickListen{
        //fun onitemHold(toString: String)
        //fun onitemClick(usuario: ChatDirecto)
    }

    override fun getItemCount(): Int {
        return listaPublicacion.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {
        val vistaMuro = LayoutInflater.from(parent.context).inflate(R.layout.drawer_muro_publicaciones, parent, false)

        return TLosChatViewHolder(vistaMuro)
    }

    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        val Muro_Nombre = holder.itemView.findViewById<TextView>(R.id.TV_Muro_nombre)
        val Muro_Correo = holder.itemView.findViewById<TextView>(R.id.TV_Muro_correo)
        val Muro_Contenido = holder.itemView.findViewById<TextView>(R.id.TV_Muro_Conenido)
        val Muro_Img_Perfil = holder.itemView.findViewById<ImageView>(R.id.IMG_Muro_imagen)

        Muro_Nombre.text = listaPublicacion[position].nombre
        Muro_Correo.text = listaPublicacion[position].desc
        Muro_Contenido.text = listaPublicacion[position].correo
        Picasso.get().load(listaPublicacion[position].imagen).into(Muro_Img_Perfil)

    }
}