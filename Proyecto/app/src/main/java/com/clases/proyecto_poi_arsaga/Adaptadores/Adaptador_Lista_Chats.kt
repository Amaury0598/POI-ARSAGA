package com.clases.proyecto_poi_arsaga.Adaptadores

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.chats_grupos.view.*
import java.util.*

class Adaptador_Lista_Chats(private val context: Main_Frag,
                            var listaChatsGrupos:  MutableList<ChatDirecto>,
                            private val itemClickListener: OnGrupoClickListen) : RecyclerView.Adapter<Adaptador_Lista_Chats.TLosChatViewHolder>() {

    class TLosChatViewHolder(View: View): RecyclerView.ViewHolder(View){

    }

    interface OnGrupoClickListen{
        //fun onitemHold(toString: String)
        fun onitemClick(usuario: ChatDirecto)
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
        //listaChatsGrupos = listaChatsGrupos.asReversed()
        holder.itemView.setOnClickListener { itemClickListener.onitemClick(listaChatsGrupos[position]) }
        //holder.itemView.IMG_grupo.setOnClickListener { itemClickListener.onitemHold(listaChatsGrupos[position].id.toString()) }

        val CNombre = holder.itemView.findViewById<TextView>(R.id.TV_Nombre)
        val CMensaje = holder.itemView.findViewById<TextView>(R.id.TV_Contenido)
        val CHora = holder.itemView.findViewById<TextView>(R.id.TV_Hora)
        val CFoto = holder.itemView.findViewById<CircleImageView>(R.id.IMG_Muro_imagen)
        val Cseen = holder.itemView.findViewById<ImageView>(R.id.IMG_seen)

        var user : String = ""
        var fotoUser : String = ""
        var ultimoMensaje : String = ""
        var status : Boolean = false
        if(listaChatsGrupos[position].usuario1 == Main_Frag.userActual.correo) {
            user = listaChatsGrupos[position].nombre2
            status = listaChatsGrupos[position].status2
            fotoUser = listaChatsGrupos[position].fotoUsuario2
        }else{
            user = listaChatsGrupos[position].nombre1
            status = listaChatsGrupos[position].status1
            fotoUser = listaChatsGrupos[position].fotoUsuario1
        }

        if(listaChatsGrupos[position].ultimoMensajeDe ==  Main_Frag.userActual.correo)
            ultimoMensaje = "tu: " + listaChatsGrupos[position].ultimoMensaje
        else
            ultimoMensaje = listaChatsGrupos[position].ultimoMensaje

        CNombre.text = user
        CMensaje.text = ultimoMensaje
        CHora.text = SimpleDateFormat("hh:mm a\ndd/MM/YY", Locale.getDefault()).format(listaChatsGrupos[position].timeStamp)
        Picasso.get().load(fotoUser).into(CFoto)
        if(status)
            CFoto.borderColor = Color.parseColor("#29D63A")
        else
            CFoto.borderColor = Color.parseColor("#C1D9D5")

        /*if(listaChatsGrupos[position].visto) Cseen.visibility = (View.VISIBLE)
        else Cseen.visibility = (View.GONE)*/
    }
}