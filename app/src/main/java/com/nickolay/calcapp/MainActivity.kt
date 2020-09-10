package com.nickolay.calcapp


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var isDark = false

    private fun saveKey(theme: Int) = sharedPrefs.edit().putInt(PREFS_KEY_THEME, theme).apply()
    private fun loadKey() = sharedPrefs.getInt(PREFS_KEY_THEME, 0)

    private val sharedPrefs by lazy {
        getSharedPreferences(
            (MainActivity::class).qualifiedName,
            Context.MODE_PRIVATE
        )
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initTheme()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.resultValue.observe(this, { tvResult.text = it })
        viewModel.historyValue.observe(this, { tvHistory.text = it })

        initButtons()
    }

    private fun initTheme() {
        isDark = loadKey() == THEME_DARK
        if (isDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveKey(prefsMode)
    }

    private fun initButtons() {
        val bNumbers = arrayOf(bDecimal, b_0, b_1, b_2, b_3, b_4, b_5, b_6, b_7, b_8, b_9)
        val bOperations = arrayOf(bPlus, bMinus, bMultiply, bDivide)
        bNumbers.forEach { button ->
            button.setOnClickListener { viewModel.onNumberClick(button.id) }
        }

        bDelete.setOnClickListener { viewModel.doBackspace() }
        bDelete.setOnLongClickListener {
            viewModel.doDeleteValue()
            true
        }
        bClear.setOnClickListener { viewModel.doClear() }
        bPercent.setOnClickListener { viewModel.onPercentClick() }

        bOperations.forEach { button ->
            button.setOnClickListener { viewModel.onOperationsClick(button.id) }
        }

        bResult.setOnClickListener { viewModel.onEquallyClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        menu.findItem(R.id.i_theme_dark).isChecked = isDark
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.i_theme_dark -> {
                item.isChecked = !item.isChecked
                isDark = item.isChecked
                if (isDark)
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                else
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
            }
            R.id.i_about -> {
                AboutDialog(this).show()

            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


    companion object {
        private const val PREFS_KEY_THEME = "theme"
        private const val THEME_LIGHT = 0
        private const val THEME_DARK = 1
    }

}