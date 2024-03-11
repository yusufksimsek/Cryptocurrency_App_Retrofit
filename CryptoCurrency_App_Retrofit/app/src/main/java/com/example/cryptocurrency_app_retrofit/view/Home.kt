package com.example.cryptocurrency_app_retrofit.view
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_app_retrofit.R
import com.example.cryptocurrency_app_retrofit.adapter.CoinAdapter
import com.example.cryptocurrency_app_retrofit.databinding.FragmentHomeBinding
import com.example.cryptocurrency_app_retrofit.model.ApiCoinClient
import com.example.cryptocurrency_app_retrofit.model.CoinListRequest
import com.example.cryptocurrency_app_retrofit.model.CoinModelItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var imageSlider: ImageView
    private val imageList = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6)
    private var currentImageIndex = 0

    companion object {
        var cryptoList = ArrayList<CoinModelItem>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        imageSlider = view.findViewById(R.id.imageSlider)
        startImageSlider()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }

        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        coinAdapter = CoinAdapter(requireContext(), cryptoList)
        binding.rvHome.adapter = coinAdapter

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.length >= 1) {
                    searchCrypto(query)
                } else {
                    coinAdapter.updateData(cryptoList)
                }
            }
        })

        loadData()

        return view

    }

    private fun loadData() {
        binding.swipeRefreshLayout.isRefreshing = true
        val request = CoinListRequest(
            currency = "USD",
            sort = "rank",
            order = "ascending",
            offset = 0,
            limit = 100,
            meta = true
        )

        val call = ApiCoinClient.cryptoService.getCoins(request)

        call.enqueue(object : Callback<List<CoinModelItem>> {
            override fun onResponse(
                call: Call<List<CoinModelItem>>,
                response: Response<List<CoinModelItem>>
            ) {
                if (response.isSuccessful) {
                    cryptoList = (response.body() ?: emptyList()) as ArrayList<CoinModelItem>
                    coinAdapter.updateData(cryptoList)
                } else {
                    Log.e("newcode", "API request failure: ${response.code()}")
                }

                binding.swipeRefreshLayout.isRefreshing = false

            }

            override fun onFailure(call: Call<List<CoinModelItem>>, t: Throwable) {
                t.printStackTrace()
                Log.e("newcode", "API call failure: ${t.message}")
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })

    }

    private fun searchCrypto(query: String) {
        val filteredList = cryptoList.filter { it.name.contains(query, ignoreCase = true) || it.code.contains(query, ignoreCase = true)}
        coinAdapter.updateData(filteredList)
    }

    private fun startImageSlider() {
        val handler = Handler()

        val imageRunnable = object : Runnable {
            override fun run() {
                if (currentImageIndex == imageList.size) {
                    currentImageIndex = 0
                }

                imageSlider.animate()
                    .translationX(-imageSlider.width.toFloat())
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        imageSlider.setImageResource(imageList[currentImageIndex])
                        imageSlider.translationX = imageSlider.width.toFloat()
                        imageSlider.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(500)
                            .start()
                        currentImageIndex++
                    }
                    .start()

                handler.postDelayed(this, 4000)
            }
        }

        imageSlider.setImageResource(imageList[currentImageIndex])
        currentImageIndex++
        handler.postDelayed(imageRunnable, 4000)
    }

}
