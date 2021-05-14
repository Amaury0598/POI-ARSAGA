package com.clases.proyecto_poi_arsaga.Modelos

import java.io.Serializable

data class Grupos(

        var nombre: String = "",
        var correo_usuarios: MutableList<String>? = mutableListOf<String>(),
        var foto: String = "",
        var admin: String = "",
        var deGrupo: String = ""
) : Serializable{
    override fun toString(): String {
        return nombre
    }
}

data class Publicaciones(
        var grupo : String = "",
        var correo : String = "",
        var foto : String = "",
        var nombre : String = "",
        var id : String = "",
        var mensajePublicacion : String = ""
){

}