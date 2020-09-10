package com.nickolay.calcapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nickolay.calcapp.ModelRepository.Companion.OPERATION_DIVIDE
import com.nickolay.calcapp.ModelRepository.Companion.OPERATION_MINUS
import com.nickolay.calcapp.ModelRepository.Companion.OPERATION_MULTIPLY
import com.nickolay.calcapp.ModelRepository.Companion.OPERATION_PLUS

class MainViewModel: ViewModel() {

    private val _resultValue = MutableLiveData<String>().apply {
        value = "0"
    }

    private val resultCallback = object : OnDataCallback {
        override fun onDataReady(data: String) {
            _resultValue.value = data
        }
    }

    private val _historyValue = MutableLiveData<String>().apply {
        value = ""
    }

    private val historyCallback = object : OnDataCallback {
        override fun onDataReady(data: String) {
            _historyValue.value = data
        }
    }

    private var repoModel: ModelRepository = ModelRepository(resultCallback, historyCallback)

    val resultValue: LiveData<String> = _resultValue
    val historyValue: LiveData<String> = _historyValue

    fun onNumberClick(buttonID: Int) {
        val digit = when (buttonID) {
                R.id.b_0 -> '0'
                R.id.b_1 -> '1'
                R.id.b_2 -> '2'
                R.id.b_3 -> '3'
                R.id.b_4 -> '4'
                R.id.b_5 -> '5'
                R.id.b_6 -> '6'
                R.id.b_7 -> '7'
                R.id.b_8 -> '8'
                R.id.b_9 -> '9'
                R.id.bDecimal -> '.'
                else -> throw NumberFormatException()
        }
        repoModel.doDigitalClick(digit)
    }

    fun doBackspace() {
        repoModel.doBackspace()
    }

    fun doDeleteValue() {
        if (_resultValue.value == "0") {
            repoModel.doClear()
        } else {
            repoModel.doDelete()
        }
    }

    fun doClear(){
        repoModel.doClear()
    }

    fun onPercentClick(){
        repoModel.onPercentClick()
    }

    fun onOperationsClick(buttonID: Int) {
        val operation = when(buttonID) {
            R.id.bPlus     -> OPERATION_PLUS
            R.id.bMinus    -> OPERATION_MINUS
            R.id.bMultiply -> OPERATION_MULTIPLY
            R.id.bDivide   -> OPERATION_DIVIDE
            else -> throw NumberFormatException()
        }
        repoModel.onOperationsClick(operation)
    }

    fun onEquallyClick(){
        repoModel.onEquallyClick()
    }
}