package com.clases.proyecto_poi_arsaga

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.clases.proyecto_poi_arsaga.Adaptadores.AdaptorChat
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import kotlinx.android.synthetic.main.drawe_modperfil.*
import java.util.*

class Chat_Grupal : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")

    val listamensajes = mutableListOf<Mensaje>()
    var TipoC: Int = 0
    private var userActual: Usuario = Usuario()
    //var usuarioSeleccionado: Usuario? = null
    var ChatDirecto: ChatDirecto? = null
    private var adaptadorChat = AdaptorChat(this, listamensajes, TipoC)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__grupal)

        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener {
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(intent.extras != null){
                        //usuarioSeleccionado = intent.getSerializableExtra("UsuarioSeleccionado") as Usuario
                        //userActual = intent.getSerializableExtra("userActual") as Usuario
                        ChatDirecto = intent.getSerializableExtra("ChatDirecto") as ChatDirecto
                        if(ChatDirecto!!.usuario2 == "Grupal") {
                            TV_Nombre_Chat.text = ChatDirecto!!.usuario1

                            Picasso.get().load(ChatDirecto!!.fotoUsuario1).into(IV_CHAT_HEADER)
                        }
                        else {
                            if (ChatDirecto!!.usuario1 == userActual!!.correo) {
                                TV_Nombre_Chat.text = ChatDirecto!!.nombre2
                                Picasso.get().load(ChatDirecto!!.fotoUsuario2).into(IV_CHAT_HEADER)
                            }
                            else {
                                TV_Nombre_Chat.text = ChatDirecto!!.nombre1
                                Picasso.get().load(ChatDirecto!!.fotoUsuario1).into(IV_CHAT_HEADER)
                            }
                        }
                        TipoC = intent.getIntExtra("Tipo", 0)



                    }else{
                        TV_Nombre_Chat.text = "Desconocido"
                    }
                    adaptadorChat = AdaptorChat(this, listamensajes, TipoC)
                    RV_chat_grupal.adapter = adaptadorChat

                    cargarMensajes()
                }







        BT_back_chats_grupal.setOnClickListener {
            /*val intent = Intent(this@Chat_Grupal, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("userActual", userActual)
            startActivity(intent)*/
            finish()
        }

        BT_enviar_mensaje.setOnClickListener {
            val mensajeEscrito = TX_mensaje.text.toString()
            if(mensajeEscrito.isEmpty()){
                //Toast.makeText(this, "Mensaje en blanco", Toast.LENGTH_SHORT).show()
            }
            else{
                //val chatmensaje = ChatMensaje("Juan", mensajeEscrito, Date(), false, 0, true)
                /*val chatRespuesta = ChatMensaje("Lucas", "No estoy", Date(), false, 0, false)
                listamensajes.add(chatRespuesta)*/

                val chatmensaje = Mensaje(userActual!!.correo, userActual!!.nombre, false, ServerValue.TIMESTAMP, mensajeEscrito)
                agregarMensaje(chatmensaje)




            }
        }

    }

    private fun cargarMensajes(){
        var cargarMensajeRef = database.getReference().child("ChatLog").child(ChatDirecto!!.id)


        cargarMensajeRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    listamensajes.clear()
                    for( m in snapshot.children){
                        val mensaje = m.getValue(Mensaje::class.java) as Mensaje;
                        if(mensaje.de == userActual!!.correo)
                            mensaje.esMio = true
                        listamensajes.add(mensaje)
                    }

                    adaptadorChat.notifyDataSetChanged()
                    RV_chat_grupal.scrollToPosition(listamensajes.size - 1)

                }

            }




        })



    }

    private fun agregarMensaje(mensaje: Mensaje){
        database.getReference().child("ChatLog").child(ChatDirecto!!.id).push().setValue(mensaje)
        if(ChatDirecto!!.usuario2 != "Grupal") {
            ChatDirecto!!.timeStamp = mensaje.timeStamp
            ChatDirecto!!.ultimoMensajeDe = userActual!!.correo
            ChatDirecto!!.ultimoMensaje = mensaje.mensaje

            database.getReference().child("ChatDirecto").child(ChatDirecto!!.id).setValue(ChatDirecto)
        }
        TX_mensaje.text.clear()

    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Chat_Grupal, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        //intent.putExtra("userActual", userActual)
        startActivity(intent)
    }*/
}