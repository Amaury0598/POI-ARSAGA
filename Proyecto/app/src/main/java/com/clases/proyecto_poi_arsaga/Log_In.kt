package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_log__in.*

class Log_In : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log__in)


        BT_entrar.setOnClickListener {
            finish()
            val miIntent = Intent(this, MainActivity::class.java)
            startActivity(miIntent)
        }
        BT_registrate.setOnClickListener {
            val miIntent = Intent(this, Registro::class.java)
            startActivity(miIntent)
        }
    }
}