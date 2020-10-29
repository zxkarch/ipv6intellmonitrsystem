package com.example.saierclient.ui.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.saierclient.R;

import java.util.List;
import java.util.Map;

public class MyImageListAdapter extends ArrayAdapter<Map<String, String>> {
    private int resourceId;

    public MyImageListAdapter(Context context, int resource, List<Map<String, String>> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @SuppressLint("WrongConstant")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, String> objitem = getItem(position);//获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ((TextView) view.findViewById(R.id.cameraName)).setText(objitem.get("设备名"));
        ((TextView) view.findViewById(R.id.cameraDate)).setText(objitem.get("获取时间"));
        ((TextView) view.findViewById(R.id.cameraValue)).setText(objitem.get("运行值"));
        Glide.with(getContext()).load(objitem.get("图片地址")).into((ImageView) view.findViewById(R.id.content_image));
        //((ImageView) view.findViewById(R.id.content_image)).setImageURL(objitem.get("图片地址"));
        return view;
    }
}
