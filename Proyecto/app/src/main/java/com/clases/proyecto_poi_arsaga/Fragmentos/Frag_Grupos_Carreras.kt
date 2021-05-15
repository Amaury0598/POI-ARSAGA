package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Grupos_Carreras
import com.clases.proyecto_poi_arsaga.Crear_Grupo
import com.clases.proyecto_poi_arsaga.General_Grupos
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.clases.proyecto_poi_arsaga.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Frag_Grupos_Carreras : Fragment(), Adaptador_Grupos_Carreras.OnGrupoClickListen {

    private val database = FirebaseDatabase.getInstance();
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private val auth = FirebaseAuth.getInstance()
    val listaGrupos = mutableListOf<Grupos>()
    val listaSubGrupos = mutableListOf<Grupos>()
    val adaptador_grupos_carreras = Adaptador_Grupos_Carreras(activity, listaGrupos, listaSubGrupos, this)
    private val gruposRef = database.getReference("Grupos"); //crear "rama" (tabla)
    private lateinit var loading:LoadingDialogFragment
    private lateinit var nogrupos : TextView
    private lateinit var userActual: Usuario

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //userActual = arguments?.getSerializable("userActual") as Usuario
        loading = LoadingDialogFragment(this)
        loading.startLoading("Cargando Grupos")

        val miViewGrupos =  inflater.inflate(R.layout.frag_grupos_carreras, container, false)

        nogrupos = miViewGrupos.findViewById<TextView>(R.id.TV_nogrupos)
        val btnCrearGrupo = miViewGrupos.findViewById<FloatingActionButton>(R.id.FAB_crearGrupo)



        var recycler_lista = miViewGrupos.findViewById<RecyclerView>(R.id.RV_grupos_carreras)
        val miGrid = GridLayoutManager(context, 2)
        recycler_lista.layoutManager = miGrid
        recycler_lista.adapter = adaptador_grupos_carreras


        btnCrearGrupo.setOnClickListener {
            val intent = Intent(activity, Crear_Grupo::class.java)
            activity?.startActivity(intent)
        }

        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(userActual.encriptado)
                        userActual.desencriptarUsuario()
                    cargarLista()
                }

        return miViewGrupos
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun cargarLista(){

        gruposRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot!!.exists()){
                    listaGrupos.clear()
                    listaSubGrupos.clear()
                    for( g in snapshot.children){
                        val grupo: Grupos = g.getValue(Grupos::class.java) as Grupos;
                        if(grupo.deGrupo.isEmpty()) {
                            for (cu in grupo.correo_usuarios!!) {
                                if (cu == userActual!!.correo) {
                                    listaGrupos.add(grupo)
                                    break
                                }

                            }
                        }else{
                            for (cu in grupo.correo_usuarios!!) {
                                if (cu == userActual!!.correo) {
                                    listaSubGrupos.add(grupo)
                                    break
                                }

                            }
                        }

                    }
                    adaptador_grupos_carreras.notifyDataSetChanged()
                    nogrupos.text = "Únete a mas Grupos es Genial !!!"
                    if (listaGrupos.size < 3)nogrupos.visibility = (View.VISIBLE)
                    else nogrupos.visibility = (View.GONE)
                    loading.isDismiss()
                }
            }
        })
    }

    override fun onitemHold(integrantes: Int) {
        Toast.makeText(activity, "Numero de integrantes $integrantes", Toast.LENGTH_SHORT).show()
    }

    override fun onSpinnerClick(sub_gpo: Grupos) {
        Toast.makeText(activity, sub_gpo.nombre, Toast.LENGTH_SHORT).show()
    }

    override fun onitemClick(gpo: Grupos) {

        val intent = Intent(activity, General_Grupos::class.java)
        intent.putExtra("Grupo", gpo)
        activity?.startActivity(intent)
        /*val intent = Intent(activity, Chat_Grupal::class.java)
        val chatDirecto=ChatDirecto()
        chatDirecto.id = gpo.nombre
        chatDirecto.usuario1 = gpo.nombre
        chatDirecto.fotoUsuario1 = gpo.foto
        chatDirecto.usuario2 = "Grupal"
        intent.putExtra("userActual", userActual)
        intent.putExtra( "Tipo", 0)
        intent.putExtra("ChatDirecto", chatDirecto)
        activity?.startActivity(intent)*/
    }
}