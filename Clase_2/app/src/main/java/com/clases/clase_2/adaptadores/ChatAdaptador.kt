package com.clases.clase_2.adaptadores

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.clase_2.R

class ChatAdaptador:RecyclerView.Adapter<ChatAdaptador.ChatViewHolder>() {

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val myView = LayoutInflater.from(parent.context)
            .inflate(R.layout.burbuja_mensajes, parent, false)
        return ChatViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val tvNombre = holder.itemView.findViewById<TextView>(R.id.tv_Nombre)
        val tvHora = holder.itemView.findViewById<TextView>(R.id.tv_Hora)
        val tvMensaje = holder.itemView.findViewById<TextView>(R.id.tv_Mensaje)
        val tvGravity = holder.itemView.findViewById<LinearLayout>(R.id.msg_Layout)

        tvNombre.text = when(position) {
            0 -> "Juan Manuel"
            1 -> "Jesus Angel"
            2 -> "León Marcos"
            3 -> "Julio Mango"
            else -> "Desconocido"
        }

        tvHora.text = when(position){
            0 -> "22:30 pm"
            1 -> "22:33 pm"
            2 -> "22:35 pm"
            else -> "22:37 pm"
        }
        tvMensaje.text = when(position){
            0 -> "Hola, buen día, ¿Cómo le va? lalalalal alalalallal alalalalall alalalalall alalalalla alalalal"
            1 -> "Hola, buen día, ¿Cómo le va? lalalallalalallaalallalalalaalalalalalallalalalaalalalal"
            2 -> "Hola, buen día, ¿Cómo le va?"
            else -> "No lo se, muchas gracias"
        }

        tvGravity.gravity = when(position){
            0 -> Gravity.END
            1 -> Gravity.START
            2 -> Gravity.END
            3 -> Gravity.START
            else -> Gravity.START
        }
    }
}