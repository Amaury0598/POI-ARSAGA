package com.clases.proyecto_poi_arsaga.Fragmentos

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Integrantes
import com.clases.proyecto_poi_arsaga.Modelos.Grupos
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R

class Dialog_Agregar_I: DialogFragment(), Adaptador_Integrantes.OnItemGrupoClickListener {


    var listaIntegrantes = mutableListOf<Usuario>()
    private var nuevoGrupo : Grupos = Grupos()
    val AdaptAIntegrantes = Adaptador_Integrantes(4,listaIntegrantes,nuevoGrupo.correo_usuarios!!,  this);


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        CargarLista()

        val animation = AnimationUtils.loadAnimation(context, R.anim.dialog_itegrantes)
        val animation2 = AnimationUtils.loadAnimation(context, R.anim.dialog_itegrantes_2)
        val animationout = AnimationUtils.loadAnimation(context, R.anim.dialog_integrantes_out)

        val DialogView_A = inflater.inflate(R.layout.dialog_modificar_integrantes, container, false)
        val Dialog_Agregar_I = AlertDialog.Builder(context).setView(DialogView_A)


        val RDialog = DialogView_A.findViewById<RelativeLayout>(R.id.RelLay_DialogAgregar)
        val Recy_Intregrantes = DialogView_A.findViewById<RecyclerView>(R.id.Dialog_Recy_Agregar_integrantes)
        val Aceptar = DialogView_A.findViewById<Button>(R.id.BTN_Aceptar_A_integrantes)

        Recy_Intregrantes.layoutManager = LinearLayoutManager(context);
        Recy_Intregrantes.adapter = AdaptAIntegrantes;

        Recy_Intregrantes.startAnimation(animation)
        RDialog.startAnimation(animation2)
        Aceptar.startAnimation(animation2)
        val AlertD = Dialog_Agregar_I.show()
        AlertD.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))

        Aceptar.setOnClickListener {

            Recy_Intregrantes.startAnimation(animationout)
            RDialog.startAnimation(animationout)
            Aceptar.startAnimation(animationout)
            AlertD.dismiss()

        }
        return DialogView_A
    }

    fun CargarLista(){

        listaIntegrantes.add(Usuario("Amaury", "amg05@hotmail.com", "", "", "", 10))
        listaIntegrantes.add(Usuario("Leo", "Leo@hotmail.com", "", "", "", 10))
        listaIntegrantes.add(Usuario("Darien", "Darien@hotmail.com", "", "", "", 10))
        listaIntegrantes.add(Usuario("José", "José@hotmail.com", "", "", "", 10))
    }

    override fun AddtoList(correo: String, position: String, estatus: Int) {

    }

    override fun RemoveFromList(correo: String, position: String, estatus: Int) {

    }

}