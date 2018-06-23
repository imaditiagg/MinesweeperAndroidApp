package com.example.aditi.minesweeper2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    EditText editText1;
    Button button1,button2,button3;
   public static  final String M = "No_of_rows";
   public static  final String N= "No_of_cols";
   public static final String NAME ="Name";
   int m=0,n=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editText1 =findViewById(R.id.name);
        button1 =findViewById(R.id.easy);
        button2=findViewById(R.id.medium);
        button3=findViewById(R.id.diff);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

    }


    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.easy) {
            m=8;
            n=8;
        }
        if(id == R.id.medium) {
            m=11;
            n=11;
        }
        if(id == R.id.diff) {
            m=13;
            n=13;
        }
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(M,m);
        intent.putExtra(N,n);
        intent.putExtra(NAME,editText1.getText().toString());
        startActivity(intent);

        }

    }

