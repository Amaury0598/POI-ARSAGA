package com.clases.proyecto_poi_arsaga

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialog
import com.clases.proyecto_poi_arsaga.Modelos.TareaUsuarios
import com.clases.proyecto_poi_arsaga.Modelos.Tareas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import java.util.*

class Asignar_nueva_tarea : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val database = FirebaseDatabase.getInstance();
    private val tareaRef = database.getReference("Tareas"); //crear "rama" (tabla)
    private val tareaUsuariosRef = database.getReference("TareasUsuarios")
    private val auth = FirebaseAuth.getInstance()
    private lateinit var loading : LoadingDialog

    var Year = 0
    var Month = 0
    var Day = 0

    var SaveYear = 0
    var SaveMonth = 0
    var SaveDay = 0

    private lateinit var actualDate : String

    private fun getActualDate():String{

        val cal = Calendar.getInstance()
        Day = cal.get(Calendar.DAY_OF_MONTH)
        Month = cal.get(Calendar.MONTH)
        Year = cal.get(Calendar.YEAR)
        var fDay = Day.toString()
        var fMonth = Month.toString()
        if (Day < 10){
            fDay = "0$fDay"
        }
        if (Month < 10) {
            fMonth = "0$fMonth"
        }
        return "$fDay/$fMonth/$SaveYear"

    }

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
        actualDate = getActualDate()
        val Crear = findViewById<Button>(R.id.BT_CrearTarea)
        val Back = findViewById<ImageButton>(R.id.BT_back_CT)
        val FechaLimite = findViewById<EditText>(R.id.ET_Asignar_fecha_tarea)
        val nombreTarea = findViewById<EditText>(R.id.ET_Asignar_nombre_tarea)
        val descTarea = findViewById<EditText>(R.id.ET_Asignar_descripcion_tarea)
        val checkASG_Coins = findViewById<CheckBox>(R.id.checkBox3)
        val ASG_Coins = findViewById<EditText>(R.id.ET_Asignar_cantidad)

        val grupoActual = General_Grupos.grupoActual
        var tarea = Tareas()


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
            if(formatDate() < actualDate){
                FechaLimite.error = "Introduce una fecha válida"
                FechaLimite.requestFocus()
                return@setOnClickListener
            }
            if(checkASG_Coins.isChecked){
                if(coins < 0){
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

            var listaTareaUsuarios = mutableListOf<TareaUsuarios>()
            for(c in grupoActual.correo_usuarios!!){
                listaTareaUsuarios.add(TareaUsuarios(c, "Pendiente"))
            }
            tareaUsuariosRef.child(tarea.id).setValue(listaTareaUsuarios)
                    .addOnSuccessListener {
                        loading.isDismiss()
                        finish()
                    }


        }

        FechaLimite.setOnClickListener {
            DatePickerDialog(this, this, Year, Month, Day).show()
            Log.d("fecha", FechaLimite.text.toString())

        }
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
}