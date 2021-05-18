package com.clases.proyecto_poi_arsaga

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.*
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Asignar_nueva_tarea : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val database = FirebaseDatabase.getInstance();
    private val tareaRef = database.getReference("Tareas"); //crear "rama" (tabla)
    private val tareaUsuariosRef = database.getReference("TareasUsuarios")
    private val auth = FirebaseAuth.getInstance()
    private lateinit var loading : LoadingDialog



    var SaveYear = 0
    var SaveMonth = 0
    var SaveDay = 0

    //                                                  11/05/2021

    private lateinit var actualDate : String

    private fun formatDate() : String{
        var fDay = SaveDay.toString()
        var fMonth = SaveMonth.toString()
        if (SaveDay < 10){
            fDay = "0$fDay"
        }
        if (SaveMonth < 10){
            fMonth = "0$fMonth"
        }
        return "$fDay/$fMonth/$SaveYear"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)
        loading = LoadingDialog(this)
        actualDate = Date_ASG.getActualDate()
        val Crear = findViewById<Button>(R.id.BT_CrearTarea)
        val Back = findViewById<ImageButton>(R.id.BT_back_CT)
        val FechaLimite = findViewById<EditText>(R.id.ET_Asignar_fecha_tarea)
        val nombreTarea = findViewById<EditText>(R.id.ET_Asignar_nombre_tarea)
        val descTarea = findViewById<EditText>(R.id.ET_Asignar_descripcion_tarea)
        val checkASG_Coins = findViewById<CheckBox>(R.id.checkBox3)
        val ASG_Coins = findViewById<EditText>(R.id.ET_Asignar_cantidad)
        val nombreGrupo = findViewById<TextView>(R.id.TV_NombreGrupo_AT)

        val grupoActual = General_Grupos.grupoActual
        nombreGrupo.text = grupoActual.nombre
        var tarea = Tareas()

        val cal = Calendar.getInstance()
        val Day = cal.get(Calendar.DAY_OF_MONTH)
        val Month = cal.get(Calendar.MONTH)
        val Year = cal.get(Calendar.YEAR)

        Back.setOnClickListener {
            finish()
        }

        Crear.setOnClickListener {
            FechaLimite.error = null
            val fechaLim = FechaLimite.text.toString()
            val nombre = nombreTarea.text.toString()
            val desc = descTarea.text.toString()
            var coins = ASG_Coins.text.toString().toIntOrNull()
            if(coins == null)
                coins = 0


            if(nombre.isEmpty()){
                nombreTarea.error = "Introduce un nombre"
                nombreTarea.requestFocus()
                return@setOnClickListener
            }
            if(desc.isEmpty()){
                descTarea.error = "Introduce una descripción"
                descTarea.requestFocus()
                return@setOnClickListener
            }
            if(fechaLim.isEmpty()){
                FechaLimite.error = "Introduce una fecha válida"
                FechaLimite.requestFocus()
                return@setOnClickListener
            }
            if(!Date_ASG.compareToActualDate(formatDate())){
                FechaLimite.error = "Introduce una fecha válida"
                FechaLimite.requestFocus()
                return@setOnClickListener
            }
            if(checkASG_Coins.isChecked){
                if(coins < 1){
                    ASG_Coins.error = "Introduce una cantidad válida"
                    ASG_Coins.requestFocus()
                    return@setOnClickListener
                }

            }
            loading.startLoading("Creando tarea")
            tarea = Tareas(nombre, desc, "", formatDate(), grupoActual.foto, coins)
            val nuevaTarea = tareaRef.child(grupoActual.nombre).push()
            tarea.id = nuevaTarea.key.toString()
            nuevaTarea.setValue(tarea)

            var listaTareaUsuarios = lTareaUsuarios()
            listaTareaUsuarios.fecha = tarea.fecha
            for(c in grupoActual.correo_usuarios!!){
                listaTareaUsuarios.listaUsuarios.add(TareaUsuarios(c, "Pendiente"))
            }
            val key = tareaUsuariosRef.child(tarea.id).push()
            listaTareaUsuarios.id = key.key.toString()
            key.setValue(listaTareaUsuarios)
                    .addOnSuccessListener {
                        sendEmail(tarea)
                        if(loading != null)
                            loading.isDismiss()
                        finish()
                    }


        }

        FechaLimite.setOnClickListener {
            DatePickerDialog(this, this, Year, Month, Day).show()
            Log.d("fecha", FechaLimite.text.toString())

        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        SaveDay = dayOfMonth
        SaveMonth = month
        SaveYear = year
        ET_Asignar_fecha_tarea.setText(formatDate())

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }

    private fun sendEmail(tarea : Tareas){
        val username = "Zi3WkVRdrWko3KG5dfAgJi1MCTHiTaK/dSyrUZlsx80"
        val password = "N5OQR7b4c8kcapzewOqMFw"
        val emailFrom = Encrypt.descencriptar(username, "correo")
        val emailTo = General_Grupos.grupoActual.admin
        var emailCC = fillEmailCC()


        val subject = "Nueva tarea creada en: " + General_Grupos.grupoActual.nombre
        val text = "Nombre de la tarea: " + tarea.nombre + "\n\n" + "Descripción de la tarea: " + tarea.desc + "\n\n" + "Fecha de vencimiento: " + tarea.fecha

        val props = Properties()
        putIfMissing(props, "mail.smtp.host", "smtp.gmail.com")
        putIfMissing(props, "mail.smtp.port", "587")
        putIfMissing(props, "mail.smtp.auth", "true")
        putIfMissing(props, "mail.smtp.starttls.enable", "true")

        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(Encrypt.descencriptar(username, "correo"), Encrypt.descencriptar(password, "correo"))
            }
        })

        session.debug = true

        val mimeMessage = MimeMessage(session)
        mimeMessage.setFrom(InternetAddress(emailFrom))
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
        mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailCC, false))
        mimeMessage.setText(text)
        mimeMessage.subject = subject
        mimeMessage.sentDate = Date()



        val smtpTransport = session.getTransport("smtp")
        smtpTransport.connect()
        smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
        smtpTransport.close()
    }

    private fun putIfMissing(props: Properties, key: String, value: String) {
        if (!props.containsKey(key)) {
            props[key] = value
        }
    }

    private fun fillEmailCC() : String{
        var emailCC = ""
        for(c in General_Grupos.grupoActual.correo_usuarios!!){
            emailCC = "$emailCC $c"
        }
        return emailCC
    }

}