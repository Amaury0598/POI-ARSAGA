package com.clases.proyecto_poi_arsaga

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.clases.proyecto_poi_arsaga.Modelos.ChatDirecto
import com.clases.proyecto_poi_arsaga.Modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class StatusActivity : Application(), LifecycleObserver {


    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        isOnline(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        isOnline(true)
    }

    private fun isOnline(status:Boolean){
        var userActual : Usuario? = null
        val database = FirebaseDatabase.getInstance();
        val auth = FirebaseAuth.getInstance()
        val userRef = database.getReference("Usuarios")
        if(!auth.uid.isNullOrEmpty()) {
            userRef.child(auth.uid.toString()).get()
                    .addOnSuccessListener {
                        if (it.exists()) {
                            userActual = it.getValue(Usuario::class.java) as Usuario
                            userActual!!.status = status
                            userRef.child(auth.uid.toString()).setValue(userActual)
                                    .addOnSuccessListener { updateChatListInfo1(userActual!!) }

                        }
                    }
        }
    }

    private fun updateChatListInfo1(user: Usuario){
        val database = FirebaseDatabase.getInstance();
        val chatDirectoRef = database.getReference("ChatDirecto")
        chatDirectoRef.orderByChild("usuario1").equalTo(user.correo).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (cl in snapshot.children){
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.status1 = user.status
                        chatDirectoRef.child(chatD.id).setValue(chatD)
                    }
                }
                updateChatListInfo2(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateChatListInfo2(user: Usuario){
        val database = FirebaseDatabase.getInstance();
        val chatDirectoRef = database.getReference("ChatDirecto")
        chatDirectoRef.orderByChild("usuario2").equalTo(user.correo).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (cl in snapshot.children){
                        val chatD: ChatDirecto = cl.getValue(ChatDirecto::class.java) as ChatDirecto
                        chatD.status2 = user.status

                        chatDirectoRef.child(chatD.id).setValue(chatD)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}