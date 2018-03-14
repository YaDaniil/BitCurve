package com.yadaniil.bitcurve.screens.settings.restore

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.utils.Navigator
import kotlinx.android.synthetic.main.activity_mnemonic_restore.*
import org.bitcoinj.crypto.MnemonicCode
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.textChangedListener
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel

/**
 * Created by danielyakovlev on 2/12/18.
 */

class MnemonicRestoreActivity : AppCompatActivity(), MnemonicSuggestionAdapter.OnWordClick {

    private val viewModel by viewModel<MnemonicRestoreViewModel>()

    private lateinit var mnemonicAdapter: MnemonicSuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mnemonic_restore)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initMnemonicEditText()
        initSuggestions()
    }

    private fun initMnemonicEditText() {
        mnemonic.textChangedListener {
            afterTextChanged {
                try {
                    val lastWordTyped = it?.toString()?.substring(it.lastIndexOf(' ') + 1, it.lastIndex + 1)
                    updateSuggestions(lastWordTyped ?: "")
                } catch (e: StringIndexOutOfBoundsException) {
                    updateSuggestions(it.toString())
                }

            }
        }
    }

    private fun initSuggestions() {
        val wordList = if(mnemonic.text.isBlank()) emptyList<String>() else MnemonicCode.INSTANCE.wordList
        mnemonicAdapter = MnemonicSuggestionAdapter(wordList.toMutableList(), this)

        word_list.layoutManager = LinearLayoutManager(this)
        word_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        word_list.adapter = mnemonicAdapter
    }

    private fun updateSuggestions(string: String) {
        if(string.isBlank()) {
            mnemonicAdapter.updateSuggestions(mutableListOf())
        } else {
            val newSuggestions: MutableList<String> = ArrayList()
            MnemonicCode.INSTANCE.wordList.forEach {
                if(it.startsWith(string, false))
                    newSuggestions.add(it)
            }
            mnemonicAdapter.updateSuggestions(newSuggestions)
        }
    }

    override fun onClick(holder: MnemonicSuggestionAdapter.WordHolder, word: String) {
        val text = mnemonic.text.toString()
        val position = getCurrentWordPosition()
        val insertToEnd = position.second == text.length
        mnemonic.text.replace(position.first, position.second,
                word + if (insertToEnd) " " else "")
        mnemonic.setSelection(getCurrentWordPosition().second)
    }

    private fun getCurrentWordPosition(): Pair<Int, Int> {
        val text = mnemonic.text.toString()
        var end = mnemonic.selectionEnd
        var start = end - 1
        if (start < 0) {
            return Pair(0, end)
        }

        while (start >= 0) {
            if (Character.isSpaceChar(text.get(start))) {
                break
            }
            start--
        }

        while (end < text.length) {
            if (Character.isSpaceChar(text.get(end))) {
                break
            }
            end++
        }

        return Pair(++start, end)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            R.id.action_done -> {
                restoreWallet()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun restoreWallet() {
        val progress = indeterminateProgressDialog(R.string.loading) { setCancelable(false) }
        progress.show()
        viewModel.restoreWallet(mnemonic.text.trim().toString())
                .observe(this, Observer {
                    it?.let { isWalletRestored ->
                        progress.dismiss()
                        if (isWalletRestored)
                            Navigator.toAccountsActivity(this)
                        else
                            toast(R.string.wallet_restoring_error)
                    }
                })
    }
}
