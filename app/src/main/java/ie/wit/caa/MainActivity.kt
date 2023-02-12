package ie.wit.caa

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caa.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
//import android.widget.Toast
//import com.aitangba.wheel.WheelView


class MainActivity : AppCompatActivity() {
    private val choices = arrayOf("Drug", "Cyber", "Property", "Violent", "Traffic", "Workplace", "Sports", "Home", "Natural")
    private lateinit var caaLayout : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        caaLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(caaLayout.root)

        caaLayout.progressBar.max = 10000

        caaLayout.amountPicker.minValue = 1
        caaLayout.amountPicker.maxValue = 1000

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, choices)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do something when an item is selected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do something when nothing is selected
            }
        }
    }

}