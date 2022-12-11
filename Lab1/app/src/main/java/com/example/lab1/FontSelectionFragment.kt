package com.example.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
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

    fun setupWith(fonts: Array<String>) {
        for (name in fonts) {
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