package com.github.qxtno.coronastats.fragments

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.adapters.ContinentsAdapter
import com.github.qxtno.coronastats.adapters.HistoricalAdapter
import com.github.qxtno.coronastats.databinding.FragmentGlobalDataBinding
import com.github.qxtno.coronastats.helpers.OnlineStatus
import com.github.qxtno.coronastats.helpers.StringHelper
import com.github.qxtno.coronastats.helpers.Toaster
import com.github.qxtno.coronastats.model.ContinentResponse
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.viewModel.GlobalDataViewModel
import java.text.DateFormat
import java.util.*


class GlobalDataFragment : Fragment(R.layout.fragment_global_data),
    ContinentsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentGlobalDataBinding
    private lateinit var onlineStatus: OnlineStatus
    private var stringHelper: StringHelper = StringHelper()
    private lateinit var toaster: Toaster
    private lateinit var viewModel: GlobalDataViewModel
    private var historicalAdapter: HistoricalAdapter? = null
    private var continentsAdapter: ContinentsAdapter? = null
    private var continentsList: List<ContinentResponse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGlobalDataBinding.inflate(inflater)
        viewModel = ViewModelProvider(requireActivity()).get(GlobalDataViewModel::class.java)

        setHasOptionsMenu(true)
        toaster = Toaster(requireActivity())
        onlineStatus = OnlineStatus(requireActivity())

        initRecyclerViews()
        dataObserver()
        handleHistoricalClick()
        handleContinentsClick()

        return binding.root
    }

    private fun initRecyclerViews() {
        binding.apply {
            recyclerViewHistorical.layoutManager = LinearLayoutManager(requireActivity())
            recyclerViewHistorical.setHasFixedSize(true)

            recyclerViewContinents.layoutManager = LinearLayoutManager(requireActivity())
            recyclerViewContinents.setHasFixedSize(true)
        }
    }

    private fun handleHistoricalClick() {
        binding.apply {
            showHistoricalButton.setOnClickListener {
                if (recyclerViewHistorical.visibility == GONE) {
                    historicalObserver()
                    recyclerViewHistorical.visibility = VISIBLE
                    labels.visibility = VISIBLE
                    showHistoricalButton.text = getText(R.string.hide_historical)
                } else {
                    recyclerViewHistorical.visibility = GONE
                    labels.visibility = GONE
                    showHistoricalButton.text = getText(R.string.show_historical)
                }
            }
        }
    }

    private fun handleContinentsClick() {
        binding.apply {
            showContinentsButton.setOnClickListener {
                if (recyclerViewContinents.visibility == GONE) {
                    continentsObserver()
                    recyclerViewContinents.visibility = VISIBLE
                    showContinentsButton.text = getText(R.string.hide_continents)
                } else {
                    recyclerViewContinents.visibility = GONE
                    showContinentsButton.text = getText(R.string.show_continents)
                }
            }
        }
    }

    private fun historicalObserver() {
        viewModel.getHistoricalAll().observe(viewLifecycleOwner,
            { t ->
                historicalAdapter = HistoricalAdapter(t.reversed())
                binding.recyclerViewHistorical.adapter = historicalAdapter
                historicalAdapter?.notifyDataSetChanged()
            })
    }

    private fun dataObserver() {
        viewModel.getGlobalAll().observe(viewLifecycleOwner,
            { t ->
                setTextViews(t)
            })
    }

    private fun continentsObserver() {
        viewModel.getContinents().observe(viewLifecycleOwner, { t ->
            continentsAdapter = ContinentsAdapter(t, this)
            binding.recyclerViewContinents.adapter = continentsAdapter
            continentsAdapter?.notifyDataSetChanged()
            continentsList = t
        })
    }

    private fun setTextViews(t: SummaryResponse) {
        binding.apply {
            val date = Date(t.updated)
            val format =
                DateFormat.getDateTimeInstance().format(date)
            updatedTextView.text = format

            card.casesTextView.text = stringHelper.stringBuilderThreeParamsHorizontal(
                getString(R.string.cases),
                t.cases.toString(),
                t.todayCases.toString()
            )
            card.deathsTextView.text = stringHelper.stringBuilderThreeParams(
                getString(R.string.deaths),
                t.deaths.toString(),
                t.todayDeaths.toString()
            )
            card.recoveredTextView.text = stringHelper.stringBuilderThreeParams(
                getString(R.string.recovered),
                t.recovered.toString(),
                t.todayRecovered.toString()
            )
            card.activeTextView.text = stringHelper.stringBuilderTwoParams(
                getString(R.string.active),
                t.active.toString()
            )
            card.criticalTextView.text = stringHelper.stringBuilderTwoParams(
                getString(R.string.critical),
                t.critical.toString()
            )
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.change_country).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                if (onlineStatus.isOnline()) {
                    refresh()
                } else {
                    toaster.toast(getString(R.string.check_internet_connection))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refresh() {
        viewModel.updateHistoricalAll().observe(
            viewLifecycleOwner,
            { t ->
                historicalAdapter = HistoricalAdapter(t.reversed())
                binding.recyclerViewHistorical.adapter = historicalAdapter
                historicalAdapter?.notifyDataSetChanged()
            })
        viewModel.updateGlobalAll().observe(viewLifecycleOwner,
            { t ->
                setTextViews(t)
            })
        viewModel.updateContinents().observe(viewLifecycleOwner, { t ->
            continentsAdapter = ContinentsAdapter(t, this)
            binding.recyclerViewContinents.adapter = continentsAdapter
            continentsAdapter?.notifyDataSetChanged()
            continentsList = t
        })
        toaster.toast(getString(R.string.updated))
    }

    override fun onItemClick(position: Int) {
        val continent = continentsList[position].continent
        val action = GlobalDataFragmentDirections.actionGlobalStatsBottomToContinentStats(continent)
        view?.findNavController()?.navigate(action)
    }
}
