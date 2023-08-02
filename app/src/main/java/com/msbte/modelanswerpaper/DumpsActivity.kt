package com.msbte.modelanswerpaper

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject

class DumpsActivity : AppCompatActivity(), PaymentResultListener {
    lateinit var cb1: CheckBox
    lateinit var cb2: CheckBox
    lateinit var cb3: CheckBox
    lateinit var cb4: CheckBox
    lateinit var cb5: CheckBox
    lateinit var b1: Button
    lateinit var b2: Button
    lateinit var b3: Button
    lateinit var b4: Button
    lateinit var b5: Button
    lateinit var pr1: TextView
    lateinit var pr2: TextView
    lateinit var pr3: TextView
    lateinit var pr4: TextView
    lateinit var pr5: TextView
    lateinit var fprice: TextView
    lateinit var pstatus: TextView
    lateinit var paybtn: Button
    var famount = 0
    var myIdArr = ArrayList<String>()
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var intenttransf: Intent
    lateinit var eurl: String
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dumps)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        toolbar = findViewById(R.id.mytoolbar)
        setSupportActionBar(toolbar)

        // Initialize SharedPreferences object
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Get the visibility status from SharedPreferences
        val isButtonVisible1 = sharedPreferences.getBoolean("b1", false)
        val isButtonVisible2 = sharedPreferences.getBoolean("b2", false)
        val isButtonVisible3 = sharedPreferences.getBoolean("b3", false)
        val isButtonVisible4 = sharedPreferences.getBoolean("b4", false)
        val isButtonVisible5 = sharedPreferences.getBoolean("b5", false)
        b1 = findViewById(R.id.button1)
        b2 = findViewById(R.id.button2)
        b3 = findViewById(R.id.button3)
        b4 = findViewById(R.id.button4)
        b5 = findViewById(R.id.button5)
        cb1 = findViewById(R.id.checkBox1)
        cb2 = findViewById(R.id.checkBox2)
        cb3 = findViewById(R.id.checkBox3)
        cb4 = findViewById(R.id.checkBox4)
        cb5 = findViewById(R.id.checkBox5)
        pr1 = findViewById(R.id.price1)
        pr2 = findViewById(R.id.price2)
        pr3 = findViewById(R.id.price3)
        pr4 = findViewById(R.id.price4)
        pr5 = findViewById(R.id.price5)
        pstatus = findViewById(R.id.pstatus)
        fprice = findViewById(R.id.totaltext)
        paybtn = findViewById(R.id.idBtnPay)
        cb1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (cb1.isChecked()) {
                val price = pr1.getText().toString().toInt()
                famount = famount + price
                fprice.setText("Total : ₹$famount")
                myIdArr.add("b1")
            } else {
                val price = pr1.getText().toString().toInt()
                famount = famount - price
                fprice.setText("Total : ₹$famount")
                myIdArr.remove("b1")
            }
        })
        cb2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (cb2.isChecked()) {
                val price = pr2.getText().toString().toInt()
                famount = famount + price
                fprice.setText("Total : ₹$famount")
                myIdArr.add("b2")
            } else {
                val price = pr2.getText().toString().toInt()
                famount = famount - price
                fprice.setText("Total : ₹$famount")
                myIdArr.remove("b2")
            }
        })
        cb3.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (cb3.isChecked()) {
                val price = pr3.getText().toString().toInt()
                famount = famount + price
                fprice.setText("Total : ₹$famount")
                myIdArr.add("b3")
            } else {
                val price = pr3.getText().toString().toInt()
                famount = famount - price
                fprice.setText("Total : ₹$famount")
                myIdArr.remove("b3")
            }
        })
        cb4.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (cb4.isChecked()) {
                val price = pr4.getText().toString().toInt()
                famount = famount + price
                fprice.setText("Total : ₹$famount")
                myIdArr.add("b4")
            } else {
                val price = pr4.getText().toString().toInt()
                famount = famount - price
                fprice.setText("Total : ₹$famount")
                myIdArr.remove("b4")
            }
        })
        cb5.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (cb5.isChecked()) {
                val price = pr5.getText().toString().toInt()
                famount = famount + price
                fprice.setText("Total : ₹$famount")
                myIdArr.add("b5")
            } else {
                val price = pr4.getText().toString().toInt()
                famount = famount - price
                fprice.setText("Total : ₹$famount")
                myIdArr.remove("b5")
            }
        })
        paybtn.setOnClickListener(View.OnClickListener {
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
                    "MSBTE Solution App is an aplication that provides study materials and resources for students preparing for MSBTE diploma exams"
                )
                `object`.put("theme.color", "")
                //                    object.put("currency", "INR");
//                    object.put("receipt", "receipt#1");
                `object`.put("payment_capture", 1)
                `object`.put("amount", amount)
                `object`.put("prefill.contact", "9999999999")
                `object`.put("prefill.email", "onkardokhe1234@gmail.com")
                //                    JSONObject notes = new JSONObject();
//                    notes.put("notes_key_1", "MSBTE Solution App");
//                    notes.put("notes_key_2", "By PhonixDev");
//                    object.put("notes", notes);
                checkout.open(this@DumpsActivity, `object`)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
        if (isButtonVisible1) {
            b1.setVisibility(View.VISIBLE)
        } else {
            b1.setVisibility(View.GONE)
        }
        if (isButtonVisible2) {
            b2.setVisibility(View.VISIBLE)
        } else {
            b2.setVisibility(View.GONE)
        }
        if (isButtonVisible3) {
            b3.setVisibility(View.VISIBLE)
        } else {
            b3.setVisibility(View.GONE)
        }
        if (isButtonVisible4) {
            b4.setVisibility(View.VISIBLE)
        } else {
            b4.setVisibility(View.GONE)
        }
        if (isButtonVisible5) {
            b5.setVisibility(View.VISIBLE)
        } else {
            b5.setVisibility(View.GONE)
        }
        b1.setOnClickListener(View.OnClickListener {
            intenttransf = Intent(applicationContext, DumpsPDFActivity::class.java)
            eurl =
                "https://drive.google.com/drive/folders/1MNQiaxSRxS22r4Sl8yyGhCD5faE00gyX?usp=sharing"
            intenttransf!!.putExtra("url", eurl)
            startActivity(intenttransf)
        })
        b2.setOnClickListener(View.OnClickListener {
            intenttransf = Intent(applicationContext, DumpsPDFActivity::class.java)
            eurl =
                "https://drive.google.com/drive/folders/1Gm4HLCWty7KjypPB14Gm9-76bwhhxo5M?usp=sharing"
            intenttransf!!.putExtra("url", eurl)
            startActivity(intenttransf)
        })
        b3.setOnClickListener(View.OnClickListener {
            intenttransf = Intent(applicationContext, DumpsPDFActivity::class.java)
            eurl =
                "https://drive.google.com/drive/folders/1oqcuqxB8ze-1QaYy3ULONnYm2OpY1aX3?usp=sharing"
            intenttransf!!.putExtra("url", eurl)
            startActivity(intenttransf)
        })
        b4.setOnClickListener(View.OnClickListener {
            intenttransf = Intent(applicationContext, DumpsPDFActivity::class.java)
            eurl =
                "https://drive.google.com/drive/folders/1vp3IyCQSb3uvOmiw4IAhUwsJlrUtQIQV?usp=sharing"
            intenttransf!!.putExtra("url", eurl)
            startActivity(intenttransf)
        })
        b5.setOnClickListener(View.OnClickListener {
            intenttransf = Intent(applicationContext, DumpsPDFActivity::class.java)
            eurl =
                "https://drive.google.com/drive/folders/10iEgyiS8qdGvMst8lidvwybCZNpmDqJg?usp=sharing"
            intenttransf!!.putExtra("url", eurl)
            startActivity(intenttransf)
        })
    }

    override fun onPaymentSuccess(s: String) {
        for (element in myIdArr) {
            if (element.equals("b1", ignoreCase = true)) {
                b1!!.visibility = View.VISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("b1", true)
                editor.apply()
            }
            if (element.equals("b2", ignoreCase = true)) {
                b2!!.visibility = View.VISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("b2", true)
                editor.apply()
            }
            if (element.equals("b3", ignoreCase = true)) {
                b3!!.visibility = View.VISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("b3", true)
                editor.apply()
            }
            if (element.equals("b4", ignoreCase = true)) {
                b4!!.visibility = View.VISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("b4", true)
                editor.apply()
            }
            if (element.equals("b5", ignoreCase = true)) {
                b5!!.visibility = View.VISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("b5", true)
                editor.apply()
            }
        }
        pstatus!!.text = "Payment Successfully. Transaction No :$s"
        fprice!!.text = "0.00"
        cb1!!.isChecked = false
        cb2!!.isChecked = false
        cb3!!.isChecked = false
        cb4!!.isChecked = false
        cb5!!.isChecked = false
    }

    override fun onPaymentError(i: Int, s: String) {
        pstatus!!.text = "Something went wrong$s"
        fprice!!.text = "0.00"
        cb1!!.isChecked = false
        cb2!!.isChecked = false
        cb3!!.isChecked = false
        cb4!!.isChecked = false
        cb5!!.isChecked = false
    }
}