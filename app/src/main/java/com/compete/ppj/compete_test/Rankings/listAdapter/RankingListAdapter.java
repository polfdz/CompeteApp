package com.compete.ppj.compete_test.Rankings.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.raceMenu.gridAdapter.RunnerBlock;

import java.util.ArrayList;

/**
 * Created by Pol on 28/12/2015.
 */
public class RankingListAdapter extends ArrayAdapter<RankingRunnerBlock> {
    Context context;
    int layoutResourceId;
    ArrayList<RankingRunnerBlock> data = new ArrayList<RankingRunnerBlock>();

    public RankingListAdapter(Context context, int layoutResourceId,
                       ArrayList<RankingRunnerBlock> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtUserName = (TextView) row.findViewById(R.id.tRankingRunnerName);
            holder.imageCountry = (ImageView) row.findViewById(R.id.iRankingRunner);
            holder.txtPoints = (TextView) row.findViewById(R.id.tRankingRunnerPoints);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        RankingRunnerBlock item = data.get(position);
        holder.txtUserName.setText(item.getRunnerName());
        holder.imageCountry.setImageBitmap(item.getImage());
        holder.txtPoints.setText(item.getRunnerPoints() +" p");
        return row;
    }

    static class RecordHolder {
        TextView txtUserName;
        TextView txtPoints;
        ImageView imageCountry;

    }
}
