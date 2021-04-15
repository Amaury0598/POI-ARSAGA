package com.clases.proyecto_poi_arsaga.Modelos

import java.util.*

data class ChatMensaje (

    val mensaje: String,
    val horaEnvio: Date,
    val visto: Boolean,
    val id: Long,
    val mimensaje: Boolean
)