package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Adaptadores.AdaptorChat
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.Modelos.ChatLog
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.Modelos.Mensaje
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import java.util.*

class Chat_Grupal : AppCompatActivity() {

    val listamensajes = mutableListOf<Mensaje>()
    var correoActual: String = ""
    var idChatDirecto: String = ""
    private var adaptadorChat = AdaptorChat(correoActual,this, listamensajes)
    private val database = FirebaseDatabase.getInstance();
    private val mensajeRef = database.getReference("ChatDirecto");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__grupal)

        if(intent.extras != null){
            TV_Nombre_Chat.text = intent.getStringExtra("Nombre")
            correoActual = intent.getStringExtra("correoActual").toString()
            idChatDirecto = intent.getStringExtra("idChatDirecto").toString()

        }else{
            TV_Nombre_Chat.text = "Desconocido"
        }

        RV_chat_grupal.adapter = adaptadorChat
        cargarMensajes()

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

                val chatmensaje = Mensaje(correoActual, false, Date(), mensajeEscrito)
                agregarMensaje(chatmensaje)




            }
        }
    }

    private fun cargarMensajes(){
        var cargarMensajeRef = database.getReference().child("ChatLog").child(idChatDirecto)

        cargarMensajeRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {
                    listamensajes.clear()
                    for(cl in snapshot.children) {
                        val mensaje: Mensaje = cl.getValue(Mensaje::class.java) as Mensaje;
                        if(mensaje.de == correoActual)
                            mensaje.esMio = true
                        listamensajes.add(mensaje)

                    }
                    adaptadorChat.notifyDataSetChanged()
                    RV_chat_grupal.scrollToPosition(listamensajes.size - 1)
                } else{


                }
            }

        })
    }

    private fun agregarMensaje(mensaje: Mensaje){
        database.getReference().child("ChatLog").child(idChatDirecto).push().setValue(mensaje)
        mensaje.esMio = true
        listamensajes.add(mensaje)
        adaptadorChat.notifyDataSetChanged()
        RV_chat_grupal.scrollToPosition(listamensajes.size - 1)
        TX_mensaje.text.clear()
    }
}