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
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialog
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.drawe_modperfil.*
import java.util.*

class Mod_Perfil : AppCompatActivity() {
    private lateinit var userActual : Usuario
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("Usuarios")
    private val chatDirectoRef = database.getReference("ChatDirecto")
    private var auth = FirebaseAuth.getInstance()
    private var uri : Uri? = null
    private val defaultImage = "https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fdefault.jpg?alt=media&token=bcfafc2d-19da-4811-a083-2c6d1f7e3951"
    private lateinit var loading : LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawe_modperfil)
        loading = LoadingDialog(this)
        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{
                    loading.startLoading("Cargando Datos")
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(userActual.encriptado)
                        userActual.desencriptarUsuario()
                    ET_MPnuevonombre.setText(userActual?.nombre)
                    ET_MPdesc.setText(userActual?.desc)
                    if(loading != null)
                        loading.isDismiss()
                }


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
        loading.startLoading("Validando Datos")
        if(contraUser.isNotEmpty()) {
            auth.currentUser?.let { user ->

                val credential = EmailAuthProvider.getCredential(user.email!!, contraUser)
                user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                user.updatePassword(nuevaContraUser)
                                        .addOnCompleteListener { newPass ->
                                            if(newPass.isSuccessful){
                                                deletePreviousImageToStorage(nuevoNombreUser, nuevaDescUser)
                                            }
                                        }
                                        .addOnFailureListener { failPass ->
                                            if(loading != null)
                                                loading.isDismiss()
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
                                if(loading != null)
                                    loading.isDismiss()
                                ET_MPcontraAuth.error = "Contraseña incorrecta"
                                ET_MPcontraAuth.requestFocus()
                            } else {
                                if(loading != null)
                                    loading.isDismiss()
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

            if(defaultImage != userActual.imagen) {
                val delete = FirebaseStorage.getInstance().getReferenceFromUrl(userActual.imagen)
                delete.delete()
                        .addOnSuccessListener {
                            uploadImageToStorage(nombre, desc)
                        }
                        .addOnFailureListener {
                            if(loading != null)
                                loading.isDismiss()
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
                        userActual?.imagen = it.toString()
                        updateUserInfo(nombre, desc)
                    }.addOnFailureListener {
                        if(loading != null)
                            loading.isDismiss()
                        Toast.makeText(this@Mod_Perfil, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    if(loading != null)
                        loading.isDismiss()
                    Toast.makeText(this@Mod_Perfil, it.message, Toast.LENGTH_SHORT).show()
                }
    }

    private fun updateUserInfo(nombre:String, desc:String){
        val user = Usuario(nombre, userActual?.correo!!, userActual?.imagen!!, desc, userActual?.carrera!!, userActual.coins, userActual.status, userActual.encriptado)
        if(user.encriptado)
            user.encriptarUsuario()
        userRef.child(auth.uid.toString()).setValue(user)
                .addOnSuccessListener {
                    if(user.encriptado)
                        user.desencriptarUsuario()
                    updateChatListInfo1(user)
                }


    }

    private fun updateChatListInfo1(user: Usuario){
        chatDirectoRef.orderByChild("usuario1").equalTo(user.correo).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (cl in snapshot.children){
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.fotoUsuario1 = user.imagen
                        chatD.nombre1 = user.nombre
                        Log.d("Listener", "Disparado user1")
                        chatDirectoRef.child(chatD.id).setValue(chatD)
                    }
                }
                updateChatListInfo2(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateChatListInfo2(user: Usuario){
        chatDirectoRef.orderByChild("usuario2").equalTo(user.correo).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (cl in snapshot.children){
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.fotoUsuario2 = user.imagen
                        chatD.nombre2 = user.nombre
                        Log.d("Listener", "Disparado user2")
                        chatDirectoRef.child(chatD.id).setValue(chatD)
                    }
                }
                val intent = Intent(this@Mod_Perfil, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                /*val u = userActual
                intent.putExtra("userActual", user)*/
                if(loading != null)
                    loading.isDismiss()
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data

            Picasso.get().load(uri.toString()).into(BT_MPimagen)
            IMBT_MPfoto.alpha = 0f

        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}