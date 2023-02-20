//package ie.wit.caa.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import ie.wit.caa.R
//import ie.wit.caa.activities.ReportCrimeActivity
//import ie.wit.caa.adapter.CrimeAdapter
//import ie.wit.caa.main.caaApp
//import ie.wit.caa.databinding.ActivityListBinding
//
//class List : AppCompatActivity() {
//        lateinit var app: caaApp
//        private lateinit var listLayout : ActivityListBinding
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            listLayout = ActivityListBinding.inflate(layoutInflater)
//            setContentView(listLayout.root)
//
//            app = this.application as caaApp
//            listLayout.recyclerView.layoutManager = LinearLayoutManager(this)
//            listLayout.recyclerView.adapter = CrimeAdapter(app.crimeStore.findAll())
//        }
//
//        override fun onCreateOptionsMenu(menu: Menu): Boolean {
//            menuInflater.inflate(R.menu.menu_list, menu)
//            return true
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            return when (item.itemId) {
//                R.id.action_Crime -> { startActivity(
//                    Intent(this,
//                        ReportCrimeActivity::class.java))
//                    true
//                }
//                else -> super.onOptionsItemSelected(item)
//            }
//        }
//    }
