package com.example.lab1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.databinding.FragmentFontSelectionBinding
import com.example.lab1.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var adapter: HistoryAdapter
    private lateinit var history: Array<HistoryItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater)

        binding.historyRecycleView.layoutManager = LinearLayoutManager(context)
        initData()

        return binding.root
    }

    private fun initData() {
        val helper = LabSQLiteHelper.newInstance(context)
        history = helper.getHistory()

        adapter = HistoryAdapter(history)
        binding.historyRecycleView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}