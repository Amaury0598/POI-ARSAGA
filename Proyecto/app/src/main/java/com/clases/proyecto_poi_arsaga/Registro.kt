package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private val grupoRef = database.getReference("Grupos");
    private var correo: String = ""
    private val defaultDesc = "Sin descripción"
    private val defaultImage: String = "https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/default.jpg?alt=media&token=1de049d7-cc43-4d37-8903-9c4178f4baee"
    val listaGrupos = mutableListOf<Grupos>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        BT_reg_iniciar.setOnClickListener{
            finish()
        }

        BT_registrarse.setOnClickListener {
            val nombre = ET_Nombre.text.toString()
            correo = ET_Correo.text.toString()
            val contraseña = ET_Contraseña.text.toString()
            val contraseña2 = ET_Contraseña2.text.toString()

            if(nombre.isNotEmpty() && correo.isNotEmpty() && contraseña.isNotEmpty() && contraseña2.isNotEmpty()) {
                if(contraseña == contraseña2) {
                    insertarUsuario(nombre, correo, contraseña)
                }
                else{
                    Toast.makeText(this, "Escriba la misma contraseña", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*private fun insertarUsuario(usuario: Usuario){
        val nuevoUsuario = userRef.push()
        //userRef.child(correo).setValue(usuario)
        nuevoUsuario.setValue(usuario);
    }*/

    private fun insertarUsuario(nombre: String, correo: String, contraseña: String) {
        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Toast.makeText(this@Registro, "Se ha registrado con éxito", Toast.LENGTH_SHORT).show()
                        val grupo: String = sp_carreras.selectedItem.toString()
                        val u:Usuario = Usuario(nombre, correo, defaultImage, defaultDesc, grupo)
                        userRef.child(auth.currentUser.uid).setValue(u)

                        agregarUsuarioGrupo(u)
                    }
                }
                .addOnFailureListener {
                    when(it.message){
                        "The email address is badly formatted." -> {
                            Toast.makeText(this@Registro, "El correo electrónico está mal introducido", Toast.LENGTH_SHORT).show()
                        }
                        "The email address is already in use by another account." -> {
                            Toast.makeText(this@Registro, "El correo electrónico introducido ya está en uso.", Toast.LENGTH_SHORT).show()
                        }
                        "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                            Toast.makeText(this@Registro, "La contraseña debe contener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@Registro, it.message, Toast.LENGTH_SHORT).show()
                            Log.d("ERROR-AUTH", it.message!!)
                        }
                    }

                }

    }

    private fun agregarUsuarioGrupo(usuarioRegistrado:Usuario){
        val path = "Grupos/${usuarioRegistrado.carrera}"
        database.getReference(path).get()
                .addOnSuccessListener {
                    val gpo = it.getValue(Grupos::class.java)
                    gpo!!.correo_usuarios!!.add(usuarioRegistrado.correo)
                    database.getReference(path).setValue(gpo)
                    val intent = Intent(this@Registro, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("userActual", usuarioRegistrado)
                    startActivity(intent)
                    /*BT_registrarse.visibility = View.GONE
                    BT_reg_iniciar.visibility = View.VISIBLE*/
                }
                .addOnFailureListener{
                    Toast.makeText(this@Registro, it.message, Toast.LENGTH_SHORT).show()
                }
        /*var grupo = grupoRef.orderByChild("nombre").equalTo(grupo)
        grupo.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {

                    for(u in snapshot.children) {
                        var gpo: Grupos = u.getValue(Grupos::class.java) as Grupos;
                        gpo.correo_usuarios!!.add(usuarioRegistrado.correo)
                        val cGrupos = mapOf<String, MutableList<String>?>(
                                "correo_usuarios" to gpo.correo_usuarios
                        )
                        grupoRef.child(gpo.nombre).updateChildren(cGrupos)

                    }

                }

                //val miIntent = Intent(this@Registro, MainActivity::class.java)
                //miIntent.putExtra("userActual", usuarioRegistrado)
                //finish()

                //startActivity(miIntent)
                BT_registrarse.visibility = View.GONE
                BT_reg_iniciar.visibility = View.VISIBLE
            }

        })*/
    }
}