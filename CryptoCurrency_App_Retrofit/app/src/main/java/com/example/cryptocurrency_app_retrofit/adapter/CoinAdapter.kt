package com.example.cryptocurrency_app_retrofit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_app_retrofit.R
import com.example.cryptocurrency_app_retrofit.databinding.CardTasarimBinding
import com.example.cryptocurrency_app_retrofit.model.CoinModelItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class CoinAdapter(
    var mContext: Context,
    var cryptoList: MutableList<CoinModelItem>,
    var showTrashIcon: Boolean = false,
) : RecyclerView.Adapter<CoinAdapter.CardCoinHolder>() {

    inner class CardCoinHolder(var tasarim: CardTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCoinHolder {
        val binding = CardTasarimBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardCoinHolder(binding)
    }

    override fun onBindViewHolder(holder: CardCoinHolder, position: Int) {
        val crypto = cryptoList[position]
        val t = holder.tasarim

        Picasso.get().load(crypto.webp64).into(t.imageView)
        t.tvCoinCode.text = crypto.name
        val formattedRate = String.format("%.4f", crypto.rate)
        t.tvValueName.text = "$$formattedRate"

        if (crypto.delta.day > 1) {
            t.tvChange.text = "+%${String.format("%.4f", crypto.delta.day - 1)}"
            t.tvChange.setTextColor(ContextCompat.getColor(mContext, R.color.green))
        } else {
            t.tvChange.text = "-%${String.format("%.4f", 1 - crypto.delta.day)}"
            t.tvChange.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }

        var favoriteCheck:Boolean
        if (isFavorite(crypto.code)) {
            t.favIcon.setImageResource(R.drawable.fav_full)
            favoriteCheck = true
        } else {
            t.favIcon.setImageResource(R.drawable.fav_empty)
            favoriteCheck = false
        }

        if (showTrashIcon) {
            t.tvCoinCode.text = crypto.code
            t.favIcon.setImageResource(R.drawable.trash)
            t.tvChange.visibility = View.GONE
            t.favIcon.setOnClickListener {
                Snackbar.make(it, "Remove ${crypto.code} coin from favorites?", Snackbar.LENGTH_SHORT)
                    .setAction("Yes") {
                        removeFav(crypto.code)
                        favoriteCheck = false
                    }
                    .show()
            }
        } else {
            if(favoriteCheck){
                t.favIcon.setOnClickListener {
                    Snackbar.make(it, "Remove ${crypto.code} coin from favorites?", Snackbar.LENGTH_SHORT)
                        .setAction("Yes") {
                            removeFav(crypto.code)
                            t.favIcon.setImageResource(R.drawable.fav_empty)
                            notifyItemChanged(position)
                            favoriteCheck = false
                        }
                        .show()
                }
            }else{
                t.favIcon.setOnClickListener {
                    Snackbar.make(it, "Add ${crypto.code} coin to favorites?", Snackbar.LENGTH_SHORT)
                        .setAction("Yes") {
                            addFav(crypto.code)
                            t.favIcon.setImageResource(R.drawable.fav_full)
                            favoriteCheck = true
                            notifyItemChanged(position)
                        }
                        .show()
                }
            }
        }
    }

    private fun isFavorite(code: String): Boolean {
        val sharedPreferences = mContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(code, false)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    fun addFav(code: String) {
        val sharedPreferences = mContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(code, true)
        editor.apply()

        val crypto = cryptoList.find { it.code == code }
        crypto?.let {
            if (!cryptoList.contains(it)) {
                cryptoList.add(it)
                notifyDataSetChanged()
            }
        }

    }

    fun removeFav(code: String) {
        val sharedPreferences = mContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(code)
        editor.apply()

        if(showTrashIcon) {
            removeCoin(code)
        }
    }

    fun removeCoin(code: String) {
        cryptoList.removeAll { it.code == code }
        notifyDataSetChanged()
    }

    fun updateData(newCryptoList: List<CoinModelItem>) {
        cryptoList.clear()
        cryptoList.addAll(newCryptoList)
        notifyDataSetChanged()
    }
}