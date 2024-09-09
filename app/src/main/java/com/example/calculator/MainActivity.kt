package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

enum class Mode {
    NoMode, Add, Subtract;
}

class MainActivity : AppCompatActivity() {
    // Declare variables used in calc app
    private var lastWasMode = false
    private var currentMode = Mode.NoMode
    private var labelText = ""
    private var savedNumber = 0
    private lateinit var calcTxtView: TextView

    // function that is executed when the MainActivity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize calc_txt_view
        calcTxtView = findViewById(R.id.txt_view_calc)
        // Set up calculator
        setup()
    }

    // Setup function, adds event listeners to the buttons
    private fun setup() {
        val buttons: Array<Button> = arrayOf(
            findViewById(R.id.btn_0),
            findViewById(R.id.btn_1),
            findViewById(R.id.btn_2),
            findViewById(R.id.btn_3),
            findViewById(R.id.btn_4),
            findViewById(R.id.btn_5),
            findViewById(R.id.btn_6),
            findViewById(R.id.btn_7),
            findViewById(R.id.btn_8),
            findViewById(R.id.btn_9)
        )

        // Add on click listeners to the btn s
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                pressedNumber(i)
            }
        }

        // Add listeners to calculator operations btn s
        findViewById<Button>(R.id.btn_plus).setOnClickListener{changeMode(Mode.Add)}
        findViewById<Button>(R.id.btn_minus).setOnClickListener{changeMode(Mode.Subtract)}
        findViewById<Button>(R.id.btn_c).setOnClickListener{pressedClear()}
        findViewById<Button>(R.id.btn_equal).setOnClickListener{pressedEquals()}
    }

    private fun pressedClear() {
        lastWasMode = false
        currentMode = Mode.NoMode
        labelText = ""
        savedNumber = 0
        calcTxtView.text = "0"
    }

    private fun pressedEquals() {
        if(lastWasMode)
            return
        val labelInt = labelText.toInt()
        when(currentMode){
            Mode.NoMode -> return
            Mode.Add -> savedNumber += labelInt
            Mode.Subtract -> savedNumber -= labelInt
        }
        currentMode = Mode.NoMode
        labelText = "$savedNumber"
        updateLabelText()
        lastWasMode = true
    }

    private fun pressedNumber(num: Int) {
        val numString = num.toString()
        if(lastWasMode) {
            lastWasMode = false
            labelText = ""
        }
        labelText = "$labelText$numString"
        updateLabelText()
    }

    private fun updateLabelText(){
        if(labelText.length > 8) {
            pressedClear()
            calcTxtView.text = "E"
            return
        }
        // in case of user input '00000number'
        val labelInt = labelText.toInt()
        labelText = labelInt.toString()

        if(currentMode == Mode.NoMode) {
            savedNumber = labelInt
        }

        calcTxtView.text = labelText

    }

    private fun changeMode(mode: Mode){
        if(savedNumber == 0) {
            return
        }
        currentMode = mode
        lastWasMode = true
    }
}