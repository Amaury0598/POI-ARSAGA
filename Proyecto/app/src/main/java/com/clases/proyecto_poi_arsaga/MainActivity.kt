package com.clases.proyecto_poi_arsaga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoA
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoB
import com.clases.proyecto_poi_arsaga.Fragmentos.FragmentoC
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

                R.id.menu_perfil -> {
                    cambiarFragmento(FragmentoA(), "Perfil" )
                }
                R.id.menu_opciones -> {
                    cambiarFragmento(FragmentoB(), "Opciones" )
                }
                R.id.menu_cerrar -> {
                    cambiarFragmento(FragmentoC(), "Cerrar" )
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