package com.mbahgojol.storyapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<GetAllStoryResponse.Story>("data")
        data?.let { model ->
            binding.apply {
                title.text = model.name
                deskirpsi.text = model.description
                image.load(model.photoUrl)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}