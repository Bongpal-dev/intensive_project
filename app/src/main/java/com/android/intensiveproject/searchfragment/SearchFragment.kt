package com.android.intensiveproject.searchfragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.intensiveproject.TAG
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.databinding.FragmentSearchBinding
import com.android.intensiveproject.extention.gone
import com.android.intensiveproject.extention.visible
import com.android.intensiveproject.fragment.checkBackStack
import com.android.intensiveproject.retrofit.ImageItemDetail
import com.android.intensiveproject.retrofit.SearchClient
import com.android.intensiveproject.util.PrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

const val MY_FAVORITE = "MyFavorite"
const val SEARCH_KEYWORD = "SearchKeyword"

class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val imageSearchAdapter by lazy { ImageSearchAdapter(requireContext()) }
    private var searchResults = mutableListOf<ImageItemDetail>()
    private val prefUtil by lazy { PrefUtil(requireContext()) }
    private val viewModel by lazy { ViewModelProvider(this).get(SearchFragmentViewModel::class.java) }

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
        initRecyclerView()
        initSearchBar()
        initFavoriteButton()
    }

    private fun initSearchBar() {
        initClearIcon()
        searchPressWithEnter()
        searchPressWithButton()
    }

    override fun onResume() {
        super.onResume()
        initKeyboardFocus()
        checkBackStack(parentFragmentManager)
    }

    private fun initKeyboardFocus() {
        with(binding.etSearchBar) {
            if (text.isEmpty()) {
                showKeyboard(this)
                requestFocus()
                return
            }
            binding.rvSearchResult.requestFocus()
        }
    }

    private fun searchPressWithEnter() {
        with(binding) {
            etSearchBar.setOnKeyListener { _, keyCode, event ->
                val searchKeyword = etSearchBar.text.toString()

                if (keyCode == KeyEvent.KEYCODE_ENTER && etSearchBar.text.isEmpty()) {
                    Toast.makeText(context, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
                    return@setOnKeyListener true
                }

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    searchKeyword.getItem()
                    hideKeyboard()
                    rvSearchResult.requestFocus()
                    Log.i(TAG, "${etSearchBar.text}")
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun searchPressWithButton() {
        with(binding) {
            btnSearch.setOnClickListener {
                val searchKeyword = etSearchBar.text.toString()

                if (etSearchBar.text.isEmpty()) {
                    Toast.makeText(context, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                searchKeyword.getItem()
                hideKeyboard()
                rvSearchResult.requestFocus()
            }
        }
    }


    private fun initClearIcon() {
        with(binding) {
            etSearchBar.doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    ivTextClear.gone()
                }
                if (!it.isNullOrEmpty()) {
                    ivTextClear.visible()
                    btnTextClear.setOnClickListener {
                        etSearchBar.text.clear()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvSearchResult) {
            adapter = imageSearchAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemDeco())

        }
        imageSearchAdapter.submitList(searchResults)
    }

    inner class ItemDeco : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)

            if (position == 0) {
                outRect.top = 60.dpToPx(requireContext())
            }
        }
    }

    private fun String.getItem() {
        val API_KEY = "aa9dcfa994967af44de93c594a380bab"

        lifecycleScope.launch {
            val resultImages = mutableListOf<ImageItemDetail>()

            makeRequest(this@getItem).let {
                SearchClient.retrofitClient.getImageItems("KakaoAK ${API_KEY}", it)
            }.documents?.forEach {
                resultImages += it
            }
            searchResults = resultImages
            withContext(Dispatchers.Main) {
                Log.i(TAG, "$searchResults")
                imageSearchAdapter.submitList(searchResults)
                binding.rvSearchResult.adapter = imageSearchAdapter
            }
        }
    }

    private fun initFavoriteButton() {
        object : ImageSearchAdapter.ClickFavoriteListener {
            override fun onClick(item: ImageItemDetail) {
                prefUtil.togglePref(item)
                imageSearchAdapter.notifyDataSetChanged()
            }
        }.also { imageSearchAdapter.onClick = it }
    }

    fun Int.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private fun makeRequest(query: String): HashMap<String, Any> {
        var page = Random.nextInt(1, 51)
        return hashMapOf(
            "query" to query,
            "page" to page,
            "size" to 80,
        )
    }

    private fun showKeyboard(editText: EditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if (hasFocus) {
                imm.showSoftInput(editText, 0)
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
    }
}

