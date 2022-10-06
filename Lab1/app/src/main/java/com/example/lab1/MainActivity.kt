package com.example.lab1

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.lab1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
    StyleSelectionFragmentDelegate, FontSelectionFragmentDelegate {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var styleSelectionFrag: SelectionButtonsFragment
    private lateinit var fontSelectionFrag: FontSelectionFragment

    private val availableFonts = arrayOf(
        "sans-serif", "sans-serif-light", "monospace", "cursive", "casual",
        "times-new-roman", "palatino"
    )

    private var selectedStyle: Int = 0
    private var selectedFontName: String = ""

    fun applyFont(view: View) {
        val newTypeFace = Typeface.create(selectedFontName, selectedStyle) //Always return value
        mainBinding.textInputEditText.typeface = newTypeFace
    }

    fun clearFont(view: View) {
        styleSelectionFrag.reset()
        fontSelectionFrag.reset()

        applyFont(findViewById(mainBinding.okButton.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mainBinding.root)

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
        Log.d("TEST", type.toString())
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