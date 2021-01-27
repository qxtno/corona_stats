package com.github.qxtno.coronastats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.qxtno.coronastats.databinding.CountryItemBinding

class CountriesAdapter(
    private val countries: List<String>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    inner class CountriesViewHolder(private val binding: CountryItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(string: String) {
            with(binding) {
                countryNameTextView.text = string
            }
        }

        init {
            binding.countryNameTextView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryItemBinding.inflate(inflater, parent, false)

        return CountriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}