package com.example.basicscodelab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basicscodelab.ui.theme.BasicsCodelabTheme
import kotlin.math.*

object CalculatorScreen {
    fun factorial(n: Double): Double {
        val intN = n.toInt()
        return if (n < 0 || n % 1 != 0.0) Double.NaN
        else if (intN == 0 || intN == 1) 1.0
        else (2..intN).fold(1.0) { acc, i -> acc * i }
    }
    @Composable
    fun SetupLayout(modifier: Modifier = Modifier) {
        var displayText by remember { mutableStateOf("0") }
        var firstNumber by remember { mutableStateOf("") }
        var operator by remember { mutableStateOf("") }
        var secondNumber by remember { mutableStateOf("") }
        var isScientific by remember { mutableStateOf(false) }

        fun updateDisplay() {
            displayText = buildString {
                append(firstNumber.ifEmpty { "0" })
                if (operator.isNotEmpty()) {
                    append(" $operator")
                    if (secondNumber.isNotEmpty()) {
                        append(" $secondNumber")
                    }
                }
            }
        }

        fun onNumberClick(num: String) {
            if (operator.isEmpty()) {
                firstNumber = if (firstNumber == "0") num else firstNumber + num
            } else {
                secondNumber = if (secondNumber == "0") num else secondNumber + num
            }
            updateDisplay()
        }

        fun onDotClick() {
            if (operator.isEmpty()) {
                if (!firstNumber.contains(".")) {
                    firstNumber = if (firstNumber.isEmpty()) "0." else "$firstNumber."
                }
            } else {
                if (!secondNumber.contains(".")) {
                    secondNumber = if (secondNumber.isEmpty()) "0." else "$secondNumber."
                }
            }
            updateDisplay()
        }

        fun onOperatorClick(op: String) {
            if (firstNumber.isNotEmpty()) {
                operator = op
                updateDisplay()
            }
        }

        fun onBackspace() {
            when {
                secondNumber.isNotEmpty() -> {
                    secondNumber = secondNumber.dropLast(1)
                }
                operator.isNotEmpty() -> {
                    operator = ""
                }
                firstNumber.isNotEmpty() -> {
                    firstNumber = firstNumber.dropLast(1)
                    if (firstNumber.isEmpty()) {
                        firstNumber = "0"
                    }
                }
            }
            updateDisplay()
        }

        fun onClear() {
            firstNumber = ""
            operator = ""
            secondNumber = ""
            displayText = "0"
        }

        fun onEqual() {
            if (firstNumber.isNotEmpty() && operator.isNotEmpty() && secondNumber.isNotEmpty()) {
                val num1 = firstNumber.toDouble()
                val num2 = secondNumber.toDouble()
                val result = when (operator) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "x" -> num1 * num2
                    "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                    "%" -> num1 % num2
                    "^" -> num1.pow(num2)
                    else -> 0.0
                }
                displayText = if (result % 1 == 0.0) result.toInt().toString() else result.toString()
                firstNumber = displayText
                operator = ""
                secondNumber = ""
            }
        }

        fun onScientificFunction(func: String) {
            if (firstNumber.isEmpty()) return
            val num = firstNumber.toDouble()
            val result = when (func) {
                "1/x" -> if (num != 0.0) 1 / num else Double.NaN
                "x!" -> factorial(num)
                "√x" -> sqrt(num)
                "sin" -> sin(Math.toRadians(num))
                "cos" -> cos(Math.toRadians(num))
                "tan" -> tan(Math.toRadians(num))
                "arcsin" -> Math.toDegrees(asin(num))
                "arccos" -> Math.toDegrees(acos(num))
                "arctan" -> Math.toDegrees(atan(num))
                "log" -> log10(num)
                "ln" -> ln(num)
                else -> num
            }
            displayText = if (result % 1 == 0.0) result.toInt().toString() else result.toString()
            firstNumber = displayText
            operator = ""
            secondNumber = ""
        }

        Surface(
            color = Color.White,
            modifier = modifier.padding(10.dp)
        ) {
            Column {
                CalculatorTextResult(
                    text = displayText,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isScientific) {
                        // Standard
                        Row(modifier = modifier) {
                            CalculatorButton("AC", onClick = { onClear() }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("<-", onClick = { onBackspace() }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("%", onClick = { onOperatorClick("%") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("/", onClick = { onOperatorClick("/") }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton("7", onClick = { onNumberClick("7") })
                            CalculatorButton("8", onClick = { onNumberClick("8") })
                            CalculatorButton("9", onClick = { onNumberClick("9") })
                            CalculatorButton("x", onClick = { onOperatorClick("x") }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton("4", onClick = { onNumberClick("4") })
                            CalculatorButton("5", onClick = { onNumberClick("5") })
                            CalculatorButton("6", onClick = { onNumberClick("6") })
                            CalculatorButton("-", onClick = { onOperatorClick("-") }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton("1", onClick = { onNumberClick("1") })
                            CalculatorButton("2", onClick = { onNumberClick("2") })
                            CalculatorButton("3", onClick = { onNumberClick("3") })
                            CalculatorButton("+", onClick = { onOperatorClick("+") }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton(".", onClick = { onDotClick() })
                            CalculatorButton("0", onClick = { onNumberClick("0") })
                            CalculatorButton("=", onClick = { onEqual() }, containerColor = Color(0xFFFEA72E), textColor = Color.White)
                            CalculatorButton("sci", onClick = { isScientific = true }, containerColor = Color.Gray, textColor = Color.White)
                        }
                    } else {
                        // Scientific
                        Row(modifier = modifier) {
                            CalculatorButton("1/x", onClick = { onScientificFunction("1/x") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("x!", onClick = { onScientificFunction("x!") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("xʸ", onClick = { onOperatorClick("^") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("√x", onClick = { onScientificFunction("√x") }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton("sin", onClick = { onScientificFunction("sin") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("cos", onClick = { onScientificFunction("cos") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("tan", onClick = { onScientificFunction("tan") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("AC", onClick = { onClear() }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton("arcsin", onClick = { onScientificFunction("arcsin") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("arccos", onClick = { onScientificFunction("arccos") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("arctan", onClick = { onScientificFunction("arctan") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("<-", onClick = { onBackspace() }, containerColor = Color.Gray, textColor = Color.White)
                        }
                        Row(modifier = modifier) {
                            CalculatorButton("log", onClick = { onScientificFunction("log") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("ln", onClick = { onScientificFunction("ln") }, containerColor = Color.Gray, textColor = Color.White)
                            CalculatorButton("=", onClick = { onEqual() }, containerColor = Color(0xFFFEA72E), textColor = Color.White)
                            CalculatorButton("std", onClick = { isScientific = false })
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CalculatorButton(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        containerColor: Color = Color.White,
        textColor: Color = Color.Black
    ) {
        ElevatedButton(
            onClick = onClick,
            modifier = modifier
                .padding(8.dp)
                .height(50.dp)
                .width(70.dp),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Black),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = textColor
            )
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
            )
        }
    }

    @Composable
    fun CalculatorTextResult(text: String, modifier: Modifier = Modifier) {
        val scrollState = rememberScrollState()

        LaunchedEffect(text) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        Surface(
            color = Color.White,
            border = BorderStroke(2.dp, Color.Black),
            modifier = modifier
                .height(80.dp)
                .padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = text,
                    fontSize = 28.sp,
                    color = Color.Black,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    BasicsCodelabTheme {
        CalculatorScreen.SetupLayout()
    }
}
