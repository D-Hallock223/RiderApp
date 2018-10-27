package com.example.xhaxs.rider.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.R;

public class SRPosAdapter extends RecyclerView.Adapter<SRPosAdapter.SRPosVH> {

    private CreateRideDetailData[] mCreateRideDetailData;
    private SearchRideActivity mSearchRiderActivity;

    public SRPosAdapter(SearchRideActivity searchRideActivity, CreateRideDetailData[] createRideDetailData) {
        super();
        this.mCreateRideDetailData = createRideDetailData;
        this.mSearchRiderActivity = searchRideActivity;
    }

    @NonNull
    @Override
    public SRPosVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.pos_item, viewGroup, false);
        return new SRPosVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SRPosVH srPosVH, int i) {
        final int index = i;
        srPosVH.mTextViewOwner.setText(mCreateRideDetailData[i].getRideOwner().getUname());
        srPosVH.mTextViewFrom.setText(mCreateRideDetailData[i].getFromLoc().toString());
        srPosVH.mTextViewTo.setText(mCreateRideDetailData[i].getToLoc().toString());
        srPosVH.mLinearLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchRiderActivity.showDetails(mCreateRideDetailData[index], index);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCreateRideDetailData == null || mCreateRideDetailData.length == 0) return 0;
        return mCreateRideDetailData.length;
    }

    public void swapList(CreateRideDetailData[] createRideDetailData) {
        mCreateRideDetailData = createRideDetailData;
        notifyDataSetChanged();
    }

    public void updateSpecificItem(CreateRideDetailData crdd, int index){
        mCreateRideDetailData[index] = crdd;
        notifyItemChanged(index);
    }

    public static class SRPosVH extends RecyclerView.ViewHolder {

        private TextView mTextViewOwner;
        private TextView mTextViewFrom;
        private TextView mTextViewTo;
        private ImageView mImageViewOwner;
        private LinearLayout mLinearLayoutParent;

        public SRPosVH(View view) {
            super(view);
            mImageViewOwner = view.findViewById(R.id.ib_pos_img_icon);
            mTextViewOwner = view.findViewById(R.id.tv_pos_owner_name);
            mTextViewFrom = view.findViewById(R.id.tv_pos_from);
            mTextViewTo = view.findViewById(R.id.tv_pos_to);
            mLinearLayoutParent = view.findViewById(R.id.parentSR_POS);
        }
    }

}
