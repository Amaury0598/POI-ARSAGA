package com.clases.proyecto_poi_arsaga

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
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
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_crear_tarea.view.*
import kotlinx.android.synthetic.main.dialog_modificar_integrantes.view.*

class General_Grupos : AppCompatActivity(), Adaptador_Integrantes.OnItemGrupoClickListener {

    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private lateinit var userActual:Usuario
    private val userRef = database.getReference("Usuarios")
    private val gruposRef = database.getReference("Grupos")

    var listaIntegrantesModificar = mutableListOf<Usuario>()
    var listaIntegrantesSubgrupo = mutableListOf<Usuario>()
    private var modGrupo :Grupos = Grupos()
    private var nuevoSubgrupo : Grupos = Grupos()

    val AdaptAIntegrantesModificar = Adaptador_Integrantes(1, listaIntegrantesModificar,modGrupo.correo_usuarios!!,  this);
    val AdaptAIntegrantesSubGrupo = Adaptador_Integrantes(2, listaIntegrantesSubgrupo,nuevoSubgrupo.correo_usuarios!!,  this);

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
        if(intent != null) {
            grupoActual = intent.getSerializableExtra("Grupo") as Grupos
            for (c in grupoActual.correo_usuarios!!){
                modGrupo.correo_usuarios!!.add(c)
            }

            modGrupo.nombre = grupoActual.nombre
            modGrupo.admin = grupoActual.admin
            modGrupo.foto = grupoActual.foto
            modGrupo.deGrupo = grupoActual.deGrupo

            //modGrupo = grupoActual

        }
        else
            finish()

