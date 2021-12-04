package com.example.guesswhosthat

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.widget.TextView
import com.example.guesswhosthat.Helpers.SocketHandler


class LoadingDialog {

    private lateinit var activity : Activity
    private lateinit var dialog : AlertDialog

    constructor(myActivity: Activity) {
        activity = myActivity
    }

    fun startLoadingDialog(msj: String, id: String) {
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        var inflater : LayoutInflater = activity.layoutInflater
        var view = inflater.inflate(R.layout.custom_loading_dialog,null)
        var btnCancel : TextView = view.findViewById(R.id.cancel_loading)

        btnCancel.setOnClickListener {
            // Cerrar Dialog
            dialog.dismiss()
            // Cambiar musica Menu
            val mIntent = Intent()
            mIntent.action="Menu"
            MenuActivity.fa.sendBroadcast(mIntent)
            GameActivity.fa.finish()
            SocketHandler.mSocket!!.emit("no_se_armo",id)
        }

        view.findViewById<TextView>(R.id.txtDialogMsj).text = msj
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() = dialog.dismiss()

    fun delayedDismiss(time: Number){
        val handler = Handler()
        handler.postDelayed(Runnable { dialog.dismiss() }, time as Long)

    }
}