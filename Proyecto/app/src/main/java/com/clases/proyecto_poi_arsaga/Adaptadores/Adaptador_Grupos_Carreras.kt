package com.clases.proyecto_poi_arsaga.Adaptadores

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.General_Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class Adaptador_Grupos_Carreras(private val context: FragmentActivity?,
                                val listaChatsGrupos:  MutableList<Grupos>,
                                val listaChatsSubGrupos:  MutableList<Grupos>,
                                private val itemClickListener: OnGrupoClickListen) : RecyclerView.Adapter<Adaptador_Grupos_Carreras.Grupos_Carreras_ViewHolder>() {
    private val database = FirebaseDatabase.getInstance();
    private val grupoRef = database.getReference("Grupos")
    private val subGrupos = mutableListOf<Grupos>()
    private lateinit var contextAd:Context
    private lateinit var subGrupoSpinner : Spinner

    class Grupos_Carreras_ViewHolder(View: View) : RecyclerView.ViewHolder(View) {

    }

    interface OnGrupoClickListen {
        fun onitemHold(integrantes: Int)
        fun onitemClick(gpo: Grupos)
        fun onSpinnerClick(sub_gpo : Grupos)
    }

    override fun getItemCount(): Int {
        return listaChatsGrupos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Grupos_Carreras_ViewHolder {
        contextAd = parent.context
        subGrupos.clear()

        val vistagrupos_carreras = LayoutInflater.from(parent.context).inflate(R.layout.drawer_grupos_carreras, parent, false)
        return Grupos_Carreras_ViewHolder(vistagrupos_carreras)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Grupos_Carreras_ViewHolder, position: Int) {

        holder.itemView.setOnClickListener { itemClickListener.onitemClick((listaChatsGrupos[position])) }


        //holder.itemView.TV_nombre_grupos_carreras.setOnClickListener { itemClickListener.onitemHold(listaChatsGrupos[position].integrantes) }

        val CG_Nombre = holder.itemView.findViewById<TextView>(R.id.TV_nombre_grupos_carreras)
        val CG_Imagen = holder.itemView.findViewById<ImageView>(R.id.IMG_grupos_carreras)
        subGrupoSpinner = holder.itemView.findViewById<Spinner>(R.id.spinner4)
        //holder.itemView.setOnClickListener { itemClickListener.onSpinnerClick(subGrupoSpinner.selectedItem as Grupos) }


        subGrupoSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var gpo = parent!!.getItemAtPosition(position) as Grupos
                if(gpo.nombre != "Subgrupos") {
                    subGrupoSpinner.setSelection(0)
                    val intent = Intent(contextAd, General_Grupos::class.java)
                    intent.putExtra("Grupo", gpo)
                    contextAd?.startActivity(intent)
                }

            }

        }

        cargarSubgrupos(position)


        CG_Nombre.text = listaChatsGrupos[position].nombre
        Picasso.get().load(listaChatsGrupos[position].foto).into(CG_Imagen)

    }

    private fun cargarSubgrupos(position: Int){
        subGrupos.clear()
        subGrupos.add(Grupos("Subgrupos"))
        for(sb in listaChatsSubGrupos){
            if(sb.deGrupo == listaChatsGrupos[position].nombre)
                subGrupos.add(sb)
        }
        //if(context!=null)
        if(subGrupos.isNotEmpty()) {
            //val Grupo = Grupos("asd", subGrupos[0].correo_usuarios, "asd", "asd", "asd", "asd")
            //var stringList: List<Grupos> = listOf<Grupos>(Grupo)
            subGrupoSpinner.visibility = View.VISIBLE

            subGrupoSpinner.adapter = ArrayAdapter(contextAd, android.R.layout.simple_list_item_1, subGrupos.toList())

        }else{
            subGrupoSpinner.visibility = View.GONE
        }




    }
}