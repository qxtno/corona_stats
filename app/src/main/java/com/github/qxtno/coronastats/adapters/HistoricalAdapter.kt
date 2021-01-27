package com.github.qxtno.coronastats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.qxtno.coronastats.databinding.HistoricalItemBinding
import com.github.qxtno.coronastats.model.Historical

class HistoricalAdapter(private val historical: List<Historical>) :
    RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder>() {

    inner class HistoricalViewHolder(private val binding: HistoricalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historical: Historical) {
            with(binding) {
                dateTextView.text = historical.date
                casesTextView.text = historical.cases.toString()
                deathsTextView.text = historical.deaths.toString()
                recoveredTextView.text = historical.recovered.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoricalItemBinding.inflate(inflater, parent, false)

        return HistoricalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        holder.bind(historical[position])
    }

    override fun getItemCount(): Int = historical.size
}