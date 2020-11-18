package com.example.tmaptest

sealed class MapState{
    object Addresses: MapState()
    object Map: MapState()
    data class Error(val text: String): MapState()
}