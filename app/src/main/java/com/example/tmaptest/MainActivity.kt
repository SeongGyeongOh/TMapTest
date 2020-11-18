package com.example.tmaptest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapPOIItem
import com.skt.Tmap.TMapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MapViewModel()
        val tmapview = TMapView(this);
        tmapview.setSKTMapApiKey("l7xx872df66192334e2584076d0a0e048d9b")
        setContent {
            RootView(viewModel)
        }
    }
}

@Composable
fun RootView(
    viewModel: MapViewModel
){
    val currentState by viewModel.mapState.observeAsState()
    Column() {
        Row {
            Button(onClick = {
                GlobalScope.launch {
                    viewModel.mapIntent.send(MapIntent.ShowMap)
                }
            }) {
                Text(text = "TEST")
            }
            Button(onClick = {
                Log.i("asdf", "null"+viewModel.itemState.value!!.get(0).poiName)
                GlobalScope.launch {
                    viewModel.mapIntent.send(MapIntent.ShowAddresses)
                }
            }) {
                Text(text = "TEST2")
            }
        }
        when(currentState){
            is MapState.Map -> {
                MapView(viewModel)
            }
            is MapState.Addresses -> {
//                Log.i("asdfg", "null?"+viewModel.itemState.poiName)
                MapList(viewModel.itemState.value as ArrayList<TMapPOIItem>)
    //            MapM()
            }
        }
    }
}

@Composable
fun MapList(itemState: ArrayList<TMapPOIItem>) {
    LazyColumnFor(items = itemState) {item ->
        Text(text = item.poiName)
    }
}
@Composable
fun MapM(){
    Text("aaa")
}
@Composable
fun MapView(
    viewModel: MapViewModel
) {
    val context = ContextAmbient.current
    val customView = remember {
        TMapView(context).apply {

        }
    }

    Column() {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        val tmapData = TMapData()
        val itemState = viewModel.itemState
//        val viewModel = MapViewModel()
        val state = MutableLiveData<ArrayList<TMapPOIItem>>()
        Row() {
            TextField(value = textState.value, onValueChange = {textState.value = it})
            Button(onClick = {
                GlobalScope.launch(Dispatchers.Main) {
                    tmapData.findAllPOI(textState.value.text){
                        itemState.postValue(it)

//                        itemState.value = it
//                        it.forEach{
//                            itemState.postValue(it)
//                        }
//                        Log.i("asdf", itemState.value.toString())
                    }
                }
            }) {
                Text(text = "제발")
            }
        }
//
//        Button(onClick = {
////            Log.i("asdf", itemState.size.toString())
//            GlobalScope.launch {
//                viewModel.mapIntent.send(MapIntent.ShowAddresses)
//            }
//        }) {
//            Text(text = "TEST")
////            Text(text = itemState.)
//        }

//        Log.i("asdf", itemState.get(0).toString())

        AndroidView(viewBlock = { customView }) {
            customView
        }
    }
}

