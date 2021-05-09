package com.clases.proyecto_poi_arsaga

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Integrantes
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import kotlinx.android.synthetic.main.activity_crear_grupo.*
import java.util.*

class Crear_Grupo : AppCompatActivity(), Adaptador_Integrantes.OnItemGrupoClickListener {

    var listaIntegrantes = mutableListOf<Usuario>()
    val AdaptIntegrantes = Adaptador_Integrantes(listaIntegrantes, this);
    val listaChecked = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_grupo)

        llenarlista();
        val Integrantes = PopupWindow(this)
        val LLay = findViewById<ConstraintLayout>(R.id.ID_CrearGrupo)
        val Key_Edit = findViewById<EditText>(R.id.ET_Nombre_grupo)

        BT_back_CG.setOnClickListener{
            finish();
        }
        Key_Edit.setOnClickListener {
            Integrantes?.dismiss();
        }

        BTN_Agegrar_integrantes.setOnClickListener {

            val view = layoutInflater.inflate(R.layout.dialog_integrantes, null);
            Integrantes.contentView = view;
            val IntegrantesReecycler = view.findViewById<RecyclerView>(R.id.Dialog_Recycler_Integrantes)
            val EsLinear = LinearLayoutManager(this);

            IntegrantesReecycler.layoutManager = EsLinear;
            IntegrantesReecycler.adapter = AdaptIntegrantes;

            val Aceptar = view.findViewById<Button>(R.id.BTN_Aceptar_integrantes);

            Aceptar.setOnClickListener {
                listaChecked
                Integrantes.dismiss();
            }
            Integrantes.showAtLocation(BTN_Agegrar_integrantes, Gravity.CENTER,0, 0);
        }


        LLay.setOnClickListener {
            Integrantes?.dismiss();
        }
    }

    fun llenarlista(){
        listaIntegrantes.add(Usuario("El Rasho MacQueen la Machina mas veloz del mundo","Kuchau", "1", "Adios"))
        listaIntegrantes.add(Usuario("Oliver","Mis piernas !!!",  "2", "Adios"))
        listaIntegrantes.add(Usuario("Benito","yipi yipi yipiy kejeje asdwajdjwa dwajdwadaw", "3", "Adios"))
        listaIntegrantes.add(Usuario("José José","Hola, como estas", "4", "4"))
        listaIntegrantes.add(Usuario("Messi","lalalalaa lalalalaa allalaala lalalala alallala alala", "5", "Adios"))
    }

    override fun AddtoList(Nombre: String, position: String) {
        listaChecked.add(Nombre)
        Toast.makeText(this, "${listaChecked.size}", Toast.LENGTH_SHORT).show()
    }

    override fun RemoveFromList(NombreB: String, position: String) {
        listaChecked.remove(NombreB)
    }
}