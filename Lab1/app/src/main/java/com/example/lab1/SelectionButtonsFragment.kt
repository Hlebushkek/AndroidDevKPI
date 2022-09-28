package com.example.lab1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import com.example.lab1.databinding.ActivityMainBinding
import com.example.lab1.databinding.FragmentSelectionButtonsBinding

class SelectionButtonsFragment : Fragment() {

    private lateinit var binding: FragmentSelectionButtonsBinding

    public var delegate: StyleSelectionFragmentDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectionButtonsBinding.inflate(inflater)

        binding.styleRadioGroup.setOnCheckedChangeListener { _, id ->
            var index = 0
            val view = view?.let {
                index = binding.styleRadioGroup.indexOfChild(view?.findViewById(id))
            }
            delegate?.selectedStyleDidChange(index)
        }

        return binding.root
    }

    public fun reset() {
        binding.styleRadioGroup.check(binding.styleRadioGroup.children.first().id)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SelectionButtonsFragment()
    }
}