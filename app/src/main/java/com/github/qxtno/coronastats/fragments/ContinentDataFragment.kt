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
import com.github.qxtno.coronastats.adapters.CountriesAdapter
import com.github.qxtno.coronastats.databinding.FragmentContinentDataBinding
import com.github.qxtno.coronastats.helpers.ContinentsHelper
import com.github.qxtno.coronastats.helpers.OnlineStatus
import com.github.qxtno.coronastats.helpers.StringHelper
import com.github.qxtno.coronastats.helpers.Toaster
import com.github.qxtno.coronastats.model.ContinentResponse
import com.github.qxtno.coronastats.viewModel.ContinentDataViewModel
import com.github.qxtno.coronastats.viewModel.ContinentDataViewModelFactory
import java.text.Collator
import java.text.DateFormat
import java.util.*

class ContinentDataFragment : Fragment(R.layout.fragment_continent_data),
    CountriesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentContinentDataBinding
    private lateinit var viewModel: ContinentDataViewModel
    private var stringHelper: StringHelper = StringHelper()
    private lateinit var onlineStatus: OnlineStatus
    private lateinit var toaster: Toaster
    private var countriesList: List<String> = emptyList()
    private var list: List<String> = emptyList()
    private var adapter: CountriesAdapter? = null
    private val args: ContinentDataFragmentArgs by navArgs()
    private val continentsHelper = ContinentsHelper()
    private var sortedMap: Map<String, String> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContinentDataBinding.inflate(inflater)

        (activity as MainActivity).changeBottomNavVisibility(false)
        setHasOptionsMenu(true)
        toaster = Toaster(requireActivity())
        onlineStatus = OnlineStatus(requireActivity())

        initRecyclerView()
        handleCountriesClick()

        val continent = args.continent
        val continentTranslated = continentsHelper.translateContinent(continent)
        (activity as MainActivity).actionBar.title = continentTranslated

        setViewModel(continent)
        dataObserver()

        return binding.root
    }

    private fun setViewModel(continent: String) {
        viewModel = ViewModelProvider(this, ContinentDataViewModelFactory(continent)).get(
            ContinentDataViewModel::class.java
        )
    }

    private fun handleCountriesClick() {
        binding.apply {
            showCountriesButton.setOnClickListener {
                if (recyclerView.visibility == View.GONE) {
                    getCountries()
                    recyclerView.visibility = View.VISIBLE
                    showCountriesButton.text = getText(R.string.hide_countries)
                } else {
                    recyclerView.visibility = View.GONE
                    showCountriesButton.text = getText(R.string.show_countries)
                }
            }
        }
    }

    private fun getCountries() {
        viewModel.getCountries().observe(viewLifecycleOwner, { t ->
            val map = t.associate { Pair(it.country, it.countryInfo.iso2 ?: it.country) }
            val continentMap = list.associateWith { map[it] ?: it }
            countriesList = continentMap.keys.map {
                val string = map[it] ?: ""

                string
            }
            val translatedContinentMap =
                countriesList.associateBy { Locale("", it).displayName }
            val collator = Collator.getInstance(Locale("pl", "PL"))
            sortedMap = translatedContinentMap.toSortedMap(compareBy(collator, { it }))
            countriesList = sortedMap.keys.map {
                val string = Locale("", sortedMap[it] ?: "").displayName

                string
            }
            adapter = CountriesAdapter(countriesList, this)
            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
    }

    private fun dataObserver() {
        viewModel.getContinent().observe(viewLifecycleOwner, { t ->
            setTextViews(t)
            list = t.countries
        })
    }

    private fun setTextViews(t: ContinentResponse) {
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

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int) {
        val value = countriesList[position]
        val values = sortedMap.filterKeys { it == value }.values
        val re = Regex("[^A-Za-z0-9 ]")
        val countryCode = re.replace(values.toString(), "")
        val action =
            ContinentDataFragmentDirections.actionContinentStatsToContinentCountryData(countryCode)
        view?.findNavController()?.navigate(action)
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
        viewModel.updateContinent().observe(viewLifecycleOwner, { t ->
            setTextViews(t)
            list = t.countries
            adapter = CountriesAdapter(list, this)
            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
        toaster.toast(getString(R.string.updated))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.change_country).isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).changeBottomNavVisibility(true)
    }
}