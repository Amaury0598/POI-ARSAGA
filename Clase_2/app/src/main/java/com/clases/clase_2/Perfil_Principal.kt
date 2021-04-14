package com.clases.clase_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_tareas.*

class Perfil_Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)



        btn_Ver_Msg.setOnClickListener {
            val miIntent = Intent(this, List_Chats::class.java)
            startActivity(miIntent)
        }
        btn_back_3.setOnClickListener {
            finish()
        }
    }
}