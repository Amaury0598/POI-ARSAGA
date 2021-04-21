package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoA
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoB
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoC
import com.clases.proyecto_poi_arsaga.Fragmentos.Main_Frag
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    fun cambiarFragmento(FragmentoNuevo: Fragment, tag: String){

        val fragmentoanterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoanterior == null) {
            supportFragmentManager.beginTransaction().replace(R.id.Contenedor, FragmentoNuevo)
                .commit()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val miNav = findViewById<NavigationView>(R.id.main_nav)
        val miDrawer = findViewById<DrawerLayout>(R.id.main_drawer)
        val mitoolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        setSupportActionBar(mitoolbar)

        val toggle = ActionBarDrawerToggle(this, miDrawer, mitoolbar, R.string.app_name, R.string.app_name)

        miDrawer.addDrawerListener(toggle)
        toggle.syncState()

        miNav.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.menu_home -> {
                    val miIntent = Intent(this, Chat_Grupal::class.java)
                    startActivity(miIntent)
                }
                R.id.menu_perfil -> {
                  cambiarFragmento(FragmentoA(), "Lista")
                }
                R.id.menu_opciones -> {
                    cambiarFragmento(FragmentoB(), "Opciones" )
                }
                R.id.menu_cerrar -> {
                    finish()
                    val miIntent = Intent(this, Log_In::class.java)
                    startActivity(miIntent)
                }
                else -> {
                    cambiarFragmento(FragmentoA(), "Default" )

                }
            }
            miDrawer.closeDrawer(GravityCompat.START)
            true
        }
    }
}