package mikkoromo.cgipre_assignment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_add_observation.*
import kotlinx.android.synthetic.main.rv_item.*


class AddObservationActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var notes: String
    lateinit var rarity: String
    lateinit var timestamp: String
    private var latitude = ""
    private var longitude = ""
    private var imagePath = ""
    private lateinit var observationViewModel: ObservationViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequestCode = 123
    private val imageRequestCode = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observation)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.add_observation)

        observationViewModel = ViewModelProvider(this).get(ObservationViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        button_saveObservation.setOnClickListener {

            if (formIsValid()) {
                name = editText_name.text.toString()
                notes = editText_notes.text.toString()
                timestamp = Utils().getCurrentDateTime()

                // Check the selected rarity
                if (radioGroup_rarity.checkedRadioButtonId != -1) {
                    when {
                        radioButton_common.isChecked -> rarity = "1"
                        radioButton_rare.isChecked -> rarity = "2"
                        radioButton_extremelyRare.isChecked -> rarity = "3"
                    }
                }

                getLocation()
            }
        }

        button_takePhoto.setOnClickListener {
            getImage()
        }

        checkLocationPermission()
    }

    private fun formIsValid(): Boolean {
        return if (editText_name.text.isEmpty() || editText_notes.text.isEmpty()) {
            Toast.makeText(this, "You must fill all fields before saving", Toast.LENGTH_SHORT)
                .show()
            false
        } else if (radioGroup_rarity.checkedRadioButtonId == -1) {
            Toast.makeText(this, "You must choose a rarity", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun getLocation() {

        if (checkLocationPermission()) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                        observationViewModel.insert(
                            Record(
                                0,
                                name,
                                notes,
                                rarity,
                                timestamp,
                                latitude,
                                longitude,
                                imagePath
                            )
                        )
                        finish()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "You must allow location services to save an observation",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getImage() {
        val intent = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_OPEN_DOCUMENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), imageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imageRequestCode && resultCode == RESULT_OK) {
            imagePath = data?.data.toString()
            imageView_preview.setImageURI(imagePath.toUri())
            imageView_preview.visibility = View.VISIBLE
            Log.d("dbg", imagePath)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode) {
            if (grantResults[0] == -1) {
                finish()
                Toast.makeText(
                    this,
                    "You must allow location services to save an observation",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request for permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationRequestCode
            )
            return (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        } else {
            return true
        }
    }


}