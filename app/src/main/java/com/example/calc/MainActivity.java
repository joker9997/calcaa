package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
public class MainActivity extends AppCompatActivity {
    private TextView display;
    private String currentInput = "";
    private double firstOperand = 0;
    private String operator = "";
    private boolean isOperatorClicked = false;
    private DecimalFormat decimalFormat = new DecimalFormat("0.######");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        setNumberButtonListeners();
        setOperationButtonListeners();
        setFunctionButtonListeners();
    }
    // Set listeners for number buttons (0-9)
    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five,
                R.id.six, R.id.seven, R.id.eight, R.id.nine
        };
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                if (isOperatorClicked) {
                    currentInput = ""; // Start fresh input after operator
                    isOperatorClicked = false;
                }
                currentInput += button.getText().toString();
                display.setText(currentInput);
            }
        };
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }
    // Set listeners for operation buttons (+, -, *, /, √)
    private void setOperationButtonListeners() {
        int[] operationButtonIds = { R.id.sum, R.id.minus, R.id.multiply, R.id.division, R.id.squareRoot };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                String operation = button.getText().toString();

                if (operation.equals("√")) {
                    // Square Root Operation
                    if (!currentInput.isEmpty()) {
                        double value = Double.parseDouble(currentInput);
                        if (value >= 0) {
                            currentInput = decimalFormat.format(Math.sqrt(value));
                            display.setText(currentInput);
                        } else {
                            display.setText("Error");
                        }
                    }
                } else {
                    if (!currentInput.isEmpty()) {
                        firstOperand = Double.parseDouble(currentInput);
                        operator = operation;
                        isOperatorClicked = true;
                    }
                }
            }
        };
        for (int id : operationButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }


    private void setFunctionButtonListeners() {
        // Equals button
        findViewById(R.id.equals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentInput.isEmpty() && !operator.isEmpty()) {
                    double secondOperand = Double.parseDouble(currentInput);
                    double result = calculateResult(firstOperand, secondOperand, operator);
                    currentInput = decimalFormat.format(result);
                    display.setText(currentInput);
                    operator = "";
                }
            }
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentInput = "";
                firstOperand = 0;
                operator = "";
                display.setText("0");
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentInput.isEmpty()) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                    display.setText(currentInput.isEmpty() ? "0" : currentInput);
                }
            }
        });

        // Sign change button (±)
        findViewById(R.id.signChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentInput.isEmpty()) {
                    double value = Double.parseDouble(currentInput);
                    value = value * -1;
                    currentInput = decimalFormat.format(value);
                    display.setText(currentInput);
                }
            }
        });
    }
    private double calculateResult(double firstOperand, double secondOperand, String operator) {
        switch (operator) {
            case "+":
                return firstOperand + secondOperand;
            case "-":
                return firstOperand - secondOperand;
            case "*":
                return firstOperand * secondOperand;
            case "/":
                if (secondOperand != 0) {
                    return firstOperand / secondOperand;
                } else {
                    display.setText("Error");
                    return 0;
                }
            default:
                return 0;
        }
    }
}
