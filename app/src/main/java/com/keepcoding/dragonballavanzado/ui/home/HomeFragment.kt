package com.keepcoding.dragonballavanzado.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepcoding.dragonballavanzado.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val adapter = HomeAdapter {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHeroList()

        with(binding) {
            heroList.adapter = adapter
            heroList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.favoriteIcon.setOnClickListener { 
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
        }
    }

    private fun setObservers() {

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.heros.collect { heros ->
                adapter.setHeros(heros)
            }
        }
    }
}