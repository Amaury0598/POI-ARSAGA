package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Fragmentos.*
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var userActual : Usuario? = null
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")
    /*companion object {
        var userActual : Usuario? = null
        var currentAuthUser = FirebaseAuth.getInstance().currentUser
    }*/


    fun cambiarFragmento(FragmentoNuevo: Fragment, tag: String){

        val fragmentoanterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoanterior == null) {
            /*var b : Bundle = Bundle()

            b.putSerializable("userActual", userActual)
            FragmentoNuevo.arguments = b*/
            supportFragmentManager.beginTransaction().replace(R.id.Contenedor, FragmentoNuevo)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //userActual = intent.getSerializableExtra("userActual") as Usuario

        val miNav = findViewById<NavigationView>(R.id.main_nav)
        val mibarraNav = findViewById<BottomNavigationView>(R.id.BTN_bottom_nav)
        val miDrawer = findViewById<DrawerLayout>(R.id.main_drawer)
        val mitoolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        val HeaderNav = miNav.getHeaderView(0)

        val NombreUsuario = HeaderNav.findViewById<TextView>(R.id.HeaderNombre)
        val ImagenUsuario =  HeaderNav.findViewById<ImageView>(R.id.BT_Headerimagen)



        setSupportActionBar(mitoolbar)

        val toggle = ActionBarDrawerToggle(this, miDrawer, mitoolbar, R.string.app_name, R.string.app_name)

        miDrawer.addDrawerListener(toggle)
        toggle.syncState()

        miNav.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.menu_home -> {

                }
                R.id.menu_perfil -> {
                  cambiarFragmento(FragmentoA(), "Pefíl")
                }
                R.id.menu_opciones -> {
                    cambiarFragmento(FragmentoB(), "Opciones" )
                }
                R.id.menu_cerrar -> {
                    userRef.child(auth.uid.toString()).get()
                        .addOnSuccessListener {
                            if (it.exists()) {
                                userActual = it.getValue(Usuario::class.java) as Usuario

                                userActual!!.status = false
                                if(userActual!!.encriptado)
                                    userActual!!.encriptarUsuario()
                               userRef.child(auth.uid.toString()).setValue(
                                    userActual
                                )
                                   .addOnSuccessListener {
                                       if(userActual!!.encriptado)
                                           userActual!!.desencriptarUsuario()
                                       updateChatListInfo1(userActual!!)

                                   }
                            }
                        }

                }
                else -> {
                    cambiarFragmento(FragmentoA(), "Default" )

                }
            }
            miDrawer.closeDrawer(GravityCompat.START)
            true
        }

        mibarraNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {

                R.id.Menu_grupos -> {
                    cambiarFragmento(Frag_Grupos_Carreras(), "Grupos")
                }
                R.id.Menu_contactos -> {
                    cambiarFragmento(Main_Frag(),"Contactos")
                }
                else  -> {
                    cambiarFragmento(Frag_Grupos_Carreras(), "Default")
                }
            }
            true
        }
        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(userActual!!.encriptado)
                        userActual!!.desencriptarUsuario()
                    NombreUsuario.text = userActual?.nombre
                    Picasso.get().load(userActual?.imagen).into(ImagenUsuario)

                    cambiarFragmento(Frag_Grupos_Carreras(), "Grupos")
                }
    }

    private fun updateChatListInfo1(user: Usuario){
        val database = FirebaseDatabase.getInstance();
        val chatDirectoRef = database.getReference("ChatDirecto")
        chatDirectoRef.orderByChild("usuario1").equalTo(user.correo).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (cl in snapshot.children){
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
        chatDirectoRef.orderByChild("usuario2").equalTo(user.correo).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (cl in snapshot.children){
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.status2 = user.status

                        chatDirectoRef.child(chatD.id).setValue(chatD)
                    }
                }
                FirebaseAuth.getInstance().signOut()
                finish()
                val miIntent = Intent(this@MainActivity, Log_In::class.java)
                startActivity(miIntent)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}