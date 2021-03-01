package com.example.comprarenelmercado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userViews.UserOrManagerActivity;

public class ViewBalance extends AppCompatActivity {

    User user;
    TextView tv_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_balance);

        user = UserOrManagerActivity.getUser();

        tv_balance = findViewById(R.id.tv_Balance);
        tv_balance.setText(Float.toString(user.getBalance()));
    }
}