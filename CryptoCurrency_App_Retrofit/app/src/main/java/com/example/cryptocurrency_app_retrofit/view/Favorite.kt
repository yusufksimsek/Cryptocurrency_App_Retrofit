package com.example.cryptocurrency_app_retrofit.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_app_retrofit.adapter.CoinAdapter
import com.example.cryptocurrency_app_retrofit.databinding.FragmentFavoriteBinding
import com.example.cryptocurrency_app_retrofit.model.CoinModelItem

class Favorite : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var coinAdapter: CoinAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        val favoriCryptoList = loadFavorites()

        //val layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavorite.layoutManager = layoutManager

        coinAdapter = CoinAdapter(requireContext(), favoriCryptoList.toMutableList(), showTrashIcon = true)
        binding.rvFavorite.adapter = coinAdapter

        coinAdapter.updateData(favoriCryptoList)

        return view
    }

    private fun loadFavorites() : List<CoinModelItem> {
        val sharedPreferences = requireContext().getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val favoriCoinler = sharedPreferences.all.keys.toList()
        Log.e("Favorite Coins", favoriCoinler.toString())

        val favoriCryptoList = ArrayList<CoinModelItem>()

        for (coinCode in favoriCoinler) {
            val isFav = sharedPreferences.getBoolean(coinCode, false)
            val cryptoModel = Home.cryptoList.find { it.code == coinCode }

            if (cryptoModel != null && isFav) {
                favoriCryptoList.add(cryptoModel)
            }
        }

        return favoriCryptoList

    }

}