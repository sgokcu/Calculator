package com.example.hesapmakinesi

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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
        if (view !is Button) return  // Sadece butonlar işlenir

        if (isNewOp) {
            expression = ""
            isNewOp = false
        }

        val text = view.text.toString()

        val safeText = when (text) {
            "×" -> "*"
            "÷" -> "/"
            else -> text
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

        // Son sayıyı bulmak için ters sırada operatör ararız
        val lastOpIndex = expression.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))

        val numberStart = if (lastOpIndex == -1) 0 else lastOpIndex + 1
        val number = expression.substring(numberStart)

        if (number.isEmpty()) return

        if (number.startsWith("-")) {
            // Zaten negatifse başındaki "-" işaretini kaldır
            expression = expression.removeRange(numberStart, numberStart + 1)
        } else {
            // Değilse başına "-" ekle
            expression = expression.substring(0, numberStart) + "-" + number
        }

        binding.editTextText.setText(expression)
    }



    fun btnEqual(view: View) {
        try {
            val exp = Expression(expression)
            val result = exp.calculate()

            if (result.isNaN()) {
                binding.editTextText.setText("Hata")
                Toast.makeText(this, "Geçersiz işlem", Toast.LENGTH_SHORT).show()
            } else {
                binding.editTextText.setText(result.toString())
                expression = result.toString()
                isNewOp = true
            }
        } catch (e: Exception) {
            binding.editTextText.setText("Hata")
        }
    }

    fun btnPercent(view: View) {
        try {
            val result = Expression(expression).calculate() / 100
            binding.editTextText.setText(result.toString())
            expression = result.toString()
            isNewOp = true
        } catch (e: Exception) {
            binding.editTextText.setText("Hata")
        }
    }
}
