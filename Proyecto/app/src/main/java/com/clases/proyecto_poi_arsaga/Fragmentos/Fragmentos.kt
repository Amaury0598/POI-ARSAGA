package com.clases.proyecto_poi_arsaga.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.clases.proyecto_poi_arsaga.Mod_Perfil
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialogFragment
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class FragmentoA : Fragment()  {

    private lateinit var userActual : Usuario
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")
    private lateinit var loading : LoadingDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val miView =  inflater.inflate(R.layout.drawer_perfil, container, false)
        val PNombre = miView.findViewById<TextView>(R.id.TV_Pnombre)
        val PCarrera = miView.findViewById<TextView>(R.id.TV_Pcarrera)
        val PRol = miView.findViewById<TextView>(R.id.TV_Prol)
        val PDesc = miView.findViewById<TextView>(R.id.TV_Pdesc)
        val ModPerfil = miView.findViewById<Button>(R.id.BTN_Pmodificar)
        val PImagen = miView.findViewById<ImageView>(R.id.IV_Pfoto
        )
        loading = LoadingDialogFragment(this)
        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{
                    loading.startLoading("Cargando Datos")
                    userActual = it.getValue(Usuario::class.java) as Usuario
                    if(userActual.encriptado)
                        userActual.desencriptarUsuario()
                    PNombre.text = userActual?.nombre
                    PCarrera.text = userActual?.carrera
                    PRol.text = "Estudiante"
                    PDesc.text = userActual?.desc
                    Picasso.get().load(userActual?.imagen).into(PImagen)
                    loading.isDismiss()
                }


        ModPerfil.setOnClickListener {
            val intent = Intent(activity, Mod_Perfil::class.java)
            intent.putExtra("userActual", userActual)
            activity?.startActivity(intent)
        }

        return miView;
    }

}

class FragmentoB : Fragment(){
    private lateinit var userActual : Usuario
    private val database = FirebaseDatabase.getInstance();
    private val auth = FirebaseAuth.getInstance()
    private val userRef = database.getReference("Usuarios")
    private lateinit var loading : LoadingDialogFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val miView =  inflater.inflate(R.layout.drawe_configuracion, container, false)
        val encriptar = miView.findViewById<Switch>(R.id.SW_Encriptar)
        loading = LoadingDialogFragment(this)

        userRef.child(auth.uid.toString()).get()
            .addOnSuccessListener {
                userActual = it.getValue(Usuario::class.java) as Usuario
                if(userActual.encriptado) {
                    userActual.desencriptarUsuario()
                    encriptar.isChecked = true
                }else{
                    encriptar.isChecked = false
                }



            }

        encriptar.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){

                userActual.encriptado = true
                userActual.encriptarUsuario()
                userRef.child(auth.uid.toString()).setValue(userActual)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Se han encriptado tus datos", Toast.LENGTH_SHORT).show() }
            }else{

                userActual.encriptado = false
                userActual.desencriptarUsuario()
                userRef.child(auth.uid.toString()).setValue(userActual)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Se han desencriptado tus datos", Toast.LENGTH_SHORT).show() }
            }
        }




        return miView
    }
}

class FragmentoC: Fragment(R.layout.activity_log__in) {

}