package com.clases.proyecto_poi_arsaga.Adaptadores

import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.Modelos.Mensaje
import com.clases.proyecto_poi_arsaga.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.drawer_burbuja_archivos.view.*
import kotlinx.android.synthetic.main.drawer_burbuja_chat.view.LY_mensaje_burbuja
import kotlinx.android.synthetic.main.drawer_burbuja_imagen.view.*
import kotlinx.android.synthetic.main.drawer_burbuja_ubicacion.view.*
import java.util.*

private const val TipoMensaje = 0
private const val TipoUbicacion = 1
private const val TipoImagen = 2
private const val TipoArchivo = 3

private const val imagenUbicacion = "https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/chat%2Fubicacion.png?alt=media&token=58cbf69a-a9a2-4c78-81b8-872ade6bb59d"

class AdaptorChat(private val context: Chat_Grupal, private val listamensajes: MutableList<Mensaje>, private  val Tipo : Int, private val itemClickListener: AdaptorChat.OnPubliClickListen) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>()   {

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

    interface OnPubliClickListen{
        //fun onitemHold(toString: String)
        fun onitemClick(mensaje: Mensaje)
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

        when(listamensajes[position].tipoMensaje){
            "Texto" -> {
                TipoContenido = TipoMensaje
            }
            "Imagen" -> {
                TipoContenido = TipoImagen
            }
            "Archivo" -> {
                TipoContenido = TipoArchivo
            }
            "Ubicacion" -> {
                TipoContenido = TipoUbicacion
            }
        }

        //if(listamensajes[position].tipo == 0L) TipoContenido = TipoMensaje

        //if(listamensajes[position].tipo == 1L) TipoContenido = TipoUbicacion

        //if(listamensajes[position].tipo == 2L) TipoContenido = TipoImagen

        //if(listamensajes[position].tipo == 3L) TipoContenido = TipoArchivo

        return TipoContenido
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        lateinit var MensajeEnviado: TextView
        lateinit var MensajeHora: TextView
        lateinit var Vis_nombre: LinearLayout
        lateinit var QuienEnvio: TextView

        lateinit var ImagenMostrada: ImageView

