package com.clases.clase_2.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clases.clase_2.R

class TodosLos_ChatAdaptador:RecyclerView.Adapter<TodosLos_ChatAdaptador.TLosChatViewHolder>() {

    class TLosChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLosChatViewHolder {
        val myView2 = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat,parent,false)
        return TLosChatViewHolder(myView2)
    }

    override fun onBindViewHolder(holder: TLosChatViewHolder, position: Int) {

        val CNombre = holder.itemView.findViewById<TextView>(R.id.tvNombre_C)
        val CMensaje = holder.itemView.findViewById<TextView>(R.id.tv_Mensaje_C)
        val CHora = holder.itemView.findViewById<TextView>(R.id.tv_Hora_C)

        CNombre.text = when(position){
            0 -> "Juan Manuel"
            1 -> "Jesus Angel"
            2 -> "León Marcos"
            3 -> "Julio Mango"
            else -> "Desconocido"
        }
        CMensaje.text = when(position){
            0 -> "Hola, buen día, ¿Cómo le va?"
            1 -> "Hola, buen día, ¿Cómo le va? lalalal lalalalla alallalal alalalall alalalal allaa"
            2 -> "Hola, buen día, ¿Cómo le va lalalalalalallalalalallaa?"
            else -> "No lo se, muchas gracias"
        }
        CHora.text = when(position){
            0 -> "22:30"
            1 -> "22:35"
            2 -> "22:40"
            else -> "22:55"
        }
    }
}