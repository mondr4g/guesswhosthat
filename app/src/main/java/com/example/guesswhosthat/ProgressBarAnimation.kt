package com.example.guesswhosthat

import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import android.widget.TextView

class ProgressBarAnimation(context: Context, progressBar: ProgressBar, txtView: TextView, from: Float, to : Float) : Animation() {
    var context : Context
    var progressBar : ProgressBar
    var txtView : TextView
    var from : Float = 0.0F
    var to : Float = 0.0F

    init {
        this.context = context
        this.progressBar = progressBar
        this.txtView = txtView
        this.from = from
        this.to = to
    }

    @Override
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        var value : Float = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
        txtView.text = value.toInt().toString() + " %"

        if (value == to) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }
}