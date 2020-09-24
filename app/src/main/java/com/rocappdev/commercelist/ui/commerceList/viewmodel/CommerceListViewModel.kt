package com.rocappdev.commercelist.ui.commerceList.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocappdev.commercelist.domain.Categories
import com.rocappdev.commercelist.domain.Commerce
import com.rocappdev.commercelist.repository.CommerceList
import com.rocappdev.commercelist.repository.CommerceRepository
import com.rocappdev.commercelist.repository.ErrorResponse
import com.rocappdev.commercelist.util.SortCommercesByDistance
import com.rocappdev.commercelist.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CommerceListViewModel : ViewModel() {
    private val commerceRepository = CommerceRepository()

    private var allCommerces = listOf<Commerce>()

    private val _commerceList = MutableLiveData<CommerceList>()
    val commerceList: LiveData<CommerceList> get() = _commerceList

    private val _error = MutableLiveData<ErrorResponse>()
    val error: LiveData<ErrorResponse> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        viewModelScope.launch {
            getCommerces()
        }
    }

    private fun getCommerces() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val commerceResponse = commerceRepository.getCommerces()
            Log.d(TAG, commerceResponse.toString())
            when (commerceResponse) {
                is CommerceList -> {
                    allCommerces = commerceResponse.list
                    _commerceList.postValue(CommerceList(commerceResponse.list))
                }
                is ErrorResponse -> {
                    _error.postValue(commerceResponse)
                }
            }
            _loading.postValue(false)
        }
    }

    fun getFilteredCommerces(category: Categories) {
        var filteredList = allCommerces.toMutableList()

        _loading.postValue(true)
        if (category.category_name != Categories.ALL.category_name) {
            filteredList =
                (filteredList.filter { commerce -> commerce.category == category.category_name }).toMutableList()
        }
        Log.d(TAG, filteredList.toString())
        _commerceList.postValue(CommerceList(filteredList))
        _loading.postValue(false)
    }

    fun getSortCommercesByDistance(latitude: Double, longitude: Double) {
        _loading.postValue(true)
        val sortedList = commerceList.value?.list
        Collections.sort(sortedList, SortCommercesByDistance(latitude, longitude))
        Collections.sort(allCommerces, SortCommercesByDistance(latitude, longitude))
        Log.d(TAG, sortedList.toString())
        if (sortedList != null)
            _commerceList.postValue(CommerceList(sortedList))
        _loading.postValue(false)
    }
}