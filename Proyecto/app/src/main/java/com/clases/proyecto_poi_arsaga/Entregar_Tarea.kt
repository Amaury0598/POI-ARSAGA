package com.clases.proyecto_poi_arsaga

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import com.clases.proyecto_poi_arsaga.Adaptadores.AdaptorChat
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import kotlinx.android.synthetic.main.activity_entregar_tarea.*
import java.util.*
import java.util.jar.Manifest

class Entregar_Tarea : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")
    private val tareasEntregadasRef = database.getReference("TareasEntregadas")
    private val tareasUsuariosRef = database.getReference("TareasUsuarios")
    private var userActual: Usuario = Usuario()
    private var archivos = Multimedia()
    private var uri : Uri? = null
    private lateinit var nameFile : String
    private lateinit var loading: LoadingDialog
    private var sePagaConCoins : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entregar_tarea)
        loading = LoadingDialog(this)
        loading.startLoading("Cargando datos")
        var estatus = intent.getIntExtra("estatus",4)


        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener {
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    when(estatus){
                        0 -> {
                            Pendiente()

                        }
                        1 -> {
                            Entregada()

                        }
                        2 -> {
                            No_Entregada()

                        }
                        3 -> {
                            VerTarea()
                        }
                        4 ->{
                            finish()
                        }
                    }
                    if(loading != null)
                        loading.isDismiss()
                }


    }

    private fun Pendiente(){

        nameFile = ""



        var tarea = intent.getSerializableExtra("tareaActual") as Tareas
        BT_back_EntregarT.setOnClickListener {
            finish()
        }

        TV_Entrega_Archivo.setOnClickListener {
            val F_intent = Intent(Intent.ACTION_GET_CONTENT)
            F_intent.type = "*/*"
            startActivityForResult(F_intent, 1)
        }

        BTN_Entregar_Tarea.setOnClickListener {
            if(archivos.nombreArchivo.isEmpty()){
                TV_Entrega_Archivo.setTextColor(Color.parseColor("#C70039"))
                Toast.makeText(this, "Escoge el archivo a subir", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loading.startLoading("Entregando Tarea")
            addToStorage(tarea)


        }

        TV_Entrega_nombre_tarea.text = tarea.nombre
        TV_Entrega_nombre_grupo.text = General_Grupos.grupoActual.nombre
        TV_Entrega_Decripcion.text = tarea.desc
        TV_Entrega_Vencimiento.text = tarea.fecha
        TV_Entrega_Coins.text = TV_Entrega_Coins.text.toString() + tarea.coins
        Picasso.get().load(General_Grupos.grupoActual.foto).into(IMG_Entrega_imagen)
    }

    private fun VerTarea(){




        var tarea = intent.getSerializableExtra("tareaActual") as Tareas
        var correo = intent.getStringExtra("correo")
        BT_back_EntregarT.setOnClickListener {
            finish()
        }

        TV_Entrega_Archivo.visibility = View.GONE
        imageView3.visibility = View.GONE

        BTN_Entregar_Tarea.visibility = View.GONE

        TV_Entrega_nombre_tarea.text = tarea.nombre
        TV_Entrega_nombre_grupo.text = General_Grupos.grupoActual.nombre
        TV_Entrega_Decripcion.text = tarea.desc
        TV_Entrega_Vencimiento.text = tarea.fecha
        TV_Entrega_Coins.text = TV_Entrega_Coins.text.toString() + tarea.coins
        Picasso.get().load(General_Grupos.grupoActual.foto).into(IMG_Entrega_imagen)
        tareasEntregadasRef.child(tarea.id).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(it in snapshot.children){
                    val tareaEnt = it.getValue(TareaEntregada::class.java) as TareaEntregada
                    if(tareaEnt.correo == correo){


                            TV_Entrega_Nombre_Archivo.text = tareaEnt.multimedia.nombreArchivo
                            TV_Entrega_Url_Archivo.text = tareaEnt.multimedia.urlArchivo
                            TV_Entrega_Nombre_Archivo.setTextColor(Color.parseColor("#00CBE6"))

                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        TV_Entrega_Nombre_Archivo.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }else{
                    startDownloading()
                }
            }else{
                startDownloading()
            }
        }




    }

    private fun Entregada(){

        var downloadID : Long = 0


        var tarea = intent.getSerializableExtra("tareaActual") as Tareas
        BT_back_EntregarT.setOnClickListener {
            finish()
        }

        TV_Entrega_Archivo.visibility = View.GONE
        imageView3.visibility = View.GONE

        BTN_Entregar_Tarea.visibility = View.GONE

        TV_Entrega_nombre_tarea.text = tarea.nombre
        TV_Entrega_nombre_grupo.text = General_Grupos.grupoActual.nombre
        TV_Entrega_Decripcion.text = tarea.desc
        TV_Entrega_Vencimiento.text = tarea.fecha
        TV_Entrega_Coins.text = TV_Entrega_Coins.text.toString() + tarea.coins
        Picasso.get().load(General_Grupos.grupoActual.foto).into(IMG_Entrega_imagen)
        tareasEntregadasRef.child(tarea.id).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(it in snapshot.children){
                    val tareaEnt = it.getValue(TareaEntregada::class.java) as TareaEntregada
                    if(tareaEnt.correo == userActual.correo){

                            TV_Entrega_Nombre_Archivo.text = tareaEnt.multimedia.nombreArchivo
                            TV_Entrega_Url_Archivo.text = tareaEnt.multimedia.urlArchivo
                            TV_Entrega_Nombre_Archivo.setTextColor(Color.parseColor("#00CBE6"))
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //SI SE HARÁ EN UN RECYCLER VIEW, ESTO SERÍA EL ONITEMCLICK
        TV_Entrega_Nombre_Archivo.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }else{
                    startDownloading()
                }
            }else{
                startDownloading()
            }
        }




    }

    private fun startDownloading() {
        val url = TV_Entrega_Url_Archivo.text.toString()
        val nombre = TV_Entrega_Nombre_Archivo.text.toString()

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(nombre)
        request.setDescription("Descargando...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloading()
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun No_Entregada(){




        var tarea = intent.getSerializableExtra("tareaActual") as Tareas
        BT_back_EntregarT.setOnClickListener {
            finish()
        }

        TV_Entrega_Archivo.setTextColor(Color.parseColor("#C70039"))

        BTN_Entregar_Tarea.visibility = View.GONE
        TV_Entrega_Nombre_Archivo.visibility = View.GONE
        TV_Entrega_Vencimiento.setTextColor(Color.parseColor("#C70039"))

        TV_Entrega_nombre_tarea.text = tarea.nombre
        TV_Entrega_nombre_grupo.text = General_Grupos.grupoActual.nombre
        TV_Entrega_Decripcion.text = tarea.desc
        TV_Entrega_Vencimiento.text = tarea.fecha
        TV_Entrega_Coins.text = TV_Entrega_Coins.text.toString() + tarea.coins
        Picasso.get().load(General_Grupos.grupoActual.foto).into(IMG_Entrega_imagen)
        TV_Entrega_Nombre_Archivo.text = ""
        TV_Entrega_Nombre_Archivo.setTextColor(Color.parseColor("#00CBE6"))
        sePagaConCoins = Date_ASG.canPayWithASGCoins(tarea.fecha)
        if(sePagaConCoins){
            BTN_Entregar_Tarea_Coins.visibility = View.VISIBLE
            TV_Entrega_Nombre_Archivo.visibility = View.VISIBLE
            nameFile = ""

            BTN_Entregar_Tarea_Coins.setOnClickListener {
                if(archivos.nombreArchivo.isEmpty()){
                    TV_Entrega_Archivo.setTextColor(Color.parseColor("#C70039"))
                    Toast.makeText(this, "Escoge el archivo a subir", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(userActual.coins < 100){
                    Toast.makeText(this, "Debes contar con 100 ASG Coins", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                userActual.coins -= 100
                loading.startLoading("Entregando Tarea")
                addToStorage(tarea)
            }

            TV_Entrega_Archivo.setOnClickListener {
                val F_intent = Intent(Intent.ACTION_GET_CONTENT)
                F_intent.type = "*/*"
                startActivityForResult(F_intent, 1)
            }
        }

    }

    private fun addToStorage(tarea : Tareas){



            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/tareas/archivos/$filename")
            ref.putFile(uri!!)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            archivos.urlArchivo = it.toString()

                            updateData(tarea)
                        }.addOnFailureListener {
                            if(loading != null)
                                loading.isDismiss()
                            Toast.makeText(this@Entregar_Tarea, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }


    }

    private fun updateData(tarea: Tareas){
        val tareaEntrega = TareaEntregada(tarea.id, userActual.correo, userActual.nombre, userActual.imagen, archivos)
        tareasEntregadasRef.child(tarea.id).push().setValue(tareaEntrega)
        userActual.coins += tarea.coins
        userRef.child(auth.uid.toString()).setValue(userActual)
        tareasUsuariosRef.child(tarea.id).get().addOnSuccessListener { it ->
            for (c in it.children) {
                val tareasUsuarios = c.getValue(lTareaUsuarios::class.java) as lTareaUsuarios
                var i = -1
                for (l in tareasUsuarios.listaUsuarios) {
                    i++
                    if (l.correo == userActual.correo) {
                        break
                    }
                }

                tareasUsuarios.listaUsuarios[i].status = "Entregada"
                tareasUsuariosRef.child(tarea.id).child(tareasUsuarios.id).setValue(tareasUsuarios)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if(sePagaConCoins)
                                    userRef.child(auth.uid.toString()).setValue(userRef)
                                if(loading != null)
                                    loading.isDismiss()

                                finish()
                            }
                        }
                        .addOnFailureListener { error ->
                            if(loading != null)
                                loading.isDismiss()
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            loading.startLoading("Cargando archivo")
            uri = data.data!!
            data.data?.let { returnUri ->
                contentResolver.query(returnUri, null, null, null, null)
            }?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                //val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                nameFile = cursor.getString(nameIndex)
                archivos = Multimedia(nameFile, uri.toString())
                TV_Entrega_Nombre_Archivo.text = nameFile
                TV_Entrega_Nombre_Archivo.setTextColor(Color.parseColor("#00CBE6"))
                TV_Entrega_Archivo.setTextColor(Color.parseColor("#FFFFFF"))

            }
            if(loading != null)
                loading.isDismiss()
        }
    }
}