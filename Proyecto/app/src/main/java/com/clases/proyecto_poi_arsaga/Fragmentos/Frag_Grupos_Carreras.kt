package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Grupos_Carreras
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.R

class Frag_Grupos_Carreras : Fragment(), Adaptador_Grupos_Carreras.OnGrupoClickListen {

    val listaGrupos = mutableListOf<Grupos>()
    val adaptador_grupos_carreras = Adaptador_Grupos_Carreras(this, listaGrupos, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cargarLista()



        val miViewGrupos =  inflater.inflate(R.layout.frag_grupos_carreras, container, false)

        val nogrupos = miViewGrupos.findViewById<TextView>(R.id.TV_nogrupos)
        nogrupos.text = "Unete a mas grupos es genial !!"
        if (listaGrupos.size < 3) nogrupos.visibility = (View.VISIBLE)
        else nogrupos.visibility = (View.GONE)

        var recycler_lista = miViewGrupos.findViewById<RecyclerView>(R.id.RV_grupos_carreras)
        val miGrid = GridLayoutManager(context, 2)
        recycler_lista.layoutManager = miGrid
        recycler_lista.adapter = adaptador_grupos_carreras

        return miViewGrupos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun cargarLista(){

        listaGrupos.add(Grupos("LMAD", 5, "https://image.flaticon.com/icons/png/512/1465/1465606.png", 0))
        listaGrupos.add(Grupos("LCC", 3, "https://images-na.ssl-images-amazon.com/images/I/31gpv-ZU4vL.png", 1))
        listaGrupos.add(Grupos("LM", 10, "https://icons-for-free.com/iconfiles/png/512/math+tutor+icon-1320195955563127435.png", 2))
        listaGrupos.add(Grupos("LF", 5, "https://image.flaticon.com/icons/png/512/746/746960.png", 3))
        listaGrupos.add(Grupos("LA", 8, "https://www.highmeadowschool.org/wp-content/uploads/2016/11/abacus-icon.png", 4))
        listaGrupos.add(Grupos("LSTI", 3, "https://img.icons8.com/bubbles/452/cyber-security.png", 5))

        adaptador_grupos_carreras.notifyDataSetChanged()

    }

    override fun onitemHold(integrantes: Int) {
        Toast.makeText(activity, "Numero de integrantes $integrantes", Toast.LENGTH_SHORT).show()
    }

    override fun onitemClick(nombre: String) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        intent.putExtra("Nombre", nombre)
        activity?.startActivity(intent)
    }
}