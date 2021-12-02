package com.example.guesswhosthat

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialog {

    private lateinit var activity : Activity
    private lateinit var dialog : AlertDialog

    constructor(myActivity: Activity) {
        activity = myActivity
    }

    fun startLoadingDialog() {
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        var inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_loading_dialog,null))
        builder.setCancelable(true)


        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() = dialog.dismiss()
}