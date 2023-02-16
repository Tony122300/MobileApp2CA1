package ie.wit.caa

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caa.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import com.google.android.material.snackbar.Snackbar
import ie.wit.caa.main.caaApp
import ie.wit.caa.models.CaaModel

//import android.widget.Toast
//import com.aitangba.wheel.WheelView


class MainActivity : AppCompatActivity() {
    private val choices = arrayOf(
        "Drug",
        "Cyber",
        "Property",
        "Violent",
        "Traffic",
        "Workplace",
        "Sports",
        "Home",
        "Natural"
    )
    private lateinit var caaLayout: ActivityMainBinding
    lateinit var app: caaApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = this.application as caaApp
        caaLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(caaLayout.root)


        caaLayout.amountPicker.minValue = 1
        caaLayout.amountPicker.maxValue = 10

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, choices)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Do something when an item is selected
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do something when nothing is selected
            }
        }
        caaLayout.addCrime.setOnClickListener{
            val name = caaLayout.FullName.text.toString()
            val description = caaLayout.Description.text.toString()

            val crimeType = caaLayout.spinner.selectedItem.toString()
            val crimeLevel = caaLayout.amountPicker.value
            if (name.isNotEmpty() && description.isNotEmpty()) {
                val crime = CaaModel(name = name, description = description, type = crimeType, level = crimeLevel)
              app.crimeStore.create(crime)
                Snackbar.make(caaLayout.root, "Crime added successfully", Snackbar.LENGTH_LONG).show()
                caaLayout.FullName.setText("")
                caaLayout.Description.setText("")
            } else {
                Toast.makeText(applicationContext, "Please enter both name and description of the crime", Toast.LENGTH_LONG).show()
            }
        }

    }


            override fun onCreateOptionsMenu(menu: Menu): Boolean {
                // Inflate the menu; this adds items to the action bar if it is present.
                menuInflater.inflate(R.menu.menu_caa, menu)
                return true
            }
        }
