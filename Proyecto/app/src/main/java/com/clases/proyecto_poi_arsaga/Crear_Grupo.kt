package com.clases.proyecto_poi_arsaga

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Integrantes
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialog
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_crear_grupo.*
import kotlinx.android.synthetic.main.drawe_modperfil.*
import java.util.*
import kotlin.random.Random

class Crear_Grupo : AppCompatActivity(), Adaptador_Integrantes.OnItemGrupoClickListener {

    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("Usuarios")
    private val grupoRef = database.getReference("Grupos")
    var listaIntegrantes = mutableListOf<Usuario>()
    private var nuevoGrupo :Grupos = Grupos()
    private lateinit var loading : LoadingDialog
    val AdaptIntegrantes = Adaptador_Integrantes(listaIntegrantes,nuevoGrupo.correo_usuarios!!,  this);
    private var uri : Uri? = null
    var defaultImages = mutableListOf<String>()


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_crear_grupo)
        loading = LoadingDialog(this)
        loading.startLoading()
        nuevoGrupo.admin = MainActivity.userActual!!.correo
        nuevoGrupo.correo_usuarios!!.add(MainActivity.userActual!!.correo)
        llenarDefaultImages()

        val view = layoutInflater.inflate(R.layout.dialog_integrantes, null);
        val Integrantes = PopupWindow(this)
        val IntegrantesReecycler = view.findViewById<RecyclerView>(R.id.Dialog_Recycler_Integrantes)
        val EsLinear = LinearLayoutManager(this);

        val animation = AnimationUtils.loadAnimation(this, R.anim.dialog_itegrantes)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.dialog_itegrantes_2)
        val animationout = AnimationUtils.loadAnimation(this, R.anim.dialog_integrantes_out)
        val RLay = view.findViewById<RelativeLayout>(R.id.RelLay_Dialog)
        val Aceptar = view.findViewById<Button>(R.id.BTN_Aceptar_integrantes)

        val LLay = findViewById<ConstraintLayout>(R.id.ID_CrearGrupo)
        val Key_Edit = findViewById<EditText>(R.id.ET_Nombre_grupo)

        BT_back_CG.setOnClickListener{
            finish();
        }
        Key_Edit.setOnClickListener {
            IntegrantesReecycler.startAnimation(animationout)
            RLay.startAnimation(animationout)
            Aceptar.startAnimation(animationout)
            Handler().postDelayed({Integrantes?.dismiss();}, animationout.duration)
        }
        Select_Grupofoto.setOnClickListener {
            val F_intent = Intent(Intent.ACTION_PICK)
            F_intent.type = "image/*"
            startActivityForResult(F_intent, 1)
        }

        BTN_Agegrar_integrantes.setOnClickListener {

            Integrantes.contentView = view;
            IntegrantesReecycler.layoutManager = EsLinear;
            IntegrantesReecycler.adapter = AdaptIntegrantes;

            Integrantes.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            IntegrantesReecycler.startAnimation(animation)
            RLay.startAnimation(animation2)
            Aceptar.startAnimation(animation2)

            Aceptar.setOnClickListener {
                IntegrantesReecycler.startAnimation(animationout)
                RLay.startAnimation(animationout)
                Aceptar.startAnimation(animationout)
                Handler().postDelayed({Integrantes?.dismiss();}, animationout.duration)
            }
            Integrantes.showAtLocation(BTN_Agegrar_integrantes, Gravity.CENTER,0, 0);
        }

        LLay.setOnClickListener {
            IntegrantesReecycler.startAnimation(animationout)
            RLay.startAnimation(animationout)
            Aceptar.startAnimation(animationout)
            Handler().postDelayed({Integrantes?.dismiss();}, animationout.duration)
        }

        BTN_Crar_grupo.setOnClickListener {

            nuevoGrupo.nombre = ET_Nombre_grupo.text.toString()
            if(nuevoGrupo.nombre.isEmpty()){

                ET_Nombre_grupo.error = "Introduce un nombre"
                ET_Nombre_grupo.requestFocus()
                return@setOnClickListener
            }
            loading.startLoading()
            grupoRef.child(nuevoGrupo.nombre).get()
                    .addOnSuccessListener {
                        if(it.exists()) {
                            loading.isDismiss()
                            ET_Nombre_grupo.error = "Ya hay un grupo con ese nombre"
                            ET_Nombre_grupo.requestFocus()
                            return@addOnSuccessListener
                        }
                        else{

                            if(uri!=null){
                                uploadImageToStorage()
                            }else{
                                nuevoGrupo.foto = getRandomImage()
                                addGroup()
                            }
                        }
                    }
        }
        llenarlista()
    }



    private fun uploadImageToStorage(){
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/grupos/$filename")
        ref.putFile(uri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        nuevoGrupo.foto = it.toString()
                        addGroup()
                    }.addOnFailureListener {
                        loading.isDismiss()
                        Toast.makeText(this@Crear_Grupo, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    loading.isDismiss()
                    Toast.makeText(this@Crear_Grupo, it.message, Toast.LENGTH_SHORT).show()
                }
    }
    private fun addGroup(){
        grupoRef.child(nuevoGrupo.nombre).setValue(nuevoGrupo)
        val intent = Intent(this@Crear_Grupo, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        val u = MainActivity.userActual
        intent.putExtra("userActual", u)
        loading.isDismiss()
        startActivity(intent)
    }

    private fun llenarDefaultImages(){
        defaultImages.add("https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fgrupos%2Fdefault%2F1.png?alt=media&token=e320bbd9-642e-4355-80cd-7b4886bda254")
        defaultImages.add("https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fgrupos%2Fdefault%2F2.png?alt=media&token=c5762501-88cc-488e-a574-095c25805c72")
        defaultImages.add("https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fgrupos%2Fdefault%2F3.png?alt=media&token=5d922f53-26e1-4a19-b4f2-882cd124fe44")
        defaultImages.add("https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fgrupos%2Fdefault%2F4.png?alt=media&token=ffc697cc-1522-48b5-aaff-9176062c7bb0")
        defaultImages.add("https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fgrupos%2Fdefault%2F5.png?alt=media&token=074390d6-e043-4dfe-8d87-919619c3b9b5")
        defaultImages.add("https://firebasestorage.googleapis.com/v0/b/app-poi-15c77.appspot.com/o/images%2Fgrupos%2Fdefault%2F6.png?alt=media&token=c0219ed4-7fd2-479d-80d2-31265330c9fd")

    }

    private fun getRandomImage() : String{
        var i = Random.nextInt(0,6)
        return defaultImages[i]
    }

    fun llenarlista(){
        userRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (u in snapshot.children) {
                        val user = u.getValue(Usuario::class.java) as Usuario
                        if (user.correo != MainActivity.userActual?.correo)
                            listaIntegrantes.add(user)
                    }
                }
                loading.isDismiss()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun AddtoList(correo: String, position: String) {
        nuevoGrupo.correo_usuarios!!.add(correo)
        Log.d("nuevoGrupo", "Se añadió: $correo")
        //Toast.makeText(this, "${listaChecked.size}", Toast.LENGTH_SHORT).show()
    }

    override fun RemoveFromList(correo: String, position: String) {
        nuevoGrupo.correo_usuarios!!.remove(correo)
        Log.d("nuevoGrupo", "Se removió: $correo")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data

            Picasso.get().load(uri.toString()).into(Muestra_Grupoimagen)
            Select_Grupofoto.alpha = 0f

        }
    }
}