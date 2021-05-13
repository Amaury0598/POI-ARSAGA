package com.clases.proyecto_poi_arsaga

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Integrantes
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Estatus_tarea_grupo
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Muro
import com.clases.proyecto_poi_arsaga.Fragmentos.Frag_Ver_Tareas_Asignadas
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialog
import com.clases.proyecto_poi_arsaga.Modelos.Tareas
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.databinding.ActivityChatGrupalBinding.inflate
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.dialog_modificar_integrantes.view.*

class General_Grupos : AppCompatActivity(), Adaptador_Integrantes.OnItemGrupoClickListener {

    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()

    private val userRef = database.getReference("Usuarios")
    private val tareasRef = database.getReference("Tareas")

    var listaIntegrantes = mutableListOf<Usuario>()
    private var nuevoGrupo :Grupos = Grupos()

    val AdaptAIntegrantes = Adaptador_Integrantes(listaIntegrantes,nuevoGrupo.correo_usuarios!!,  this);

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

        val animation = AnimationUtils.loadAnimation(this, R.anim.dialog_itegrantes)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.dialog_itegrantes_2)
        val animationout = AnimationUtils.loadAnimation(this, R.anim.dialog_integrantes_out)

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

                    val DialogView_SB = layoutInflater.inflate(R.layout.dialog_crear_subgrupo, null)
                    val Dialog_Agregar_SB = android.app.AlertDialog.Builder(this).setView(DialogView_SB)

                    val LayDialog = DialogView_SB.findViewById<RelativeLayout>(R.id.Dialog_Subgrupo)
                    val Edit_Nombre_SB = DialogView_SB.findViewById<TextInputLayout>(R.id.Content_Edit_SG)
                    val Agregar_I_SB = DialogView_SB.findViewById<Button>(R.id.BTN_Agregar_I_Subgrupo)
                    val Aceptar_SB = DialogView_SB.findViewById<Button>(R.id.BTN_Aceptar_C_Subgrupo)

                    Edit_Nombre_SB.startAnimation(animation)
                    LayDialog.startAnimation(animation2)
                    Agregar_I_SB.startAnimation(animation2)
                    Aceptar_SB.startAnimation(animation2)

                    val AlertSB = Dialog_Agregar_SB.show()

                    Agregar_I_SB.setOnClickListener {

                        val DialogView_AIntregrantes = layoutInflater.inflate(R.layout.dialog_integrantes, null)
                        val Dialog_Elegir_I = android.app.AlertDialog.Builder(this).setView(DialogView_AIntregrantes)

                        val LayDialog_I = DialogView_AIntregrantes.findViewById<RelativeLayout>(R.id.RelLay_Dialog)
                        val Recy_Agregar_I = DialogView_AIntregrantes.findViewById<RecyclerView>(R.id.Dialog_Recycler_Integrantes)
                        val Aceptar_I = DialogView_AIntregrantes.findViewById<Button>(R.id.BTN_Aceptar_integrantes)

                        Recy_Agregar_I.layoutManager = LinearLayoutManager(this);

                        //Recy_Agregar_I.adapter = AdaptIntegrantes  Adaptador

                        Recy_Agregar_I.startAnimation(animation)
                        LayDialog_I.startAnimation(animation2)
                        Aceptar_I.startAnimation(animation2)

                        val Alert_AI = Dialog_Elegir_I.show()

                        Aceptar_I.setOnClickListener {

                        }

                        Alert_AI.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

                        Aceptar_I.setOnClickListener {

                            Recy_Agregar_I.startAnimation(animationout)
                            LayDialog_I.startAnimation(animationout)
                            Aceptar_I.startAnimation(animationout)
                            Alert_AI.dismiss()

                        }
                    }

                    AlertSB.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

                    Aceptar_SB.setOnClickListener {

                        Edit_Nombre_SB.startAnimation(animationout)
                        LayDialog.startAnimation(animationout)
                        Agregar_I_SB.startAnimation(animationout)
                        Aceptar_SB.startAnimation(animationout)
                        AlertSB.dismiss()
                    }
                }

                R.id.menu_modificar_grupo -> {

                    val DialogView_A = layoutInflater.inflate(R.layout.dialog_modificar_integrantes, null)
                    val Dialog_Agregar_I = android.app.AlertDialog.Builder(this).setView(DialogView_A)

                    val RDialog = DialogView_A.findViewById<RelativeLayout>(R.id.RelLay_DialogAgregar)
                    val Recy_Agregar_I = DialogView_A.findViewById<RecyclerView>(R.id.Dialog_Recycler_Integrantes)
                    val Aceptar = DialogView_A.findViewById<Button>(R.id.BTN_Aceptar_A_integrantes)

                    Recy_Agregar_I.layoutManager = LinearLayoutManager(this);

                    //Recy_Agregar_I.adapter = AdaptIntegrantes  Adaptador

                    Recy_Agregar_I.startAnimation(animation)
                    RDialog.startAnimation(animation2)
                    Aceptar.startAnimation(animation2)
                    val AlertD = Dialog_Agregar_I.show()

                    AlertD.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

                    Aceptar.setOnClickListener {

                        Recy_Agregar_I.startAnimation(animationout)
                        RDialog.startAnimation(animationout)
                        Aceptar.startAnimation(animationout)
                        AlertD.dismiss()
                    }
                }
                else -> {

                }
            }
            true
        }

    }

    override fun AddtoList(correo: String, position: String) {
        TODO("Not yet implemented")
    }

    override fun RemoveFromList(correo: String, position: String) {
        TODO("Not yet implemented")
    }

    fun CargarLista(){

        listaIntegrantes.add(Usuario("Amaury", "amg05@hotmail.com", "", "", "", 10))
        listaIntegrantes.add(Usuario("Leo", "Leo@hotmail.com", "", "", "", 10))
        listaIntegrantes.add(Usuario("Darien", "Darien@hotmail.com", "", "", "", 10))
        listaIntegrantes.add(Usuario("José", "José@hotmail.com", "", "", "", 10))
    }


}