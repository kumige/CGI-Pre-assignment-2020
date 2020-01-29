package mikkoromo.cgipre_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var model: ObservationViewModel
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.my_observations)

        adapter = RecyclerViewAdapter(this)
        rv_main.adapter = adapter
        rv_main.layoutManager = LinearLayoutManager(this)

        model = ViewModelProvider(this).get(ObservationViewModel::class.java)
        model.allRecords.observe(this, Observer { records ->
            records?.let { adapter.setData(it) }
        })

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddObservationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_observation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // Sort observations by date
        R.id.item_date -> {
            model.allRecords.removeObservers(this)
            model.allRecords.observe(this, Observer { records ->
                records?.let { adapter.setData(it) }
            })

            true
        }
        // Sort observations by rarity
        R.id.item_rarity -> {
            model.allRecords.removeObservers(this)
            model.allRecordsByRarity.observe(this, Observer { records ->
                records?.let { adapter.setData(it) }
            })

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
