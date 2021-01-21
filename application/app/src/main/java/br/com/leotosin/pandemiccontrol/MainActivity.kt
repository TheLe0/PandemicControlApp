package br.com.leotosin.pandemiccontrol

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import br.com.leotosin.pandemiccontrol.setting.Configuration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val counter : TextView = findViewById(R.id.countingField)
        val counting = this.getStoredCounting()
        counter.text = counting

        val increaseButton :FloatingActionButton = findViewById(R.id.addOneButton)
        if (this.outOfBounds(counting!!))
        {
            increaseButton.isEnabled = false
            increaseButton.isClickable = false
        }
        else
        {
            increaseButton.isEnabled = true
            increaseButton.isClickable = true
        }

        val decreaseButton :FloatingActionButton = findViewById(R.id.delOneButton)
        if (this.isNegative(counting!!))
        {
            decreaseButton.isEnabled = false
            decreaseButton.isClickable = false
        }
        else
        {
            decreaseButton.isEnabled = true
            decreaseButton.isClickable = true
        }

        val settings :FloatingActionButton = findViewById(R.id.settingsButton)
        settings.setOnClickListener{
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRestart()
    {
        super.onRestart()

        val counter : TextView = findViewById(R.id.countingField)
        val counting = this.getStoredCounting()
        counter.text = counting

        val increaseButton :FloatingActionButton = findViewById(R.id.addOneButton)
        if (this.outOfBounds(counting!!))
        {
            increaseButton.isEnabled = false
            increaseButton.isClickable = false
        }
        else
        {
            increaseButton.isEnabled = true
            increaseButton.isClickable = true
        }

        val decreaseButton :FloatingActionButton = findViewById(R.id.delOneButton)
        if (this.isNegative(counting!!))
        {
            decreaseButton.isEnabled = false
            decreaseButton.isClickable = false
        }
        else
        {
            decreaseButton.isEnabled = true
            decreaseButton.isClickable = true
        }
    }

    fun increaseOne(view: View)
    {
        val counter : TextView = findViewById(R.id.countingField)
        val counting = (counter.text.toString().toInt() + 1).toString()
        counter.text = counting
        this.updateCounting(counting)

        val increaseButton :FloatingActionButton = findViewById(R.id.addOneButton)
        if (this.outOfBounds(counting!!))
        {
            increaseButton.isEnabled = false
            increaseButton.isClickable = false
        }

        val decreaseButton :FloatingActionButton = findViewById(R.id.delOneButton)
        if (this.isNegative(counting!!))
        {
            decreaseButton.isEnabled = false
            decreaseButton.isClickable = false
        }
        else
        {
            decreaseButton.isEnabled = true
            decreaseButton.isClickable = true
        }
    }

    fun decreaseOne(view: View)
    {
        val counter : TextView = findViewById(R.id.countingField)
        val counting = (counter.text.toString().toInt() - 1).toString()
        counter.text = counting
        this.updateCounting(counting)

        val increaseButton :FloatingActionButton = findViewById(R.id.addOneButton)
        if (!this.outOfBounds(counting!!))
        {
            increaseButton.isEnabled = true
            increaseButton.isClickable = true
        }

        val decreaseButton :FloatingActionButton = findViewById(R.id.delOneButton)
        if (this.isNegative(counting!!))
        {
            decreaseButton.isEnabled = false
            decreaseButton.isClickable = false
        }
        else
        {
            decreaseButton.isEnabled = true
            decreaseButton.isClickable = true
        }
    }

    private fun updateCounting(counting :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor :SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.STORED_COUNTING, counting)
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