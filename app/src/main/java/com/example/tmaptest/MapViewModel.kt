package com.example.tmaptest

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skt.Tmap.TMapPOIItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
//    val itemState = mutableStateListOf(TMapPOIItem())
    val itemState = MutableLiveData<MutableList<TMapPOIItem>>()

    val mapIntent = Channel<MapIntent>(Channel.UNLIMITED)
    val _mapState = MutableLiveData<MapState>()
    val mapState: LiveData<MapState> get() = _mapState

    init {
        handleIntent()
    }

    fun handleIntent(){
        viewModelScope.launch {
            mapIntent.consumeAsFlow().collect {
                when(it){
                    is MapIntent.ShowMap -> showMap()
                    is MapIntent.ShowAddresses -> showAddresses()
                }
            }
        }
    }
    fun showMap(){
        viewModelScope.launch {
            _mapState.value = MapState.Map
        }
    }

    fun showAddresses(){
        viewModelScope.launch {
            _mapState.value = try{
                MapState.Addresses
            }catch (e: Exception){
                MapState.Error(e.stackTraceToString())
            }
        }
    }
}