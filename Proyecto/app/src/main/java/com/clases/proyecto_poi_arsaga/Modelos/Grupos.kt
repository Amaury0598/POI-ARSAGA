package com.clases.proyecto_poi_arsaga.Modelos

data class Grupos(

        var nombre: String = "",
        var correo_usuarios: MutableList<String>? = mutableListOf<String>(),
        var foto: String = ""
)