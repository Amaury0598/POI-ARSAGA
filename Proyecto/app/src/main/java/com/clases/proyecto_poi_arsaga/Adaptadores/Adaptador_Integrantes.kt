package com.clases.proyecto_poi_arsaga.Adaptadores

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R

class Adaptador_Integrantes(var listaIntegrantes: MutableList<Usuario>,
                            private val itemClickListener: OnItemGrupoClickListener) : RecyclerView.Adapter<Adaptador_Integrantes.IntegrantesViewHolder>() {

    class IntegrantesViewHolder(View: View): RecyclerView.ViewHolder(View){
    }

    interface OnItemGrupoClickListener{
        //fun onitemHold(toString: String)
        fun AddtoList(Nombre : String, position: String)
        fun RemoveFromList(NombreB: String, position: String)
    }

    override fun getItemCount(): Int {
        return listaIntegrantes.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegrantesViewHolder {

        val vistaIntegrantes = LayoutInflater.from(parent.context).inflate(R.layout.drawer_integrante, parent, false)
        return IntegrantesViewHolder(vistaIntegrantes)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: IntegrantesViewHolder, position: Int) {

        val CNombre = holder.itemView.findViewById<CheckBox>(R.id.Check_integrante)

        CNombre.text = listaIntegrantes[position].nombre

        CNombre.setOnClickListener {

            if(CNombre.isChecked) {
                itemClickListener.AddtoList(listaIntegrantes[position].nombre, listaIntegrantes[position].imagen)
            }else{
                itemClickListener.RemoveFromList(listaIntegrantes[position].nombre, listaIntegrantes[position].imagen)
            }
        }
    }
}