package com.clases.proyecto_poi_arsaga.Adaptadores

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.Modelos.Mensaje
import com.clases.proyecto_poi_arsaga.R
import kotlinx.android.synthetic.main.drawer_burbuja_chat.view.*
import java.util.*

class AdaptorChat(private val userActual: String, private val context: Chat_Grupal, private val listamensajes: MutableList<Mensaje>) : RecyclerView.Adapter<AdaptorChat.ChatViewHolder>()  {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
       val vistaschat = LayoutInflater.from(context)
           .inflate(R.layout.drawer_burbuja_chat, parent, false)
        return ChatViewHolder(vistaschat)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {


        val MensajeEnviado = holder.itemView.findViewById<TextView>(R.id.tv_contenido)
        val MensajeHora = holder.itemView.findViewById<TextView>(R.id.tv_hora)
        val Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.LY_Nombre)
        var Vis_div_nombre = holder.itemView.findViewById<View>(R.id.DIV_nombre)
        MensajeEnviado.text = listamensajes[position].mensaje
        MensajeHora.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(listamensajes[position].hora)

        if(listamensajes[position].esMio) {

            val contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams
            Vis_nombre.visibility = (View.GONE)
            Vis_div_nombre.visibility = (View.GONE)
            val nuevosParam = FrameLayout.LayoutParams(
                contenedorParam.width,
                contenedorParam.height,
                Gravity.END
            )


            holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
        }
        else {

            val contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams
            MensajeEnviado.gravity = Gravity.START
            Vis_nombre.visibility = (View.VISIBLE)
            Vis_div_nombre.visibility = (View.VISIBLE)
            val nuevosParam = FrameLayout.LayoutParams(
                contenedorParam.width,
                contenedorParam.height,
                Gravity.START
            )
            holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
        }


    }

    override fun getItemCount(): Int {
        return listamensajes.size
    }
}