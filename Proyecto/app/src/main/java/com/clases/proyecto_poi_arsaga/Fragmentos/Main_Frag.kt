package com.clases.proyecto_poi_arsaga.Fragmentos


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clases.proyecto_poi_arsaga.Adaptadores.Adaptador_Lista_Chats
import com.clases.proyecto_poi_arsaga.Chat_Grupal
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.Modelos.ChatMensaje
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_lista__chats__grupos.*
import java.util.*


class Main_Frag :  Fragment(), Adaptador_Lista_Chats.OnGrupoClickListen {

    private val database = FirebaseDatabase.getInstance();
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private val chatDirectoRef = database.getReference("ChatDirecto");

    val listaChats = mutableListOf<ChatMensaje>()
    val adaptadorChatlistadechats = Adaptador_Lista_Chats(this, listaChats, this)
    var listaCorreos = mutableListOf<String>()
    var userActual:Usuario? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        userActual = arguments?.getSerializable("userActual") as Usuario

        cargarLista()
        autoCompletar()
        val miView =  inflater.inflate(R.layout.activity_lista__chats__grupos, container, false)
        var recycler_lista = miView.findViewById<RecyclerView>(R.id.RV_lista_chats_grupos)
        val milinear = LinearLayoutManager(context)
        val miAutoComplete = miView.findViewById<AutoCompleteTextView>(R.id.AC_CHATGRUPAL)

        recycler_lista.layoutManager = milinear
        recycler_lista.adapter = adaptadorChatlistadechats


        miAutoComplete.setOnItemClickListener {parent, view, position, id ->

            var usuarioSeleccionado: String = miAutoComplete.text.toString().replace("\n", "/-/");
            var nombre = obtenerNombre(usuarioSeleccionado)
            var correo = obtenerCorreo(usuarioSeleccionado, nombre)
            if(usuarioSeleccionado.isNotEmpty()) {
                miAutoComplete.text.clear();

                val intent = Intent(activity, Chat_Grupal::class.java)
                intent.putExtra("Nombre", nombre)
                intent.putExtra("Correo", correo)
                intent.putExtra("userActual", userActual)
                intent.putExtra("Tipo", 1)
                verSiTieneChat(obtenerChatDirectoUsuarios(correo, userActual!!.correo), intent)

            }

        }

        return miView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun cargarLista(){

        listaChats.add(ChatMensaje("Javier","Hola, como estas", Date(), false, 0, true))
        listaChats.add(ChatMensaje("El Rasho MacQueen la Machina mas veloz del mundo","Kuchau", Date(), true, 1, false))
        listaChats.add(ChatMensaje("Oliver","Mis piernas !!!", Date(), false, 2, true))
        listaChats.add(ChatMensaje("Benito","yipi yipi yipiy kejeje asdwajdjwa dwajdwadaw", Date(), true, 4, false))
        listaChats.add(ChatMensaje("José José","Hola, como estas", Date(), true, 5, true))
        listaChats.add(ChatMensaje("Messi","lalalalaa lalalalaa allalaala lalalala alallala alala", Date(), false, 6, true))


        adaptadorChatlistadechats.notifyDataSetChanged()

    }

    private fun verSiTieneChat(usu: String, intent: Intent){
        var chatDir: ChatDirecto = ChatDirecto("", usu)
        var chatDirectoRef = database.getReference().child("ChatDirecto").orderByChild("usuarios").equalTo(usu)
        chatDirectoRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {

                    for(cd in snapshot.children) {
                        val chatD: ChatDirecto = cd.getValue(ChatDirecto::class.java) as ChatDirecto;
                        intent.putExtra("idChatDirecto", chatD.id)
                    }
                } else{
                    //Toast.makeText(activity, "Debería de insertarse", Toast.LENGTH_SHORT).show()

                    intent.putExtra("idChatDirecto", crearSalaChat(chatDir))

                }
                activity?.startActivity(intent)
            }

        })
    }

    private fun crearSalaChat(chatD: ChatDirecto): String{
        val nuevaSala=chatDirectoRef.push();
        chatD.id = nuevaSala.key.toString();

        nuevaSala.setValue(chatD)
        return chatD.id
    }

    private fun obtenerChatDirectoUsuarios(correo1:String, correo2:String) : String{
        val datoRes: String
        if(correo1 > correo2)
            datoRes = correo1 + "/-/" + correo2
        else
            datoRes = correo2 + "/-/" + correo1
        return datoRes
    }

    private fun obtenerNombre(cadena:String) : String{
        var nombre: String = ""
        for (i in 0 until cadena.length) {
            if(cadena[i] == '/' && cadena[i+1] == '-' && cadena[i+2] == '/')
                break
            nombre += cadena[i]
        }
        return nombre
    }

    private fun obtenerCorreo(cadena:String, nombre:String) : String{
        var correo = cadena.replace(nombre + "/-/", "")
        return correo
    }

    override fun onitemHold(toString: String) {
        Toast.makeText(activity, "El id es: $toString", Toast.LENGTH_SHORT).show()
    }


    override fun onitemClick(usuario: String) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        intent.putExtra("Nombre", usuario)
        intent.putExtra("Tipo", 1)
        activity?.startActivity(intent)
    }

    private fun autoCompletar() {

        userRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){

                    for( u in snapshot.children){
                        val user: Usuario = u.getValue(Usuario::class.java) as Usuario;
                            if(user.correo != userActual!!.correo)
                                listaCorreos.add(user.nombre+"\n"+user.correo)//Darien Miguel Sánchez Arévalo\nsadarien@gmail.com
                    }

                    //val correosArray: Array<String> = listaCorreos.toTypedArray()
                    val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, listaCorreos);
                    AC_CHATGRUPAL.setAdapter(adapter)
                }
            }
        })
    }
}