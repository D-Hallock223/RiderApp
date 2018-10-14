package com.example.xhaxs.rider.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.R;

public class SRAutoAdapter extends RecyclerView.Adapter<SRAutoAdapter.SRAutoVH> {
    private PlaceData[] mPlaceData;
    private SearchRideActivity mSearchRideActivty;
    private int mType;

    public SRAutoAdapter(SearchRideActivity SearchRideActivty, PlaceData[] placeData, int type) {
        this.mPlaceData = placeData;
        this.mSearchRideActivty = SearchRideActivty;
        this.mType = type;
    }

    @NonNull
    @Override
    public SRAutoAdapter.SRAutoVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.text_view_autocomplete, viewGroup, false);
        return new SRAutoAdapter.SRAutoVH(view);
    }

    @Override
    public int getItemCount() {
        return ((mPlaceData == null) ? 0 : mPlaceData.length);
    }

    @Override
    public void onBindViewHolder(@NonNull final SRAutoAdapter.SRAutoVH SRAutoVH, int i) {
        final int index = i;
        PlaceData pd = mPlaceData[i];
        SRAutoVH.mTextViewMain.setText(pd.getPlaceNameMain());
        SRAutoVH.mTextViewExtra.setText(pd.getPlaceNameSecondary());
        SRAutoVH.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == 1) {
                    EditText etext = mSearchRideActivty.findViewById(R.id.et_sr_from_location);
                    etext.setText(mPlaceData[index].toString());
                    mSearchRideActivty.setFromLocation(mPlaceData[index]);
                } else if (mType == 2) {
                    EditText etext = mSearchRideActivty.findViewById(R.id.et_sr_to_location);
                    etext.setText(mPlaceData[index].toString());
                    mSearchRideActivty.setToLocation(mPlaceData[index]);
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

    public static class SRAutoVH extends RecyclerView.ViewHolder {
        public TextView mTextViewMain;
        public TextView mTextViewExtra;
        public LinearLayout mParentLayout;

        public SRAutoVH(View view) {
            super(view);
            mTextViewMain = view.findViewById(R.id.tv_search_autocomplete_item_main);
            mTextViewExtra = view.findViewById(R.id.tv_search_autocomplete_item_extra);
            mParentLayout = view.findViewById(R.id.parentLayoutAC_CR);
        }
    }
}
