package com.example.nitantsood.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by NITANT SOOD on 16-06-2017.
 */

public class MyButton extends Button {
    public int mineUnder=0;
    public int clicked=0;
    public int weight=0;
    public int flag=0;
    public int rowNumber;
    public int colNumber;
    public MyButton(Context context) {
        super(context);
    }
}
