package com.nickolay.calcapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModelRepositoryTest {
    private var textResult = ""
    private var historyResult = ""

    private val resultCallback = object : OnDataCallback {
        override fun onDataReady(data: String) {
            textResult = data
        }
    }

    private val historyCallback = object : OnDataCallback {
        override fun onDataReady(data: String) {
            historyResult = data
        }
    }

    private val modelRepository = ModelRepository(resultCallback, historyCallback)

    @Test
    fun doDigitInput() {
        textResult = "0"
        val inputData = "0123456789.987654310"
        for (ch in inputData) {
            modelRepository.doDigitalClick(ch)
        }
        assertEquals(textResult,"123456789.987654310")
    }

    @Test
    fun doBackspace() {
        textResult = "0"
        val inputData = ".987654310"
        for (ch in inputData) {
            modelRepository.doDigitalClick(ch)
        }
        assertEquals(textResult,"0.987654310")
        for (i in 1..9)
            modelRepository.doBackspace()
        modelRepository.doDigitalClick('1')
        assertEquals(textResult,"0.1")
    }

    @Test
    fun doDelete() {
        textResult = "0"
        val inputData = "987654310"
        for (ch in inputData) {
            modelRepository.doDigitalClick(ch)
        }
        assertEquals(textResult,"987654310")
        modelRepository.doDelete()
        assertEquals(textResult,"0")
    }

    @Test
    fun onPercentClick() {
        val inputData = "987654310"
        for (ch in inputData) {
            modelRepository.doDigitalClick(ch)
        }
        assertEquals(textResult,"987654310")
        modelRepository.onPercentClick()
        assertEquals(textResult,"9876543.1")
    }

    @Test
    fun onOperationsClickAndHistory() {
        modelRepository.doDigitalClick('6')
        modelRepository.onOperationsClick('-')
        assertEquals(textResult,"6")
        assertEquals(historyResult,"6 -")

        modelRepository.doDigitalClick('5')
        modelRepository.onOperationsClick('+')
        assertEquals(textResult, (6-5).toString())
        assertEquals(historyResult,"6 - 5 +")

        modelRepository.doDigitalClick('4')
        modelRepository.onOperationsClick('*')
        assertEquals(textResult,((6-5)+4).toString())
        assertEquals(historyResult,"6 - 5 + 4 *")

        modelRepository.doDigitalClick('3')
        modelRepository.onOperationsClick('÷')
        assertEquals(textResult,(((6-5)+4)*3).toString())
        assertEquals(historyResult,"6 - 5 + 4 * 3 ÷")

        modelRepository.doDigitalClick('2')
        modelRepository.onOperationsClick('+')
        assertEquals(textResult,((((6.0-5)+4)*3)/2).toString())
        assertEquals(historyResult,"6 - 5 + 4 * 3 ÷ 2 +")
    }

    @Test
    fun doClear() {
        modelRepository.doDigitalClick('6')
        modelRepository.onOperationsClick('-')
        modelRepository.doDigitalClick('5')
        modelRepository.onOperationsClick('+')
        modelRepository.doDigitalClick('4')
        modelRepository.onOperationsClick('*')
        modelRepository.doDigitalClick('3')
        modelRepository.onOperationsClick('÷')
        modelRepository.doDigitalClick('2')
        modelRepository.onOperationsClick('+')

        assertEquals(textResult,"7.5")
        assertEquals(historyResult,"6 - 5 + 4 * 3 ÷ 2 +")

        modelRepository.doClear()
        assertEquals(textResult,"0")
        assertEquals(historyResult,"")
    }


    @Test
    fun onEquallyClick() {
        modelRepository.doDigitalClick('6')
        modelRepository.onEquallyClick()
        modelRepository.onEquallyClick()
        assertEquals(textResult,"6")
        assertEquals(historyResult,"6 =")

        modelRepository.onOperationsClick('-')
        modelRepository.doDigitalClick('1')
        modelRepository.onEquallyClick()
        assertEquals(textResult,(6-1).toString())
        assertEquals(historyResult,"6 - 1 =")

        modelRepository.onEquallyClick()
        assertEquals(textResult,(6-1-1).toString())
        assertEquals(historyResult,"5 - 1 =")
        modelRepository.onEquallyClick()
        assertEquals(textResult,(6-1-1-1).toString())
        assertEquals(historyResult,"4 - 1 =")

        modelRepository.onOperationsClick('+')
        modelRepository.doDigitalClick('4')
        modelRepository.onOperationsClick('*')
        modelRepository.doDigitalClick('3')
        modelRepository.onOperationsClick('÷')
        modelRepository.doDigitalClick('2')
        modelRepository.onEquallyClick()

        assertEquals(textResult,((((6.0-1-1-1)+4)*3)/2).toString())
        assertEquals(historyResult,"3 + 4 * 3 ÷ 2 =")
    }
}