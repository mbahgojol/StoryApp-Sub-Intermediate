package com.mbahgojol.storyapp.ui.addStory

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.data.model.GlobalResponse
import com.mbahgojol.storyapp.databinding.ActivityAddStoryBinding
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.text
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var pathImg = ""
    private val viewModel by viewModels<AddStoryViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            pathImg = result.getUriFilePath(this).toString()
            binding.image.load(uriContent)
        } else {
            val exception = result.error
            Toast.makeText(this, exception?.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.apply {
            btnCamera.setOnClickListener {
                cropImage.launch(
                    options {
                        setImageSource(
                            includeGallery = false, includeCamera = true
                        )
                    }
                )
            }

            btnGallery.setOnClickListener {
                cropImage.launch(
                    options {
                        setImageSource(
                            includeGallery = true, includeCamera = false
                        )
                    }
                )
            }

            btnUpload.setOnClickListener {
                val des = deskirpsi.text()
                val tsLong = System.currentTimeMillis() / 1000
                val ts = "img_".plus(tsLong)

                val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("description", des)
                    .addFormDataPart(
                        "photo",
                        ts,
                        File(pathImg).asRequestBody("image/*".toMediaTypeOrNull())
                    )

                location?.let { loc ->
                    body.addFormDataPart("lat", loc.latitude.toString())
                    body.addFormDataPart("lon", loc.longitude.toString())
                }

                viewModel.upload(body.build())
            }

            tandaiLokasi.setOnCheckedChangeListener { _, check ->
                if (check) {
                    getMyLastLocation()
                }
            }
        }

        viewModel.resultStateStory.observe(this) {
            when (it) {
                is ResultState.Error -> {
                    val snackbar = Snackbar.make(
                        binding.root,
                        it.e.message.toString(),
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.show()
                }
                is ResultState.Success<*> -> {
                    val response = it.data as GlobalResponse
                    if (!response.error) Toast.makeText(
                        this,
                        getString(R.string.sukses_upload),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }
                is ResultState.Loading -> {
                    binding.progressBar.isVisible = it.loading
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {

                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}