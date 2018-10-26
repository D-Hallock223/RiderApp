package com.example.xhaxs.rider.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xhaxs.rider.Activity.RideSummaryActivity;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RideUserJoinSummaryAdapter extends RecyclerView.Adapter<RideUserJoinSummaryAdapter.UserJoinVH> {

    ArrayList<UserSumData> userSumData;
    private RideSummaryActivity mRideSummaryActivity;

    public RideUserJoinSummaryAdapter(RideSummaryActivity rideSummaryActivity,
                                      ArrayList<UserSumData> userSumData) {
        super();
        this.userSumData = userSumData;
        this.mRideSummaryActivity = rideSummaryActivity;
    }


    @NonNull
    @Override
    public UserJoinVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.textview_ride_join_details, viewGroup, false);
        return new RideUserJoinSummaryAdapter.UserJoinVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserJoinVH userJoinVH, int i) {
        final int index = i;
        userJoinVH.userNameTextView.setText(userSumData.get(i).getUname());
        userJoinVH.emailTextView.setText(userSumData.get(i).getEmail());
    }

    @Override
    public int getItemCount() {
        return (userSumData == null ? 0 : userSumData.size());
    }

    public static class UserJoinVH extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private TextView userNameTextView;
        private TextView emailTextView;

        public UserJoinVH(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_ride_join_user_summary);
            userNameTextView = itemView.findViewById(R.id.tv_ride_join_user_name);
            emailTextView = itemView.findViewById(R.id.tv_ride_join_user_email);
        }
    }

    public void swapList(ArrayList<UserSumData> userSumData1) {
        userSumData = userSumData1;
        notifyDataSetChanged();
    }
}
