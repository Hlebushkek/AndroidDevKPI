package com.example.lab1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val history: Array<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        var item = history[position]
        holder.textView.text = item.text
        holder.fontNameView.text = item.fontName
        holder.fontStyleView.text = fontStyleToString(item.fontStyle)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    private fun fontStyleToString(fontStyle: Int): String {
        return when(fontStyle) {
            0 -> "Normal"
            1 -> "Bold"
            2 -> "Italic"
            3 -> "Bold Italic"
            else -> "[UNDEFINED]"
        }
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val fontNameView: TextView = itemView.findViewById(R.id.fontNameView)
        val fontStyleView: TextView = itemView.findViewById(R.id.fontStyleView)
    }

}