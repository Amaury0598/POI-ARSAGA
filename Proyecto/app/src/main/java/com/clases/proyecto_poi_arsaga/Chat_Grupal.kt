package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.clases.proyecto_poi_arsaga.Adaptadores.AdaptorChat
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import java.util.*

class Chat_Grupal : AppCompatActivity() {

    val listamensajes = mutableListOf<ChatMensaje>()
    private val adaptadorChat = AdaptorChat(this, listamensajes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__grupal)

        if(intent.extras != null){
            TV_Nombre_Chat.text = intent.getStringExtra("Nombre")
        }else{
            TV_Nombre_Chat.text = "Desconocido"
        }

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
                val chatmensaje = ChatMensaje(mensajeEscrito, Date(), false, 0, true)
                listamensajes.add(chatmensaje)

                val chatRespuesta = ChatMensaje("No estoy", Date(), false, 0, false)
                listamensajes.add(chatRespuesta)

                adaptadorChat.notifyDataSetChanged()
                RV_chat_grupal.scrollToPosition(listamensajes.size - 1)
                TX_mensaje.text.clear()
            }
        }
    }
}