package com.example.lab1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import com.example.lab1.databinding.FragmentFontSelectionBinding

class FontSelectionFragment : Fragment() {

    private lateinit var binding: FragmentFontSelectionBinding

    var delegate: FontSelectionFragmentDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFontSelectionBinding.inflate(inflater)

        binding.fontRadioGroup.setOnCheckedChangeListener { group, id ->
            val radio = group.findViewById<RadioButton>(id)
            val index: Int = group.indexOfChild(radio)
            delegate?.fontSelectionDidChange(index)
        }

        return binding.root
    }

    fun reset() {
        binding.fontRadioGroup.check(binding.fontRadioGroup.children.first().id)
    }

    fun setupFonts(availableFonts: ArrayList<String>) {
        for (name in availableFonts) {
            val button = RadioButton(context)
            button.text = name
            button.id = View.generateViewId()
            binding.fontRadioGroup.addView(button)
        }

        binding.fontRadioGroup.check(binding.fontRadioGroup.children.first().id)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FontSelectionFragment()
    }
}