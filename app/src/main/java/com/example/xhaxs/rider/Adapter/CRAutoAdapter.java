package com.example.xhaxs.rider.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xhaxs.rider.Activity.CreateRideActivity;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.R;

public class CRAutoAdapter extends RecyclerView.Adapter<CRAutoAdapter.CRAutoVH> {
    private PlaceData[] mPlaceData;
    private CreateRideActivity mCreateRideActivity;
    private int mType;

    public CRAutoAdapter(CreateRideActivity createRideActivity, PlaceData[] placeData, int type) {
        this.mPlaceData = placeData;
        this.mCreateRideActivity = createRideActivity;
        this.mType = type;
    }

    @NonNull
    @Override
    public CRAutoVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.text_view_autocomplete, viewGroup, false);
        return new CRAutoVH(view);
    }

    @Override
    public int getItemCount() {
        return (mPlaceData == null ? 0 : mPlaceData.length);
    }

    @Override
    public void onBindViewHolder(@NonNull final CRAutoVH crAutoVH, int i) {
        final int index = i;
        PlaceData pd = mPlaceData[i];
        crAutoVH.mTextViewMain.setText(pd.getPlaceNameMain());
        crAutoVH.mTextViewExtra.setText(pd.getPlaceNameSecondary());
        crAutoVH.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == 1) {
                    EditText etext = mCreateRideActivity.findViewById(R.id.et_create_ride_from_location);
                    etext.setText(mPlaceData[index].toString());
                    mCreateRideActivity.setFromLocation(mPlaceData[index]);
                } else if (mType == 2) {
                    EditText etext = mCreateRideActivity.findViewById(R.id.et_create_ride_to_location);
                    etext.setText(mPlaceData[index].toString());
                    mCreateRideActivity.setToLocation(mPlaceData[index]);
                }
            }
        });

    }

    public void swapList(PlaceData[] placeData) {
        mPlaceData = placeData;
        notifyDataSetChanged();
    }

    public void changeType(int type) {
        mType = type;
    }

    public static class CRAutoVH extends RecyclerView.ViewHolder {
        public TextView mTextViewMain;
        public TextView mTextViewExtra;
        public LinearLayout mParentLayout;

        public CRAutoVH(View view) {
            super(view);
            mTextViewMain = view.findViewById(R.id.tv_search_autocomplete_item_main);
            mTextViewExtra = view.findViewById(R.id.tv_search_autocomplete_item_extra);
            mParentLayout = view.findViewById(R.id.parentLayoutAC_CR);
        }
    }
}
