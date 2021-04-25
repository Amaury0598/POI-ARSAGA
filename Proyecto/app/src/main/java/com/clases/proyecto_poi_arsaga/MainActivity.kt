package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Fragmentos.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

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
        val mibarraNav = findViewById<BottomNavigationView>(R.id.BTN_bottom_nav)
        val miDrawer = findViewById<DrawerLayout>(R.id.main_drawer)
        val mitoolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

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

        mibarraNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {

                R.id.Menu_grupos -> {
                    cambiarFragmento(Frag_Grupos_Carreras(), "Grupos")
                }
                R.id.Menu_tareas -> {

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
    }
}