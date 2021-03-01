package com.clases.clase_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.clases.clase_2.adaptadores.TodosLos_ChatAdaptador
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_todosloschats.*

class List_Chats : AppCompatActivity() {

    private val listMsgChats = TodosLos_ChatAdaptador()

    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todosloschats)
        val rvListChats = findViewById<RecyclerView>(R.id.rvListaChats)
        rvListChats.adapter = listMsgChats

        btn_ir.setOnClickListener {
            val miIntent = Intent(this, MainActivity::class.java)
            startActivity(miIntent)
        }
    }
}