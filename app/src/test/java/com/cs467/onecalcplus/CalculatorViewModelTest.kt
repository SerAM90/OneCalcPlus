package com.cs467.onecalcplus

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorViewModelTest {

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    @Test
    fun testAddition() {
        viewModel.onNumberClick("5")
        viewModel.onOperationClick("+")
        viewModel.onNumberClick("3")
        viewModel.calculate()
        assertEquals("8", viewModel.displayState.value)
    }

    @Test
    fun testMultiplication() {
        viewModel.onNumberClick("4")
        viewModel.onOperationClick("×")
        viewModel.onNumberClick("6")
        viewModel.calculate()
        assertEquals("24", viewModel.displayState.value)
    }

    @Test
    fun testDivisionPrecision() {
        viewModel.onNumberClick("1")
        viewModel.onNumberClick("0") // "10"
        viewModel.onOperationClick("÷")
        viewModel.onNumberClick("3")
        viewModel.calculate()
        assertEquals("3.3333333333", viewModel.displayState.value)
    }

    @Test
    fun testBackspace() {
        viewModel.onNumberClick("1")
        viewModel.onNumberClick("2")
        viewModel.onNumberClick("3")
        viewModel.onBackspaceClick()
        assertEquals("12", viewModel.displayState.value)
        viewModel.onBackspaceClick()
        assertEquals("1", viewModel.displayState.value)
        viewModel.onBackspaceClick()
        assertEquals("0", viewModel.displayState.value)
    }

    @Test
    fun testScientificSquareRoot() {
        viewModel.onNumberClick("1")
        viewModel.onNumberClick("6") // "16"
        viewModel.onScientificOperation("√")
        assertEquals("4", viewModel.displayState.value)
    }

    @Test
    fun testClear() {
        viewModel.onNumberClick("1")
        viewModel.onNumberClick("2")
        viewModel.onNumberClick("3") // "123"
        viewModel.clear()
        assertEquals("0", viewModel.displayState.value)
    }
}
