package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    //Representa se o último botão clicado é um número ou não
    var lastNumeric: Boolean = false

    //se verdadeiro, não permite adicionar um outro dot
    var lastDot: Boolean = false

    private var tvInput: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
        lastDot = false

    }

    fun onClear(view: View) {
        tvInput?.text = ""
    }

    //Anexa (append) o ponto (dot) à TextView
    fun onDecimalPoint(view: View) {
        //se o último valor anexado (append) for numérico, então anexa o dot, ou não.
        if (lastNumeric && !lastDot) {
            tvInput?.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    //Anexa os operadores a TextView conforme os Buttons.
    fun onOperator(view: View) {
        tvInput?.text?.let {
            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
            }
        }
    }

    //Calcula o output
    fun onEqual(view: View) {
        //A solução só pode ser encontrada se o último valor for um número.
        if (lastNumeric) {
            //Lê o valor de TextView
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try {
                //se o valor começa com '-' então vamos separá-lo e realizar o cálculo somente com o valor.
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                //se o valor contém algum dos operadores, irá separar(split) a string usando o operador escolhido como divisão.
                when {
                    tvValue.contains("-") -> {
                        val splitValue = tvValue.split("-")

                        var one = splitValue[0] //Valor one
                        var two = splitValue[1] //Valor two

                        if (prefix.isEmpty()) { //Se o prefixo não estiver vazio, nós o anexaremos com o primeiro valor, ou seja, one.
                            one = prefix + one
                        }

                        /**
                         * Aqui como o valor one e two serão calculados com base no operador, se o resultado
                         * contiver o zero após o ponto decimal,ele será removido e o resultado exibido na TextView.*/
                        tvInput?.text =
                            removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())

                    }
                    tvValue.contains("+") -> {
                        val splitValue = tvValue.split("+")

                        var one = splitValue[0]
                        var two = splitValue[1]

                        if (prefix.isEmpty()) {
                            one = prefix + one
                        }
                        tvInput?.text =
                            removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())

                    }
                    tvValue.contains("/") -> {
                        val splitValue = tvValue.split("/")

                        var one = splitValue[0]
                        var two = splitValue[1]

                        if (prefix.isEmpty()) {
                            one = prefix + one
                        }
                        tvInput?.text =
                            removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())

                    }
                    tvValue.contains("*") -> {
                        val splitValue = tvValue.split("*")

                        var one = splitValue[0]
                        var two = splitValue[1]

                        if (prefix.isEmpty()) {
                            one = prefix + one
                        }
                        tvInput?.text =
                            removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())

                    }
                }

            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    //Remove o zero depois do ponto decimal
    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0"))
            value = result.substring(0, result.length - 2)

        return value
    }

    //checa se algum operador é usado ou não
    private fun isOperatorAdded(value: String): Boolean {

        //Aqui primeiro vai verificar se o valor começa com "-" então irá ignorá-lo.
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }

}