package com.clases.proyecto_poi_arsaga

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.clases.proyecto_poi_arsaga.Adaptadores.AdaptorChat
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat__grupal.*
import kotlinx.android.synthetic.main.activity_entregar_tarea.*
import java.util.*


class Chat_Grupal : AppCompatActivity(), AdaptorChat.OnPubliClickListen {

    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")

    val listamensajes = mutableListOf<Mensaje>()
    var TipoC: Int = 0
    private var userActual: Usuario = Usuario()
    //var usuarioSeleccionado: Usuario? = null
    var ChatDirecto: ChatDirecto? = null
    private var adaptadorChat = AdaptorChat(this, listamensajes, TipoC, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__grupal)

        val MenuChat = findViewById<Toolbar>(R.id.Menu_Chat)

        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener {
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(intent.extras != null){
                        //usuarioSeleccionado = intent.getSerializableExtra("UsuarioSeleccionado") as Usuario
                        //userActual = intent.getSerializableExtra("userActual") as Usuario
                        ChatDirecto = intent.getSerializableExtra("ChatDirecto") as ChatDirecto
                        if(ChatDirecto!!.usuario2 == "Grupal") {
                            TV_Nombre_Chat.text = ChatDirecto!!.usuario1

                            Picasso.get().load(ChatDirecto!!.fotoUsuario1).into(IV_CHAT_HEADER)
                        }
                        else {
                            if (ChatDirecto!!.usuario1 == userActual!!.correo) {
                                TV_Nombre_Chat.text = ChatDirecto!!.nombre2
                                Picasso.get().load(ChatDirecto!!.fotoUsuario2).into(IV_CHAT_HEADER)
                            }
                            else {
                                TV_Nombre_Chat.text = ChatDirecto!!.nombre1
                                Picasso.get().load(ChatDirecto!!.fotoUsuario1).into(IV_CHAT_HEADER)
                            }
                        }
                        TipoC = intent.getIntExtra("Tipo", 0)



                    }else{
                        TV_Nombre_Chat.text = "Desconocido"
                    }
                    adaptadorChat = AdaptorChat(this, listamensajes, TipoC, this)
                    RV_chat_grupal.adapter = adaptadorChat

                    cargarMensajes()
                }

        BT_back_chats_grupal.setOnClickListener {
            /*val intent = Intent(this@Chat_Grupal, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("userActual", userActual)
            startActivity(intent)*/
            finish()
        }

        BT_enviar_mensaje.setOnClickListener {
            val mensajeEscrito = TX_mensaje.text.toString()
            if(mensajeEscrito.isEmpty()){
                //Toast.makeText(this, "Mensaje en blanco", Toast.LENGTH_SHORT).show()
            }
            else{
                //val chatmensaje = ChatMensaje("Juan", mensajeEscrito, Date(), false, 0, true)
                /*val chatRespuesta = ChatMensaje("Lucas", "No estoy", Date(), false, 0, false)
                listamensajes.add(chatRespuesta)*/

                val chatmensaje = Mensaje(
                    userActual!!.correo,
                    userActual!!.nombre,
                    false,
                    ServerValue.TIMESTAMP,
                    mensajeEscrito,
                    "Texto"
                )
                agregarMensaje(chatmensaje)
            }
        }

