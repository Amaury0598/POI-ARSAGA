package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Muro
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Ver_Tareas_Asignadas
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class General_Grupos : AppCompatActivity() {

    fun cambiarFragmento(FragmentoNuevo: Fragment, tag: String){

        val fragmentoanterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoanterior == null) {

            supportFragmentManager.beginTransaction().replace(R.id.ContenedorGrupos, FragmentoNuevo)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_grupos)


        val BarraGrupos = findViewById<BottomNavigationView>(R.id.BTN_bottom_nav_Grupos)
        val Back_General = findViewById<ImageButton>(R.id.BT_back_Gen)
        val MenuTareas = findViewById<Toolbar>(R.id.menu_Gen)

        Back_General.setOnClickListener {
            finish()
        }

        BarraGrupos.setOnNavigationItemSelectedListener {

            when(it.itemId){

                R.id.Gen_Tareas -> {

                }
                R.id.Gen_Muro -> {
                    cambiarFragmento(Frag_Muro(), "Muro")
                }
                R.id.Gen_VerTareas -> {
                    cambiarFragmento(Frag_Ver_Tareas_Asignadas(), "Ver Tareas del Grupo")
                }
                else -> {

                }
            }
            true
        }

        MenuTareas.setOnMenuItemClickListener {

            when(it.itemId){

                R.id.menu_crear_tarea -> {
                    val intent = Intent(this, Asignar_nueva_tarea::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_crear_subgrupo -> {

                }
                R.id.menu_modificar_grupo -> {

                }
                else -> {

                }
            }
            true
        }
    }

}