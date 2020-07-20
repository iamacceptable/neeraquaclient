package com.neeraqua.neerapp.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.neeraqua.neerapp.MainActivity;
import com.neeraqua.neerapp.R;
import com.neeraqua.neerapp.TerminalConstant;
import com.neeraqua.neerapp.ThankingPage;
import com.neeraqua.neerapp.response.Booking;
import com.neeraqua.neerapp.response.CommonResponse;
import com.neeraqua.neerapp.response.FetchBottleBookingResponse;
import com.neeraqua.neerapp.response.TimeSlotResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;
import static com.neeraqua.neerapp.TerminalConstant.MY_PREFS;
import static com.neeraqua.neerapp.TerminalConstant.USER_ID_KEY;

public class MyCartFragment extends BaseFragment {

    private RecyclerView ordersRecycler;
    TextView totalPrice;
    String price;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_cart, container, false);
        ordersRecycler = view.findViewById(R.id.ordersRecycler);
        totalPrice = view.findViewById(R.id.totalprice);
        //totalPrice.setText(price);
        //Toast.makeText(getActivity(), ""+price, Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.cartLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCartEmpty) {
                    showToast("Sorry, your cart is empty");
                } else {
                    showFilterDialog();
                }
            }
        });
        getMyCartItem();
        return view;
    }

    private boolean isCartEmpty = false;

    private void getMyCartItem() {
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getMyCartList(loginId).enqueue(new Callback<FetchBottleBookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchBottleBookingResponse> call, @NonNull Response<FetchBottleBookingResponse> response) {
                if (response.isSuccessful()) {
                    FetchBottleBookingResponse bookingResponse = response.body();
                    if (bookingResponse != null && !bookingResponse.getBooking().isEmpty()) {
                        List<Booking> list = bookingResponse.getBooking();
                        vendorId = bookingResponse.getVendorId();
                        price = "Total price Rs:" + bookingResponse.getBotprice();
                        totalPrice.setText(price);
                        ordersRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        ordersRecycler.setAdapter(new MyCartAdapter(list));
                        getTimeSlotList();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchBottleBookingResponse> call, @NonNull Throwable t) {
                showToast("Sorry, no item available in your cart");
                totalPrice.setText("0");
                isCartEmpty = true;
                ordersRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ordersRecycler.setAdapter(new MyCartAdapter(new ArrayList<Booking>()));
            }
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setView(View.inflate(getActivity(), R.layout.timing_cart, null));
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Spinner timeSlotSpinner = alertDialog.findViewById(R.id.timeSlotSpinner);
        Button doBookingButton = alertDialog.findViewById(R.id.doBookingButton);
        ArrayAdapter<String> dataAdapterState = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeList);
        dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(dataAdapterState);
        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                timeSlot = timeList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                timeSlot = "";
            }
        });
        doBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                confirmBooking();
            }
        });
    }

    private List<String> timeList = new ArrayList<>();
    private String timeSlot = "";
    private String vendorId = "";

    private void getTimeSlotList() {
        showProgress();
        RetrofitApi.getPaniServicesObject().getTimeSlotList(vendorId).enqueue(new Callback<TimeSlotResponse>() {
            @Override
            public void onResponse(@NonNull Call<TimeSlotResponse> call, @NonNull Response<TimeSlotResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    TimeSlotResponse timeSlotResponse = response.body();
                    if (timeSlotResponse != null && timeSlotResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        timeList = timeSlotResponse.getTimeSlot();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSlotResponse> call, @NonNull Throwable t) {
                stopProgress();
            }
        });
    }

    public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

        private List<Booking> list;

        public MyCartAdapter(List<Booking> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyCartAdapter.MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.cart_items, parent, false);
            return new MyCartAdapter.MyCartViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyCartAdapter.MyCartViewHolder holder, final int position) {
            /*Empty for now*/
            final Booking bottleAvailable = list.get(position);
            String brand = "Company : ".concat(bottleAvailable.getbBrand().toUpperCase());
            holder.itemAddress.setText(brand);
            String bottelPrice = "Rs:" + bottleAvailable.getPerPrice();
            holder.itemPriceDet.setText(bottelPrice);
            String quantity = bottleAvailable.getQuantity();
            if (quantity.equals("1")) {
                quantity = quantity.concat(" bottle");
            } else {
                quantity = quantity.concat(" bottles");
            }
            holder.itemQuantity.setText(quantity);
            Glide.with(MyCartFragment.this).load(bottleAvailable.getbImage()).placeholder(R.drawable.bottle_logo).into(holder.itemImage);
            holder.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteFromCart(bottleAvailable.getCartId());
                    if (list.isEmpty()) {
                        totalPrice.setText(0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyCartViewHolder extends RecyclerView.ViewHolder {

            private ImageView itemDelete;
            private ImageView itemImage;
            private TextView itemDate;
            private TextView itemBrand;
            private TextView itemMobile;
            private TextView itemQuantity;
            private TextView itemAddress;
            private TextView itemPriceDet;

            MyCartViewHolder(View itemView) {
                super(itemView);
                itemDelete = itemView.findViewById(R.id.itemDelete);
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

    private void deleteFromCart(String cartId) {
        showProgress();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().deleteFromCartServerCall(cartId, loginId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    showToast(response.body().getErrorMessage());
                    if (response.body().getErrorCode().equalsIgnoreCase(TerminalConstant.SUCCESS)) {
                        getMyCartItem();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                stopProgress();
                showToast(t.toString());
            }
        });
    }

    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        this.onCreate(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private void confirmBooking() {
        showProgress();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getBookingResponse(loginId, timeSlot, date).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        showToast(commonResponse.getErrorMessage());
                        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new ThankingPage(), false, true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                stopProgress();
                showToast(t.toString());
            }
        });
    }
}