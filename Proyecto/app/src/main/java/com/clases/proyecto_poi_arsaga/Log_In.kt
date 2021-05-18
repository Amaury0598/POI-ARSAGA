package com.clases.proyecto_poi_arsaga

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_log__in.*
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class Log_In : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private lateinit var loading : LoadingDialog
    //private val grupoRef = database.getReference("Grupos");

    private var correo: String = ""
    val listaGrupos = mutableListOf<Grupos>()
    //Zi3WkVRdrWko3KG5dfAgJi1MCTHiTaK/dSyrUZlsx80
    //N5OQR7b4c8kcapzewOqMFw
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log__in)
        loading = LoadingDialog(this)

        //var correo = Encrypt.descencriptar("Zi3WkVRdrWko3KG5dfAgJi1MCTHiTaK/dSyrUZlsx80", "correo")
        //var contra = Encrypt.descencriptar("N5OQR7b4c8kcapzewOqMFw", "correo")
        if(!auth.uid.isNullOrEmpty()){
            loading.startLoading("Procesando Datos")

            logIn()
        }

        //cargarLista()

        BT_entrar.setOnClickListener {
            correo = ET_Correo.text.toString()
            var contraseña: String = ET_Contraseña.text.toString()
            if(correo.isNotEmpty() && contraseña.isNotEmpty()){
                loading.startLoading("Validando Datos")
                buscarUsuario(correo, contraseña)
            }else{
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        BT_registrate.setOnClickListener {
            val miIntent = Intent(this, Registro::class.java)
            startActivity(miIntent)
        }




    }





    private fun buscarUsuario(correo: String, contraseña: String) {
       auth.signInWithEmailAndPassword(correo, contraseña)
           .addOnCompleteListener {
               if(it.isSuccessful){

                   logIn()

               }
           }
           .addOnFailureListener {
               if(loading != null)
                   loading.isDismiss()
               Toast.makeText(this@Log_In, "La Contraseña y/o el Correo están incorrectos", Toast.LENGTH_SHORT).show()
           }

    }

    private fun logIn() {
        userRef.child(auth.uid.toString()).get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val user = it.getValue(Usuario::class.java) as Usuario
                    user!!.status = true
                    userRef.child(auth.uid.toString()).setValue(user)
                        .addOnSuccessListener {
                            if(user.encriptado)
                                user.desencriptarUsuario()
                            updateChatListInfo1(user)

                        }
                }
            }
    }

    private fun updateChatListInfo1(user: Usuario){
        val database = FirebaseDatabase.getInstance();
        val chatDirectoRef = database.getReference("ChatDirecto")
        chatDirectoRef.orderByChild("usuario1").equalTo(user.correo).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (cl in snapshot.children) {
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.status1 = user.status
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
        val database = FirebaseDatabase.getInstance();
        val chatDirectoRef = database.getReference("ChatDirecto")
        chatDirectoRef.orderByChild("usuario2").equalTo(user.correo).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (cl in snapshot.children) {
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.status2 = user.status

                        chatDirectoRef.child(chatD.id).setValue(chatD)


                    }
                }
                //val user = it.getValue(Usuario::class.java)
                val intent = Intent(this@Log_In, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                //intent.putExtra("userActual", user)
                if (loading != null)
                    loading.isDismiss()
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    /*fun cargarLista(){
        var admin = mutableListOf<String>()
        admin.add("admin");
        listaGrupos.add(Grupos("LMAD", admin, "https://image.flaticon.com/icons/png/512/1465/1465606.png" ))
        listaGrupos.add(Grupos("LCC", admin, "https://images-na.ssl-images-amazon.com/images/I/31gpv-ZU4vL.png"))
        listaGrupos.add(Grupos("LM", admin, "https://icons-for-free.com/iconfiles/png/512/math+tutor+icon-1320195955563127435.png"))
        listaGrupos.add(Grupos("LF", admin, "https://image.flaticon.com/icons/png/512/746/746960.png"))
        listaGrupos.add(Grupos("LA", admin, "https://www.highmeadowschool.org/wp-content/uploads/2016/11/abacus-icon.png"))
        listaGrupos.add(Grupos("LSTI", admin, "https://img.icons8.com/bubbles/452/cyber-security.png"))

        insertarGrupos()

    }

    fun insertarGrupos(){
        for (lg in listaGrupos){
            grupoRef.child(lg.nombre).setValue(lg)
        }
    }*/

}