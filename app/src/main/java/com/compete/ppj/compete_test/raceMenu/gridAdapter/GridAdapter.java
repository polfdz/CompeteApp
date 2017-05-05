package com.compete.ppj.compete_test.raceMenu.gridAdapter;

/**
 * Created by Pol on 16/11/2015.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

import org.w3c.dom.Text;

/**
 *
 * @author manish.s
 *
 */
public class GridAdapter extends ArrayAdapter<RunnerBlock> {
    Context context;
    int layoutResourceId;
    ArrayList<RunnerBlock> data = new ArrayList<RunnerBlock>();

    public GridAdapter(Context context, int layoutResourceId,
                                 ArrayList<RunnerBlock> data) {
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
            holder.txtTitle = (TextView) row.findViewById(R.id.tRunner);
            holder.imageItem = (ImageView) row.findViewById(R.id.iRunner);
            holder.imageConnection = (ImageView) row.findViewById(R.id.iConnectionIndicator);
            holder.txtDistance = (TextView) row.findViewById(R.id.tRunnerDistance);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        RunnerBlock item = data.get(position);
        holder.txtTitle.setText(item.getRunnerName());
        holder.imageItem.setImageBitmap(item.getImage());
        holder.imageConnection.setImageBitmap(item.getImageConnection());
        String runnerDistance = String.format("%.1f", Double.parseDouble((item.getRunnerDistance())));
        holder.txtDistance.setText(runnerDistance +" km");
        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        TextView txtDistance;
        ImageView imageItem;
        ImageView imageConnection;

    }
}