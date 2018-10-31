package com.example.xhaxs.rider.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xhaxs.rider.Activity.MyRidesActivity;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.R;

import java.util.ArrayList;

public class MyRidesAdapter extends RecyclerView.Adapter<MyRidesAdapter.MyRidesVH> {

    private ArrayList<CreateRideDetailData> mCreateRideDetailData;
    private MyRidesActivity mMyRidesActivity;

    public MyRidesAdapter(MyRidesActivity myRidesActivity, ArrayList<CreateRideDetailData> createRideDetailData) {
        super();
        mCreateRideDetailData = createRideDetailData;
        mMyRidesActivity = myRidesActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRidesVH myRidesVH, int i) {
        final int index = i;
        myRidesVH.mTextViewOwner.setText(mCreateRideDetailData.get(i).getRideOwner().getUname());
        myRidesVH.mTextViewFrom.setText(mCreateRideDetailData.get(i).getFromLoc().toString());
        myRidesVH.mTextViewTo.setText(mCreateRideDetailData.get(i).getToLoc().toString());
        myRidesVH.mLinearLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyRidesActivity.showDetails(mCreateRideDetailData.get(index), index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mCreateRideDetailData == null ? 0 : mCreateRideDetailData.size());
    }

    @NonNull
    @Override
    public MyRidesVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.pos_item, viewGroup, false);
        return new MyRidesVH(view);
    }

    public static class MyRidesVH extends  RecyclerView.ViewHolder{

        private TextView mTextViewOwner;
        private TextView mTextViewFrom;
        private TextView mTextViewTo;
        private ImageView mImageViewOwner;
        private LinearLayout mLinearLayoutParent;

        public MyRidesVH(@NonNull View itemView) {
            super(itemView);
            mImageViewOwner = itemView.findViewById(R.id.ib_pos_img_icon);
            mTextViewOwner = itemView.findViewById(R.id.tv_pos_owner_name);
            mTextViewFrom = itemView.findViewById(R.id.tv_pos_from);
            mTextViewTo = itemView.findViewById(R.id.tv_pos_to);
            mLinearLayoutParent = itemView.findViewById(R.id.parentSR_POS);
        }
    }

    public void swapList(ArrayList<CreateRideDetailData> createRideDetailData) {
        mCreateRideDetailData = createRideDetailData;
        notifyDataSetChanged();
    }

    public void updateSpecificItem(CreateRideDetailData crdd, int index){
        mCreateRideDetailData.set(index, crdd);
        notifyItemChanged(index);
    }

    public void removeItem(int index){
        mCreateRideDetailData.remove(index);
        notifyItemRemoved(index);
    }
}
