package com.example.cryptocurrency_app_retrofit.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_app_retrofit.adapter.NewsAdapter
import com.example.cryptocurrency_app_retrofit.databinding.FragmentNewsBinding
import com.example.cryptocurrency_app_retrofit.model.ApiResponseNews
import com.example.cryptocurrency_app_retrofit.model.Article
import com.example.cryptocurrency_app_retrofit.service.NewsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptocurrency_app_retrofit.R

class News : Fragment() {

    private val BASE_URL = "https://newsapi.org/"
    private lateinit var binding: FragmentNewsBinding
    private var newsList = ArrayList<Article>()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsAPI: NewsAPI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(requireContext(), newsList)
        binding.rvNews.adapter = newsAdapter

        loadData()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            loadData()
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsAPI = retrofit.create(NewsAPI::class.java)
        val call = newsAPI.getArticles()

        call.enqueue(object : Callback<ApiResponseNews> {
            override fun onResponse(
                call: Call<ApiResponseNews>,
                response: Response<ApiResponseNews>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {

                        val filteredNewsList = apiResponse.articles.filter { news ->
                            !news.urlToImage.isNullOrBlank() && !news.description.isNullOrBlank()
                        }
                        newsList.clear()
                        newsList.addAll(filteredNewsList)
                        newsAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponseNews>, t: Throwable) {
                Log.e("NewsFragment", "API call failure", t)
            }
        })


    }


}