        MenuChat.setOnMenuItemClickListener {

            when(it.itemId){

                R.id.menu_Archivos -> {
                    subirArchivo()
                }
                R.id.menu_Ubicacion -> {
                    revisarPermisos()
                }
                R.id.menu_Imagen -> {
                    subirImagen()
                }
                else -> {

                }
            }
            true
        }
    }

    private fun subirArchivo() {
        val F_intent = Intent(Intent.ACTION_GET_CONTENT)
        F_intent.type = "application/*"
        startActivityForResult(F_intent, 3)
    }

    private fun subirImagen() {
        val F_intent = Intent(Intent.ACTION_PICK)
        F_intent.type = "image/*"
        startActivityForResult(F_intent, 2)
    }

    private fun cargarMensajes(){
        var cargarMensajeRef = database.getReference().child("ChatLog").child(ChatDirecto!!.id)

        cargarMensajeRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    listamensajes.clear()
                    for (m in snapshot.children) {
                        val mensaje = m.getValue(Mensaje::class.java) as Mensaje;
                        if (mensaje.de == userActual!!.correo)
                            mensaje.esMio = true
                        listamensajes.add(mensaje)
                    }

                    adaptadorChat.notifyDataSetChanged()
                    RV_chat_grupal.scrollToPosition(listamensajes.size - 1)

                }
            }
        })
    }

    private fun agregarMensaje(mensaje: Mensaje){

        database.getReference().child("ChatLog").child(ChatDirecto!!.id).push().setValue(mensaje)
        if(ChatDirecto!!.usuario2 != "Grupal") {
            ChatDirecto!!.timeStamp = mensaje.timeStamp
            ChatDirecto!!.ultimoMensajeDe = userActual!!.correo
            when(mensaje.tipoMensaje){
                "Imagen" -> {
                    ChatDirecto!!.ultimoMensaje = "Imagen Enviada"
                }
                "Texto" -> {
                    ChatDirecto!!.ultimoMensaje = mensaje.mensaje
                }
                "Archivo" -> {
                    ChatDirecto!!.ultimoMensaje = "Archivo Enviado"
                }
                "Ubicacion" -> {
                    ChatDirecto!!.ultimoMensaje = "Ubicación Enviada"
                }
                else -> {
                    ChatDirecto!!.ultimoMensaje = mensaje.mensaje
                }
            }


            database.getReference().child("ChatDirecto").child(ChatDirecto!!.id).setValue(
                ChatDirecto
            )
        }
        TX_mensaje.text.clear()
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Chat_Grupal, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        //intent.putExtra("userActual", userActual)
        startActivity(intent)
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            1 -> {
                if (resultCode == RESULT_OK) {
                    val direccionSelect =
                        data?.getSerializableExtra("Ubicacion") as GlobalPositioningSystem
                    val chatmensaje = Mensaje(
                        userActual!!.correo,
                        userActual!!.nombre,
                        false,
                        ServerValue.TIMESTAMP,
                        direccionSelect.toString(),
                        "Ubicacion",
                        "",
                        direccionSelect
                    )
                    agregarMensaje(chatmensaje)
                } else {
                    //Toast.makeText(this, "No hay Ubicación", Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
                if (resultCode == RESULT_OK && data != null) {
                    val uri = data.data
                    data.data?.let { returnUri ->
                        contentResolver.query(returnUri, null, null, null, null)
                    }?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        //val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        cursor.moveToFirst()
                        var nameFile = cursor.getString(nameIndex)
                        val chatmensaje = Mensaje(
                            userActual!!.correo,
                            userActual!!.nombre,
                            false,
                            ServerValue.TIMESTAMP,
                            nameFile,
                            "Imagen",
                            ""
                        )
                        uploadImageToStorage(uri, chatmensaje)


                    }

                } else {
                    //Toast.makeText(this, "No hay Ubicación", Toast.LENGTH_SHORT).show()
                }
            }

            3 -> {
                if (resultCode == RESULT_OK && data != null) {
                    val uri = data.data
                    data.data?.let { returnUri ->
                        contentResolver.query(returnUri, null, null, null, null)
                    }?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        //val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        cursor.moveToFirst()
                        var nameFile = cursor.getString(nameIndex)
                        val chatmensaje = Mensaje(
                            userActual!!.correo,
                            userActual!!.nombre,
                            false,
                            ServerValue.TIMESTAMP,
                            nameFile,
                            "Archivo",
                            ""
                        )
                        uploadFileToStorage(uri, chatmensaje)


                    }

                } else {
                    //Toast.makeText(this, "No hay Ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun uploadFileToStorage(uri: Uri?, mensaje: Mensaje) {
        val filename = mensaje.mensaje + UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/chat/files/$filename")
        ref.putFile(uri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    mensaje.url = it.toString()
                    agregarMensaje(mensaje)
                }.addOnFailureListener {
                    //if(loading != null)
                    //loading.isDismiss()
                    Toast.makeText(this@Chat_Grupal, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                //if(loading != null)
                //loading.isDismiss()
                Toast.makeText(this@Chat_Grupal, it.message, Toast.LENGTH_SHORT).show()
            }

    }

    private fun uploadImageToStorage(uri: Uri?, mensaje: Mensaje){
        val filename = mensaje.mensaje + UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/chat/images/$filename")
        ref.putFile(uri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    mensaje.url = it.toString()
                    agregarMensaje(mensaje)
                }.addOnFailureListener {
                    //if(loading != null)
                        //loading.isDismiss()
                    Toast.makeText(this@Chat_Grupal, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                //if(loading != null)
                    //loading.isDismiss()
                Toast.makeText(this@Chat_Grupal, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Se requiere aceptar el permiso", Toast.LENGTH_SHORT).show()
                revisarPermisos()
            } else {
                Toast.makeText(this, "Permisio concedido", Toast.LENGTH_SHORT).show()
                abrirMapa()
            }
        }
    }

    private fun abrirMapa() {
        var u_intent = Intent(this, MapsActivity::class.java)
        u_intent.putExtra("Opcion", 1)
        startActivityForResult(u_intent, 1)
    }


    private fun revisarPermisos() {
        // Apartir de Android 6.0+ necesitamos pedir el permiso de ubicacion
        // directamente en tiempo de ejecucion de la app
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tenemos permiso para la ubicacion
            // Solicitamos permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,

                    ),
                1
            )
        } else {
            // Ya se han concedido los permisos anteriormente
            abrirMapa()
        }
    }

    override fun onitemClick(mensaje: Mensaje) {
        when(mensaje.tipoMensaje){
            "Texto" -> {
                Toast.makeText(this, "Texto", Toast.LENGTH_SHORT).show()
            }
            "Imagen" -> {
                var u_intent = Intent(this, MapsActivity::class.java)
                u_intent.putExtra("Opcion", 3)
                u_intent.putExtra("Imagen", mensaje.url)
                u_intent.putExtra("ImagenNombre", mensaje.mensaje)
                startActivity(u_intent)
                //Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show()
            }
            "Archivo" -> {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                Toast.makeText(this, "Pulsaste que si", Toast.LENGTH_SHORT).show()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                Toast.makeText(this, "Pulsaste que no", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
            }
            "Ubicacion" -> {
                var u_intent = Intent(this, MapsActivity::class.java)
                u_intent.putExtra("Opcion", 2)
                u_intent.putExtra("latitud", mensaje.gps.latitude)
                u_intent.putExtra("longitud", mensaje.gps.longitude)
                startActivity(u_intent)
            }
            else -> {
                Toast.makeText(this, "Ojo cuidado", Toast.LENGTH_SHORT).show()
            }
        }
    }

}