package com.neeraqua.neerapp;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.neeraqua.neerapp.fragments.BaseFragment;
import com.neeraqua.neerapp.fragments.FragmentLauncher;
import com.neeraqua.neerapp.fragments.HomeFragment;
import com.neeraqua.neerapp.response.Bottle;
import com.neeraqua.neerapp.response.FetchBannerResponse;
import com.neeraqua.neerapp.response.TimeSlotResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ItemDetails extends BaseFragment {

    private ImageView itemDetailImage;
    private TextView itemDetailName;
    private TextView itemDetailDescription;
    private TextView itemDetailPrice;
    private TextView itemDetailQuantity;
    private Button proceedBtn;
    private Bottle mBottleResponse;
    private String counterValue;
    private Spinner timeSlotSpinner;
    private List<String> timeList = new ArrayList<>();
    private int currentPage = 0;

    public ItemDetails(Bottle bottleAvailable, String counterValue) {
        mBottleResponse = bottleAvailable;
        this.counterValue = counterValue;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_item_details, container, false);
        itemDetailImage = view.findViewById(R.id.itemDetailImage);
        itemDetailName = view.findViewById(R.id.itemDetailName);
        itemDetailDescription = view.findViewById(R.id.itemDetailDescription);
        itemDetailPrice = view.findViewById(R.id.itemDetailPrice);
        proceedBtn = view.findViewById(R.id.proceedBtn);
        pager = view.findViewById(R.id.pager);
        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        itemDetailQuantity = view.findViewById(R.id.itemDetailQuantity);
        timeSlotSpinner = view.findViewById(R.id.timeSlotSpinner);
        getTimeSlotList();
        setBottleDetailsFromResponse();
        getBannerListFromServer();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (currentPage == bannerList.size()) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 2000, 5000);
        return view;
    }

    /*private void confirmBooking() {
        showProgress();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getBookingResponse(loginId, mBottleResponse.getBId(),
                counterValue, mBottleResponse.getBVendorId(), timeSlot, date).enqueue(new Callback<CommonResponse>() {
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
    }*/

    private String timeSlot = "";

    private void getTimeSlotList() {
        showProgress();
        RetrofitApi.getPaniServicesObject().getTimeSlotList(mBottleResponse.getBVendorId()).enqueue(new Callback<TimeSlotResponse>() {
            @Override
            public void onResponse(@NonNull Call<TimeSlotResponse> call, @NonNull Response<TimeSlotResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    TimeSlotResponse timeSlotResponse = response.body();
                    if (timeSlotResponse != null && timeSlotResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        timeList = timeSlotResponse.getTimeSlot();
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
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSlotResponse> call, @NonNull Throwable t) {
                stopProgress();
            }
        });
    }

    private ViewPager pager;
    private List<String> bannerList = new ArrayList<>();

    private void getBannerListFromServer() {
        RetrofitApi.getPaniServicesObject().getSubBannerList().enqueue(new Callback<FetchBannerResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchBannerResponse> call, @NonNull Response<FetchBannerResponse> response) {
                if (response.isSuccessful()) {
                    FetchBannerResponse bannerResponse = response.body();
                    if (bannerResponse != null) {
                        if (bannerResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                            bannerList = bannerResponse.getBannerList();
                            pager.setAdapter(new MyAdapter(bannerList));
                        } else {
                            showToast("Sorry no banner available right now for display");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchBannerResponse> call, @NonNull Throwable t) {
                showToast("Sorry no banner available righ tnow for diaplay");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private void setBottleDetailsFromResponse() {
        Glide.with(getActivity()).load(mBottleResponse.getBImage()).centerCrop().placeholder(R.drawable.bottle_logo).into(itemDetailImage);
        itemDetailName.setText(mBottleResponse.getBBrand());
        itemDetailDescription.setText(mBottleResponse.getBDescription());
        String bottles = "";
        if (counterValue.equals("1")) {
            bottles = counterValue.concat(" bottle");
        } else {
            bottles = counterValue.concat(" bottles");
        }
        itemDetailQuantity.setText(bottles);
        double price = Double.parseDouble(mBottleResponse.getBPrice()) * Double.parseDouble(counterValue);
        itemDetailPrice.setText("₹ ".concat(String.valueOf(price)));
    }

    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }

    public class MyAdapter extends PagerAdapter {

        private List<String> images;

        MyAdapter(List<String> images) {
            this.images = images;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup view, final int position) {
            View myImageLayout = LayoutInflater.from(getActivity()).inflate(R.layout.banner_single_image, view, false);
            ImageView myImage = myImageLayout.findViewById(R.id.image);
            Picasso.get().load(images.get(position)).fit().error(getResources().getDrawable(R.drawable.banner)).into(myImage);
            view.addView(myImageLayout, 0);
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }
    }
}
