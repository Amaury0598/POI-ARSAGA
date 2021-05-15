package com.clases.proyecto_poi_arsaga.Modelos

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encrypt {

    private const val CIPHER_TRANSFORM = "AES/CBC/PKCS5PADDING"

    fun encriptar(textoDescifrado: String, llave: String) : String{
        val cipher = manageCipher(llave, Cipher.ENCRYPT_MODE)

        val textoCifrado = cipher!!.doFinal(
            textoDescifrado.toByteArray(
                charset("UTF-8")
            )
        )

        //var resultadoString = String(textoCifrado)

        val resultadoEnBase = String(Base64.encode(textoCifrado, Base64.NO_PADDING))

        return resultadoEnBase


    }

    fun descencriptar(textoCifrado: String, llave: String) : String{
        val textoCifradoBytes = Base64.decode(textoCifrado, Base64.NO_PADDING)

        val cipher = manageCipher(llave, Cipher.DECRYPT_MODE)


        val textoDescifrado = String(cipher!!.doFinal(
            textoCifradoBytes
        ))

        return textoDescifrado


    }

    private fun manageCipher(llave : String, MODE : Int): Cipher? {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORM)
        val llaveBytesFinal = ByteArray(16)
        val llaveBytesOriginal = llave.toByteArray(charset("UTF-8"))
        System.arraycopy(
            llaveBytesOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(
                llaveBytesOriginal.size,
                llaveBytesFinal.size
            )
        )
        val secretKeySpec: SecretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )
        val vectorInicializacion = IvParameterSpec(llaveBytesFinal)
        cipher.init(
            MODE,
            secretKeySpec,
            vectorInicializacion
        )
        return cipher
    }
}