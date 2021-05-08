package com.clases.proyecto_poi_arsaga.Modelos

import android.app.Activity
import android.app.AlertDialog
import com.clases.proyecto_poi_arsaga.R
import kotlinx.android.synthetic.main.dialog_cargando.*

class LoadingDialog(val mActivity:Activity) {
    private lateinit var isDialog:AlertDialog
    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_cargando, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog = builder.create()
        isDialog.show()
    }
    fun isDismiss(){
        isDialog.dismiss()
    }
}