package com.example.tmaptest

sealed class MapIntent{
    object ShowAddresses: MapIntent()
    object ShowMap: MapIntent()
}