package com.clases.proyecto_poi_arsaga.Modelos

import java.util.*

data class ChatMensaje (

    val usuario: String,
    val mensaje: String,
    val horaEnvio: Date,
    val visto: Boolean,
    val id: Long,
    val mimensaje: Boolean
)

class Mensaje(
        var de: String = "",
        var esMio: Boolean = false,
        val timeStamp: Any? = null,
        var mensaje: String = ""
) {

}

class ChatDirecto(
        var id: String = "",
        var usuarios:String = ""
) {

}

class ChatLog(
        var chatLog: MutableList<Mensaje> = mutableListOf<Mensaje>()
) {

}






