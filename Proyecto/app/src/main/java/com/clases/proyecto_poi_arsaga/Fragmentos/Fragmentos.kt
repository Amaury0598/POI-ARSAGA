package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Mod_Perfil
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.google.firebase.database.FirebaseDatabase

class FragmentoA : Fragment()  {

    private val database = FirebaseDatabase.getInstance();
    var userActual: Usuario? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        userActual = arguments?.getSerializable("userActual") as Usuario

        val miView =  inflater.inflate(R.layout.drawer_perfil, container, false)
        val PNombre = miView.findViewById<TextView>(R.id.TV_Pnombre)
        val PCarrera = miView.findViewById<TextView>(R.id.TV_Pcarrera)
        val PRol = miView.findViewById<TextView>(R.id.TV_Prol)
        val PDesc = miView.findViewById<TextView>(R.id.TV_Pdesc)
        val ModPerfil = miView.findViewById<Button>(R.id.BTN_Pmodificar
        )
        PNombre.text = userActual?.nombre
        PCarrera.text = userActual?.carrera
        PRol.text = "Estudiante"
        PDesc.text = userActual?.desc

        ModPerfil.setOnClickListener {
            val intent = Intent(activity, Mod_Perfil::class.java)
            intent.putExtra("USUARIO", userActual)
            activity?.startActivity(intent)
        }

        return miView;
    }

}

class FragmentoB : Fragment(R.layout.drawe_configuracion){
}

class FragmentoC: Fragment(R.layout.activity_log__in) {

}