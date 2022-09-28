package com.example.lab1

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.lab1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val avaliableFonts = arrayOf(
        "sans-serif", "sans-serif-light", "random"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mainBinding.root)

        for (name in avaliableFonts) {
            val button = RadioButton(this)
            button.text = name
            button.id = View.generateViewId()
            mainBinding.fontRadioGroup.addView(button)
        }
        mainBinding.fontRadioGroup.check(mainBinding.fontRadioGroup.children.first().id)
    }

    fun applyFont(view: View) {
        val fontName = getSelectedFont()
        val typefaceID = getSelectedFontStyle()

        val newTypeFace = Typeface.create(fontName, typefaceID) //Always return value
        mainBinding.textInputEditText.typeface = newTypeFace
    }

    private fun getSelectedFont(): String {
        val selectedID = mainBinding.fontRadioGroup.checkedRadioButtonId
        val selectedButton = mainBinding.fontRadioGroup.findViewById<RadioButton>(selectedID) ?: return ""

        return selectedButton.text.toString()
    }

    private fun getSelectedFontStyle(): Int {
        val selectedID = mainBinding.styleRadioGroup.checkedRadioButtonId

        return mainBinding.styleRadioGroup.indexOfChild(findViewById(selectedID))
    }

    fun clearFont(view: View) {
        mainBinding.fontRadioGroup.check(mainBinding.fontRadioGroup.children.first().id)
        mainBinding.styleRadioGroup.check(mainBinding.styleRadioGroup.children.first().id)
        applyFont(findViewById(mainBinding.okButton.id))
    }
}