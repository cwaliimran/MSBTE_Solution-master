package com.msbte.modelanswerpaper

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.msbte.modelanswerpaper.databinding.ActivityDumpsBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject


class DumpsActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityDumpsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var famount = 0
    private var myIdArr = ArrayList<String>()
    private lateinit var intenttransf: Intent
    private lateinit var eurl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDumpsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.mytoolbar)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        setupButtons()
        setupCheckBoxes()

        binding.idBtnPay.setOnClickListener(View.OnClickListener {
            val samount = famount.toString()
            val amount = Math.round(samount.toFloat() * 100)
            val checkout = Checkout()
            checkout.setKeyID("rzp_live_SzNU5aszBMzHu5") // change key here if you want
            checkout.setImage(R.mipmap.ic_launcher_round)
            val `object` = JSONObject()
            try {
                `object`.put("name", "MSBTE Solution APP")
                `object`.put(
                    "description",
                    "MSBTE Solution App is an application that provides study materials and resources for students preparing for MSBTE diploma exams"
                )
                `object`.put("theme.color", "")
                `object`.put("payment_capture", 1)
                `object`.put("amount", amount)
                `object`.put("prefill.contact", "9999999999")
                `object`.put("prefill.email", "onkardokhe1234@gmail.com")
                checkout.open(this@DumpsActivity, `object`)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })

        restoreButtonVisibility()
    }

    private fun setupButtons() {
        val buttons = arrayOf(
            binding.button1, binding.button2, binding.button3, binding.button4, binding.button5
        )
        val urls = arrayOf(
            "https://drive.google.com/drive/folders/1MNQiaxSRxS22r4Sl8yyGhCD5faE00gyX?usp=sharing",
            "https://drive.google.com/drive/folders/1Gm4HLCWty7KjypPB14Gm9-76bwhhxo5M?usp=sharing",
            "https://drive.google.com/drive/folders/1oqcuqxB8ze-1QaYy3ULONnYm2OpY1aX3?usp=sharing",
            "https://drive.google.com/drive/folders/1vp3IyCQSb3uvOmiw4IAhUwsJlrUtQIQV?usp=sharing",
            "https://drive.google.com/drive/folders/10iEgyiS8qdGvMst8lidvwybCZNpmDqJg?usp=sharing"
        )

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                intenttransf = Intent(applicationContext, DumpsPDFActivity::class.java)
                eurl = urls[i]
                intenttransf.putExtra("url", eurl)
                startActivity(intenttransf)
            }
        }
    }

    private fun setupCheckBoxes() {
        val checkBoxes = arrayOf(
            binding.checkBox1,
            binding.checkBox2,
            binding.checkBox3,
            binding.checkBox4,
            binding.checkBox5
        )
        val prices =
            arrayOf(binding.price1, binding.price2, binding.price3, binding.price4, binding.price5)

        for (i in checkBoxes.indices) {
            checkBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                val price = prices[i].text.toString().toInt()
                famount += if (isChecked) price else -price
                binding.totaltext.text = "Total : ₹$famount"
                val buttonId = "b${i + 1}"
                if (isChecked) {
                    myIdArr.add(buttonId)
                } else {
                    myIdArr.remove(buttonId)
                }
            }
        }
    }

    private fun restoreButtonVisibility() {
        val buttons = arrayOf(
            binding.button1, binding.button2, binding.button3, binding.button4, binding.button5
        )
        val buttonPrefs = arrayOf("b1", "b2", "b3", "b4", "b5")

        for (i in buttons.indices) {
            val isVisible = sharedPreferences.getBoolean(buttonPrefs[i], false)
            buttons[i].visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    override fun onPaymentSuccess(s: String) {
        for (element in myIdArr) {
            val button = getButtonById(element)
            if (button != null) {
                button.visibility = View.VISIBLE
                sharedPreferences.edit().putBoolean(element, true).apply()
            }
        }
        binding.pstatus.text = "Payment Successfully. Transaction No: $s"
        binding.totaltext.text = "0.00"
        for (checkBox in arrayOf(
            binding.checkBox1,
            binding.checkBox2,
            binding.checkBox3,
            binding.checkBox4,
            binding.checkBox5
        )) {
            checkBox.isChecked = false
        }
    }

    override fun onPaymentError(i: Int, s: String) {
        binding.pstatus.text = "Something went wrong: $s"
        binding.totaltext.text = "0.00"
        for (checkBox in arrayOf(
            binding.checkBox1,
            binding.checkBox2,
            binding.checkBox3,
            binding.checkBox4,
            binding.checkBox5
        )) {
            checkBox.isChecked = false
        }
    }

    private fun getButtonById(id: String): View? {
        return when (id) {
            "b1" -> binding.button1
            "b2" -> binding.button2
            "b3" -> binding.button3
            "b4" -> binding.button4
            "b5" -> binding.button5
            else -> null
        }
    }
}