package com.yadaniil.bitcurve.screens.settings.restore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.bitcurve.R
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

/**
 * Created by danielyakovlev on 2/12/18.
 */


class MnemonicSuggestionAdapter(private val words: MutableList<String>,
                                private val onWordClick: OnWordClick)
    : RecyclerView.Adapter<MnemonicSuggestionAdapter.WordHolder>() {

    interface OnWordClick {
        fun onClick(holder: WordHolder, word: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val view =  LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mnemonic_word, parent, false)
        return WordHolder(view)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        val wordString = words[position]
        holder.word.text = wordString
        holder.rootLayout.onClick { onWordClick.onClick(holder, wordString) }
    }

    fun updateSuggestions(newSuggestions: MutableList<String>) {
        words.clear()
        words.addAll(newSuggestions)
        notifyDataSetChanged()
    }

    override fun getItemCount() = words.size

    class WordHolder(view: View) : RecyclerView.ViewHolder(view) {
        var word: TextView = view.find(R.id.word)
        var rootLayout: LinearLayout = view.find(R.id.root_layout)
    }
}