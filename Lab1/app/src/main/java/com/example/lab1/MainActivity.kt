package com.example.lab1

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lab1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
    StyleSelectionFragmentDelegate, FontSelectionFragmentDelegate {

    private lateinit var binding: ActivityMainBinding

    private lateinit var styleSelectionFrag: SelectionButtonsFragment
    private lateinit var fontSelectionFrag: FontSelectionFragment

    private val availableFonts = arrayOf(
        "sans-serif", "sans-serif-light", "monospace", "cursive", "casual",
        "times-new-roman", "palatino"
    )

    private var selectedStyle: Int = 0
    private var selectedFontName: String = ""

    fun applyFont(view: View) {
        val text = binding.textInputEditText.text.toString()
        if (text.isEmpty()) { return }

        val typeFace = Typeface.create(selectedFontName, selectedStyle) //Always return value

        val dialog = PopupDialogFragment(text, typeFace)

        dialog.show(supportFragmentManager, "popup")
    }

    fun clearFont(view: View) {
        binding.textInputEditText.text?.clear()
        styleSelectionFrag.reset()
        fontSelectionFrag.reset()

        applyFont(findViewById(binding.okButton.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        styleSelectionFrag = SelectionButtonsFragment.newInstance()
        styleSelectionFrag.delegate = this
        openFrag(styleSelectionFrag, R.id.textStylePlaceholder)

        fontSelectionFrag = FontSelectionFragment.newInstance()
        fontSelectionFrag.delegate = this
        openFrag(fontSelectionFrag, R.id.fontSelectionPlaceholder)
    }

    override fun onStart() {
        super.onStart()

        fontSelectionFrag.setupWith(availableFonts)
    }

    //MARK: Implement Interfaces

    override fun selectedStyleDidChange(type: Int) {
        selectedStyle = type
    }

    override fun fontSelectionDidChange(name: String) {
        selectedFontName = name
    }

    //MARK: Helpers

    private fun openFrag(frag: Fragment, holderID: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(holderID, frag)
            .commit()
    }
}