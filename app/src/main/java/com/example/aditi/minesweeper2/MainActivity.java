package com.example.aditi.minesweeper2;

import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private int currentStatus;
    public static final int INCOMPLETE=1;
    public static final int PLAYER_WON=2;
    public static final int LOST=3;
    private int textSize;

    ImageButton ImgButton;
    TextView tv,timerview;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillisec= 600000;
    boolean timerRunning;


    private LinearLayout rootLayout;
    private int m;//number of rows
    private int n; //number of columns
    private ArrayList<LinearLayout> rows;
    private MineButton[][] board;
    private boolean areMinesSet = false;
    private int rowClicked;
    private int colClicked;
    private int noOfMines=0;
    private int[] x = {-1, -1, -1, 0, 0, +1, +1, +1};
    private int[] y = {-1, 0, +1, -1, +1, -1, 0, +1};
    static int count = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.linearLayout);


        setUpBoard();

    }

    public void setUpBoard() {
        currentStatus=INCOMPLETE;

        Intent intent =getIntent();

        m = intent.getIntExtra(MainActivity2.M,0);
        n = intent.getIntExtra(MainActivity2.N,0);


        rows = new ArrayList<>();
        board = new MineButton[m][n];
        rootLayout.removeAllViews();
        LinearLayout ll =new LinearLayout(this);
        ImgButton = new ImageButton(this);
        ImgButton.setBackground(getResources().getDrawable(R.drawable.smile));
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(200, 120);

        ImgButton.setLayoutParams(l);
        ImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areMinesSet=false;
                setUpBoard();

            }
        }); //reset


        tv=new TextView(this);
        tv.setText(" GoodLuck " +intent.getStringExtra(MainActivity2.NAME));
        LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(430, 120);

        tv.setLayoutParams(l2);
        tv.setTextSize(15);
        ll.addView(tv);
        ll.addView(ImgButton);

        timerview =new TextView(this);


        rootLayout.addView(ll);

        for (int i = 0; i < m; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL); //though by default it's horizontal
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            linearLayout.setLayoutParams(layoutParams);
            rows.add(linearLayout);
            rootLayout.addView(linearLayout);
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                MineButton button = new MineButton(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                button.setLayoutParams(layoutParams);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);

                LinearLayout row = rows.get(i);
                row.addView(button); //add button in ith row

                board[i][j] = button;
                button.setter(0,i,j);


            }
        }


    }


    public void onClick(View view) {
        if(currentStatus==INCOMPLETE) {
            MineButton button = (MineButton) view;
            rowClicked = button.getRow();
            colClicked = button.getCol();

            if (!areMinesSet) { //if mines are not set i.e it's a first click
                setMines(rowClicked, colClicked);
                areMinesSet = true;
             //    revealall();
             //   Toast.makeText(MainActivity.this, "mines are set", Toast.LENGTH_SHORT).show();
            }
            uncover(rowClicked, colClicked);
            checkGameStatus();
        }
    }

    public void setMines(int curRow,int curCol) {
        if(m==8 && n==8) {
            noOfMines = 10;
            textSize=45;
        }
        else if(m==11&& n==11) {
            noOfMines = 15;
            textSize=42;
        }
        else if(m==13 &&n==13) {
            noOfMines = 20;
            textSize=30;
        }
        Random rand = new Random();
        int randCol, randRow;

        int count = 0;
        // don't set mine on currrow, currcol and it's neighbours
        for(int i=0;i<8;i++)
        {
            int a = curRow+x[i];
            int b=curCol+y[i];
            if(a>=0 && a<m && b>0 && b<n) {
                MineButton btn = board[a][b];
                btn.can_set_mines = false;
            }

        }

        while (count < noOfMines) {
            randRow = rand.nextInt(m - 1);
            randCol = rand.nextInt(n - 1);
            if(randRow != curRow || randCol != curCol) {
            MineButton button = board[randRow][randCol];
                if (!button.hasMine() && button.can_set_mines) {
                    button.value= -1;
                    count++;//mineset

                }
            }
            }


        //set neighbours
        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {
                MineButton button = board[i][j];

                if (button.value == -1) {

                    for (int k = 0; k < 8; k++) {
                        if (i + x[k] >= 0 && i + x[k] < m && j + y[k] >= 0 && j + y[k] < n) {
                            MineButton button1 = board[i + x[k]][j + y[k]];
                            if (!button1.hasMine()) {

                                int n1 = button1.value;
                                n1++;
                                button1.value = n1;
                            }

                        }

                    }

                }
            }
        }
    }



    public void revealall()
    {
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                MineButton button =board[i][j];
                button.setText(String.valueOf(button.value));

            }
        }



    }

    public boolean onLongClick(View view) {
        if(currentStatus==INCOMPLETE) {
            MineButton button = (MineButton) view;
           // Toast.makeText(MainActivity.this, "long click", Toast.LENGTH_SHORT).show();
            button.setText("F");
            button.flagged = true;
            return true;
        }
        return false;
    }


    public void uncover(int row, int col) {

        MineButton button = board[row][col];

        //if it has mine
        if (button.value == -1) {
            button.setText("M");
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize-10);
            currentStatus=LOST;
            button.setBackgroundColor(getResources().getColor(R.color.red));
            Toast.makeText(this, "Oops Game over.. You lost !!", Toast.LENGTH_SHORT).show();
        }
        //if it is numbered block
        else if (button.value > 0) {
            button.setBackgroundColor(getResources().getColor(R.color.mycolor2));
            button.setText(String.valueOf(button.value));
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            button.setEnabled(false);
            button.reveal = true;
            if(button.value==1)
                button.setTextColor(getResources().getColor(R.color.red));
            else if(button.value==2)
                button.setTextColor(getResources().getColor(R.color.blue));
            else if(button.value==3)
                button.setTextColor(getResources().getColor(R.color.green));
            else if(button.value==4)
                button.setTextColor(getResources().getColor(R.color.purple));

        }
        //if it is blank
        else if (button.value == 0) {
            //uncover
           // Toast.makeText(this, "Blank Button clicked", Toast.LENGTH_SHORT).show();
            button.reveal=true;
            button.setBackgroundColor(getResources().getColor(R.color.mycolor2));
            button.setEnabled(false);
            for (int i = 0; i < 8; i++) {
                if ((row + x[i] >= 0 && row + x[i] < m && col + y[i] >= 0 && col + y[i] < n)) {
                    MineButton btn = board[row + x[i]][col + y[i]];
                    if (btn.value != -1 && btn.reveal == false && btn.flagged==false){//if it has no mine and not revealed yet
                        btn.setBackgroundColor(getResources().getColor(R.color.mycolor2));
                        btn.setEnabled(false);
                        uncover2(row + x[i], col + y[i]);
                }}
            }

        }

    }

    public void uncover2(int row, int col) {

        MineButton button = board[row][col];
        if((button.reveal ==false) && (button.flagged==false)) {
            if (button.value > 0) {
                button.setText(String.valueOf(button.value));
                button.setBackgroundColor(getResources().getColor(R.color.mycolor2));
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                button.setEnabled(false);
                button.reveal = true;
                if(button.value==1)
                    button.setTextColor(getResources().getColor(R.color.red));
                else if(button.value==2)
                    button.setTextColor(getResources().getColor(R.color.blue));
                else if(button.value==3)
                    button.setTextColor(getResources().getColor(R.color.green));
                else if(button.value==4)
                    button.setTextColor(getResources().getColor(R.color.purple));
                return;
            } else {

                button.reveal = true;
                for (int i = 0; i < 8; i++) {
                    if ((row + x[i] >= 0 && row + x[i] < m && col + y[i] >= 0 && col + y[i] < n)) {
                        MineButton btn = board[row + x[i]][col + y[i]];
                        if (btn.value != -1 && btn.reveal == false && btn.flagged==false) {
                            btn.setBackgroundColor(getResources().getColor(R.color.mycolor2));
                            btn.setEnabled(false);
                            uncover2(row + x[i], col + y[i]);


                        }
                    }
                }
            }
        }}


        public void checkGameStatus(){
        for(int i=0;i<m;i++){
            for (int j=0;j<n;j++){
                if(board[i][j].value>0 && board[i][j].reveal==false)
                    return;
            }
        }

        //won
            currentStatus=PLAYER_WON;
            Toast.makeText(this,"Congrats..You Won !! ",Toast.LENGTH_SHORT).show();
        }

    }




