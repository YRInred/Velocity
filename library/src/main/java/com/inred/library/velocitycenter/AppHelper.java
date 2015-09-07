package com.inred.library.velocitycenter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inred.library.R;

/**
 * Created by inred on 2015/9/7.
 */
public class AppHelper {



    public void showToast(String text,Context context) {
        Toast  toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        textView.setText(text);
        linearLayout.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(linearLayout);
        toast.show();
    }

    public void showToast(int text,Context context) {
        Toast  toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        textView.setText(text);
        linearLayout.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(linearLayout);
        toast.show();
    }

}
