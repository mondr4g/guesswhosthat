package com.example.guesswhosthat.Session

import android.app.Activity
import android.app.AlertDialog
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.guesswhosthat.GameActivity
import com.example.guesswhosthat.R

class CustomWinDialog {
    private lateinit var activity : Activity
    private lateinit var dialog : AlertDialog

    constructor(myActivity: Activity) {
        activity = myActivity
    }

    fun startLoadingDialog(win: Boolean) {
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        var inflater : LayoutInflater = activity.layoutInflater
        var view = inflater.inflate(R.layout.custom_win_dialog,null)
        var btnOk : Button = view.findViewById<Button>(R.id.btnAccDia)
        if(win){
            view.findViewById<LottieAnimationView>(R.id.dial_anim).setAnimation(R.raw.winner_anim)
            view.findViewById<TextView>(R.id.txtMsjDial).setText(R.string.win_msj)
            view.findViewById<TextView>(R.id.btnAccDia).setText(R.string.winbtn)
            //view.findViewById<TextView>(R.id.btnAccDia).background(R.color.blue)

        }
        else{
            view.findViewById<LottieAnimationView>(R.id.dial_anim).setAnimation(R.raw.loser_anim)
            view.findViewById<TextView>(R.id.txtMsjDial).setText(R.string.win_msj)
            view.findViewById<TextView>(R.id.btnAccDia).setText(R.string.losbtn)
        }

        btnOk.setOnClickListener {
            dialog.dismiss()
            GameActivity.fa.finish()
        }

        builder.setView(view)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
//        txtDialogMsj

    }

    fun dismissDialog() = dialog.dismiss()

    fun delayedDismiss(time: Number){
        val handler = Handler()
        handler.postDelayed(Runnable { dialog.dismiss() }, time as Long)

    }
}