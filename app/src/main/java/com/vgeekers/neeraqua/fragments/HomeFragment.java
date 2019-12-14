package com.vgeekers.neeraqua.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vgeekers.neeraqua.ItemDetails;
import com.vgeekers.neeraqua.R;
import com.vgeekers.neeraqua.TerminalConstant;
import com.vgeekers.neeraqua.response.Bottle;
import com.vgeekers.neeraqua.response.CommonResponse;
import com.vgeekers.neeraqua.response.FetchAllBottlesResponse;
import com.vgeekers.neeraqua.response.FetchBannerResponse;
import com.vgeekers.neeraqua.retrofit.RetrofitApi;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.vgeekers.neeraqua.TerminalConstant.MY_PREFS;
import static com.vgeekers.neeraqua.TerminalConstant.USER_ID_KEY;

public class HomeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ViewPager pager;
    private int currentPage = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getAllBottlesServerCall();
        setupStateSpinner();
        recyclerView = view.findViewById(R.id.homeMainRecycler);
        pager = view.findViewById(R.id.pager);
        view.findViewById(R.id.cartLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new MyCartFragment(), true, false);
            }
        });
        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
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
        getBannerListFromServer();
        return view;
    }

    private List<String> bannerList = new ArrayList<>();

    private void getBannerListFromServer() {
        RetrofitApi.getPaniServicesObject().getBannerList().enqueue(new Callback<FetchBannerResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchBannerResponse> call, @NonNull Response<FetchBannerResponse> response) {
                if (response.isSuccessful()) {
                    FetchBannerResponse bannerResponse = response.body();
                    if (bannerResponse != null) {
                        if (bannerResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                            bannerList = bannerResponse.getBannerList();
                            pager.setAdapter(new MyAdapter(bannerList));
                        } else {
                            showToast("Sorry no banner available righ tnow for diaplay");
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

    private void getAllBottlesServerCall() {
        showProgress();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getAllBottlesResponse(loginId).enqueue(new Callback<FetchAllBottlesResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchAllBottlesResponse> call, @NonNull Response<FetchAllBottlesResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    FetchAllBottlesResponse allBottlesResponse = response.body();
                    if (allBottlesResponse != null && allBottlesResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        List<Bottle> bottleList = allBottlesResponse.getBottles();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new BottleAvailableAdapter(bottleList));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchAllBottlesResponse> call, @NonNull Throwable t) {
                stopProgress();
                showToast(t.toString());
            }
        });
    }

    public class BottleAvailableAdapter extends RecyclerView.Adapter<BottleAvailableAdapter.BottleAvailableViewHolder> {

        List<Bottle> list;

        BottleAvailableAdapter(List<Bottle> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public BottleAvailableAdapter.BottleAvailableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.bottle_available_item, parent, false);
            return new BottleAvailableAdapter.BottleAvailableViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull final BottleAvailableAdapter.BottleAvailableViewHolder holder, int position) {
            final Bottle bottleAvailable = list.get(position);
            holder.itemName.setText(bottleAvailable.getBBrand());
            int bottleOffer = bottleAvailable.getmOffer();
            String offerText;
            if (bottleOffer > 0) {
                offerText = String.valueOf(bottleAvailable.getmOffer()).concat("% Off");
                holder.itemTag.setText(offerText);
            } else {
                holder.itemTag.setVisibility(View.GONE);
            }
            holder.itemDescrition.setText(bottleAvailable.getBDescription());
            String price = "₹ " + bottleAvailable.getBPrice();
            holder.itemPrice.setText(price);
            if (getActivity() != null) {
                Glide.with(getActivity()).load(bottleAvailable.getBImage()).placeholder(R.drawable.bottle_logo).into(holder.itemImage);
            }
            final int[] counter = {1};
            holder.itemQty.setText(String.valueOf(counter[0]));
            holder.positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter[0] += 1;
                    holder.itemQty.setText(String.valueOf(counter[0]));
                }
            });
            holder.negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counter[0] > 1) {
                        counter[0] -= 1;
                        holder.itemQty.setText(String.valueOf(counter[0]));
                    }
                }
            });
            holder.viewDetailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String counterValue = holder.itemQty.getText().toString().trim();
                    FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new ItemDetails(bottleAvailable, counterValue), true, false);
                }
            });
            holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quantity = holder.itemQty.getText().toString().trim();
                    getAddToCartServerCall(bottleAvailable.getBId(), quantity, bottleAvailable.getBVendorId());
                }
            });
            //add to cart functionality by calling holder.addToCartBtn
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class BottleAvailableViewHolder extends RecyclerView.ViewHolder {

            private TextView itemName;
            private TextView itemTag;
            private TextView itemDescrition;
            private TextView itemPrice;
            private TextView itemQty;
            private ImageView itemImage;
            private ImageView positive;
            private ImageView negative;
            private Button addToCartBtn;
            private Button viewDetailBtn;

            BottleAvailableViewHolder(View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.itemName);
                itemTag = itemView.findViewById(R.id.itemTag);
                itemDescrition = itemView.findViewById(R.id.itemDescription);
                itemPrice = itemView.findViewById(R.id.itemPrice);
                itemQty = itemView.findViewById(R.id.itemQty);
                itemImage = itemView.findViewById(R.id.itemImage);
                positive = itemView.findViewById(R.id.positive);
                negative = itemView.findViewById(R.id.negative);
                addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
                viewDetailBtn = itemView.findViewById(R.id.viewDetailBtn);
            }
        }
    }

    private void getAddToCartServerCall(String bottleAvailableId, String quantity, String bVendorId) {
        showProgress();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().addToCartServerCall(loginId, bottleAvailableId,quantity,bVendorId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorMessage() != null) {
                        showToast(commonResponse.getErrorMessage());
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

    @Override
    public void backPressed() {
        if (doubleBackToExitPressedOnce) {
            getActivity().finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showToast("Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private boolean doubleBackToExitPressedOnce = false;

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