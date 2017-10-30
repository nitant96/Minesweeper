package com.example.nitantsood.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.color.holo_red_dark;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    Button on_off;
    MyButton buttons[][];
    LinearLayout mainLayout;
    LinearLayout rowLayout[];
    LinearLayout upperRow;
    public int mineAccountRow[];
    public int mineAccountCol[];
    public static int FIRST_CLICK = 0;
    public static int FIRST_row,FIRST_col;
    public static int n;
    public static int NUMBER_OF_FLAGS=0;
    TextView T[] = new TextView[2];
    public int noOfMines=0;
    MyButton firstClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
    }
    public boolean onCreateOptionsMenu(Menu  menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        resetAll();
        if(id==R.id.Easy){
            n=8;
        }
        else if(id==R.id.Medium){
            n=10;
        }
        else
            n=12;
        setUpBoard();
        return true;
    }
    //level is selected according to pressand then sets up board
    public void level(View v) {
        if (v.getId() == R.id.easybutton) {
            n = 8;
        } else if (v.getId() == R.id.mediumbutton) {
            n = 10;
        } else if (v.getId() == R.id.hardbutton) {
            n = 12;
        }
        setUpBoard();
    }
    //to reset all things
    void resetAll(){
        on_off=null;
        buttons=null;
        rowLayout=null;
        upperRow=null;
        mineAccountCol=null;
        mineAccountRow=null;
        FIRST_CLICK=0;
        n=0;
        FIRST_col=0;
        FIRST_row=0;
        firstClick=null;
        NUMBER_OF_FLAGS=0;
        noOfMines=0;
        n=0;
    }
    //sets up the board according to level
    public void setUpBoard() {
        rowLayout = new LinearLayout[n];
        buttons = new MyButton[n][n];
        mainLayout.removeAllViews();
        int color=Color.parseColor("#00000000");
        mainLayout.setBackgroundColor(color);
        upperRow = new LinearLayout(this);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1);
        upperRow.setLayoutParams(param1);
        upperRow.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < 2; i++) {
            T[i] = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            params.setMargins(5, 5, 5, 5);
            T[i].setLayoutParams(params);
            T[i].setTextSize(50);
        }
        upperRow.addView(T[0]);
        on_off = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(5, 5, 5, 5);
        on_off.setLayoutParams(params);
        on_off.setText("Check");
        on_off.setId(R.id.bun);
        on_off.setOnClickListener(this);
        on_off.setBackground(ContextCompat.getDrawable(this,R.drawable.reset_button));
        upperRow.addView(on_off);
        upperRow.addView(T[1]);
        upperRow.setBackgroundColor(color);
        mainLayout.addView(upperRow);
        int textColor=Color.parseColor("#ffcc0000");
        T[0].setTextColor(textColor);
        T[1].setTextColor(textColor);
        T[1].setGravity(Gravity.RIGHT);
        T[0].setText("0");
        T[1].setText("0");
        for (int i = 0; i < n; i++) {
            rowLayout[i] = new LinearLayout(this);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            //params.setMargins(5,5,5,5);
            rowLayout[i].setLayoutParams(param2);
            rowLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rowLayout[i]);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new MyButton(this);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                //params.setMargins(5,5,5,5);
                buttons[i][j].setLayoutParams(param2);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                buttons[i][j].setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle));
                rowLayout[i].addView(buttons[i][j]);
            }
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                buttons[i][j].rowNumber=i;
                buttons[i][j].colNumber=j;
            }
        }
    }
    //checker to make sure first click is having weight zero
    public boolean checkStart(int row,int col){
        if(row==FIRST_row+1 && col==FIRST_col+1)
            return true;
        else if(row==FIRST_row && col==FIRST_col+1)
            return true;
        else if(row==FIRST_row && col==FIRST_col-1)
            return true;
        else if(row==FIRST_row+1 && col==FIRST_col-1)
            return true;
        else if(row==FIRST_row+1 && col==FIRST_col)
            return true;
        else if(row==FIRST_row-1 && col==FIRST_col-1)
            return true;
        else if(row==FIRST_row-1 && col==FIRST_col)
            return true;
        else if(row==FIRST_row-1 && col==FIRST_col+1)
            return true;
        else
            return false;
    }
    //setup mines after first click
    public void setupMines(int level) {
        int checker=0;
        if (level == 8) {
            noOfMines = 16;
        } else if (level == 10) {
            noOfMines = 25;
        } else
            noOfMines = 36;
        T[0].setText(noOfMines+"");
        mineAccountCol = new int[noOfMines];
        mineAccountRow = new int[noOfMines];
        for (int i = 0; i < noOfMines; i++) {       //loop for randomly placing mines
            int row, col;
            row = (int) (Math.random() * (level-1));
            col = (int) (Math.random() * (level-1));
            for (int j = 0; j<i; j++) {           //loop for checking mines
                if ((mineAccountCol[j] == col && mineAccountRow[j] == row)|| buttons[row][col].clicked==1 || checkStart(row,col)) {
                    checker=1;
                    break;
                }
            }
            if(checker==0 && buttons[row][col].clicked!=1){
                mineAccountRow[i]=row;
                mineAccountCol[i]=col;
                buttons[row][col].mineUnder=1;
            }
            else{
                i--;
            }
            checker=0;
        }
        assignWeights();
        return;
    }
    //assign weights to all the buttons according to placed mines
    void assignWeights(){
        for(int i=0;i<noOfMines;i++){
            int row=mineAccountRow[i];
            int col=mineAccountCol[i];
            if(row==0){
                if(col==0){
                    buttons[row][col+1].weight++;
                    buttons[row+1][col+1].weight++;
                    buttons[row+1][col].weight++;
                }
                else if(col==n-1){
                    buttons[row][col-1].weight++;
                    buttons[row+1][col-1].weight++;
                    buttons[row+1][col].weight++;
                }
                else{
                    buttons[row][col-1].weight++;
                    buttons[row+1][col-1].weight++;
                    buttons[row+1][col].weight++;
                    buttons[row][col+1].weight++;
                    buttons[row+1][col+1].weight++;
                }
            }
            else if(row==n-1){
                if(col==0){
                    buttons[row][col+1].weight++;
                    buttons[row-1][col+1].weight++;
                    buttons[row-1][col].weight++;
                }
                else if(col==n-1){
                    buttons[row][col-1].weight++;
                    buttons[row-1][col-1].weight++;
                    buttons[row-1][col].weight++;
                }
                else{
                    buttons[row][col-1].weight++;
                    buttons[row-1][col-1].weight++;
                    buttons[row-1][col].weight++;
                    buttons[row][col+1].weight++;
                    buttons[row-1][col+1].weight++;
                }
            }
            else if(col==0){
                buttons[row][col+1].weight++;
                buttons[row+1][col].weight++;
                buttons[row+1][col+1].weight++;
                buttons[row-1][col].weight++;
                buttons[row-1][col+1].weight++;
            }
            else if(col==n-1){
                buttons[row][col-1].weight++;
                buttons[row+1][col].weight++;
                buttons[row+1][col-1].weight++;
                buttons[row-1][col].weight++;
                buttons[row-1][col-1].weight++;
            }
            else{
                buttons[row][col-1].weight++;
                buttons[row+1][col].weight++;
                buttons[row+1][col-1].weight++;
                buttons[row-1][col].weight++;
                buttons[row-1][col-1].weight++;
                buttons[row][col+1].weight++;
                buttons[row+1][col+1].weight++;
                buttons[row-1][col+1].weight++;
            }
        }
        return;
    }
    //display the weight when a click happens
    void showWeight(MyButton clickedButton,int row,int col){
        if(clickedButton.clicked==0) {
            clickedButton.setEnabled(false);
            clickedButton.clicked = 1;
            clickedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.clicked_button));
            if (clickedButton.weight == 0) {
                clickedButton.setText("");                          //here should change the color too
                if (row == 0) {
                    if (col == 0) {
                        showWeight(buttons[row][col + 1], row, col + 1);
                        showWeight(buttons[row + 1][col + 1], row + 1, col + 1);
                        showWeight(buttons[row + 1][col], row + 1, col);
                    } else if (col == n - 1) {
                        showWeight(buttons[row][col - 1], row, col - 1);
                        showWeight(buttons[row + 1][col - 1], row + 1, col - 1);
                        showWeight(buttons[row + 1][col], row + 1, col);
                    } else {
                        showWeight(buttons[row][col - 1], row, col - 1);
                        showWeight(buttons[row + 1][col - 1], row + 1, col - 1);
                        showWeight(buttons[row + 1][col], row + 1, col);
                        showWeight(buttons[row][col + 1], row, col + 1);
                        showWeight(buttons[row + 1][col + 1], row + 1, col + 1);
                    }
                } else if (row == n - 1) {
                    if (col == 0) {
                        showWeight(buttons[row][col + 1], row, col + 1);
                        showWeight(buttons[row - 1][col + 1], row - 1, col + 1);
                        showWeight(buttons[row - 1][col], row - 1, col);
                    } else if (col == n - 1) {
                        showWeight(buttons[row][col - 1], row, col - 1);
                        showWeight(buttons[row - 1][col - 1], row - 1, col - 1);
                        showWeight(buttons[row - 1][col], row - 1, col);
                    } else {
                        showWeight(buttons[row][col - 1], row, col - 1);
                        showWeight(buttons[row - 1][col - 1], row - 1, col - 1);
                        showWeight(buttons[row - 1][col], row - 1, col);
                        showWeight(buttons[row][col + 1], row, col + 1);
                        showWeight(buttons[row - 1][col + 1], row - 1, col + 1);
                    }
                } else if (col == 0) {
                    showWeight(buttons[row][col + 1], row, col + 1);
                    showWeight(buttons[row + 1][col], row + 1, col);
                    showWeight(buttons[row + 1][col + 1], row + 1, col + 1);
                    showWeight(buttons[row - 1][col], row - 1, col);
                    showWeight(buttons[row - 1][col + 1], row - 1, col + 1);
                } else if (col == n - 1) {
                    showWeight(buttons[row][col - 1], row, col - 1);
                    showWeight(buttons[row + 1][col], row + 1, col);
                    showWeight(buttons[row + 1][col - 1], row + 1, col - 1);
                    showWeight(buttons[row - 1][col], row - 1, col);
                    showWeight(buttons[row - 1][col - 1], row - 1, col - 1);
                } else {
                    showWeight(buttons[row][col - 1], row, col - 1);
                    showWeight(buttons[row + 1][col], row + 1, col);
                    showWeight(buttons[row + 1][col - 1], row + 1, col - 1);
                    showWeight(buttons[row - 1][col], row - 1, col);
                    showWeight(buttons[row - 1][col - 1], row - 1, col - 1);
                    showWeight(buttons[row][col + 1], row, col + 1);
                    showWeight(buttons[row + 1][col + 1], row + 1, col + 1);
                    showWeight(buttons[row - 1][col + 1], row - 1, col + 1);
                }
            } else {
                String s = (clickedButton.weight + "");
                clickedButton.setText(s);
            }
        }
    }
    //handles click on the buttons
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bun) {
            checkGame();
        }
        else{

            if (FIRST_CLICK == 0) {
                FIRST_CLICK = 1;
                firstClick = (MyButton) v;
                FIRST_row=firstClick.rowNumber;
                FIRST_col=firstClick.colNumber;
                setupMines(n);
                showWeight(firstClick,firstClick.rowNumber,firstClick.colNumber);
            }
            else{
                MyButton clickedButton = (MyButton) v;
                if (clickedButton.mineUnder == 1) {
                    gameover(clickedButton);
                }
                else if(clickedButton.clicked==0){
                    if(clickedButton.flag==1){
                        Toast.makeText(this,"Please remove the Flag first",Toast.LENGTH_LONG).show();
                    }
                    else{
                        showWeight(clickedButton,clickedButton.rowNumber,clickedButton.colNumber);
                    }
                }
                else if(clickedButton.clicked==1){
                    Toast.makeText(this,"Mine is already opened, select another !!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //gameover when mine is detected on click
    void gameover(MyButton mineButtonClicked){
        Toast.makeText(this,"Oops, GameOver !!",Toast.LENGTH_SHORT).show();
        on_off.setEnabled(false);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                buttons[i][j].setText(buttons[i][j].weight+"");
            }
        }
        for(int i=0;i<noOfMines;i++){
            buttons[mineAccountRow[i]][mineAccountCol[i]].setText("*");
            buttons[mineAccountRow[i]][mineAccountCol[i]].setBackground(ContextCompat.getDrawable(this,R.drawable.button_mine));
            buttons[mineAccountRow[i]][mineAccountCol[i]].setEnabled(false);
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                buttons[i][j].setEnabled(false);
            }
        }
    }
    void checkGame(){
        int checker=1;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(buttons[i][j].clicked!=1 && buttons[i][j].mineUnder!=1){
                    checker=0;
                    break;
                }
            }
            if(checker==0){
                break;
            }
        }
        if(checker==0){
            Toast.makeText(this,"Some mines still not open ",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            if(noOfMines!=NUMBER_OF_FLAGS){
                Toast.makeText(this,"All mines are still not Flagged",Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Toast.makeText(this,"You did it !! Congratulations",Toast.LENGTH_SHORT).show();
                on_off.setEnabled(false);
                return;
            }
        }

    }
    //handles long click on the buttons
    @Override
    public boolean onLongClick(View v) {
        MyButton button=(MyButton) v;
        if(button.flag==0){
            T[1].setText((++NUMBER_OF_FLAGS)+"");
            button.flag=1;
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.button_flag));
            button.setText("F");
            //need  to set for  change of color
        }
        else{
            button.flag=0;
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle));
            T[1].setText((--NUMBER_OF_FLAGS)+"");
            button.setText("");
        }
        return true;
    }
}
