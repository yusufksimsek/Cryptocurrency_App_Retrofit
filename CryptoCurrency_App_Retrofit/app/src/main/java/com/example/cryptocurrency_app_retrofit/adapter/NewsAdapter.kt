package com.example.cryptocurrency_app_retrofit.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_app_retrofit.databinding.CardTasarimNewsBinding
import com.example.cryptocurrency_app_retrofit.model.Article
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    var mContext: Context,
    var newsList: List<Article>,
) : RecyclerView.Adapter<NewsAdapter.CardNewsTasarimTutucu>() {


    inner class CardNewsTasarimTutucu(var tasarim: CardTasarimNewsBinding) :
        RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardNewsTasarimTutucu {
        val binding = CardTasarimNewsBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardNewsTasarimTutucu(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: CardNewsTasarimTutucu, position: Int) {
        val news = newsList[position]
        val t = holder.tasarim

        t.tvTitle.text = news.title

        t.tvURL.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            mContext.startActivity(intent)
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())

        try {
            val date = inputFormat.parse(news.publishedAt)
            val formattedDate = outputFormat.format(date!!)
            t.tvDate.text = formattedDate
        } catch (e: Exception) {
            Log.e("NewsAdapter", "Date format couldn't be changed", e)
            t.tvDate.text = news.publishedAt
        }

        if (!news.urlToImage.isNullOrBlank()) {
            Picasso.get().load(news.urlToImage).into(t.imageViewNew)
        } else {
            t.imageViewNew.setImageResource(com.example.cryptocurrency_app_retrofit.R.drawable.image_error)
        }

        val maxDescriptionLength = 130
        if (news.description?.length ?: 0 > maxDescriptionLength) {
            val truncatedDescription = "${news.description?.substring(0, maxDescriptionLength)}..."
            t.tvDescription.text = truncatedDescription
            t.tvReadMore.visibility = View.VISIBLE
        } else {
            t.tvReadMore.visibility = View.GONE
            t.tvDescription.text = news.description
            if (news.description?.length ?: 0 < maxDescriptionLength) {
                t.tvDescription.text = t.tvDescription.text.toString().removeSuffix("...")
            }
        }

        t.tvReadMore.setOnClickListener {
            t.tvReadMore.visibility = View.GONE
            t.tvDescription.text = "${news.description.dropLast(1)}."
        }

    }
}