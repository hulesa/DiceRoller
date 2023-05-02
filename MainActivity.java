package com.example.swipingtest;

import static android.view.View.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    TextView currentTxt, dicesTxt, rollsTxt;
    Button up, down, roll, tutorial;

    Dice[] dices;

    public int[] diceTypes;
    public Dice current;
    public int result, currentIndex;

    String backupRollsTxt;
    int backupCurrentIndex, backupResult;

    @SuppressLint("SetTextI18n")
    public void DisplayDices() {
        dicesTxt.setText("Your dices: ");
        for (Dice d : dices) {
            if (d.amount != 0) {
                dicesTxt.append(d.amount + "d" + d.value + " ");
            }
        }
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diceTypes = new int[]{4, 6, 8, 10, 12, 20, 100};
        dices = new Dice[diceTypes.length];

        backupRollsTxt = "You rolled: ";
        backupCurrentIndex = 5;
        backupResult = 0;

        for (int i = 0; i < diceTypes.length; i++) {
            dices[i] = new Dice(diceTypes[i]);
        }

        currentIndex = 5;
        current = dices[currentIndex];
        result = 0;
        relativeLayout = findViewById(R.id.relative_layout);
        dicesTxt = findViewById(R.id.dices);
        rollsTxt = findViewById(R.id.rolls);
        currentTxt = findViewById(R.id.TVcurrentDice);

        currentTxt.setText("d" + current.value);
        rollsTxt.setText("You rolled: ");
        Random r = new Random();

        up = findViewById(R.id.up);
        up.setOnClickListener(v -> {
            current.amount++;
            DisplayDices();
        });

        down = findViewById(R.id.down);
        down.setOnClickListener(v -> {
            current.amount--;
            DisplayDices();
        });

        roll = findViewById(R.id.roll);
        roll.setOnClickListener(v -> {
            rollsTxt.setText("You rolled: ");
            result = 0;
            int rolled = 0;
            for (Dice d : dices) {
                int sign = 1;
                if (d.amount < 0) sign = -1;
                for (int i = 0; i < Math.abs(d.amount); i++) {
                    rolled = sign * (r.nextInt(d.value) + 1);
                    result += rolled;
                    rollsTxt.append(rolled + ", ");
                }
            }
            if(rolled != 0) rollsTxt.setText(rollsTxt.getText().subSequence(0,rollsTxt.getText().length() - 2));
            rollsTxt.append("\nTotal: " + result);
        });

        tutorial = findViewById(R.id.tutorial);
        tutorial.setOnClickListener(v -> {
            Intent i = new Intent(this, com.example.swipingtest.tutorial.class);
            startActivity(i);
        });

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight() {
                currentIndex--;
                if (currentIndex < 0){currentIndex = dices.length - 1;}
                current = dices[currentIndex];
                currentTxt.setText("d" + current.value);
            }

            public void onSwipeLeft() {
                currentIndex++;
                if (currentIndex == dices.length) {currentIndex = 0;}
                current = dices[currentIndex];
                currentTxt.setText("d" + current.value);
            }

            public void onSwipeBottom() {
                for (Dice value : dices) {
                    value.backupAmount = value.amount;
                }
                backupRollsTxt = rollsTxt.getText().toString();
                backupCurrentIndex = currentIndex;
                backupResult = result;

                for (Dice dice : dices) {dice.amount = 0;}
                currentIndex = 5;
                current = dices[currentIndex];
                result = 0;
                currentTxt.setText("d" + current.value);
                rollsTxt.setText("You rolled: ");
                DisplayDices();
            }

            public void onSwipeTop() {
                for (Dice dice : dices) {
                    dice.amount = dice.backupAmount;
                }
                currentIndex = backupCurrentIndex;
                current = dices[currentIndex];
                currentTxt.setText("d" + current.value);
                result = backupResult;
                rollsTxt.setText(backupRollsTxt);
                DisplayDices();
            }

        });
    }


    static class OnSwipeTouchListener implements OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 50;
            private static final int SWIPE_VELOCITY_THRESHOLD = 50;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }

}