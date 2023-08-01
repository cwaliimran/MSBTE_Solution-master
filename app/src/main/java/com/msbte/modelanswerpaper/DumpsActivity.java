package com.msbte.modelanswerpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DumpsActivity extends AppCompatActivity implements PaymentResultListener {

    CheckBox cb1, cb2, cb3, cb4,cb5;
    Button b1, b2, b3, b4, b5;
    TextView pr1, pr2, pr3, pr4, pr5, fprice, pstatus;
    Button paybtn;
    int famount = 0;

    ArrayList<String> myIdArr = new ArrayList<String>();

    private SharedPreferences sharedPreferences;

    Intent intenttransf;
    String eurl;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumps);



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        // Initialize SharedPreferences object
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Get the visibility status from SharedPreferences
        boolean isButtonVisible1 = sharedPreferences.getBoolean("b1", false);
        boolean isButtonVisible2 = sharedPreferences.getBoolean("b2", false);
        boolean isButtonVisible3 = sharedPreferences.getBoolean("b3", false);
        boolean isButtonVisible4 = sharedPreferences.getBoolean("b4", false);
        boolean isButtonVisible5 = sharedPreferences.getBoolean("b5", false);


        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);


        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);

        pr1 = findViewById(R.id.price1);
        pr2 = findViewById(R.id.price2);
        pr3 = findViewById(R.id.price3);
        pr4 = findViewById(R.id.price4);
        pr5 = findViewById(R.id.price5);

        pstatus = findViewById(R.id.pstatus);
        fprice = findViewById(R.id.totaltext);
        paybtn = findViewById(R.id.idBtnPay);


        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb1.isChecked()) {
                    int price = Integer.parseInt(pr1.getText().toString());
                    famount = famount + price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.add("b1");
                } else {
                    int price = Integer.parseInt(pr1.getText().toString());
                    famount = famount - price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.remove("b1");
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb2.isChecked()) {
                    int price = Integer.parseInt(pr2.getText().toString());
                    famount = famount + price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.add("b2");
                } else {
                    int price = Integer.parseInt(pr2.getText().toString());
                    famount = famount - price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.remove("b2");
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb3.isChecked()) {
                    int price = Integer.parseInt(pr3.getText().toString());
                    famount = famount + price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.add("b3");

                } else {
                    int price = Integer.parseInt(pr3.getText().toString());
                    famount = famount - price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.remove("b3");
                }
            }
        });

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb4.isChecked()) {
                    int price = Integer.parseInt(pr4.getText().toString());
                    famount = famount + price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.add("b4");
                } else {
                    int price = Integer.parseInt(pr4.getText().toString());
                    famount = famount - price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.remove("b4");
                }
            }
        });

        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb5.isChecked()) {
                    int price = Integer.parseInt(pr5.getText().toString());
                    famount = famount + price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.add("b5");
                } else {
                    int price = Integer.parseInt(pr4.getText().toString());
                    famount = famount - price;
                    fprice.setText("Total : ₹" + famount);
                    myIdArr.remove("b5");
                }
            }
        });

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String samount = String.valueOf(famount);
                int amount = Math.round(Float.parseFloat(samount) * 100);

                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_live_SzNU5aszBMzHu5");   // change key here if you want
                checkout.setImage(R.mipmap.ic_launcher_round);


                JSONObject object = new JSONObject();
                try {
                    object.put("name", "MSBTE Solution APP");
                    object.put("description", "MSBTE Solution App is an aplication that provides study materials and resources for students preparing for MSBTE diploma exams");
                    object.put("theme.color", "");
//                    object.put("currency", "INR");
//                    object.put("receipt", "receipt#1");
                    object.put("payment_capture", 1);
                    object.put("amount", amount);
                    object.put("prefill.contact", "9999999999");
                    object.put("prefill.email", "onkardokhe1234@gmail.com");
//                    JSONObject notes = new JSONObject();
//                    notes.put("notes_key_1", "MSBTE Solution App");
//                    notes.put("notes_key_2", "By PhonixDev");
//                    object.put("notes", notes);
                    checkout.open(DumpsActivity.this, object);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (isButtonVisible1) {
            b1.setVisibility(View.VISIBLE);
        } else {
            b1.setVisibility(View.GONE);
        }

        if (isButtonVisible2) {
            b2.setVisibility(View.VISIBLE);
        } else {
            b2.setVisibility(View.GONE);
        }

        if (isButtonVisible3) {
            b3.setVisibility(View.VISIBLE);
        } else {
            b3.setVisibility(View.GONE);
        }

        if (isButtonVisible4) {
            b4.setVisibility(View.VISIBLE);
        } else {
            b4.setVisibility(View.GONE);
        }

        if (isButtonVisible5) {
            b5.setVisibility(View.VISIBLE);
        } else {
            b5.setVisibility(View.GONE);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intenttransf = new Intent(getApplicationContext(), DumpsPDFActivity.class);
                eurl = "https://drive.google.com/drive/folders/1MNQiaxSRxS22r4Sl8yyGhCD5faE00gyX?usp=sharing";
                intenttransf.putExtra("url", eurl);
                startActivity(intenttransf);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intenttransf = new Intent(getApplicationContext(), DumpsPDFActivity.class);
                eurl = "https://drive.google.com/drive/folders/1Gm4HLCWty7KjypPB14Gm9-76bwhhxo5M?usp=sharing";
                intenttransf.putExtra("url", eurl);
                startActivity(intenttransf);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intenttransf = new Intent(getApplicationContext(), DumpsPDFActivity.class);
                eurl = "https://drive.google.com/drive/folders/1oqcuqxB8ze-1QaYy3ULONnYm2OpY1aX3?usp=sharing";
                intenttransf.putExtra("url", eurl);
                startActivity(intenttransf);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intenttransf = new Intent(getApplicationContext(), DumpsPDFActivity.class);
                eurl = "https://drive.google.com/drive/folders/1vp3IyCQSb3uvOmiw4IAhUwsJlrUtQIQV?usp=sharing";
                intenttransf.putExtra("url", eurl);
                startActivity(intenttransf);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intenttransf = new Intent(getApplicationContext(), DumpsPDFActivity.class);
                eurl = "https://drive.google.com/drive/folders/10iEgyiS8qdGvMst8lidvwybCZNpmDqJg?usp=sharing";
                intenttransf.putExtra("url", eurl);
                startActivity(intenttransf);
            }
        });


    }


    @Override
    public void onPaymentSuccess(String s) {

        for (String element : myIdArr) {
            if (element.equalsIgnoreCase("b1")) {
                b1.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("b1", true);
                editor.apply();
            }
            if (element.equalsIgnoreCase("b2")) {
                b2.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("b2", true);
                editor.apply();
            }
            if (element.equalsIgnoreCase("b3")) {
                b3.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("b3", true);
                editor.apply();
            }
            if (element.equalsIgnoreCase("b4")) {
                b4.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("b4", true);
                editor.apply();
            }

            if (element.equalsIgnoreCase("b5")) {
                b5.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("b5", true);
                editor.apply();
            }
        }


        pstatus.setText("Payment Successfully. Transaction No :" + s);
        fprice.setText("0.00");
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        cb5.setChecked(false);

    }


    @Override
    public void onPaymentError(int i, String s) {
        pstatus.setText("Something went wrong" + s);
        fprice.setText("0.00");
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        cb5.setChecked(false);
    }

}