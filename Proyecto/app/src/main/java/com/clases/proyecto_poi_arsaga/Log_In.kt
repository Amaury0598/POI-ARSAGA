package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_log__in.*

class Log_In : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance();
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private var correo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log__in)


        BT_entrar.setOnClickListener {
            correo = ET_Correo.text.toString()
            var contraseña: String = ET_Contraseña.text.toString()
            if(correo.isNotEmpty() && contraseña.isNotEmpty()){
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
        var encontrado: Boolean = false;
        userRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){

                    for( u in snapshot.children){
                        val user: Usuario = u.getValue(Usuario::class.java) as Usuario;
                        if(user!!.correo == correo && user!!.contraseña == contraseña){
                            encontrado = true;
                            break;
                        }
                    }
                    if(!encontrado){
                        Toast.makeText(this@Log_In, "La Contraseña y/o el Correo están incorrectos", Toast.LENGTH_SHORT).show()
                    } else {
                        finish()

                        val miIntent = Intent(this@Log_In, MainActivity::class.java)
                        miIntent.putExtra("correoActual", correo)

                        startActivity(miIntent)
                    }
                }

            }



        })

    }
}