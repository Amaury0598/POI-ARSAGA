package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_log__in.*

class Log_In : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance();
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    //private val grupoRef = database.getReference("Grupos");

    private var correo: String = ""
    val listaGrupos = mutableListOf<Grupos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log__in)

        //cargarLista()

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
        var user: Usuario? = null
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){

                    for( u in snapshot.children){
                        user = u.getValue(Usuario::class.java) as Usuario;
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
                        miIntent.putExtra("userActual", user)

                        startActivity(miIntent)
                    }
                }

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