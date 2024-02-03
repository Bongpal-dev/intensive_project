package com.android.intensiveproject.searchfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.transition.TransitionInflater
import com.android.intensiveproject.MainViewModel
import com.android.intensiveproject.R
import com.android.intensiveproject.TAG
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.model.data.Contents
import com.android.intensiveproject.databinding.FragmentSearchBinding
import com.android.intensiveproject.extention.gone
import com.android.intensiveproject.extention.moveWithAnimation
import com.android.intensiveproject.extention.visible
import com.android.intensiveproject.fragment.checkBackStack
import com.android.intensiveproject.util.ItemDeco
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val imageSearchAdapter by lazy { ImageSearchAdapter(mainViewModel.getAllPrefItems()) }
    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    private val mainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(
            MainViewModel::class.java
        )
    }
    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onStart")

        enterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        initViews()
        setupObserver()
    }

    private fun initViews() {
        initSearchBar()
        initRecyclerView()
        initFavoriteButton()
        initScrollUpButton()
    }

    private fun initScrollUpButton() {
        with(binding) {
            btnScrollUp.setOnClickListener {
                val nowPosition =
                    (rvSearchResult.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                lifecycleScope.launch {

                    rvSearchResult.smoothScrollToPosition(nowPosition - 15)

                    delay(1000L)
                    rvSearchResult.scrollToPosition(0)
                }
            }
            viewModel.scrollBtnState.observe(viewLifecycleOwner) { visible ->
                if (visible) {
                    btnScrollUp.visible()
                    scrollBtnGradient.visible()
                } else {
                    btnScrollUp.gone()
                    scrollBtnGradient.gone()
                }
            }
        }
    }


    private fun initSearchBar() {
        pressedEnterKey()
        clickSearchButton()
        clickTextClearButton()
    }

    private fun setupObserver() {
        with(viewModel) {
            toastMsg.observe(viewLifecycleOwner) { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
            searchResult.observe(viewLifecycleOwner) {
                imageSearchAdapter.submitList(it.toList())
            }
        }
        with(mainViewModel) {
            myStorages.observe(viewLifecycleOwner) {
                it.forEach { Log.i(TAG, "${it}") }
                imageSearchAdapter.storageItem = it
                imageSearchAdapter.notifyDataSetChanged()
            }
            toolBarState.observe(viewLifecycleOwner) { visible ->
                binding.layoutSearchBar.moveWithAnimation(if (visible) 0f else -60f)
            }
        }
    }

    private fun pressedEnterKey() {
        with(binding) {
            etSearchBar.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KEYCODE_ENTER && etSearchBar.text.isEmpty()) {
                    viewModel.textIsEmpty("검색어를 입력해 주세요")
                    return@setOnKeyListener true
                }

                if (keyCode == KEYCODE_ENTER && event.action == ACTION_UP) {
                    viewModel.getSearchResults(etSearchBar.text.toString())
                    mainViewModel.setSearchKeyword(etSearchBar.text.toString())
                    hideKeyboard()
                    rvSearchResult.requestFocus()
                    return@setOnKeyListener false
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun clickSearchButton() {
        with(binding) {
            btnSearch.setOnClickListener {
                if (etSearchBar.text.isEmpty()) {
                    viewModel.textIsEmpty("검색어를 입력해 주세요")
                    return@setOnClickListener
                }
                viewModel.getSearchResults(etSearchBar.text.toString())
                mainViewModel.setSearchKeyword(etSearchBar.text.toString())
                hideKeyboard()
                rvSearchResult.requestFocus()
            }
        }
    }

    private fun clickTextClearButton() {
        with(binding) {
            etSearchBar.doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    ivTextClear.gone()
                    return@doAfterTextChanged
                }
                ivTextClear.visible()
                btnTextClear.setOnClickListener {
                    etSearchBar.text.clear()
                    etSearchBar.requestFocus()
                    showKeyboard(etSearchBar)
                }
            }
        }
    }

    private fun getScrollListener(): OnScrollListener {
        return object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(-1) && imageSearchAdapter.currentList.size == viewModel.searchResult.value?.size) {
                    mainViewModel.showToolBar(true)
                    return
                }
                changeToolbarVisibleWithState(newState)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.getMoreResult(binding.etSearchBar.text.toString())
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(-1)) {
                    viewModel.showScrollUpButton(false)
                    return
                }

                if (dy > 0) {
                    viewModel.showScrollUpButton(true)
                    return
                }
                viewModel.showScrollUpButton(false)
            }
        }
    }

    private fun changeToolbarVisibleWithState(state: Int) {
        lifecycleScope.launch {
            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                delay(1500)
                mainViewModel.showToolBar(true)
                return@launch
            }
            mainViewModel.showToolBar(false)
        }
        return
    }

    private fun initRecyclerView() {
        with(binding.rvSearchResult) {
            adapter = imageSearchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            addOnScrollListener(getScrollListener())
            if (itemDecorationCount < 1) {
                addItemDecoration(ItemDeco(requireContext()))
            }
        }
    }

    private fun initFavoriteButton() {
        object : ImageSearchAdapter.ClickFavoriteListener {
            override fun onClick(item: Contents) {
                mainViewModel.togglePreferenceItem(item)
            }
        }.also { imageSearchAdapter.onClick = it }
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
    }

    private fun showKeyboard(editText: EditText) {
        imm.showSoftInput(editText, 0)
    }

    override fun onResume() {
        super.onResume()

        val prevId =
            findNavController().currentBackStackEntry?.savedStateHandle?.get<Int>("prev_frag")

        if (prevId == null) {
            binding.etSearchBar.requestFocusAndKeyboardShow()
        }

        checkBackStack(parentFragmentManager)
    }

    private fun EditText.requestFocusAndKeyboardShow() {
        val keyword = mainViewModel.getSearchKeyword().trim()

        setText(keyword)
        this.setSelection(this.text.length)
        requestFocus()
        showKeyboard(this@requestFocusAndKeyboardShow)
    }
}

