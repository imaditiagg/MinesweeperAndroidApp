package com.example.aditi.minesweeper2;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.Toast;

public class MineButton extends AppCompatButton{
     int rowNo;
     int colNo;
     int value=0;
     boolean reveal=false;
     boolean flagged=false;
      public MineButton(Context context) {
        super(context);
    }

    public void setter(int v,int i,int j)
    {
        value=v;
        rowNo=i;
        colNo=j;
    }

    public boolean hasMine()

    {

        if (this.value == -1) {
            return true;
        } else
            return false;

    }
    public int getRow(){
          return this.rowNo;
    }
    public int getCol(){
        return this.colNo;
    }

}
