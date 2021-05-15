package com.clases.proyecto_poi_arsaga.Modelos

import java.io.Serializable

class Tareas (
        var nombre: String = "",
        var desc: String = "",
        var id: String = "",
        var fecha: String = "",
        var imagen: String = "",
        var coins: Int = 0
) : Serializable {

}

class TareaEntregada (
        var idTarea: String = "",
        var correo: String = "",
        var nombre: String = "",
        var imagen: String = "",
        var multimedia:Multimedia = Multimedia()
) : Serializable {

}

class lTareaUsuarios(
        var id : String = "",
        var fecha : String = "",
        var listaUsuarios: MutableList<TareaUsuarios> = mutableListOf<TareaUsuarios>()
)

class TareaUsuarios(
        var correo: String = "",
        var status: String = ""
){

}

class Multimedia(
        var nombreArchivo: String ="",
        var urlArchivo: String = ""
){

}