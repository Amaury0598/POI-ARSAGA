package com.clases.proyecto_poi_arsaga

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoA
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.drawe_modperfil.*
import java.util.*

class Mod_Perfil : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("Usuarios")
    private var uri : Uri? = null
    private val defaultImage = "https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/default.jpg?alt=media&token=aa6316f8-e9c3-4531-b5a5-c7b0ac698d47"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawe_modperfil)

        ET_MPnuevonombre.setText(MainActivity.userActual?.nombre)
        ET_MPdesc.setText(MainActivity.userActual?.desc)

        BT_back_MP.setOnClickListener {
            finish()
        }

        BTN_MPaceptar.setOnClickListener {
            updateUser()
        }

        IMBT_MPfoto.setOnClickListener {
            Log.d("RegistedActivity", "Try to show photo selector")
            val F_intent = Intent(Intent.ACTION_PICK)
            F_intent.type = "image/*"
            startActivityForResult(F_intent, 0)
        }

    }

    private fun updateUser(){
        val contraUser = ET_MPcontraAuth.text.toString()
        val nuevaContraUser = ET_MPnuevacontra.text.toString()
        val nuevoNombreUser = ET_MPnuevonombre.text.toString()
        val nuevaDescUser = ET_MPdesc.text.toString()

        if(nuevoNombreUser.isEmpty()){
            ET_MPnuevonombre.error = "No puede estar vacío"
            ET_MPnuevonombre.requestFocus()
            return
        }

        if(nuevaDescUser.isEmpty()){
            ET_MPdesc.error = "No puede estar vacío"
            ET_MPdesc.requestFocus()
            return
        }

        if(contraUser.isNotEmpty()) {
            MainActivity.currentAuthUser?.let { user ->

                val credential = EmailAuthProvider.getCredential(user.email!!, contraUser)
                user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                user.updatePassword(nuevaContraUser)
                                        .addOnCompleteListener { newPass ->
                                            if(newPass.isSuccessful){
                                                uploadImageToStorage(nuevoNombreUser, nuevaDescUser)
                                            }
                                        }
                                        .addOnFailureListener { failPass ->
                                            when(failPass.message){

                                                "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                                                    ET_MPnuevacontra.error = "La nueva contraseña debe contener al menos 6 caracteres."
                                                    ET_MPnuevacontra.requestFocus()
                                                }
                                                else -> {
                                                    ET_MPnuevacontra.error = failPass.message
                                                    ET_MPnuevacontra.requestFocus()
                                                }
                                            }
                                        }
                            } else if (it.exception is FirebaseAuthInvalidCredentialsException) {
                                ET_MPcontraAuth.error = "Contraseña incorrecta"
                                ET_MPcontraAuth.requestFocus()
                            } else {
                                Toast.makeText(this@Mod_Perfil, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }

            }
        }else{
            deletePreviousImageToStorage(nuevoNombreUser, nuevaDescUser)
        }
    }

    private fun deletePreviousImageToStorage(nombre:String, desc:String){
        if(uri != null) {

            if(defaultImage != MainActivity.userActual?.imagen!!) {
                val delete = FirebaseStorage.getInstance().getReferenceFromUrl(MainActivity.userActual?.imagen!!)
                delete.delete()
                        .addOnSuccessListener {
                            uploadImageToStorage(nombre, desc)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@Mod_Perfil, it.message, Toast.LENGTH_SHORT).show()
                        }
            }else
                uploadImageToStorage(nombre,desc)
        }else{
            updateUserInfo(nombre, desc)
        }

    }

    private fun uploadImageToStorage(nombre:String, desc:String){
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/perfilUser/$filename")
        ref.putFile(uri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        MainActivity.userActual?.imagen = it.toString()
                        updateUserInfo(nombre, desc)
                    }
                }
    }

    private fun updateUserInfo(nombre:String, desc:String){

        val user = Usuario(nombre, MainActivity.userActual?.correo!!, MainActivity.userActual?.imagen!!, desc, MainActivity.userActual?.carrera!!)
        userRef.child(MainActivity.currentAuthUser.uid).setValue(user)

        val intent = Intent(this@Mod_Perfil, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        val u = MainActivity.userActual
        intent.putExtra("userActual", user)
        startActivity(intent)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data

            Picasso.get().load(uri.toString()).into(BT_MPimagen)
            IMBT_MPfoto.alpha = 0f

        }
    }
}