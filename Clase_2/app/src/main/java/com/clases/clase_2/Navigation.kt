package com.clases.clase_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.clases.clase_2.fragmentos.FragmentoA
import com.clases.clase_2.fragmentos.FragmentoB
import com.clases.clase_2.fragmentos.FragmentoC
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_tareas.*

class Navigation : AppCompatActivity() {

    fun CambiarFragentos(fragmentoNuevo: Fragment, tag: String){

        val fragmentoAnterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null){

        supportFragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoNuevo).commit()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)


        val minav = findViewById<NavigationView>(R.id.nav)
        val midrawer = findViewById<DrawerLayout>(R.id.drawer)


        minav.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.Opc_Perfil ->{
                    CambiarFragentos(FragmentoA(), "fragmentoA")


                }

                R.id.Opc_Chats -> {
                    CambiarFragentos(FragmentoB(), "fragmentoB")
                }
                R.id.Opc_Grupos ->{
                    CambiarFragentos(FragmentoC(), "fragmentosC")
                }
            }
            midrawer.closeDrawer(GravityCompat.START)
            true
        }
    }
}