package com.clases.proyecto_poi_arsaga

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import java.util.*

class Asignar_nueva_tarea : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var Year = 0
    var Month = 0
    var Day = 0

    var SaveYear = 0
    var SaveMonth = 0
    var SaveDay = 0


    private fun getDate(){

        val cal = Calendar.getInstance()
        Day = cal.get(Calendar.DAY_OF_MONTH)
        Month = cal.get(Calendar.MONTH)
        Year = cal.get(Calendar.YEAR)
        ET_Asignar_fecha_tarea.setText("$SaveDay-$SaveMonth-$SaveYear")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)

        val Back = findViewById<ImageButton>(R.id.BT_back_CT)
        val FechaLimite = findViewById<EditText>(R.id.ET_Asignar_fecha_tarea)

        Back.setOnClickListener {
            finish()
        }

        FechaLimite.setOnClickListener {
            getDate()
            DatePickerDialog(this, this, Year, Month, Day).show()

        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        SaveDay = dayOfMonth
        SaveMonth = month
        SaveYear = year
        getDate()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }
}