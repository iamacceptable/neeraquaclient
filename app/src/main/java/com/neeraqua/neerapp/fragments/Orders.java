package com.neeraqua.neerapp.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.neeraqua.neerapp.MainActivity;
import com.neeraqua.neerapp.R;
import com.neeraqua.neerapp.response.Booking;
import com.neeraqua.neerapp.response.FetchBottleBookingResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;
import static com.neeraqua.neerapp.TerminalConstant.MY_PREFS;
import static com.neeraqua.neerapp.TerminalConstant.USER_ID_KEY;

public class Orders extends BaseFragment {

    private RecyclerView ordersRecycler;
    private ProgressDialog mProgressDialog;

    protected void showProgress() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Processing");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    protected void stopProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_orders, container, false);
        getAllBottlesFromServerApi();
        ordersRecycler = view.findViewById(R.id.ordersRecycler);
        return view;
    }

    private void getAllBottlesFromServerApi() {
        showProgress();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getBottleList(loginId).enqueue(new Callback<FetchBottleBookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchBottleBookingResponse> call, @NonNull Response<FetchBottleBookingResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    FetchBottleBookingResponse bookingResponse = response.body();
                    if (bookingResponse != null) {
                        List<Booking> bookingList = bookingResponse.getBooking();
                        if (!bookingList.isEmpty()) {
                            ordersRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            ordersRecycler.setAdapter(new BottleAvailableAdapter(bookingList));
                        } else {
                            showToast("Sorry, no order available for booking");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchBottleBookingResponse> call, @NonNull Throwable t) {
                stopProgress();
                showToast("Sorry, no order available for booking");
            }
        });
    }

    public class BottleAvailableAdapter extends RecyclerView.Adapter<BottleAvailableAdapter.BottleAvailableViewHolder> {

        List<Booking> list;

        BottleAvailableAdapter(List<Booking> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public BottleAvailableAdapter.BottleAvailableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.order_items, parent, false);
            return new BottleAvailableAdapter.BottleAvailableViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull final BottleAvailableAdapter.BottleAvailableViewHolder holder, int position) {
            final Booking bottleAvailable = list.get(position);
            String date = "Date : " + bottleAvailable.getbDate();
            holder.itemDate.setText(date);
            String priceBot="Rs: "+bottleAvailable.getPerPrice();
            holder.itemPriceDet.setText(priceBot);
            holder.itemMobile.setText(bottleAvailable.getStatus());
            String brand = "Company : ".concat(bottleAvailable.getbBrand().toUpperCase());
            holder.itemAddress.setText(brand);
            String quantity = bottleAvailable.getQuantity();
            if (quantity.equals("1")) {
                quantity = quantity.concat(" bottle");
            } else {
                quantity = quantity.concat(" bottles");
            }
            holder.itemQuantity.setText(quantity);
            Glide.with(Orders.this).load(bottleAvailable.getbImage()).placeholder(R.drawable.bottle_logo).into(holder.itemImage);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class BottleAvailableViewHolder extends RecyclerView.ViewHolder {

            private ImageView itemImage;
            private TextView itemDate;
            private TextView itemBrand;
            private TextView itemMobile;
            private TextView itemQuantity;
            private TextView itemAddress;
            private TextView itemPriceDet;

            BottleAvailableViewHolder(View itemView) {
                super(itemView);
                itemDate = itemView.findViewById(R.id.itemDate);
                itemImage = itemView.findViewById(R.id.itemImage);
                itemMobile = itemView.findViewById(R.id.itemMobile);
                itemAddress = itemView.findViewById(R.id.itemAddress);
                itemBrand = itemView.findViewById(R.id.itemBrandName);
                itemQuantity = itemView.findViewById(R.id.itemQuantity);
                itemPriceDet = itemView.findViewById(R.id.itemPriceDet);
            }
        }
    }

    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }
}