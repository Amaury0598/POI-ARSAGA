package com.clases.proyecto_poi_arsaga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.drawe_modperfil.*

class Mod_Perfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawe_modperfil)

        BT_back_MP.setOnClickListener {
            finish()
        }


        IMBT_MPfoto.setOnClickListener {
            Log.d("RegistedActivity", "Try to show photo selector")
            val F_intent = Intent(Intent.ACTION_PICK)
            F_intent.type = "image/*"
            startActivityForResult(F_intent, 0)
        }

    }
}