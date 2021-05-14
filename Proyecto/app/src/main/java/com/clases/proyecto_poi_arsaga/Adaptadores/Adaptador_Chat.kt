package com.clases.proyecto_poi_arsaga.Adaptadores

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
import com.clases.proyecto_poi_arsaga.Modelos.Mensaje
import com.clases.proyecto_poi_arsaga.R
import kotlinx.android.synthetic.main.drawer_burbuja_chat.view.*
import java.util.*

private const val TipoMensaje = 0
private const val TipoUbicacion = 1
private const val TipoImagen = 2
private const val TipoArchivo = 3

class AdaptorChat(private val context: Chat_Grupal, private val listamensajes: MutableList<Mensaje>, private  val Tipo : Int) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    class ChatMensaje(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(mensaje : Mensaje){

        }
    }

    class ChatUbicacion(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(mensaje : Mensaje){

        }
    }

    class ChatImagen(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(mensaje : Mensaje){

        }
    }

    class ChatArchivo(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(mensaje : Mensaje){

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        lateinit var  vistaschat : View

        when(viewType) {
            TipoMensaje -> {
                val vistaschat = LayoutInflater.from(context).inflate(R.layout.drawer_burbuja_chat, parent, false)
                return ChatMensaje(vistaschat)
            }
            TipoUbicacion -> {
                val vistaschat = LayoutInflater.from(context).inflate(R.layout.drawer_burbuja_ubicacion, parent, false)
                return ChatUbicacion(vistaschat)
            }
            TipoImagen -> {
                val vistaschat = LayoutInflater.from(context).inflate(R.layout.drawer_burbuja_imagen, parent, false)
                return ChatImagen(vistaschat)
            }
            TipoArchivo -> {
                val vistaschat = LayoutInflater.from(context).inflate(R.layout.drawer_burbuja_archivos, parent, false)
                return ChatArchivo(vistaschat)
            }

            else -> {
                return ChatMensaje(vistaschat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        var TipoContenido = 0

        //if(listamensajes[position].tipo == 0L) TipoContenido = TipoMensaje

        //if(listamensajes[position].tipo == 1L) TipoContenido = TipoUbicacion

        //if(listamensajes[position].tipo == 2L) TipoContenido = TipoImagen

        //if(listamensajes[position].tipo == 3L) TipoContenido = TipoArchivo

        return TipoContenido
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)) {

            TipoMensaje -> {
                (holder as ChatMensaje).bind(listamensajes[position])
            }
            TipoUbicacion -> {
                (holder as ChatUbicacion).bind(listamensajes[position])
            }
            TipoImagen -> {
                (holder as ChatImagen).bind(listamensajes[position])
            }
            TipoArchivo -> {
                (holder as ChatArchivo).bind(listamensajes[position])
            }
        }

        val MensajeEnviado = holder.itemView.findViewById<TextView>(R.id.tv_contenido)
        val MensajeHora = holder.itemView.findViewById<TextView>(R.id.tv_hora)
        val Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.LY_Nombre)
        val QuienEnvio = holder.itemView.findViewById<TextView>(R.id.tv_nombre)
        var Vis_div_nombre = holder.itemView.findViewById<View>(R.id.DIV_nombre)
        MensajeEnviado.text = listamensajes[position].mensaje
        val dateFormater = java.text.SimpleDateFormat("HH:mm a", Locale.getDefault())
        MensajeHora.text = dateFormater.format(Date(listamensajes[position].timeStamp as Long))

        val direcc_Ubicacion = holder.itemView.findViewById<TextView>(R.id.TV_Ubicacion)
        val direcc_Hola = holder.itemView.findViewById<TextView>(R.id.TV_Ubicacion_Hora)



        //MensajeHora.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(listamensajes[position].hora)

        if (Tipo == 0) { // Es chat Grupo

            if (listamensajes[position].esMio) {

                val contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams
                Vis_nombre.visibility = (View.GONE)
                Vis_div_nombre.visibility = (View.GONE)
                val nuevosParam = FrameLayout.LayoutParams(
                        contenedorParam.width,
                        contenedorParam.height,
                        Gravity.END
                )
                holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
            } else {

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
                QuienEnvio.text = listamensajes[position].nombre
            }
        } else { // Es chat en Privado

            Vis_nombre.visibility = (View.GONE)
            Vis_div_nombre.visibility = (View.GONE)

            if (listamensajes[position].esMio) {

                val contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams
                val nuevosParam = FrameLayout.LayoutParams(
                        contenedorParam.width,
                        contenedorParam.height,
                        Gravity.END
                )
                holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
            } else {

                val contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams
                MensajeEnviado.gravity = Gravity.START
                val nuevosParam = FrameLayout.LayoutParams(
                        contenedorParam.width,
                        contenedorParam.height,
                        Gravity.START
                )
                holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
            }
        }

    }

    override fun getItemCount(): Int {
        return listamensajes.size
    }
}