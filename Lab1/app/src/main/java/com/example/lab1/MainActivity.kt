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

    private lateinit var binding: ActivityMainBinding
    private val availableFonts = arrayOf(
        "sans-serif", "sans-serif-light", "monospace", "cursive", "casual",
        "times-new-roman", "palatino"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        for (name in availableFonts) {
            val button = RadioButton(this)
            button.text = name
            button.id = View.generateViewId()
            binding.fontRadioGroup.addView(button)
        }
        binding.fontRadioGroup.check(binding.fontRadioGroup.children.first().id)
    }

    fun applyFont(view: View) {
        val fontName = getSelectedFont()
        val typefaceID = getSelectedFontStyle()

        val newTypeFace = Typeface.create(fontName, typefaceID) //Always return value
        binding.textInputEditText.typeface = newTypeFace
    }

    private fun getSelectedFont(): String {
        val selectedID = binding.fontRadioGroup.checkedRadioButtonId
        val selectedButton = binding.fontRadioGroup.findViewById<RadioButton>(selectedID) ?: return ""

        return selectedButton.text.toString()
    }

    private fun getSelectedFontStyle(): Int {
        val selectedID = binding.styleRadioGroup.checkedRadioButtonId

        return binding.styleRadioGroup.indexOfChild(findViewById(selectedID))
    }

    fun clearFont(view: View) {
        binding.textInputEditText.text?.clear()
        binding.fontRadioGroup.check(binding.fontRadioGroup.children.first().id)
        binding.styleRadioGroup.check(binding.styleRadioGroup.children.first().id)
        applyFont(findViewById(binding.okButton.id))
    }
}