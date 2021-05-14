package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Muro_General
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.General_Grupos
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.clases.proyecto_poi_arsaga.R

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import java.util.*

class Frag_Muro : Fragment(), Adaptador_Muro_General.OnPubliClickListen {

    val listaPublicacion = mutableListOf<Publicaciones>()
    val adaptadorMuro = Adaptador_Muro_General(this, listaPublicacion,this)
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private lateinit var userActual:Usuario
    private val userRef = database.getReference("Usuarios")
    private val publicacionesRef = database.getReference("Publicaciones")
    private val gruposRef = database.getReference("Grupos")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        CargarLista()
        val miViewGeneral =  inflater.inflate(R.layout.fragmento_muro_grupos, container, false)
        val CrearNueva_Publi = miViewGeneral.findViewById<FloatingActionButton>(R.id.FAB_crearpublicacion)

        val animation = AnimationUtils.loadAnimation(context, R.anim.dialog_itegrantes)
        val animation2 = AnimationUtils.loadAnimation(context, R.anim.dialog_itegrantes_2)
        val animationout = AnimationUtils.loadAnimation(context, R.anim.dialog_integrantes_out)

        val MuroRecycler = miViewGeneral.findViewById<RecyclerView>(R.id.Recy_Muro)
        val milinear = LinearLayoutManager(context)

        MuroRecycler.layoutManager = milinear
        MuroRecycler.adapter = adaptadorMuro

        CrearNueva_Publi.setOnClickListener {

            val DialogView = inflater.inflate(R.layout.dialog_publicacion, null)
            val Dialog_Pucli = AlertDialog.Builder(requireActivity()).setView(DialogView)


            val RDialog = DialogView.findViewById<RelativeLayout>(R.id.Dialog_publicacion)
            val ContentEdit = DialogView.findViewById<TextInputLayout>(R.id.Content_Edit)
            val Aceptar = DialogView.findViewById<Button>(R.id.BTN_Crear_Public)

            val mensajePublicacion = DialogView.findViewById<TextInputEditText>(R.id.TV_publicacion)


            ContentEdit.startAnimation(animation)
            RDialog.startAnimation(animation2)
            Aceptar.startAnimation(animation2)
            val AlertD = Dialog_Pucli.show()
            AlertD.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

            Aceptar.setOnClickListener {
                var pMensaje = ""
                pMensaje = mensajePublicacion.text.toString()
                if (pMensaje.isEmpty()) {
                    mensajePublicacion.error = "Escribe el mensaje"
                    mensajePublicacion.requestFocus()
                    return@setOnClickListener
                }
                var publicacion = Publicaciones(General_Grupos.grupoActual.nombre, userActual.correo, userActual.imagen, userActual.nombre, "", pMensaje)

                var nuevaPublicacion = publicacionesRef.push()
                publicacion.id = nuevaPublicacion.key.toString()
                nuevaPublicacion.setValue(publicacion)
                        .addOnSuccessListener {
                            ContentEdit.startAnimation(animationout)
                            RDialog.startAnimation(animationout)
                            Aceptar.startAnimation(animationout)
                            AlertD.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                        }


            }
        }

        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{
                    userActual = it.getValue(Usuario::class.java) as Usuario


                }

        return miViewGeneral
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun CargarLista(){
        publicacionesRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    listaPublicacion.clear()
                    for (p in snapshot.children){
                        val publicacion = p.getValue(Publicaciones::class.java) as Publicaciones
                        if(publicacion.grupo == General_Grupos.grupoActual.nombre){
                            listaPublicacion.add(publicacion)
                        }
                    }
                    adaptadorMuro.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onitemClick(publicacion: Publicaciones) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        val chatDirecto=ChatDirecto()
        chatDirecto.id = publicacion.id
        chatDirecto.usuario1 = publicacion.mensajePublicacion
        chatDirecto.fotoUsuario1 = General_Grupos.grupoActual.foto
        chatDirecto.usuario2 = "Grupal"
        intent.putExtra("userActual", userActual)
        intent.putExtra( "Tipo", 0)
        intent.putExtra("ChatDirecto", chatDirecto)
        activity?.startActivity(intent)
    }
}