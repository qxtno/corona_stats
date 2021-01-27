package com.github.qxtno.coronastats.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater)
        setHasOptionsMenu(true)

        binding.sourceButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://github.com/disease-sh/API")
            startActivity(openURL)
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.refresh).isVisible = false
        menu.findItem(R.id.change_country).isVisible = false
    }
}