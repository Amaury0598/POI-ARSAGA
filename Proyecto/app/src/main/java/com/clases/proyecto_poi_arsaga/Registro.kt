package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance();
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private val grupoRef = database.getReference("Grupos");
    private var correo: String = ""
    val listaGrupos = mutableListOf<Grupos>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)



        BT_registrarse.setOnClickListener {
            val nombre = ET_Nombre.text.toString()
            correo = ET_Correo.text.toString()
            val contraseña = ET_Contraseña.text.toString()
            val contraseña2 = ET_Contraseña2.text.toString()

            if(nombre.isNotEmpty() && correo.isNotEmpty() && contraseña.isNotEmpty() && contraseña2.isNotEmpty()) {
                if(contraseña == contraseña2) {
                    buscarUsuario(nombre, correo, contraseña)
                }
                else{
                    Toast.makeText(this, "Escriba la misma contraseña", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertarUsuario(usuario: Usuario){
        val nuevoUsuario = userRef.push()
        //userRef.child(correo).setValue(usuario)
        nuevoUsuario.setValue(usuario);
    }

    private fun buscarUsuario(nombre: String, correo: String, contraseña: String) {
        /*var encontrado: Boolean = false;
        var user: Usuario? = null
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){

                    for( u in snapshot.children){
                        user = u.getValue(Usuario::class.java) as Usuario;
                        if(user!!.correo == correo){
                            encontrado = true;
                            break;
                        }
                    }
                    if(!encontrado){
                        val usuarioRegistrado = Usuario(nombre, correo, contraseña)
                        insertarUsuario(usuarioRegistrado)
                        Toast.makeText(this@Registro, "Gracias por Registrarte", Toast.LENGTH_SHORT).show()
                        finish()
                        finish()
                        val miIntent = Intent(this@Registro, MainActivity::class.java)
                        miIntent.putExtra("userActual", user)

                        startActivity(miIntent)
                    } else {
                        Toast.makeText(this@Registro, "Se ha producido un error", Toast.LENGTH_SHORT).show()
                    }
                    }

                }



        })*/

        var logInRef = database.getReference().child("Usuarios").orderByChild("correo").equalTo(correo)
        logInRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {

                    Toast.makeText(this@Registro, "Ya hay un usuario con ese correo", Toast.LENGTH_SHORT).show()
                } else {
                    if (nombre.isNotEmpty() && correo.isNotEmpty() && contraseña.isNotEmpty()) {
                        val usuarioRegistrado = Usuario(nombre, correo, contraseña)
                        val grupo: String = sp_carreras.selectedItem.toString()

                        insertarUsuario(usuarioRegistrado)
                        Toast.makeText(this@Registro, "Gracias por Registrarte", Toast.LENGTH_SHORT).show()

                        agregarUsuarioGrupo(usuarioRegistrado, grupo)


                    }
                }
            }

        })

    }

    private fun agregarUsuarioGrupo(usuarioRegistrado:Usuario, grupo: String){
        var grupo = grupoRef.orderByChild("nombre").equalTo(grupo)
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

                val miIntent = Intent(this@Registro, MainActivity::class.java)
                //miIntent.putExtra("userActual", usuarioRegistrado)
                finish()
                
                //startActivity(miIntent)
            }

        })
    }
}