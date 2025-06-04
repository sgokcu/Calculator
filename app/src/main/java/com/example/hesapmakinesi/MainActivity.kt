package com.example.hesapmakinesi

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.hesapmakinesi.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.Expression

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var expression = ""
    private var isNewOp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun btnClick(view: View) {
        if (view !is Button) return

        if (isNewOp) {
            expression = ""
            isNewOp = false
        }

        val text = view.text.toString()

        val safeText = when (text) {
            "×", "x", "X" -> "*"
            "÷", "﹣", "−" -> "/"
            "–", "—" -> "-"
            "＋" -> "+"
            "," -> "."
            else -> text
        }
        if (safeText == ".") {
            val lastOpIndex = expression.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
            val currentNumber = if (lastOpIndex == -1) expression else expression.substring(lastOpIndex + 1)

            if (currentNumber.contains(".")) {
                return
            }
        }
        expression += safeText
        binding.editTextText.setText(expression)
    }

    fun btnClear(view: View) {
        expression = ""
        binding.editTextText.setText("0")
        isNewOp = true
    }

    fun btnToggleSign(view: View) {
        if (expression.isEmpty()) return

        val lastOpIndex = expression.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))

        val numberStart = if (lastOpIndex == -1) 0 else lastOpIndex + 1
        val number = expression.substring(numberStart)

        if (number.isEmpty()) return

        if (number.startsWith("-")) {
            expression = expression.removeRange(numberStart, numberStart + 1)
        } else {
            expression = expression.substring(0, numberStart) + "-" + number
        }
        binding.editTextText.setText(expression)
    }

    fun btnEqual(view: View) {
        try {
            val exp = Expression(expression)
            val result = exp.calculate()

            if (result.isNaN()) {
                binding.editTextText.setText("Error")
                isNewOp = true
            } else {
                binding.editTextText.setText(result.toString())
                isNewOp = true
            }
        } catch (e: Exception) {
            binding.editTextText.setText("Error")
        }
    }

    fun btnPercent(view: View) {
        try {
            val result = Expression(expression).calculate() / 100
            binding.editTextText.setText(result.toString())
            expression = result.toString()
            isNewOp = true
        } catch (e: Exception) {
            binding.editTextText.setText("Error")
        }
    }
}