        val animation = AnimationUtils.loadAnimation(this, R.anim.dialog_itegrantes)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.dialog_itegrantes_2)
        val animationout = AnimationUtils.loadAnimation(this, R.anim.dialog_integrantes_out)

        val BarraGrupos = findViewById<BottomNavigationView>(R.id.BTN_bottom_nav_Grupos)
        val Back_General = findViewById<ImageButton>(R.id.BT_back_Gen)
        val MenuTareas = findViewById<Toolbar>(R.id.menu_Gen)


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
                    if (grupoActual!!.admin != auth.currentUser.email) {
                        Toast.makeText(this, "Solo el administrador puede ver las Tareas", Toast.LENGTH_SHORT).show()

                    }else {
                        cambiarFragmento(Frag_Ver_Tareas_Asignadas(), "Ver Tareas del Grupo")
                    }
                }
                else -> {

                }
            }
            true
        }

        MenuTareas.setOnMenuItemClickListener {

            when(it.itemId){

                R.id.menu_crear_tarea -> {
                    if (grupoActual!!.admin != auth.currentUser.email) {
                        Toast.makeText(this, "Solo el administrador puede crear Tareas", Toast.LENGTH_SHORT).show()

                    }else {
                        val intent = Intent(this, Asignar_nueva_tarea::class.java)
                        this.startActivity(intent)
                    }
                }
                R.id.menu_crear_subgrupo -> {
                    if (grupoActual!!.admin != auth.currentUser.email) {
                        Toast.makeText(this, "Solo el administrador puede crear Subgrupos", Toast.LENGTH_SHORT).show()

                    } else {
                        if (grupoActual!!.deGrupo.isNotEmpty()) {
                            Toast.makeText(this, "Ve al Grupo principal para hacer un Subgrupo", Toast.LENGTH_SHORT).show()
                        }else{
                        val DialogView_SB = layoutInflater.inflate(R.layout.dialog_crear_subgrupo, null)
                        val Dialog_Agregar_SB = android.app.AlertDialog.Builder(this).setView(DialogView_SB)

                        val LayDialog = DialogView_SB.findViewById<RelativeLayout>(R.id.Dialog_Subgrupo)
                        val Edit_Nombre_SB = DialogView_SB.findViewById<TextInputLayout>(R.id.Content_Edit_SG)
                        val Agregar_I_SB = DialogView_SB.findViewById<Button>(R.id.BTN_Agregar_I_Subgrupo)
                        val Aceptar_SB = DialogView_SB.findViewById<Button>(R.id.BTN_Aceptar_C_Subgrupo)
                        val nombreSubgrupo = DialogView_SB.findViewById<TextInputEditText>(R.id.TV_Nombre_Subgrupo)

                        Edit_Nombre_SB.startAnimation(animation)
                        LayDialog.startAnimation(animation2)
                        Agregar_I_SB.startAnimation(animation2)
                        Aceptar_SB.startAnimation(animation2)
                        //listaIntegrantes = grupoActual.
                        val AlertSB = Dialog_Agregar_SB.show()

                        Agregar_I_SB.setOnClickListener {
                            loading.startLoading("Cargando Listas de Usuarios")
                            val DialogView_AIntregrantes = layoutInflater.inflate(R.layout.dialog_integrantes, null)
                            val Dialog_Elegir_I = android.app.AlertDialog.Builder(this).setView(DialogView_AIntregrantes)

                            val LayDialog_I = DialogView_AIntregrantes.findViewById<RelativeLayout>(R.id.RelLay_Dialog)
                            val Recy_Agregar_I = DialogView_AIntregrantes.findViewById<RecyclerView>(R.id.Dialog_Recycler_Integrantes)
                            val Aceptar_I = DialogView_AIntregrantes.findViewById<Button>(R.id.BTN_Aceptar_integrantes)


                            Recy_Agregar_I.layoutManager = LinearLayoutManager(this);

                            Recy_Agregar_I.adapter = AdaptAIntegrantesSubGrupo

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

                            CargarListaSubgrupo()

                        }

                        AlertSB.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

                        Aceptar_SB.setOnClickListener {

                            nuevoSubgrupo.nombre = nombreSubgrupo.text.toString()
                            if (nuevoSubgrupo.nombre.isEmpty()) {
                                nombreSubgrupo.error = "Selecciona un nombre"
                                nombreSubgrupo.requestFocus()
                                return@setOnClickListener
                            }
                            nuevoSubgrupo.deGrupo = grupoActual.nombre
                            nuevoSubgrupo.correo_usuarios!!.add(grupoActual.admin)
                            nuevoSubgrupo.foto = grupoActual.foto
                            nuevoSubgrupo.admin = grupoActual.admin
                            //              Grupo Prueba_Grupo Prueba 2

                            gruposRef.child(nuevoSubgrupo.nombre).get()
                                    .addOnSuccessListener {
                                        if (it.exists()) {
                                            Toast.makeText(this, "Ya está en uso ese Nombre", Toast.LENGTH_SHORT).show()
                                        } else {
                                            gruposRef.child(nuevoSubgrupo.nombre).setValue(nuevoSubgrupo)
                                            Edit_Nombre_SB.startAnimation(animationout)
                                            LayDialog.startAnimation(animationout)
                                            Agregar_I_SB.startAnimation(animationout)
                                            Aceptar_SB.startAnimation(animationout)
                                            AlertSB.dismiss()
                                            Toast.makeText(this, "Se ha creado el SubGrupo: ${nuevoSubgrupo.nombre}", Toast.LENGTH_SHORT).show()
                                        }
                                    }


                        }
                    }
                    }
                }

                R.id.menu_modificar_grupo -> {
                        loading.startLoading("Cargando Listas de Usuarios")
                        val DialogView_A = layoutInflater.inflate(R.layout.dialog_modificar_integrantes, null)
                        val Dialog_Agregar_I = android.app.AlertDialog.Builder(this).setView(DialogView_A)

                        val RDialog = DialogView_A.findViewById<RelativeLayout>(R.id.RelLay_DialogAgregar)
                        val Recy_Agregar_I = DialogView_A.findViewById<RecyclerView>(R.id.Dialog_Recy_Agregar_integrantes)
                        val Aceptar = DialogView_A.findViewById<Button>(R.id.BTN_Aceptar_A_integrantes)

                        Recy_Agregar_I.layoutManager = LinearLayoutManager(this);

                        Recy_Agregar_I.adapter = AdaptAIntegrantesModificar



                        Recy_Agregar_I.startAnimation(animation)
                        RDialog.startAnimation(animation2)
                        Aceptar.startAnimation(animation2)
                        val AlertD = Dialog_Agregar_I.show()

                        AlertD.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

                        Aceptar.setOnClickListener {
                            gruposRef.child(modGrupo.nombre).setValue(modGrupo)
                                    .addOnSuccessListener {
                                        grupoActual = modGrupo
                                        Recy_Agregar_I.startAnimation(animationout)
                                        RDialog.startAnimation(animationout)
                                        Aceptar.startAnimation(animationout)
                                        AlertD.dismiss()
                                    }

                        }
                        if(grupoActual.deGrupo.isNotEmpty())
                            CargarListaSubgrupoModificar()
                        else
                            CargarListaModificar()

                }
                else -> {

                }
            }
            true
        }

        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(userActual.encriptado)
                        userActual.desencriptarUsuario()
                    cambiarFragmento(Frag_Muro(), "Muro")
                    //nuevoGrupo.admin = userActual.correo
                    //nuevoGrupo.correo_usuarios!!.add(userActual.correo)

                    //CargarListaModificar()

                }

    }

    override fun AddtoList(correo: String, position: String, estatus:Int) {
        when(estatus){
            1 ->{ //Modificar
                modGrupo.correo_usuarios!!.add(correo)
            }
            2 ->{ //Subgrupo
                nuevoSubgrupo.correo_usuarios!!.add(correo)
            }
        }
    }

    override fun RemoveFromList(correo: String, position: String, estatus:Int) {
        when(estatus){
            1 ->{ //Modificar
                modGrupo.correo_usuarios!!.remove(correo)
            }
            2 ->{ //Subgrupo
                nuevoSubgrupo.correo_usuarios!!.remove(correo)
            }
        }
    }

    fun CargarListaModificar(){

        userRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    listaIntegrantesModificar.clear()
                    for (u in snapshot.children) {
                        val user = u.getValue(Usuario::class.java) as Usuario
                        if(user.encriptado)
                            user.desencriptarUsuario()
                        if (user.correo != userActual.correo)
                            listaIntegrantesModificar.add(user)
                    }
                }

                AdaptAIntegrantesModificar.notifyDataSetChanged()
                if(loading!=null)
                 loading.isDismiss()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun CargarListaSubgrupoModificar(){

        gruposRef.child(grupoActual.deGrupo).get()
                .addOnSuccessListener {
                    if(it.exists()){
                        val deGpo = it.getValue(Grupos::class.java) as Grupos
                        userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()) {
                                    listaIntegrantesModificar.clear()
                                    for (u in snapshot.children) {
                                        val user = u.getValue(Usuario::class.java) as Usuario
                                        if(user.encriptado)
                                            user.desencriptarUsuario()
                                        if (user.correo != userActual.correo) {
                                            for (u in deGpo.correo_usuarios!!){
                                                if(u == user.correo) {
                                                    listaIntegrantesModificar.add(user)
                                                    break
                                                }
                                            }

                                        }
                                    }
                                }
                                if(loading != null)
                                    loading.isDismiss()
                                AdaptAIntegrantesModificar.notifyDataSetChanged()

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                }

    }

    fun CargarListaSubgrupo(){

        userRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    listaIntegrantesSubgrupo.clear()
                    for (u in snapshot.children) {
                        val user = u.getValue(Usuario::class.java) as Usuario
                        if(user.encriptado)
                            user.desencriptarUsuario()
                        if (user.correo != userActual.correo) {
                            for (u in grupoActual.correo_usuarios!!){
                                if(u == user.correo) {
                                    listaIntegrantesSubgrupo.add(user)
                                    break
                                }
                            }

                        }
                    }
                }
                if(loading != null)
                    loading.isDismiss()
                AdaptAIntegrantesSubGrupo.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}