package com.clases.proyecto_poi_arsaga.Adaptadores

import android.icu.text.SimpleDateFormat
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.R
import kotlinx.android.synthetic.main.chats_grupos.view.*
import java.util.*

class Adaptador_Lista_Chats(private val context: Main_Frag,
                            val listaChatsGrupos:  MutableList<ChatMensaje>,
                            private val itemClickListener: OnGrupoClickListen) : RecyclerView.Adapter<Adaptador_Lista_Chats.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){

    }

    interface OnGrupoClickListen{
        fun onitemHold(toString: String)
        fun onitemClick(usuario: String)
    }

    override fun getItemCount(): Int {
        return listaChatsGrupos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {

        val vistalista = LayoutInflater.from(parent.context).inflate(R.layout.chats_grupos, parent, false)
        return TLosChatViewHolder(vistalista)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        holder.itemView.setOnClickListener { itemClickListener.onitemClick(listaChatsGrupos[position].usuario) }
        holder.itemView.IMG_grupo.setOnClickListener { itemClickListener.onitemHold(listaChatsGrupos[position].id.toString()) }

        val CNombre = holder.itemView.findViewById<TextView>(R.id.TV_Nombre)
        val CMensaje = holder.itemView.findViewById<TextView>(R.id.TV_Contenido)
        val CHora = holder.itemView.findViewById<TextView>(R.id.TV_Hora)
        val Cseen = holder.itemView.findViewById<ImageView>(R.id.IMG_seen)

        CNombre.text = listaChatsGrupos[position].usuario
        CMensaje.text = listaChatsGrupos[position].mensaje
        CHora.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(listaChatsGrupos[position].horaEnvio)

        if(listaChatsGrupos[position].visto) Cseen.visibility = (View.VISIBLE)
        else Cseen.visibility = (View.GONE)
    }
}