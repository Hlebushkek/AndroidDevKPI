package com.example.lab1

import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import android.graphics.fonts.SystemFonts
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lab1.databinding.ActivityMainBinding
import java.lang.reflect.Type

class MainActivity : AppCompatActivity(),
    StyleSelectionFragmentDelegate, FontSelectionFragmentDelegate {

    private lateinit var binding: ActivityMainBinding

    private lateinit var styleSelectionFrag: SelectionButtonsFragment
    private lateinit var fontSelectionFrag: FontSelectionFragment

    private val availableFonts = arrayListOf<String>()

    private var selectedStyle: Int = 0
    private var selectedFontIndex: Int = 0

    fun applyFont(view: View) {
        val text = binding.textInputEditText.text.toString()
        if (text.isEmpty()) { return }

        var typeface = Typeface.createFromFile(availableFonts[selectedFontIndex])
        typeface = Typeface.create(typeface, selectedStyle)
        val dialog = PopupDialogFragment(text, typeface)

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

        val fontFolderPath = "/system/fonts/"
        val fontExt = arrayOf(".ttf", ".otf", ".ttc")
        var fontNames = arrayListOf<String>()
        SystemFonts.getAvailableFonts().forEach { font ->
            val fontPath = font.file?.path.toString()
            availableFonts.add(fontPath)

            var fontName = fontPath.removePrefix(fontFolderPath)
            fontExt.forEach { ext ->
                fontName = fontName.removeSuffix(ext)
            }
            fontNames.add(fontName)
            Log.d("!!!", fontName)
        }

        fontSelectionFrag.setupWith(fontNames.toTypedArray())
    }

    //MARK: Implement Interfaces

    override fun selectedStyleDidChange(type: Int) {
        selectedStyle = type
    }

    override fun fontSelectionDidChange(index: Int) {
        selectedFontIndex = index
        styleSelectionFrag.setupFor(availableFonts[index])
    }

    //MARK: Helpers

    private fun openFrag(frag: Fragment, holderID: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(holderID, frag)
            .commit()
    }
}