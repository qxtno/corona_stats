package com.github.qxtno.coronastats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.databinding.ContinentItemBinding
import com.github.qxtno.coronastats.helpers.ContinentsHelper
import com.github.qxtno.coronastats.helpers.StringHelper
import com.github.qxtno.coronastats.model.ContinentResponse

class ContinentsAdapter(
    private val continentResponses: List<ContinentResponse>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ContinentsAdapter.ContinentsViewHolder>() {

    val stringHelper = StringHelper()
    val continentsHelper = ContinentsHelper()

    inner class ContinentsViewHolder(private val binding: ContinentItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(continentResponse: ContinentResponse) {
            with(binding) {
                continentTextView.text =
                    continentsHelper.translateContinent(continentResponse.continent)
                casesTextView.text = stringHelper.stringBuilderThreeParamsHorizontal(
                    itemView.context.getString(R.string.cases),
                    continentResponse.cases.toString(),
                    continentResponse.todayCases.toString()
                )
                deathsTextView.text = stringHelper.stringBuilderThreeParams(
                    itemView.context.getString(R.string.deaths),
                    continentResponse.deaths.toString(),
                    continentResponse.todayDeaths.toString()
                )
                recoveredTextView.text = stringHelper.stringBuilderThreeParams(
                    itemView.context.getString(R.string.recovered),
                    continentResponse.recovered.toString(),
                    continentResponse.todayRecovered.toString()
                )
                activeTextView.text = stringHelper.stringBuilderTwoParams(
                    itemView.context.getString(R.string.active),
                    continentResponse.active.toString()
                )
                criticalTextView.text = stringHelper.stringBuilderTwoParams(
                    itemView.context.getString(R.string.critical),
                    continentResponse.critical.toString()
                )
            }
        }

        init {
            binding.showDetailedDataButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContinentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContinentItemBinding.inflate(inflater, parent, false)

        return ContinentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContinentsViewHolder, position: Int) {
        holder.bind(continentResponses[position])
    }

    override fun getItemCount(): Int = continentResponses.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}