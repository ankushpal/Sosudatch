package com.example.sosudatech

import android.Manifest
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import com.bumptech.glide.Glide
import com.example.rachtr.DetailViewModel
import kotlinx.android.synthetic.main.dialog_layout.*
import android.view.Gravity

import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*
import kotlin.collections.ArrayList


class DetailsActivity : AppCompatActivity() , DetailsAdapter.OnItemClickListener {

    var detailViewModel: DetailViewModel? = null
    var detailsList = ArrayList<Data>()
    var detailsAdapter:DetailsAdapter? = null
    var ivLogout: ImageView? = null
    var idPBLoading: ProgressBar? = null
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initUI()
        callApi()
    }
    fun initUI()
    {
        idPBLoading = findViewById(R.id.idPBLoading)
        ivLogout = findViewById(R.id.ivLogout)
        ivLogout!!.setOnClickListener {
            logouDialog()
        }
    }

    fun callApi() {
        idPBLoading!!.visibility = View.VISIBLE
        if (detailViewModel == null)
            detailViewModel = ViewModelProviders.of(this, DetailViewModel.ViewModelFactory())
                .get(DetailViewModel::class.java)
        detailViewModel!!.getDetails()?.observe(this, androidx.lifecycle.Observer { baseResponse ->
            if (baseResponse != null) {
                idPBLoading!!.visibility = View.GONE
                detailsList =  baseResponse.data
                if(detailsList.size > 0)
                {
                  showDetails()
                }
            } else {
                Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }
    fun showDetails()
    {
        val rv_Details: RecyclerView = findViewById(R.id.rv_Details)
        val layoutManager = LinearLayoutManager(this)
        rv_Details.setLayoutManager(layoutManager)
        detailsAdapter = DetailsAdapter(this,  detailsList,this)
        rv_Details.setAdapter(detailsAdapter)
        detailsAdapter!!.notifyDataSetChanged()
    }
    fun logouDialog()
    {
        val alert = android.app.AlertDialog.Builder(this@DetailsActivity)
        alert.setMessage("Are you sure Logout?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                logout() // Last step. Logout function
            }).setNegativeButton("No", null)

        val alert1: android.app.AlertDialog? = alert.create()
        alert1!!.show()
    }
    private fun logout() {
        val editor = getSharedPreferences(CommonClass.Common.MY_PREFS_NAME, MODE_PRIVATE).edit()
        editor.putBoolean(CommonClass.Common.isSession, false)
        editor.apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onItemClick(data:Data,color:Int) {
        showDetails(data,color)
    }
    fun showDetails(data:Data,color:Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_layout)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        dialog.window!!.attributes = lp

        val body = dialog.findViewById(R.id.tvUserName) as TextView
        val tvEmail = dialog.findViewById(R.id.tvEmail) as TextView
        val profile_image = dialog.findViewById(R.id.profile_image) as ImageView
        val llRoot = dialog.findViewById(R.id.llRoot) as FrameLayout
        llRoot.setBackgroundColor(color)
        body.text = data.first_name+" "+data.last_name
        tvEmail.text = "EmailId: "+data.email
        Glide.with(this).load(data.avatar).into(profile_image)
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE!!
                )
            }
        }
    }
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(300000).setFastestInterval(300000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        getCompletAddress(location.latitude,location.longitude)
                  //     Toast.makeText(this@DetailsActivity,""+location.latitude.toString(),Toast.LENGTH_LONG).show()

                    }
                }
            },
            Looper.myLooper()
        )
    }

    private fun getCompletAddress(latitude:Double,longitude:Double) {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses[0].getLocality()
        tvAddress.text = city

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}