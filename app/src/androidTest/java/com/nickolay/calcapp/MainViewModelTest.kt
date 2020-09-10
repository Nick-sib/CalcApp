package com.nickolay.calcapp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private var testedValue = ""
    private var testedHistory = ""
    private val  observerValue = Observer<String> { value -> testedValue = value!! }
    private val  observerHistory = Observer<String> { value -> testedHistory = value!! }

    @Before
    fun before() {
       viewModel = MainViewModel()
       viewModel.resultValue.observeForever(observerValue)
       viewModel.historyValue.observeForever(observerHistory)
    }

    @After
    fun after(){
       viewModel.resultValue.removeObserver(observerValue)
       viewModel.resultValue.removeObserver(observerHistory)
    }

    @Test
    fun testingAllModel() {

        //testing numbers clicking
        viewModel.onNumberClick(R.id.b_1)
        viewModel.onNumberClick(R.id.bDecimal)
        viewModel.onNumberClick(R.id.b_2)
        assertEquals("1.2", testedValue)

        //testing delete buttons click and operation plus
        viewModel.doBackspace()
        viewModel.onOperationsClick(R.id.bPlus)
        viewModel.onNumberClick(R.id.b_9)
        assertEquals("9", testedValue)
        assertEquals("1 +", testedHistory)

        //testing Percent Click and Equally Click
        viewModel.onPercentClick()
        viewModel.onEquallyClick()
        assertEquals("1.09", testedValue)
        assertEquals("1 + 0.09 =", testedHistory)

        //testing Delete
        viewModel.doDeleteValue()
        assertEquals("0", testedValue)
        assertEquals("1 + 0.09 =", testedHistory)

        //testing Clear
        viewModel.doClear()
        assertEquals("0", testedValue)
        assertEquals("", testedHistory)
    }
}


