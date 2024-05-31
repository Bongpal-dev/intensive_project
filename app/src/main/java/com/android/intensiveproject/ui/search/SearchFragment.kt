package com.android.intensiveproject.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.ui.common.MainViewModel
import com.android.intensiveproject.databinding.FragmentSearchBinding
import com.android.intensiveproject.ui.common.BaseFragment
import com.android.intensiveproject.ui.common.ImageAdapter
import com.android.intensiveproject.util.extention.gone
import com.android.intensiveproject.util.extention.visible
import com.android.intensiveproject.util.ItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding>(inflate = FragmentSearchBinding::inflate) {
    private val imageAdapter by lazy { ImageAdapter() }
    private val viewModel: SearchViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
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
                imageAdapter.submitList(it.toList())
            }
        }
        with(mainViewModel) {
            favorites.observe(viewLifecycleOwner) { favorites ->
                val favoriteUrls = favorites.map { it.url }
                val newList = imageAdapter.currentList.map { item ->
                    if (item.url in favoriteUrls) item.like() else item.dislike()
                }

                imageAdapter.submitList(newList.toList())
            }
//            toolBarState.observe(viewLifecycleOwner) { visible ->
//                binding.layoutSearchBar.moveWithAnimation(if (visible) 0f else -60f)
//            }
        }
    }

    private fun pressedEnterKey() {
        with(binding) {
            etSearchBar.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KEYCODE_ENTER && etSearchBar.text.isEmpty()) {
                    viewModel.showToast("검색어를 입력해 주세요")
                    return@setOnKeyListener true
                }

                if (keyCode == KEYCODE_ENTER && event.action == ACTION_UP) {
                    searchImageByKeyword(etSearchBar.text.toString(), rvSearchResult)
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
                    viewModel.showToast("검색어를 입력해 주세요")
                    return@setOnClickListener
                }
                searchImageByKeyword(etSearchBar.text.toString(), rvSearchResult)
            }
        }
    }

    private fun searchImageByKeyword(query: String, view: View) {
        viewModel.searchImage(query)
        hideKeyboard()
        view.requestFocus()
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

//    private fun getScrollListener(): OnScrollListener {
//        return object : OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (!recyclerView.canScrollVertically(-1) && imageAdapter.currentList.size == viewModel.searchResult.value?.size) {
//                    mainViewModel.showToolBar(true)
//                    return
//                }
//                changeToolbarVisibleWithState(newState)
//
//                if (!recyclerView.canScrollVertically(1)) {
////                    viewModel.getMoreResult(binding.etSearchBar.text.toString())
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                if (!recyclerView.canScrollVertically(-1)) {
//                    viewModel.showScrollUpButton(false)
//                    return
//                }
//
//                if (dy > 0) {
//                    viewModel.showScrollUpButton(true)
//                    return
//                }
//                viewModel.showScrollUpButton(false)
//            }
//        }
//    }

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
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
//            addOnScrollListener(getScrollListener())
            if (itemDecorationCount < 1) {
                addItemDecoration(ItemDeco(requireContext()))
            }
        }
    }

    private fun initFavoriteButton() {
        object : ImageAdapter.FavoriteClickListener {
            override fun onClick(item: ImageModel) {
                mainViewModel.toggleFavoriteImage(item)
            }
        }.also { imageAdapter.favoriteClickListener = it }
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
    }

    private fun EditText.requestFocusAndKeyboardShow() {
//        val keyword = mainViewModel.getSearchKeyword().trim()

//        setText(keyword)
        this.setSelection(this.text.length)
        requestFocus()
        showKeyboard(this@requestFocusAndKeyboardShow)
    }
}

