package com.example.guesswhosthat

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var progressBar : ProgressBar
    lateinit var txtView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        progressBar = findViewById(R.id.progress_bar)
        txtView = findViewById(R.id.progress)

        progressBar.max = 100
        progressBar.scaleY = 3f

        progressAnimation()
    }

    fun progressAnimation() {
        var animation : ProgressBarAnimation = ProgressBarAnimation(this,progressBar,txtView,0.0F,100.0F)
        animation.duration = 8000
        progressBar.animation = animation
    }
}