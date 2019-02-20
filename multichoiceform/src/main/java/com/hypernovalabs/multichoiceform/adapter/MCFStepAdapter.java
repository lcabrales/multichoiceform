package com.hypernovalabs.multichoiceform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.hypernovalabs.multichoiceform.R;
import com.hypernovalabs.multichoiceform.form.MCFStepObj;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by celso on 2/19/19
 */
public class MCFStepAdapter extends ArrayAdapter<MCFStepObj> {
    private final Context mContext;
    private ArrayList<? extends MCFStepObj> mDataset;

    public MCFStepAdapter(Context context, ArrayList<? extends MCFStepObj> dataSet) {
        super(context, R.layout.mcf_simple_list_item_checked);
        mContext = context;
        mDataset = dataSet;
    }

    public void setData(ArrayList<? extends MCFStepObj> dataSet) {
        mDataset = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public MCFStepObj getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mcf_simple_list_item_checked, parent, false);
        }
        CheckedTextView textView = (CheckedTextView) convertView;
        MCFStepObj item = mDataset.get(position);
        textView.setText(mDataset.get(position).getDisplayText());
        return convertView;
    }

}
