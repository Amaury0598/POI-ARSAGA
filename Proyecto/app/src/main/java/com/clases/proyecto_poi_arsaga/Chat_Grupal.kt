package com.clases.proyecto_poi_arsaga

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.clases.proyecto_poi_arsaga.Adaptadores.AdaptorChat
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import java.util.*

class Chat_Grupal : AppCompatActivity() {


    val listamensajes = mutableListOf<Mensaje>()
    var TipoC: Int = 0
    var userActual: Usuario? = null
    var idChatDirecto: String = ""
    private var adaptadorChat = AdaptorChat(userActual,this, listamensajes, TipoC)
    private val database = FirebaseDatabase.getInstance();
    private val mensajeRef = database.getReference("ChatDirecto");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__grupal)

        if(intent.extras != null){
            TV_Nombre_Chat.text = intent.getStringExtra("Nombre")
            TipoC = intent.getIntExtra("Tipo", 0)
            userActual = intent.getSerializableExtra("userActual") as Usuario
            idChatDirecto = intent.getStringExtra("idChatDirecto").toString()


        }else{
            TV_Nombre_Chat.text = "Desconocido"
        }
        adaptadorChat = AdaptorChat(userActual,this, listamensajes, TipoC)
        RV_chat_grupal.adapter = adaptadorChat


        BT_back_chats_grupal.setOnClickListener {
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
        cargarMensajes()
    }

    private fun cargarMensajes(){
        var cargarMensajeRef = database.getReference().child("ChatLog").child(idChatDirecto)

       /* cargarMensajeRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

            }

        })*/

        cargarMensajeRef.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                /*if(snapshot!!.exists()) {

                    val children = snapshot.children.iterator()

                    while(children.hasNext()){
                        var de = children.next().value
                        var esMio = children.next().value
                        var msg = children.next().value
                        var timeStamp = children.next().value

                        val mensaje: Mensaje = Mensaje(de as String, esMio as Boolean, timeStamp, msg as String)
                        if(mensaje.de == correoActual)
                            mensaje.esMio = true
                        listamensajes.add(mensaje)

                    }

                    adaptadorChat.notifyDataSetChanged()
                    RV_chat_grupal.scrollToPosition(listamensajes.size - 1)
                }*/
            }


            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot!!.exists()) {

                    val children = snapshot.children.iterator()

                    while(children.hasNext()){
                        var de = children.next().value
                        var esMio = children.next().value
                        var msg = children.next().value
                        var nombre = children.next().value
                        var timeStamp = children.next().value

                        val mensaje: Mensaje = Mensaje(de as String, nombre as String, esMio as Boolean, timeStamp, msg as String)
                        if(mensaje.de == userActual!!.correo)
                            mensaje.esMio = true
                        listamensajes.add(mensaje)

                    }
                    adaptadorChat.notifyDataSetChanged()
                    RV_chat_grupal.scrollToPosition(listamensajes.size - 1)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun agregarMensaje(mensaje: Mensaje){
        database.getReference().child("ChatLog").child(idChatDirecto).push().setValue(mensaje)
        TX_mensaje.text.clear()
    }
}