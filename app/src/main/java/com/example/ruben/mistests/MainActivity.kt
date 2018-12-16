package com.example.ruben.mistests

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private val id_respuesta = intArrayOf(R.id.respuesta1, R.id.respuesta2, R.id.respuesta3, R.id.respuesta4)
    private var correct_respuesta: Int = 0
    private var pregunta_actual: Int = 0
    private var array_preguntas: Array<String>? = null
    private var respuestas_correctas: BooleanArray? = null
    private var pregunta: IntArray? = null
    private var texto_pregunta: TextView? = null
    private var grupo: RadioGroup? = null
    private lateinit var mp: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mp = MediaPlayer.create (this, R.raw.click)

        //Identificación de Texto y de grupo de botones radio
        texto_pregunta = findViewById(R.id.pregunta) as TextView
        grupo = findViewById(R.id.answer_group) as RadioGroup

        //Meto en el array las preguntas
        array_preguntas = resources.getStringArray(R.array.array_preguntas)
        pregunta = IntArray(array_preguntas!!.size)
        for (i in pregunta!!.indices) {
            pregunta!![i] = -1
        }
        pregunta_actual = 0
        preguntas()

        //Genero un array de respuestas (para luego consultar los aciertos)
        respuestas_correctas = BooleanArray(array_preguntas!!.size)
    }

    fun botonsiguiente(v: View) {
        comprobar()
        mp.start()
        if (pregunta_actual < array_preguntas!!.size - 1) {
            pregunta_actual++
            preguntas()
        } else {
            var correctas = 0
            var incorrectas = 0
            for (b in respuestas_correctas!!) {
                if (b)
                    correctas++
                else
                    incorrectas++
            }
            val resultado =
                String.format(Locale.getDefault(), "Correctas: %d -- Incorrectas: %d", correctas, incorrectas)

            // Abrir la nueva Activity y pasarle los resultados
            val intent = Intent(this, Resultados::class.java)
            intent.putExtra("texto", resultado)
            startActivity(intent)
        }
    }

    private fun comprobar() {
        val id_boton = grupo!!.checkedRadioButtonId
        var respuesta = -1
        for (i in id_respuesta.indices) {
            if (id_respuesta[i] == id_boton) {
                respuesta = i
            }
        }

        if (respuesta == correct_respuesta) {
            respuestas_correctas!![pregunta_actual] = true  // ACIERTO
            pregunta!![pregunta_actual] = respuesta
        }
    }

    private fun preguntas() {
        val q = array_preguntas!![pregunta_actual]
        val parts = q.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        grupo!!.clearCheck()

        texto_pregunta!!.text = parts[0]
        for (i in id_respuesta.indices) {
            val rb = findViewById<View>(id_respuesta[i]) as RadioButton
            var resp = parts[i + 1]
            if (resp[0] == '*') {
                correct_respuesta = i
                resp = resp.substring(1)
            }
            rb.text = resp
            if (pregunta!![pregunta_actual] == i) {
                rb.isChecked = true
            }
        }

        if (pregunta_actual == array_preguntas!!.size - 1) {
            boton_siguiente.setText(R.string.finish)
        } else {
            boton_siguiente.setText(R.string.next)
        }
    }


}
