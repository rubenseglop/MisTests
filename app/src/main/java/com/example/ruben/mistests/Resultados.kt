package com.example.ruben.mistests

import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import kotlinx.android.synthetic.main.activity_resultados.*

class Resultados : AppCompatActivity() {

    private var resultado: String? = null;
    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mp = MediaPlayer.create (this, R.raw.windows)
        setContentView(R.layout.activity_resultados)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            resultado = bundle.getString("texto")
        }

        textView2.setText(resultado)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun finaliza(v: View) {
        mp.start()
        finishAffinity();
    }
}
