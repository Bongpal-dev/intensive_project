package com.android.intensiveproject.searchfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.intensiveproject.MainActivityViewModel
import com.android.intensiveproject.TAG
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.databinding.FragmentSearchBinding
import com.android.intensiveproject.extention.gone
import com.android.intensiveproject.extention.visible
import com.android.intensiveproject.fragment.checkBackStack
import com.android.intensiveproject.model.retrofit.ImageItemDetail
import com.android.intensiveproject.util.ItemDeco
import com.android.intensiveproject.model.PreferenceRepository

const val MY_FAVORITE = "MyFavorite"
const val SEARCH_KEYWORD = "SearchKeyword"

class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val imageSearchAdapter by lazy { ImageSearchAdapter(requireContext()) }
    private val preferenceRepository by lazy { PreferenceRepository(requireContext()) }
    private val viewModel by lazy { ViewModelProvider(this).get(SearchFragmentViewModel::class.java) }
    private val mainActivityViewModel by lazy { ViewModelProvider(this).get(MainActivityViewModel::class.java) }
    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        hideSearchBarWithScorll()
    }

    private fun pressedEnterKey() {
        with(binding) {
            etSearchBar.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && etSearchBar.text.isEmpty()) {
                    viewModel.textIsEmpty("검색어를 입력해 주세요")
                    return@setOnKeyListener false
                }

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    viewModel.getDataFromAPI(etSearchBar.text.toString())
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
                viewModel.getDataFromAPI(etSearchBar.text.toString())
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
                }
            }
        }
    }

    private fun hideSearchBarWithScorll() {
        with(binding.rvSearchResult) {
            setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvSearchResult) {
            adapter = imageSearchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemDeco(requireContext()))
            itemAnimator = null
        }
    }

    private fun initFavoriteButton() {
        object : ImageSearchAdapter.ClickFavoriteListener {
            override fun onClick(item: ImageItemDetail) {
                mainActivityViewModel.togglePreferenceItem(preferenceRepository, item)
                imageSearchAdapter.notifyDataSetChanged()
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
        with(binding) {
            if (text.isEmpty()) {
                requestFocus()
                showKeyboard(this@requestFocusIsEmpty)
                return
            }
            rvSearchResult.requestFocus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "destroy")
    }
}

