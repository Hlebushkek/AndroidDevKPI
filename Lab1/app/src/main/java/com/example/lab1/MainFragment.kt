package com.example.lab1

import android.graphics.Typeface
import android.graphics.fonts.Font
import android.graphics.fonts.SystemFonts
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lab1.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment(),
    StyleSelectionFragmentDelegate, FontSelectionFragmentDelegate{

    private lateinit var binding: FragmentMainBinding

    private lateinit var styleSelectionFrag: FontStyleSelectionFragment
    private lateinit var fontSelectionFrag: FontSelectionFragment

    private lateinit var dbHelper: LabSQLiteHelper

    private val fontManager = FontsManager.newInstance()

    private var selectedStyle: Int = 0
    private var selectedFontIndex: Int = 0

    private fun applyFont() {
        context?.let {
            val text = binding.textInputEditText.text.toString()
            if (text.isEmpty()) { return }

            var typeface = Typeface.createFromFile(fontManager.availableFonts[selectedFontIndex])
            typeface = Typeface.create(typeface, selectedStyle)
            val dialog = PopupDialogFragment(text, typeface)

            dialog.onDismissCallback = {
                dbHelper.addHistoryUnit(text, fontManager.availableFontsName[selectedFontIndex], selectedStyle)
                Snackbar.make(binding.root, "Info was saved to history", 500).show()
            }

            dialog.show(requireActivity().supportFragmentManager, "popup")
        }
    }

    private fun clearFont() {
        styleSelectionFrag.reset()
        fontSelectionFrag.reset()

        binding.textInputEditText.text?.clear()

        applyFont()
    }

    override fun onStart() {
        super.onStart()

        styleSelectionFrag = binding.fontStyleSelectionPlaceholder.getFragment()
        styleSelectionFrag.delegate = this

        fontSelectionFrag = binding.fontSelectionPlaceholder.getFragment()
        fontSelectionFrag.setupFonts(fontManager.availableFontsName)
        fontSelectionFrag.delegate = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.okButton.setOnClickListener {
            applyFont()
        }
        binding.cancelButton.setOnClickListener {
            clearFont()
        }
        binding.historyButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }

        dbHelper = LabSQLiteHelper.newInstance(context)

        return binding.root
    }

    //MARK: Implement Interfaces

    override fun selectedStyleDidChange(type: Int) {
        selectedStyle = type
    }

    override fun fontSelectionDidChange(index: Int) {
        selectedFontIndex = index
        styleSelectionFrag.setupFor(fontManager.availableFonts[index])
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

}