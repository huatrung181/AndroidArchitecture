package com.example.demolivedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MedalViewModel: ViewModel(){
    var numberOfGoldMedal: MutableLiveData<Int> = MutableLiveData()
    var numberOfSilverMedal: MutableLiveData<Int> = MutableLiveData()
    var numberOfBronzeMedal: MutableLiveData<Int> = MutableLiveData()
    init {
        numberOfGoldMedal.value=0
        numberOfSilverMedal.value =0
        numberOfBronzeMedal.value=0
    }

}