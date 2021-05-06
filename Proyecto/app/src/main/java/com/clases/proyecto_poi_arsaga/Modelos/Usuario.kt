package com.clases.proyecto_poi_arsaga.Modelos

import java.io.Serializable


class Usuario (
        var nombre: String = "",
        var correo: String = "",
        var imagen: String = "",
        var desc: String = "",
        var carrera: String = ""
) : Serializable {

}

