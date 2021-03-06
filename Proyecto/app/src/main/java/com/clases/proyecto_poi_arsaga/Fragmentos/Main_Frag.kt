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
import com.clases.proyecto_poi_arsaga.Modelos.LoadingDialogFragment
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.clases.proyecto_poi_arsaga.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_lista__chats__grupos.*
import kotlinx.android.synthetic.main.drawe_modperfil.*


class Main_Frag :  Fragment(), Adaptador_Lista_Chats.OnGrupoClickListen {

    private val database = FirebaseDatabase.getInstance();
    private val userRef = database.getReference("Usuarios"); //crear "rama" (tabla)
    private val chatDirectoRef = database.getReference("ChatDirecto");
    private val auth = FirebaseAuth.getInstance()

    var listaChats = mutableListOf<ChatDirecto>()
    val adaptadorChatlistadechats = Adaptador_Lista_Chats(this, listaChats,this)

    var listaUsuarios = mutableListOf<Usuario>()

    companion object{
        var userActual = Usuario()
    }

    private lateinit var loading : LoadingDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loading = LoadingDialogFragment(this)
        loading.startLoading("Cargando contactos")
        //userActual = arguments?.getSerializable("userActual") as Usuario



        val miView =  inflater.inflate(R.layout.activity_lista__chats__grupos, container, false)
        var recycler_lista = miView.findViewById<RecyclerView>(R.id.RV_lista_chats_grupos)
        val milinear = LinearLayoutManager(context)
        val miAutoComplete = miView.findViewById<AutoCompleteTextView>(R.id.AC_CHATGRUPAL)

        recycler_lista.layoutManager = milinear
        recycler_lista.adapter = adaptadorChatlistadechats


        miAutoComplete.setOnItemClickListener {parent, view, position, id ->


            var usuarioSeleccionado = parent.getItemAtPosition(position) as Usuario

            miAutoComplete.text.clear();

            val intent = Intent(activity, Chat_Grupal::class.java)
            intent.putExtra("userActual", userActual)
            intent.putExtra("Tipo", 1)
            verSiTieneChat(intent, usuarioSeleccionado)

        }
        userRef.child(auth.uid.toString()).get()
                .addOnSuccessListener{ usuarioConseguido ->

                    userActual = usuarioConseguido.getValue(Usuario::class.java) as Usuario
                    if(userActual.encriptado)
                        userActual.desencriptarUsuario()
                    cargarChatDirecto()


                }



        return miView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun cargarChatDirecto(){

        chatDirectoRef.orderByChild("timeStamp").limitToLast(20)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot!!.exists()){
                            listaChats.clear()
                            for (cd in snapshot.children){
                                val chatD: ChatDirecto = cd.getValue(ChatDirecto::class.java) as ChatDirecto
                                if(chatD.ultimoMensaje.isNotEmpty()){
                                    if(chatD.usuario1 == userActual!!.correo || chatD.usuario2 == userActual!!.correo)
                                        listaChats.add(chatD)
                                }
                            }
                            listaChats.reverse()
                            adaptadorChatlistadechats.notifyDataSetChanged()

                        }
                        if(activity != null) {
                            autoCompletar()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })



    }

    private fun verSiTieneChat(intent: Intent, usuarioSeleccionado: Usuario){
        var chatDir: ChatDirecto = ChatDirecto("", userActual!!.correo, usuarioSeleccionado.correo, userActual!!.nombre, usuarioSeleccionado.nombre, userActual!!.imagen, usuarioSeleccionado.imagen, "", "", ServerValue.TIMESTAMP, userActual!!.status, usuarioSeleccionado.status)
        var encontrado = false
        var cdRef = database.getReference().child("ChatDirecto")
        cdRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {

                    for(cd in snapshot.children) {
                        val chatD: ChatDirecto = cd.getValue(ChatDirecto::class.java) as ChatDirecto;
                        if(chatD.usuario1 == userActual!!.correo && chatD.usuario2 == usuarioSeleccionado.correo){
                         encontrado = true
                        }else if (chatD.usuario1 == usuarioSeleccionado.correo && chatD.usuario2 == userActual!!.correo){
                            encontrado = true
                        }
                        if(encontrado) {
                            intent.putExtra("ChatDirecto", chatD)
                            break
                        }
                    }

                }
                if(!encontrado){
                    intent.putExtra("ChatDirecto", crearSalaChat(chatDir))
                }
                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity?.startActivity(intent)
            }

        })
    }

    private fun crearSalaChat(chatD: ChatDirecto): ChatDirecto{
        val nuevaSala=chatDirectoRef.push();
        chatD.id = nuevaSala.key.toString();

        nuevaSala.setValue(chatD)
        return chatD
    }


    override fun onitemClick(Chatdirecto: ChatDirecto) {
        val intent = Intent(activity, Chat_Grupal::class.java)
        intent.putExtra("ChatDirecto", Chatdirecto)
        intent.putExtra("userActual", userActual)
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
                    listaUsuarios.clear()

                    for( u in snapshot.children){
                        val user: Usuario = u.getValue(Usuario::class.java) as Usuario;
                        if(user.encriptado)
                            user.desencriptarUsuario()
                            if(user.correo != userActual!!.correo) {
                                
                                listaUsuarios.add(user)
                            }
                    }

                    //val correosArray: Array<String> = listaCorreos.toTypedArray()
                    if(activity != null){
                    val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, listaUsuarios);
                    AC_CHATGRUPAL.setAdapter(adapter)
                    }

                }
                if(loading != null)
                    loading.isDismiss()
            }
        })
    }
}
