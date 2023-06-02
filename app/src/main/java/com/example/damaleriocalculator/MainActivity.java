package com.example.damaleriocalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private Button zero;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button add;
    private Button sub;
    private Button mul;
    private Button div;
    private Button equal;
    private TextView info;
    private Button clear;
    private TextView result;
    private Button btnChrome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("0");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("6");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.append("9");
            }
        });

        sub.setOnClickListener((view) -> {
            result.append("-");
        });

        add.setOnClickListener((view) -> {
            result.append("+");
        });

        mul.setOnClickListener((view) -> {
            result.append("*");
        });

        div.setOnClickListener((view) -> {
            result.append("/");
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expression = result.getText().toString();
                try {
                    double resultValue = evaluateExpression(expression);
                    String formattedResult = formatResult(resultValue);
                    info.setText(formattedResult);
                } catch (ArithmeticException e) {
                    info.setText("Error");
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                result.setText("");
                info.setText("");
            }
        });

        btnChrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChrome(view);
            }
        });
    }

    private void setupUIViews() {
        zero = findViewById(R.id.btn0);
        one = findViewById(R.id.btn1);
        two = findViewById(R.id.btn2);
        three = findViewById(R.id.btn3);
        four = findViewById(R.id.btn4);
        five = findViewById(R.id.btn5);
        six = findViewById(R.id.btn6);
        seven = findViewById(R.id.btn7);
        eight = findViewById(R.id.btn8);
        nine = findViewById(R.id.btn9);
        add = findViewById(R.id.btnAdd);
        sub = findViewById(R.id.btnMin);
        mul = findViewById(R.id.btnMul);
        div = findViewById(R.id.btnDiv);
        equal = findViewById(R.id.btnEqu);
        info = findViewById(R.id.tvControl);
        result = findViewById(R.id.tvDisplay);
        clear = findViewById(R.id.btnClear);
        btnChrome = findViewById(R.id.btnChrome);
    }

    private double evaluateExpression(String expression) throws ArithmeticException {
        try {
            Stack<Double> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();

            for (int i = 0; i < expression.length(); i++) {
                char ch = expression.charAt(i);
                if (Character.isDigit(ch)) {
                    StringBuilder sb = new StringBuilder();
                    while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    i--;
                    numbers.push(Double.parseDouble(sb.toString()));
                } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    while (!operators.empty() && hasPrecedence(ch, operators.peek())) {
                        evaluateTop(numbers, operators);
                    }
                    operators.push(ch);
                } else if (ch == '(') {
                    operators.push(ch);
                } else if (ch == ')') {
                    while (!operators.empty() && operators.peek() != '(') {
                        evaluateTop(numbers, operators);
                    }
                    operators.pop(); // Pop '('
                }
            }

            while (!operators.empty()) {
                evaluateTop(numbers, operators);
            }

            return numbers.pop();
        } catch (Exception e) {
            throw new ArithmeticException("Invalid expression");
        }
    }

    private boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') {
            return false;
        }
        if ((operator1 == '*' || operator1 == '/') && (operator2 == '+' || operator2 == '-')) {
            return false;
        }
        return true;
    }

    private void evaluateTop(Stack<Double> numbers, Stack<Character> operators) {
        double operand2 = numbers.pop();
        double operand1 = numbers.pop();
        char operator = operators.pop();
        double result = 0;
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = operand1 / operand2;
                break;
        }
        numbers.push(result);
    }

    private String formatResult(double result) {
        String formattedResult = String.valueOf(result);
        if (formattedResult.endsWith(".0")) {
            formattedResult = formattedResult.replace(".0", "");
        }
        return formattedResult;
    }

    public void openChrome(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://malayanmindanao.blackboard.com"));
        startActivity(intent);
    }
}
