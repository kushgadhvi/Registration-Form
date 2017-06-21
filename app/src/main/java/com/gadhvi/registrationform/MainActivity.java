package com.gadhvi.registrationform;

import android.app.DatePickerDialog;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText DOB,mAge,mName;
    private Button submit,date_select;
    private Spinner s1;
    private int mYear, mMonth, mDay;
    private DatabaseReference mDatabase;
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DOB=(EditText)findViewById(R.id.date);
        mAge=(EditText)findViewById(R.id.age);
        mName=(EditText)findViewById(R.id.name);
        s1=(Spinner)findViewById(R.id.tech);
        mDatabase =FirebaseDatabase.getInstance().getReference().child("Form");
        submit=(Button)findViewById(R.id.done);
        date_select=(Button)findViewById(R.id.selectdate);
        loading = new ProgressDialog(this);


        date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              final  Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth= c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DATE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view,int year,
                                                  int monthOfYear, int dayOfMonth) {

                                DOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date =DOB.getText().toString().trim();
                String name = mName.getText().toString().trim();
                String age =mAge.getText().toString().trim();
                String tech =s1.getSelectedItem().toString();
                if(!TextUtils.isEmpty(date) &&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(age))
                {
                    loading.setMessage("Uploading to Firebase Database...");
                    loading.show();
                   register upload =new register(name,date,age,tech);
                    mDatabase.push().setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DOB.setText("");
                            mName.setText("");
                            mAge.setText("");
                            loading.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Please Try Again..",Toast.LENGTH_LONG).show();
                        }
                    });




                }
                else {

                    Toast.makeText(MainActivity.this,"Please Fill all the Filed",Toast.LENGTH_LONG).show();
                }
            }

        });


    }
}
