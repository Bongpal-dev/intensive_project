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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.android.intensiveproject.MainViewModel
import com.android.intensiveproject.TAG
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.data.Contents
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
        setupObserver()
        initViews()
    }

    private fun setupObserver() {
        with(viewModel) {
            toastMsg.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            searchResult.observe(viewLifecycleOwner) {
                imageSearchAdapter.submitList(it.toList())
            }
        }

        with(mainViewModel) {
            myStorages.observe(viewLifecycleOwner) {
                imageSearchAdapter.storageItem = it
                imageSearchAdapter.notifyDataSetChanged()
            }
            toolBarState.observe(viewLifecycleOwner) { visible ->
                if (visible) {
                    binding.layoutSearchBar.moveWithAnimation(0f)
                } else {
                    binding.layoutSearchBar.moveWithAnimation(-60f)
                }
            }
        }
    }


    private fun initViews() {
        initSearchBar()
        initRecyclerView()
        initFavoriteButton()
    }

    private fun initSearchBar() {
        pressedEnterKey()
        clickSearchButton()
        clickTextClearButton()
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
                    changeToolbarVisibleWithState(newState)
            }
        }
    }

    private fun changeToolbarVisibleWithState(state: Int) {
        lifecycleScope.launch {
            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                delay(3000)
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
            addItemDecoration(ItemDeco(requireContext()))
            addOnScrollListener(getScrollListener())
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
        binding.etSearchBar.requestFocusIsEmpty()
        checkBackStack(parentFragmentManager)
    }

    private fun EditText.requestFocusIsEmpty() {
        val keyword = mainViewModel.getSearchKeyword().trim()

        if (keyword.isNotEmpty()) {
            setText(keyword)
        }

        if (text.isEmpty()) {
            requestFocus()
            showKeyboard(this@requestFocusIsEmpty)
        }
    }
}

