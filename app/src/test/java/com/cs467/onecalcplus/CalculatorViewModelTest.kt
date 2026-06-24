package com.cs467.onecalcplus

import com.cs467.onecalcplus.model.CalculatorMode
import com.cs467.onecalcplus.model.CalculatorUiEvent
import com.cs467.onecalcplus.model.ConversionItem
import kotlinx.coroutines.runBlocking
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
    fun testAddition() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("5"))
        viewModel.onEvent(CalculatorUiEvent.OperationClick("+"))
        viewModel.onEvent(CalculatorUiEvent.NumberClick("3"))
        viewModel.onEvent(CalculatorUiEvent.Calculate)
        assertEquals("8", viewModel.uiState.value.display)
    }

    @Test
    fun testMultiplication() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("4"))
        viewModel.onEvent(CalculatorUiEvent.OperationClick("×"))
        viewModel.onEvent(CalculatorUiEvent.NumberClick("6"))
        viewModel.onEvent(CalculatorUiEvent.Calculate)
        assertEquals("24", viewModel.uiState.value.display)
    }

    @Test
    fun testDivisionPrecision() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("10"))
        viewModel.onEvent(CalculatorUiEvent.OperationClick("÷"))
        viewModel.onEvent(CalculatorUiEvent.NumberClick("3"))
        viewModel.onEvent(CalculatorUiEvent.Calculate)
        assertEquals("3.3333333333", viewModel.uiState.value.display)
    }

    @Test
    fun testBackspace() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("123"))
        viewModel.onEvent(CalculatorUiEvent.Backspace)
        assertEquals("12", viewModel.uiState.value.display)
        viewModel.onEvent(CalculatorUiEvent.Backspace)
        assertEquals("1", viewModel.uiState.value.display)
        viewModel.onEvent(CalculatorUiEvent.Backspace)
        assertEquals("0", viewModel.uiState.value.display)
    }

    @Test
    fun testConversionInputAppend() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.SetMode(CalculatorMode.UNIT_CONVERSION))
        val item = ConversionItem("m to ft", 3.28084, "length")
        viewModel.onEvent(CalculatorUiEvent.SelectConversion(item))
        
        // Initial placeholder is "1"
        assertEquals("1", viewModel.uiState.value.conversionInput)
        
        // Enter "1" -> should replace "1" with "1"
        viewModel.onEvent(CalculatorUiEvent.NumberClick("1"))
        assertEquals("1", viewModel.uiState.value.conversionInput)
        
        // Enter "2" -> should append to get "12"
        viewModel.onEvent(CalculatorUiEvent.NumberClick("2"))
        assertEquals("12", viewModel.uiState.value.conversionInput)
    }

    @Test
    fun testDivisionByZero() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("5"))
        viewModel.onEvent(CalculatorUiEvent.OperationClick("÷"))
        viewModel.onEvent(CalculatorUiEvent.NumberClick("0") )
        viewModel.onEvent(CalculatorUiEvent.Calculate)
        assertEquals("undefined", viewModel.uiState.value.display)
    }

    @Test
    fun testScientificOperation() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("16"))
        viewModel.onEvent(CalculatorUiEvent.ScientificOperationClick("√"))
        assertEquals("4", viewModel.uiState.value.display)
    }

    @Test
    fun testClear() = runBlocking {
        viewModel.onEvent(CalculatorUiEvent.NumberClick("123"))
        viewModel.onEvent(CalculatorUiEvent.Clear)
        assertEquals("0", viewModel.uiState.value.display)
    }
}
