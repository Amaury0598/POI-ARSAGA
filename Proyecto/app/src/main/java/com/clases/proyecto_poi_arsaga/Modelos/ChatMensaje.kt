package com.clases.proyecto_poi_arsaga.Modelos

import android.location.Address
import java.io.Serializable
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
        var nombre: String = "",
        var esMio: Boolean = false,
        val timeStamp: Any? = null,
        var mensaje: String = "",
        var tipoMensaje: String = "",
        var url: String = "",
        var gps: GlobalPositioningSystem = GlobalPositioningSystem()
) {

}

class ChatDirecto(
        var id: String = "",
        var usuario1:String = "",
        var usuario2:String = "",
        var nombre1: String = "",
        var nombre2: String = "",
        var fotoUsuario1: String = "",
        var fotoUsuario2: String = "",
        var ultimoMensajeDe: String = "",
        var ultimoMensaje: String = "",
        var timeStamp: Any? = null
) : Serializable {

}

class ChatLog(
        var chatLog: MutableList<Mensaje> = mutableListOf<Mensaje>()
) {

}

class GlobalPositioningSystem(

    var addressLine: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0


    ): Serializable{
    override fun toString(): String {
        return "Dirección: $addressLine\nLatitud: ${latitude}\nLongitud${longitude}"
    }

}





