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
        var hora: Date,
        var mensaje: String = ""
) {

}

class ChatLog(
        var msg: AbstractMutableList<Mensaje>
) {

}

class Chat(
        var nombre: String = "",
        var correo: String = "",
        var contraseña: String = "",
        var chatLog: AbstractMutableList<ChatLog>
) {

}






