package com.github.qxtno.coronastats.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.R.layout
import com.github.qxtno.coronastats.R.string
import com.github.qxtno.coronastats.activities.MainActivity
import com.github.qxtno.coronastats.adapters.CountriesAdapter
import com.github.qxtno.coronastats.databinding.FragmentCountrySelectBinding
import com.github.qxtno.coronastats.helpers.PrefsManager
import com.github.qxtno.coronastats.helpers.Toaster
import com.github.qxtno.coronastats.viewModel.CountrySelectViewModel
import com.github.qxtno.coronastats.viewModel.SharedViewModel
import java.text.Collator
import java.util.*

class CountrySelectFragment : Fragment(layout.fragment_country_select),
    CountriesAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCountrySelectBinding
    private val prefsManager = PrefsManager()
    private lateinit var viewModel: CountrySelectViewModel
    private var adapter: CountriesAdapter? = null
    private var list: List<String> = emptyList()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var sortedMap: Map<String, String> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountrySelectBinding.inflate(inflater)

        val activity = (activity as MainActivity)
        activity.changeBottomNavVisibility(false)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(CountrySelectViewModel::class.java)

        val country = prefsManager.getCountryString(requireActivity(), "country", "PL")
        binding.currentCountryTextView.text = Locale("", country ?: "Polska").displayName

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.setHasFixedSize(true)

        viewModel.getCountries().observe(viewLifecycleOwner, { t ->
            val map = t.associate {
                Pair(
                    Locale("", it.countryInfo.iso2 ?: it.country).displayName,
                    it.countryInfo.iso2 ?: it.country
                )
            }
            val collator = Collator.getInstance(Locale("pl", "PL"))
            sortedMap = map.toSortedMap(compareBy(collator, { it }))
            list = sortedMap.keys.map {
                val string = Locale("", map[it] ?: "").displayName

                string
            }

            adapter = CountriesAdapter(list, this)
            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.refresh).isVisible = false
        menu.findItem(R.id.change_country).isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).changeBottomNavVisibility(true)
    }

    override fun onItemClick(position: Int) {
        val value = list[position]
        val values = sortedMap.filterKeys { it == value }.values
        val re = Regex("[^A-Za-z0-9 ]")
        val countryCode = re.replace(values.toString(), "")
        prefsManager.setCountryString(requireContext(), "country", countryCode)
        sharedViewModel.setCountry(Locale("", countryCode).getDisplayCountry(Locale.ENGLISH))
        activity?.onBackPressed()
        val toaster = Toaster(requireContext())
        toaster.toast(getString(string.toast_changed_country))
    }
}