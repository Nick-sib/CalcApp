package com.nickolay.calcapp

class ModelRepository(
    private val resultCallback: OnDataCallback,
    private val historyCallback: OnDataCallback) {

    private var historyValue = ""
    private var currentValue = "0"
    private var memorisedValue1 = 0.0
    private var memorisedValue2 = 0.0
    private var state = STATE_INPUT_NUMBER
    private var operation: Char = OPERATION_EMPTY

    private fun showResultValue(){
       // Handler(Looper.getMainLooper()).post {
            resultCallback.onDataReady(currentValue)
        //}
    }

    private fun showHistoryValue(addReturn: Boolean = false){
        historyValue = historyValue.dropWhile {
            it == ' '
        }
       // Handler(Looper.getMainLooper()).post {
            val showThis = when {
                addReturn -> "$historyValue ="
                operation == OPERATION_EMPTY -> historyValue
                else -> "$historyValue $operation"
            }

            historyCallback.onDataReady(showThis)
        //}
    }

    fun doDigitalClick(value: Char) {
        when (state) {
            STATE_INPUT_NUMBER -> {

            }
            STATE_INPUT_OPERATION -> {
                memorisedValue2 = memorisedValue1
                memorisedValue1 = currentValue.toDouble()
                currentValue = "0"
            }
            STATE_CLICK_RETURN -> {
                memorisedValue2 = memorisedValue1
                memorisedValue1 = currentValue.toDouble()
                currentValue = "0"
                historyValue = ""
                operation = OPERATION_EMPTY
                showHistoryValue()
            }
        }
        addDigit(value)
        state = STATE_INPUT_NUMBER
        showResultValue()
    }

    private fun addDigit(value: Char){
        val digit = listOf('0','1','2','3','4','5','6','7','8','9', '.')
        if (value !in digit) return
        if (!(value == '0' && currentValue == "0") &&
            !(value == '.' && currentValue.indexOf('.') > -1)) {
            currentValue =
                if (currentValue == "0" && value != '.')
                    value.toString()
                else
                    currentValue + value
        }
    }

    fun doBackspace() {
        if (currentValue == "0") {
            operation = OPERATION_EMPTY
            return
        }
        currentValue =
            if (currentValue.length == 1)  "0"
            else currentValue.dropLast(1)
        if (currentValue == "-") currentValue = "0"
        showResultValue()
    }

    fun doDelete() {
        currentValue = "0"
        showResultValue()
    }

    fun doClear() {
        memorisedValue1 = 0.0
        memorisedValue2 = 0.0
        currentValue = "0"
        historyValue = ""
        operation = OPERATION_EMPTY
        state = STATE_INPUT_OPERATION
        showResultValue()
        showHistoryValue()
    }

    fun onPercentClick(){
        currentValue = (currentValue.toDouble() / 100).trimTails()
        showResultValue()
    }

    fun onOperationsClick(value: Char){
        when (state) {
            STATE_INPUT_NUMBER -> {
                if (operation != OPERATION_EMPTY)
                    historyValue = "$historyValue $operation"
                memorisedValue2 = memorisedValue1
                memorisedValue1 = currentValue.toDouble()
                historyValue = "$historyValue ${currentValue.trimTails()}"
                currentValue = calcResult(memorisedValue2, memorisedValue1, operation).trimTails()
                showResultValue()
            }
            STATE_INPUT_OPERATION -> {
            }
            STATE_CLICK_RETURN -> {
                historyValue = currentValue
            }
        }
        operation = value
        showHistoryValue()
        state = STATE_INPUT_OPERATION
    }

    fun onEquallyClick() {
        when (state) {
            STATE_INPUT_NUMBER -> {
                memorisedValue2 = memorisedValue1
                memorisedValue1 = currentValue.toDouble()
                historyValue += " $operation ${currentValue.trimTails()}"
                currentValue = calcResult(memorisedValue2, memorisedValue1, operation).trimTails()
            }
            STATE_INPUT_OPERATION -> {
                memorisedValue2 = memorisedValue1
                memorisedValue1 = currentValue.toDouble()
                historyValue += " $operation ${currentValue.trimTails()}"
                currentValue = calcResult(memorisedValue1, memorisedValue1, operation).trimTails()
            }
            STATE_CLICK_RETURN -> {
                memorisedValue2 = currentValue.toDouble()
                historyValue = if (operation == OPERATION_EMPTY)
                    this.currentValue.trimTails()
                else
                    "${memorisedValue2.trimTails()} $operation ${memorisedValue1.trimTails()}"

                currentValue = calcResult(memorisedValue2, memorisedValue1, operation).trimTails()
            }
        }
        showResultValue()
        showHistoryValue(true)
        state = STATE_CLICK_RETURN
    }

    private fun calcResult(value1: Double, value2: Double, operation: Char) =
          when (operation) {
            OPERATION_PLUS -> value1 + value2
            OPERATION_MINUS -> value1 - value2
            OPERATION_MULTIPLY -> value1 * value2
            OPERATION_DIVIDE -> value1 / value2
            else -> value2
        }

    private fun doTrim(value: String): String {
        var sRes = value
        //удаляем не несущие смысла хвосты цифр
        if (sRes.indexOf('.') > 0){
            sRes = sRes.dropLastWhile{
                it == '0'
            }
            if (sRes[sRes.lastIndex] == '.')
                sRes = sRes.dropLast(1)
        }
        return sRes
    }

    private fun Double.trimTails() = doTrim(this.toString())

    private fun String.trimTails() = doTrim(this)

    companion object {
        const val STATE_INPUT_NUMBER = 0
        const val STATE_INPUT_OPERATION = 1
        const val STATE_CLICK_RETURN = 2

        const val OPERATION_EMPTY =    ' '
        const val OPERATION_PLUS =     '+'
        const val OPERATION_MINUS =    '-'
        const val OPERATION_MULTIPLY = '*'
        const val OPERATION_DIVIDE =   '÷'
    }
}