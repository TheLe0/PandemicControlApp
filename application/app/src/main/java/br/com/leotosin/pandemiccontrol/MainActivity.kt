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

        val settings :FloatingActionButton = findViewById(R.id.settingsButton)
        settings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    fun increaseOne(view: View)
    {
        val counter : TextView = findViewById(R.id.countingField)
        val counting = (counter.text.toString().toInt() + 1).toString()
        counter.text = counting
        this.updateCounting(counting)
    }

    fun decreaseOne(view: View)
    {
        val counter : TextView = findViewById(R.id.countingField)
        val counting = (counter.text.toString().toInt() - 1).toString()
        counter.text = counting
        this.updateCounting(counting)
    }

    fun reset(view: View)
    {
        val counter : TextView = findViewById(R.id.countingField)
        val counting = getString(R.string.startCounting)
        counter.text = counting
        this.updateCounting(counting)
    }

    private fun updateCounting(counting :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor :SharedPreferences.Editor = preferences.edit()
        editor.putString("counting", counting)
        editor.commit()
    }

    private fun getStoredCounting() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                "counting",
                getString(R.string.startCounting)
        )
    }

}