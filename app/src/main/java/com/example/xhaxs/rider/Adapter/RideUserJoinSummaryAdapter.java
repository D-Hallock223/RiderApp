package com.example.xhaxs.rider.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhaxs.rider.Activity.RideSummaryActivity;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RideUserJoinSummaryAdapter extends RecyclerView.Adapter<RideUserJoinSummaryAdapter.UserJoinVH> {

    private ArrayList<UserSumData> userSumData;
    private RideSummaryActivity mRideSummaryActivity;
    private boolean mIsOwner;
    private String mOwnerID;
    private boolean isFinshed;

    public RideUserJoinSummaryAdapter(RideSummaryActivity rideSummaryActivity,
                                      ArrayList<UserSumData> userSumData,
                                      boolean isOwner,
                                      String ownerID,
                                      boolean isFinshed) {
        super();
        this.userSumData = userSumData;
        this.mRideSummaryActivity = rideSummaryActivity;
        this.mIsOwner = isOwner;
        this.mOwnerID = ownerID;
        this.isFinshed = isFinshed;
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
        if(mIsOwner == true && !userSumData.get(i).getUid().equals(mOwnerID) && !this.isFinshed){
            userJoinVH.clearButton.setVisibility(View.VISIBLE);
            userJoinVH.clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mRideSummaryActivity.removeUserFromList(userSumData.get(index));
                            Toast.makeText(mRideSummaryActivity.getApplicationContext(), "Clicking clear", Toast.LENGTH_SHORT).show();
                        }
                    }).run();
                    Log.d("-----------", "-----------------------------Ended function clear click------------------------");
                }
            });
        } else {
            userJoinVH.clearButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (userSumData == null ? 0 : userSumData.size());
    }

    public static class UserJoinVH extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private TextView userNameTextView;
        private TextView emailTextView;
        private ImageButton clearButton;

        public UserJoinVH(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_ride_join_user_summary);
            userNameTextView = itemView.findViewById(R.id.tv_ride_join_user_name);
            emailTextView = itemView.findViewById(R.id.tv_ride_join_user_email);
            clearButton = itemView.findViewById(R.id.ib_clear_request_join_item);
        }
    }

    public void swapList(ArrayList<UserSumData> userSumData1) {
        userSumData = null;
        if(userSumData1.size() != 0){
            userSumData = userSumData1;
        } else {
            userSumData = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
