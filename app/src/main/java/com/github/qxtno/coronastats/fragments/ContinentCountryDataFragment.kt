package com.github.qxtno.coronastats.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.activities.MainActivity
import com.github.qxtno.coronastats.adapters.HistoricalAdapter
import com.github.qxtno.coronastats.databinding.FragmentContinentCountryDataBinding
import com.github.qxtno.coronastats.helpers.OnlineStatus
import com.github.qxtno.coronastats.helpers.StringHelper
import com.github.qxtno.coronastats.helpers.Toaster
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.viewModel.ContinentCountryViewModel
import com.github.qxtno.coronastats.viewModel.ContinentCountryViewModelFactory
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.util.*

class ContinentCountryDataFragment : Fragment() {

    private lateinit var binding: FragmentContinentCountryDataBinding
    private lateinit var viewModel: ContinentCountryViewModel
    private lateinit var onlineStatus: OnlineStatus
    private val stringHelper: StringHelper = StringHelper()
    private lateinit var toaster: Toaster
    private var adapter: HistoricalAdapter? = null
    private val args: ContinentCountryDataFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContinentCountryDataBinding.inflate(inflater)
        toaster = Toaster(requireActivity())
        setHasOptionsMenu(true)
        onlineStatus = OnlineStatus(requireContext())

        val country = args.country
        (activity as MainActivity).actionBar.title = Locale("", country).displayCountry

        viewModel = ViewModelProvider(this, ContinentCountryViewModelFactory(country)).get(
            ContinentCountryViewModel::class.java
        )

        initRecyclerView()
        dataObserver()
        handleHistoricalClick()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun handleHistoricalClick() {
        binding.apply {
            showHistoricalButton.setOnClickListener {
                if (recyclerView.visibility == View.GONE) {
                    historicalObserver()
                    recyclerView.visibility = View.VISIBLE
                    labels.visibility = View.VISIBLE
                    showHistoricalButton.text = getText(R.string.hide_historical)
                } else {
                    recyclerView.visibility = View.GONE
                    labels.visibility = View.GONE
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
                Locale("", t.countryInfo.iso2 ?: t.country).displayCountry
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
                    refresh()
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

    private fun refresh() {
        viewModel.getRefreshed().observe(viewLifecycleOwner, { t ->
            if (t != null) {
                setTextViews(t)
            }
        })

        viewModel.getHistoricalRefreshed().observe(
            viewLifecycleOwner,
            { t ->
                if (t != null) {
                    adapter = HistoricalAdapter(t.reversed())
                    binding.recyclerView.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
            })

        toaster.toast(getString(R.string.updated))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.change_country).isVisible = false
    }
}