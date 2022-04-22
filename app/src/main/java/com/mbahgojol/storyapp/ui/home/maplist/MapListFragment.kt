package com.mbahgojol.storyapp.ui.home.maplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.databinding.FragmentMapListBinding
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.isNotNullOrZero
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapListFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentMapListBinding? = null
    private var googleMap: GoogleMap? = null
    private val viewModel by viewModels<MapListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getListStory()

        viewModel.storyListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Error -> {
                    val snackbar = binding?.root?.let {
                        Snackbar.make(
                            it,
                            state.e.message.toString(),
                            Snackbar.LENGTH_INDEFINITE
                        )
                    }
                    snackbar?.setAction("Retry") {
                        viewModel.getListStory()
                    }
                    snackbar?.show()
                }
                is ResultState.Success<*> -> {
                    val response = state.data as GetAllStoryResponse
                    response.listStory.forEachIndexed { index, story ->
                        if (story.lat.isNotNullOrZero() && story.lon.isNotNullOrZero()) {
                            val startLocation = LatLng(story.lat, story.lon)
                            googleMap?.addMarker(
                                MarkerOptions()
                                    .position(startLocation)
                                    .title(story.name)
                            )
                        }
                    }

                    googleMap?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(-0.7893, 113.9213),
                            5f
                        )
                    )
                }
                is ResultState.Loading -> {
                    binding?.progressBar?.isVisible = state.loading
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }
}