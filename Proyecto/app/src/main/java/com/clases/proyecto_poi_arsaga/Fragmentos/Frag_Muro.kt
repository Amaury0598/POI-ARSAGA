package com.clases.proyecto_poi_arsaga.Fragmentos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Muro_General
import com.clases.proyecto_poi_arsaga.Modelos.*
import com.clases.proyecto_poi_arsaga.R
import com.clases.proyecto_poi_arsaga.databinding.ActivityChatGrupalBinding.inflate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import java.util.*

class Frag_Muro : Fragment(), Adaptador_Muro_General.OnPubliClickListen {

    val listaPublicacion = mutableListOf<Usuario>()
    val adaptadorMuro = Adaptador_Muro_General(this, listaPublicacion,this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        CargarLista()
        val miViewGeneral =  inflater.inflate(R.layout.fragmento_muro_grupos, container, false)
        val CrearNueva_Publi = miViewGeneral.findViewById<FloatingActionButton>(R.id.FAB_crearpublicacion)

        val animation = AnimationUtils.loadAnimation(context, R.anim.dialog_itegrantes)
        val animation2 = AnimationUtils.loadAnimation(context, R.anim.dialog_itegrantes_2)
        val animationout = AnimationUtils.loadAnimation(context, R.anim.dialog_integrantes_out)

        val MuroRecycler = miViewGeneral.findViewById<RecyclerView>(R.id.Recy_Muro)
        val milinear = LinearLayoutManager(context)

        MuroRecycler.layoutManager = milinear
        MuroRecycler.adapter = adaptadorMuro

        CrearNueva_Publi.setOnClickListener {

            val DialogView = inflater.inflate(R.layout.dialog_publicacion, null)
            val Dialog_Pucli = AlertDialog.Builder(requireActivity()).setView(DialogView)


            val RDialog = DialogView.findViewById<RelativeLayout>(R.id.Dialog_publicacion)
            val ContentEdit = DialogView.findViewById<TextInputLayout>(R.id.Content_Edit)
            val Aceptar = DialogView.findViewById<Button>(R.id.BTN_Crear_Public)


            ContentEdit.startAnimation(animation)
            RDialog.startAnimation(animation2)
            Aceptar.startAnimation(animation2)
            val AlertD = Dialog_Pucli.show()
            AlertD.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

            Aceptar.setOnClickListener {

                ContentEdit.startAnimation(animationout)
                RDialog.startAnimation(animationout)
                Aceptar.startAnimation(animationout)
                AlertD.dismiss()
            }
        }

        return miViewGeneral
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun CargarLista(){

        listaPublicacion.add(Usuario("Javier","Hola, como estas", "https://cdn.icon-icons.com/icons2/2620/PNG/512/among_us_player_pink_icon_156938.png", "Hola@outloock.com", ""))
        listaPublicacion.add(Usuario("El Rasho MacQueen la Machina mas veloz del mundo","Kuchau", "https://i.pinimg.com/236x/8c/5a/bb/8c5abb828846c4f963d84592197c6268.jpg", "Adios@outloock.com", ""))
        listaPublicacion.add(Usuario("Oliver","Mis piernas !!!", "https://i.pinimg.com/236x/62/c6/7b/62c67bbea0fc21565df13b5b1998b56a.jpg", "Hola@outloock.com", ""))
        listaPublicacion.add(Usuario("Benito","yipi yipi yipiy kejeje asdwajdjwa dwajdwadaw", "https://cdn.icon-icons.com/icons2/2620/PNG/512/among_us_player_pink_icon_156938.png", "Adios@outloock.com", ""))
        listaPublicacion.add(Usuario("José José","Hola, como estas", "https://i.pinimg.com/236x/62/c6/7b/62c67bbea0fc21565df13b5b1998b56a.jpg", "Hola@outloock.com", ""))

    }
}