        lateinit var contenedorParam: ViewGroup.LayoutParams
        when(getItemViewType(position)) {

            TipoMensaje -> {
                (holder as ChatMensaje).bind(listamensajes[position])
                MensajeEnviado = holder.itemView.findViewById(R.id.tv_contenido)
                MensajeHora = holder.itemView.findViewById<TextView>(R.id.tv_hora)
                Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.LY_Nombre)
                QuienEnvio = holder.itemView.findViewById<TextView>(R.id.tv_nombre)

                MensajeEnviado.text = listamensajes[position].mensaje
                contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams

                holder.itemView.setOnClickListener { itemClickListener.onitemClick(listamensajes[position]) }

                if (Tipo == 0) { // Es chat Grupo

                    if (listamensajes[position].esMio) {


                        Vis_nombre.visibility = (View.GONE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        Vis_nombre.visibility = (View.VISIBLE)

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

                    if (listamensajes[position].esMio) {


                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
                    }
                }

            }
            TipoUbicacion -> {
                (holder as ChatUbicacion).bind(listamensajes[position])
                MensajeEnviado = holder.itemView.findViewById(R.id.TV_Ubicacion)
                MensajeHora = holder.itemView.findViewById<TextView>(R.id.TV_Ubicacion_Hora)
                Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.Vis_Ubicacion_Nombre)
                QuienEnvio = holder.itemView.findViewById<TextView>(R.id.TV_Nombre_Usu_Ubicacion)
                ImagenMostrada = holder.itemView.findViewById(R.id.IMG_Mapa)
                MensajeEnviado.text = listamensajes[position].mensaje
                Picasso.get().load(imagenUbicacion).into(ImagenMostrada)
                holder.itemView.setOnClickListener { itemClickListener.onitemClick(listamensajes[position]) }

                //
                //
                //
                contenedorParam = holder.itemView.LY_Ubicacion_burbuja.layoutParams

                if (Tipo == 0) { // Es chat Grupo

                    if (listamensajes[position].esMio) {


                        Vis_nombre.visibility = (View.GONE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_Ubicacion_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        Vis_nombre.visibility = (View.VISIBLE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_Ubicacion_burbuja.layoutParams = nuevosParam
                        QuienEnvio.text = listamensajes[position].nombre
                    }
                } else { // Es chat en Privado

                    Vis_nombre.visibility = (View.GONE)

                    if (listamensajes[position].esMio) {


                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_Ubicacion_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_Ubicacion_burbuja.layoutParams = nuevosParam
                    }
                }
            }
            TipoImagen -> {
                (holder as ChatImagen).bind(listamensajes[position])
                ImagenMostrada = holder.itemView.findViewById(R.id.IMG_Chat_imagen)
                MensajeHora = holder.itemView.findViewById<TextView>(R.id.TV_Img_Hora)
                Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.Vis_Imagen_Nombre)
                QuienEnvio = holder.itemView.findViewById<TextView>(R.id.TV_Nombre_Usu_Imagen)

                holder.itemView.setOnClickListener { itemClickListener.onitemClick(listamensajes[position]) }

                Picasso.get().load(listamensajes[position].url).into(ImagenMostrada)
                contenedorParam = holder.itemView.LY_Imagen_burbuja.layoutParams

                if (Tipo == 0) { // Es chat Grupo

                    if (listamensajes[position].esMio) {


                        Vis_nombre.visibility = (View.GONE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_Imagen_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        Vis_nombre.visibility = (View.VISIBLE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_Imagen_burbuja.layoutParams = nuevosParam
                        QuienEnvio.text = listamensajes[position].nombre
                    }
                } else { // Es chat en Privado

                    Vis_nombre.visibility = (View.GONE)

                    if (listamensajes[position].esMio) {


                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_Imagen_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_Imagen_burbuja.layoutParams = nuevosParam
                    }
                }

            }
            TipoArchivo -> {
                (holder as ChatArchivo).bind(listamensajes[position])
                MensajeEnviado = holder.itemView.findViewById(R.id.TV_Nombre_Archivo)
                MensajeHora = holder.itemView.findViewById<TextView>(R.id.TV_Archivo_Hora)
                Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.Vis_Archivo_NombreUsu)
                QuienEnvio = holder.itemView.findViewById<TextView>(R.id.TV_Nombre_UsuAchivo)

                holder.itemView.setOnClickListener { itemClickListener.onitemClick(listamensajes[position]) }

                MensajeEnviado.text = listamensajes[position].mensaje
                contenedorParam = holder.itemView.LY_Archivo_burbuja.layoutParams

                if (Tipo == 0) { // Es chat Grupo

                    if (listamensajes[position].esMio) {


                        Vis_nombre.visibility = (View.GONE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_Archivo_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        Vis_nombre.visibility = (View.VISIBLE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_Archivo_burbuja.layoutParams = nuevosParam
                        QuienEnvio.text = listamensajes[position].nombre
                    }
                } else { // Es chat en Privado

                    Vis_nombre.visibility = (View.GONE)

                    if (listamensajes[position].esMio) {


                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_Archivo_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_Archivo_burbuja.layoutParams = nuevosParam
                    }
                }
            }
            else -> {
                (holder as ChatMensaje).bind(listamensajes[position])
                MensajeEnviado = holder.itemView.findViewById(R.id.tv_contenido)
                MensajeHora = holder.itemView.findViewById<TextView>(R.id.tv_hora)
                Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.LY_Nombre)
                QuienEnvio = holder.itemView.findViewById<TextView>(R.id.tv_nombre)

                holder.itemView.setOnClickListener { itemClickListener.onitemClick(listamensajes[position]) }

                MensajeEnviado.text = listamensajes[position].mensaje
                contenedorParam = holder.itemView.LY_mensaje_burbuja.layoutParams

                if (Tipo == 0) { // Es chat Grupo

                    if (listamensajes[position].esMio) {


                        Vis_nombre.visibility = (View.GONE)

                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        Vis_nombre.visibility = (View.VISIBLE)

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

                    if (listamensajes[position].esMio) {


                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.END
                        )
                        holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
                    } else {

                        //MensajeEnviado.gravity = Gravity.START
                        val nuevosParam = FrameLayout.LayoutParams(
                            contenedorParam.width,
                            contenedorParam.height,
                            Gravity.START
                        )
                        holder.itemView.LY_mensaje_burbuja.layoutParams = nuevosParam
                    }
                }
            }
        }

        //MensajeEnviado.text = listamensajes[position].mensaje
        val dateFormater = java.text.SimpleDateFormat("HH:mm a", Locale.getDefault())
        MensajeHora.text = dateFormater.format(Date(listamensajes[position].timeStamp as Long))
        QuienEnvio.text = listamensajes[position].nombre

        //val MensajeEnviado = holder.itemView.findViewById<TextView>(R.id.tv_contenido)
        //val MensajeHora = holder.itemView.findViewById<TextView>(R.id.tv_hora)
        //val Vis_nombre = holder.itemView.findViewById<LinearLayout>(R.id.LY_Nombre)
        //val QuienEnvio = holder.itemView.findViewById<TextView>(R.id.tv_nombre)
        //var Vis_div_nombre = holder.itemView.findViewById<View>(R.id.DIV_nombre)


        //val direcc_Ubicacion = holder.itemView.findViewById<TextView>(R.id.TV_Ubicacion)
        //val direcc_Nombre = holder.itemView.findViewById<TextView>(R.id)
        //val direcc_Hola = holder.itemView.findViewById<TextView>(R.id.TV_Ubicacion_Hora)




        //MensajeHora.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(listamensajes[position].hora)



    }

    override fun getItemCount(): Int {
        return listamensajes.size
    }
}