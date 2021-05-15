package com.clases.proyecto_poi_arsaga.Adaptadores

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Adaptador_Integrantes(var estatus : Int,
                            var listaIntegrantes: MutableList<Usuario>,
                            var listaIntegrantesActivos: MutableList<String>,
                            private val itemClickListener: OnItemGrupoClickListener) : RecyclerView.Adapter<Adaptador_Integrantes.IntegrantesViewHolder>() {

    class IntegrantesViewHolder(View: View): RecyclerView.ViewHolder(View){
    }

    interface OnItemGrupoClickListener{
        //fun onitemHold(toString: String)
        fun AddtoList(correo : String, position: String, estatus: Int)
        fun RemoveFromList(correo: String, position: String, estatus: Int)
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
        val CFoto = holder.itemView.findViewById<CircleImageView>(R.id.IV_integrante)
        val CCorreo = holder.itemView.findViewById<TextView>(R.id.TV_integrantes_correo)

        Picasso.get().load(listaIntegrantes[position].imagen).into(CFoto)
        if(listaIntegrantes[position].status)
            CFoto.borderColor = Color.parseColor("#29D63A")
        else
            CFoto.borderColor = Color.parseColor("#C1D9D5")

        if(listaIntegrantesActivos.contains(listaIntegrantes[position].correo)){
            CNombre.isChecked = true
            //CNombre.toggle()
        }

        CNombre.text = listaIntegrantes[position].nombre
        CCorreo.text = listaIntegrantes[position].correo

        CNombre.setOnClickListener {

            if(CNombre.isChecked) {
                itemClickListener.AddtoList(listaIntegrantes[position].correo, listaIntegrantes[position].imagen, estatus)
            }else{
                itemClickListener.RemoveFromList(listaIntegrantes[position].correo, listaIntegrantes[position].imagen, estatus)
            }
        }
    }
}