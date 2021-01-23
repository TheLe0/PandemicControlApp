package br.com.leotosin.pandemiccontrol

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import br.com.leotosin.pandemiccontrol.setting.Configuration
import com.google.android.material.switchmaterial.SwitchMaterial

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

        val type :Int = this.getTypeCounting()?.toInt()!!
        val radioPercent :RadioButton = findViewById(R.id.percentType)
        val radioUnit :RadioButton = findViewById(R.id.unitType)

        if (type == R.id.percentType)
        {
            radioPercent.isChecked = true
            radioUnit.isChecked = false
        }
        else
        {
            radioPercent.isChecked = false
            radioUnit.isChecked = true
        }

        val block :TextView = findViewById(R.id.blockNumber)
        block.text = this.getNumBlock()

        val incSwitch :SwitchMaterial = findViewById(R.id.increaseSwitch)
        val decSwitch :SwitchMaterial = findViewById(R.id.decreaseSwitch)

        incSwitch.isChecked = this.getIncreaseSwitch().toBoolean()
        decSwitch.isChecked = this.getDecreaseSwitch().toBoolean()
    }

    fun save(view : View)
    {
        val limit :TextView = findViewById(R.id.limitCounting)
        val limitCounting = limit.text.toString()
        this.updateLimit(limitCounting)

        val counter : TextView = findViewById(R.id.currentCounting)
        val counting = counter.text.toString()

        if (!this.outOfBounds(counting) and !this.isNegative(counting))
        {
            this.updateCounting(counting)
        }

        val radioPercent :RadioButton = findViewById(R.id.percentType)

        val type :Int = if (radioPercent.isChecked) R.id.percentType else R.id.unitType

        this.updateTypeCounting(type.toString())

        val numBlock :TextView = findViewById(R.id.blockNumber)
        val block = numBlock.text.toString()

        this.updateNumBlock(block, type.toString())

        val incSwitch :SwitchMaterial = findViewById(R.id.increaseSwitch)
        val decSwitch :SwitchMaterial = findViewById(R.id.decreaseSwitch)

        this.updateIncreaseSwitch(incSwitch.isChecked.toString())
        this.updateDecreaseSwitch(decSwitch.isChecked.toString())
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

    private fun getNumBlock() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                Configuration.NUM_BLOCK,
                this.getLimit()
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
        editor.apply()
    }

    private fun updateLimit(limit :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.LIMIT_COUNTING, limit)
        editor.apply()
    }

    private fun updateTypeCounting(type :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
            Configuration.CONFIG_FILE,
            0
        )
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.TYPE_LIMIT, type)
        editor.apply()
    }

    private fun updateIncreaseSwitch(switch :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.INC_ON, switch)
        editor.apply()
    }

    private fun updateDecreaseSwitch(switch :String)
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putString(Configuration.DEC_ON, switch)
        editor.apply()
    }

    private fun verifyNumBlock(numBlock :String, type :String) :Boolean
    {
        when (type.toInt())
        {
            R.id.unitType ->
            {
                if (numBlock.toInt() !in 0..this.getLimit()?.toInt()!!)
                {
                    return false
                }
            }
            R.id.percentType ->
            {
                if (numBlock.toInt() !in 0..100)
                {
                    return false
                }
            }
        }

        return true
    }

    private fun updateNumBlock(numBlock :String, type :String)
    {

        if (this.verifyNumBlock(numBlock, type))
        {
            val preferences : SharedPreferences = getSharedPreferences(
                    Configuration.CONFIG_FILE,
                    0
            )
            val editor : SharedPreferences.Editor = preferences.edit()
            editor.putString(Configuration.NUM_BLOCK, numBlock)
            editor.apply()
        }
        else
        {
            this.adaptNumBlock(numBlock, type)
        }
    }

    private fun adaptNumBlock(numBlock :String, type :String)
    {
        val limit = this.getLimit()
        var blockValue = numBlock

        when (type.toInt())
        {
            R.id.unitType ->
            {
                if (limit != null)
                {
                    blockValue = ((blockValue.toInt()/100)*limit.toInt()).toString()
                }
            }
            R.id.percentType ->
            {
                if (limit != null)
                {
                    blockValue = ((blockValue.toInt()*100)/limit.toInt()).toString()
                }
            }
        }

        this.updateNumBlock(blockValue, type)
    }

    private fun getTypeCounting() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
            Configuration.CONFIG_FILE,
            0
        )

        return preferences.getString(
            Configuration.TYPE_LIMIT,
            R.id.unitType.toString()
        )
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

    private fun getIncreaseSwitch() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                Configuration.INC_ON,
                true.toString()
        )
    }

    private fun getDecreaseSwitch() :String?
    {
        val preferences : SharedPreferences = getSharedPreferences(
                Configuration.CONFIG_FILE,
                0
        )
        return preferences.getString(
                Configuration.DEC_ON,
                true.toString()
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
        if (counting.toInt() <= 0)
        {
            return true
        }

        return false
    }
}