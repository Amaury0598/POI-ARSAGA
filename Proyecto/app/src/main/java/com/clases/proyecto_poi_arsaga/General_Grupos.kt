package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Estatus_tarea_grupo
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Muro
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Ver_Tareas_Asignadas
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialog
import com.clases.proyecto_poi_arsaga.Modelos.Tareas
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class General_Grupos : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")
    private val tareasRef = database.getReference("Tareas")
    private lateinit var loading : LoadingDialog

    companion object{
        var grupoActual = Grupos()
    }

    fun cambiarFragmento(FragmentoNuevo: Fragment, tag: String){

        val fragmentoanterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoanterior == null) {

            supportFragmentManager.beginTransaction().replace(R.id.ContenedorGrupos, FragmentoNuevo)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = LoadingDialog(this)

        setContentView(R.layout.activity_general_grupos)
        if(intent != null)
            grupoActual = intent.getSerializableExtra("Grupo") as Grupos
        else
            finish()

        val BarraGrupos = findViewById<BottomNavigationView>(R.id.BTN_bottom_nav_Grupos)
        val Back_General = findViewById<ImageButton>(R.id.BT_back_Gen)
        val MenuTareas = findViewById<Toolbar>(R.id.menu_Gen)
        if(grupoActual!!.admin != auth.currentUser.email) {
            findViewById<View>(R.id.menu_crear_tarea).visibility = View.GONE
            findViewById<View>(R.id.Gen_VerTareas).visibility = View.GONE
        }

        Back_General.setOnClickListener {
            finish()
        }

        BarraGrupos.setOnNavigationItemSelectedListener {

            when(it.itemId){

                R.id.Gen_Tareas -> {
                    cambiarFragmento(Frag_Estatus_tarea_grupo(), "Tareas por Grupo")
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