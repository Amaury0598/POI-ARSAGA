package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
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
    private var correo: String = ""


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
        nuevoUsuario.setValue(usuario);
    }

    private fun buscarUsuario(nombre: String, correo: String, contraseña: String) {
        var encontrado: Boolean = false;
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){

                    for( u in snapshot.children){
                        val user: Usuario = u.getValue(Usuario::class.java) as Usuario;
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
                        miIntent.putExtra("correoActual", correo)

                        startActivity(miIntent)
                    } else {
                        Toast.makeText(this@Registro, "Se ha producido un error", Toast.LENGTH_SHORT).show()
                    }
                    }

                }



        })

    }
}