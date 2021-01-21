package br.com.leotosin.pandemiccontrol

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import br.com.leotosin.pandemiccontrol.setting.Configuration

class ConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        val counter : TextView = findViewById(R.id.currentCounting)
        val counting = this.getStoredCounting()
        counter.text = counting

        val limit :TextView = findViewById(R.id.limitCounting)
        val limitCounting = this.getLimit()
        limit.text = limitCounting
    }

    fun save(view : View)
    {
        val limit :TextView = findViewById(R.id.limitCounting)
        val limitCounting = limit.text.toString()
        this.updateLimit(limitCounting)

        val counter : TextView = findViewById(R.id.currentCounting)
        val counting = counter.text.toString()

        if (!this.outOfBounds(counting!!) and !this.isNegative(counting!!))
        {
            this.updateCounting(counting)
        }
    }

    fun reset(view: View)
    {
        val counter : TextView = findViewById(R.id.currentCounting)
        val counting = getString(R.string.startCounting)
        counter.text = counting
        this.updateCounting(counting)
    }

    private fun getLimit() : String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                Configuration.LIMIT_COUNTING,
                getString(R.string.startCounting)
        )
    }

    private fun updateCounting(counting :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.STORED_COUNTING, counting)
        editor.commit()
    }

    private fun updateLimit(limit :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.LIMIT_COUNTING, limit)
        editor.commit()
    }

    private fun getStoredCounting() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                Configuration.STORED_COUNTING,
                getString(R.string.startCounting)
        )
    }

    private fun getLimitCounting() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                Configuration.LIMIT_COUNTING,
                getString(R.string.startCounting)
        )
    }

    private fun outOfBounds(counting: String) :Boolean
    {
        if (counting.toInt() >= this.getLimitCounting()?.toInt()!!)
        {
            return true
        }

        return false
    }

    private fun isNegative(counting: String) :Boolean
    {
        if (counting.toInt() < 0)
        {
            return true
        }

        return false
    }
}