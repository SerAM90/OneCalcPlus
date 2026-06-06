package com.cs467.onecalcplus

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cs467.onecalcplus.ui.PortraitCalculator
import com.cs467.onecalcplus.ui.LandscapeCalculator
import com.cs467.onecalcplus.ui.SettingsWidget
import com.cs467.onecalcplus.ui.ConversionLayout
import com.cs467.onecalcplus.ui.theme.OneCalcPlusTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Hide system bars for immersive mode
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            OneCalcPlusTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        SettingsWidget(viewModel = viewModel)
                    }
                ) { innerPadding ->
                    CalculatorScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val mode = viewModel.mode.value

    Box(modifier = modifier) {
        when (mode) {
            CalculatorMode.CALCULATOR -> {
                if (isLandscape) {
                    LandscapeCalculator(viewModel)
                } else {
                    PortraitCalculator(viewModel)
                }
            }
            CalculatorMode.UNIT_CONVERSION, CalculatorMode.KITCHEN_CONVERSION -> {
                ConversionLayout(viewModel)
            }
        }
    }
}
