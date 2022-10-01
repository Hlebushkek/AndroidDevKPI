package com.example.lab1

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lab1.databinding.FragmentMainBinding

class MainFragment : Fragment(),
    StyleSelectionFragmentDelegate, FontSelectionFragmentDelegate{

    private lateinit var binding: FragmentMainBinding

    private lateinit var styleSelectionFrag: FontStyleSelectionFragment
    private lateinit var fontSelectionFrag: FontSelectionFragment

    private lateinit var dbHelper: LabSQLiteHelper

    private var selectedStyle: Int = 0
    private var selectedFontName: String = ""

    private fun applyFont() {
        val newTypeFace = Typeface.create(selectedFontName, selectedStyle) //Always return value
        binding.textInputEditText.typeface = newTypeFace

        dbHelper.addHistoryUnit(binding.textInputEditText.text.toString(), selectedFontName, selectedStyle)
    }

    private fun clearFont() {
        styleSelectionFrag.reset()
        fontSelectionFrag.reset()

        applyFont()
    }

    override fun onStart() {
        super.onStart()

        styleSelectionFrag = binding.fontStyleSelectionPlaceholder.getFragment()
        styleSelectionFrag.delegate = this

        fontSelectionFrag = binding.fontSelectionPlaceholder.getFragment()
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

    override fun fontSelectionDidChange(name: String) {
        selectedFontName = name
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

}