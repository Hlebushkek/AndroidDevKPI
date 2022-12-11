package com.example.lab1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.forEach
import com.example.lab1.databinding.FragmentFontStyleSelectionBinding

class FontStyleSelectionFragment : Fragment() {

    private lateinit var binding: FragmentFontStyleSelectionBinding

    public var delegate: StyleSelectionFragmentDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFontStyleSelectionBinding.inflate(inflater)

        binding.styleRadioGroup.setOnCheckedChangeListener { _, id ->
            var index = 0
            val view = view?.let {
                index = binding.styleRadioGroup.indexOfChild(view?.findViewById(id))
            }
            delegate?.selectedStyleDidChange(index)
        }

        return binding.root
    }

    fun setupFor(fontName: String) {
        if (fontName.lowercase().contains("bold")) {
            binding.boldRB.isEnabled = false
            binding.boldAndItalicRB.isEnabled = false
        } else {
            binding.styleRadioGroup.forEach {
                it.isEnabled = true
            }
        }
    }

    public fun reset() {
        binding.styleRadioGroup.check(binding.styleRadioGroup.children.first().id)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FontStyleSelectionFragment()
    }
}