package com.clases.proyecto_poi_arsaga.Modelos

import android.util.Base64
import java.io.Serializable
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class Usuario (
        var nombre: String = "",
        var correo: String = "",
        var imagen: String = "",
        var desc: String = "",
        var carrera: String = "",
        var coins: Int = 0,
        var status: Boolean = true,
        var encriptado: Boolean = false
) : Serializable {

    fun encriptarUsuario(){
        this.nombre = Encrypt.encriptar(this.nombre, "AMDALE")
        this.correo = Encrypt.encriptar(this.correo, "AMDALE")
        this.imagen = Encrypt.encriptar(this.imagen, "AMDALE")
        this.desc = Encrypt.encriptar(this.desc, "AMDALE")
        this.carrera = Encrypt.encriptar(this.carrera, "AMDALE")
    }

    fun desencriptarUsuario(){
        this.nombre = Encrypt.descencriptar(this.nombre, "AMDALE")
        this.correo = Encrypt.descencriptar(this.correo, "AMDALE")
        this.imagen = Encrypt.descencriptar(this.imagen, "AMDALE")
        this.desc = Encrypt.descencriptar(this.desc, "AMDALE")
        this.carrera = Encrypt.descencriptar(this.carrera, "AMDALE")
    }

    override fun toString(): String {
        return nombre + "\n" + correo
    }
}




