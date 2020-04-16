package com.fmway.operations.commonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fmway.R;
import com.fmway.operations.driver.DriverActivity;
import com.fmway.operations.passenger.PassengerActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class Payment extends AppCompatActivity {
    Double currentbalance;
    ListView listView;
    EditText balance;
    EditText cardNo;
    EditText holderName;
    EditText cvc;
    Button pay;
    Double payBalance;
    String userID;
    TextView currentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        balance = findViewById(R.id.balance);
        cardNo = findViewById(R.id.cardNo);
        holderName = findViewById(R.id.holderName);
        cvc = findViewById(R.id.cvc);
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
        currentBalance = findViewById(R.id.currentBalance);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            userID = (String) b.get("userID");


        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(userID, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    currentbalance = object.getDouble("balance");
                    currentBalance.setText((String.valueOf(currentbalance)));

                }

            }
        });
    }

    public void pay() {
        if (balance.getText().toString().equals("")|| cardNo.getText().toString().equals("") || holderName.getText().toString().equals("") ||
                cvc.getText().toString().equals("") ) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        } else if(Double.parseDouble(balance.getText().toString())>500){
            Toast.makeText(this, "You cannot deposit more than 500TL in one try!", Toast.LENGTH_SHORT).show();
        }
        else if(cardNo.getText().toString().length()!=16){
            Toast.makeText(this, "Please enter the card number correctly!", Toast.LENGTH_SHORT).show();
        }
       else  if(cvc.getText().toString().length()!=3){
            Toast.makeText(this, "Please enter the CVC correctly!", Toast.LENGTH_SHORT).show();
        }




            else {
            payBalance = Double.parseDouble(String.valueOf(balance.getText()));


            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(userID, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        currentbalance = payBalance + currentbalance;
                        object.put("balance", currentbalance);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Payment successful", Toast.LENGTH_LONG).show();

                                    ParseUser usr = ParseUser.getCurrentUser();

                                    String usrType = usr.getString("userType");

                                    if (usrType.equals("driver")) {
                                        Intent intent = new Intent(getApplicationContext(), DriverActivity.class);
                                        intent.putExtra("userID", userID);
                                        startActivity(intent);
                                    } else if (usrType.equals("passenger")) {
                                        Intent intent = new Intent(getApplicationContext(), PassengerActivity.class);
                                        intent.putExtra("userID", userID);
                                        startActivity(intent);
                                    }


                                }
                            }
                        });


                    }
                }
            });
        }
    }
}





