package com.mbahgojol.storyapp.ui.home.storylist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.data.local.SharedPref
import com.mbahgojol.storyapp.databinding.FragmentStoryListBinding
import com.mbahgojol.storyapp.ui.addStory.AddStoryActivity
import com.mbahgojol.storyapp.ui.detail.DetailActivity
import com.mbahgojol.storyapp.utils.VerticalSpaceItemDecoration
import com.mbahgojol.storyapp.utils.dp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoryListFragment : Fragment() {

    private var binding: FragmentStoryListBinding? = null
    private val viewModel by activityViewModels<StoryListViewModel>()
    private val adapter by lazy {
        StoryListAdapter { model, img, title, des ->
            Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra("data", model)

                val p1 = Pair.create<View, String>(img, getString(R.string.image))
                val p2 = Pair.create<View, String>(title, getString(R.string.title))
                val p3 = Pair.create<View, String>(des, getString(R.string.deskirpsi))

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    p1, p2, p3
                )

                startActivity(this, options.toBundle())
            }
        }
    }

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.addLoadStateListener(this::manageStatePaging)
        binding?.apply {
            rvMain.layoutManager = LinearLayoutManager(requireActivity())
            rvMain.setHasFixedSize(true)
            rvMain.adapter = adapter.withLoadStateFooter(LoadingStateAdapter {
                adapter.retry()
            })
            rvMain.addItemDecoration(VerticalSpaceItemDecoration(16.dp))

            addStory.setOnClickListener {
                Intent(requireActivity(), AddStoryActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        viewModel.resultStory.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListStory()
    }

    private fun manageStatePaging(loadState: CombinedLoadStates) {
        val isLoading = loadState.source.refresh is LoadState.Loading
        binding?.progressBar?.isVisible = isLoading

        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: loadState.source.refresh as? LoadState.Error

        if (errorState != null && (adapter.itemCount == 0)) {
            binding?.animationView?.isVisible = true
            val msgError = errorState.error.message.toString()
            val snackbar = binding?.root?.let {
                Snackbar.make(
                    it,
                    msgError,
                    Snackbar.LENGTH_INDEFINITE
                )
            }
            snackbar?.setAction("Retry") {
                adapter.retry()
            }
            snackbar?.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}