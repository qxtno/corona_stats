package com.github.qxtno.coronastats.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.adapters.HistoricalAdapter
import com.github.qxtno.coronastats.databinding.FragmentCurrentDataBinding
import com.github.qxtno.coronastats.helpers.OnlineStatus
import com.github.qxtno.coronastats.helpers.PrefsManager
import com.github.qxtno.coronastats.helpers.StringHelper
import com.github.qxtno.coronastats.helpers.Toaster
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.viewModel.CurrentDataViewModel
import com.github.qxtno.coronastats.viewModel.CurrentDataViewModelFactory
import com.github.qxtno.coronastats.viewModel.SharedViewModel
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.util.*

class CurrentDataFragment : Fragment(R.layout.fragment_current_data) {
    private lateinit var binding: FragmentCurrentDataBinding
    private lateinit var onlineStatus: OnlineStatus

    private var stringHelper: StringHelper = StringHelper()
    private lateinit var toaster: Toaster
    private var prefs: PrefsManager = PrefsManager()
    private lateinit var viewModel: CurrentDataViewModel
    private var adapter: HistoricalAdapter? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentDataBinding.inflate(inflater)
        setHasOptionsMenu(true)
        toaster = Toaster(requireActivity())
        onlineStatus = OnlineStatus(requireActivity())

        val country = prefs.getCountryString(requireActivity(), "country", "PL")

        setViewModel(Locale("", country ?: "PL").getDisplayCountry(Locale.ENGLISH))

        initRecyclerView()
        dataObserver()
        handleHistoricalClick()

        observeCountry()

        return binding.root
    }

    private fun observeCountry() {
        sharedViewModel.country.observe(requireActivity(), {
            viewModel.setCountry(it)
        })
    }

    private fun setViewModel(country: String) {
        viewModel = ViewModelProvider(
            requireActivity(), CurrentDataViewModelFactory(country)
        ).get(CurrentDataViewModel::class.java)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun handleHistoricalClick() {
        binding.apply {
            showHistoricalButton.setOnClickListener {
                if (recyclerView.visibility == GONE) {
                    historicalObserver()
                    recyclerView.visibility = VISIBLE
                    labels.visibility = VISIBLE
                    showHistoricalButton.text = getText(R.string.hide_historical)
                } else {
                    recyclerView.visibility = GONE
                    labels.visibility = GONE
                    showHistoricalButton.text = getText(R.string.show_historical)
                }
            }
        }
    }

    private fun dataObserver() {
        viewModel.getSummary().observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    setTextViews(t)
                }
            })
    }

    private fun setTextViews(t: SummaryResponse) {
        binding.apply {
            countryTextView.text =
                Locale("", t.countryInfo.iso2 ?: t.country).displayName
            Picasso.get().load(t.countryInfo.flag)
                .into(flagImageView)

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

    private fun historicalObserver() {
        viewModel.getHistorical().observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    if (t.isEmpty()) {
                        toaster.toast(getString(R.string.empty))
                    } else {
                        adapter = HistoricalAdapter(t.reversed())
                        binding.recyclerView.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }
                }
            })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                if (onlineStatus.isOnline()) {
                    refreshData()
                } else {
                    toaster.toast(getString(R.string.check_internet_connection))
                }
            }
            R.id.change_country -> {
                view?.findNavController()?.navigate(R.id.country_select)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshData() {
        viewModel.getRefreshed().observe(viewLifecycleOwner, { t ->
            if (t != null) {
                setTextViews(t)
            }
        })

        viewModel.getHistoricalRefreshed().observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    adapter = HistoricalAdapter(t.reversed())
                    binding.recyclerView.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
            })

        toaster.toast(getString(R.string.updated))
    }
}