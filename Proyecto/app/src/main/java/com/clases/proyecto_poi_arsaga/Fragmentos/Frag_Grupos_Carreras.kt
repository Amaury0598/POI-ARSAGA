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
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Frag_Grupos_Carreras : Fragment(), Adaptador_Grupos_Carreras.OnGrupoClickListen {

    val listaGrupos = mutableListOf<Grupos>()
    val adaptador_grupos_carreras = Adaptador_Grupos_Carreras(this, listaGrupos, this)
    private val database = FirebaseDatabase.getInstance();
    private val gruposRef = database.getReference("Grupos"); //crear "rama" (tabla)
    var userActual: Usuario? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userActual = arguments?.getSerializable("userActual") as Usuario




        val miViewGrupos =  inflater.inflate(R.layout.frag_grupos_carreras, container, false)

        val nogrupos = miViewGrupos.findViewById<TextView>(R.id.TV_nogrupos)
        nogrupos.text = "Unete a mas grupos, es genial !!"
        if (listaGrupos.size < 3) nogrupos.visibility = (View.VISIBLE)
        else nogrupos.visibility = (View.GONE)

        var recycler_lista = miViewGrupos.findViewById<RecyclerView>(R.id.RV_grupos_carreras)
        val miGrid = GridLayoutManager(context, 2)
        recycler_lista.layoutManager = miGrid
        recycler_lista.adapter = adaptador_grupos_carreras
        cargarLista()
        return miViewGrupos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun cargarLista(){

        gruposRef.child("hola").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot!!.exists()){
                    listaGrupos.clear()
                    for( g in snapshot.children){
                        val grupo: Grupos = g.getValue(Grupos::class.java) as Grupos;
                        for(cu in grupo.correo_usuarios!!) {
                            if (cu == userActual!!.correo) {
                                listaGrupos.add(grupo)
                                break
                            }
                        }
                    }
                    adaptador_grupos_carreras.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onitemHold(integrantes: Int) {
        Toast.makeText(activity, "Numero de integrantes $integrantes", Toast.LENGTH_SHORT).show()
    }

    override fun onitemClick(gpo: Grupos) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        val chatDirecto=ChatDirecto()
        chatDirecto.id = gpo.nombre
        chatDirecto.usuario1 = gpo.nombre
        chatDirecto.fotoUsuario1 = gpo.foto
        chatDirecto.usuario2 = "Grupal"
        intent.putExtra("userActual", userActual)
        intent.putExtra( "Tipo", 0)
        intent.putExtra("ChatDirecto", chatDirecto)
        activity?.startActivity(intent)
    }
}