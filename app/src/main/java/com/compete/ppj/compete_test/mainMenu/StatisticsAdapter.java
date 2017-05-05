package com.compete.ppj.compete_test.mainMenu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

import java.util.ArrayList;

/**
 * Created by Pol on 06/11/2015.
 */
public class StatisticsAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<String> items;
    protected ArrayList<String> arrayType;

    public  StatisticsAdapter(Activity _activity, ArrayList<String> _arrayStatistics, ArrayList<String> _arrayType) {
        activity = _activity;
        items = _arrayStatistics;
        arrayType = _arrayType;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        String statisticsText = items.get(position);
        String typeStatisticsText = arrayType.get(position);

        LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.row_statistics, null);

        final TextView textStatistics = (TextView) v.findViewById(R.id.textStatistics);
        final TextView textTypeStatistics = (TextView) v.findViewById(R.id.textTypeStatistics);

        textStatistics.setText(statisticsText);
        textTypeStatistics.setText(typeStatisticsText);
        /*
        switch(nivell) {
            case "1":
                background.setBackgroundResource(R.drawable.biblioteca_nivell1_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell1);
                break;
            case "2":
                background.setBackgroundResource(R.drawable.biblioteca_nivell2_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell2);
                break;
            case "3":
                background.setBackgroundResource(R.drawable.biblioteca_nivell3_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell3);
                break;
            case "4":
                background.setBackgroundResource(R.drawable.biblioteca_nivell4_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell4);
                break;
            case "5":
                background.setBackgroundResource(R.drawable.biblioteca_nivell5_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell5);
                break;
            case "6":
                background.setBackgroundResource(R.drawable.biblioteca_nivell6_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell6);
                break;
            case "7":
                background.setBackgroundResource(R.drawable.biblioteca_nivell7_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell7);
                break;
            case "8":
                background.setBackgroundResource(R.drawable.biblioteca_nivell8_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell8);
                break;
            case "9":
                background.setBackgroundResource(R.drawable.biblioteca_nivell9_barra);
                numero.setBackgroundResource(R.drawable.biblioteca_nivell9);
                break;
            case "-1":
                //refrany del dia
                numero.setBackgroundResource(R.drawable.biblioteca_refrany_dia_logo);
                if ( position%2  != 0 ){
                    background.setBackgroundResource(R.drawable.biblioteca_refrany_dia_barra1);
                }
                else {
                    background.setBackgroundResource(R.drawable.biblioteca_refrany_dia_barra2);
                }
            default:*/
        return v;
    }
}
