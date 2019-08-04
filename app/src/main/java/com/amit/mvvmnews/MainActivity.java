package com.amit.mvvmnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.mvvmnews.adapters.LocationsAdapter;
import com.amit.mvvmnews.model.Location;
import com.amit.mvvmnews.model.User;
import com.amit.mvvmnews.networking.LocationsApi;
import com.amit.mvvmnews.viewmodels.LocationsViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationsAdapter.OnItemClickListener {

    ArrayList<Location> locationsList = new ArrayList<>();
    LocationsAdapter locationsAdapter;
    RecyclerView rvHeadline;
    TextView textView;
    LocationsViewModel locationsViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvHeadline = findViewById(R.id.rvLocations);

        textView = findViewById(R.id.hello_msg);
        locationsViewModel = ViewModelProviders.of(this).get(LocationsViewModel.class);
//        locationsViewModel.init();
//        locationsViewModel.getNewsRepository().observe(this,locationsResponse -> {
//            textView.setText(getResources().getString(R.string.hello_msg,locationsResponse.getCustName()));
//            List<Location> locations = locationsResponse.getLocations();
//            locationsList.addAll(locations);
//            locationsAdapter.notifyDataSetChanged();
//
//        });
        fetchData();
        //setupRecyclerView();
    }

    private void setupRecyclerView() {
        if (locationsAdapter == null) {
            locationsAdapter = new LocationsAdapter(MainActivity.this, locationsList);
            rvHeadline.setLayoutManager(new LinearLayoutManager(this));
            rvHeadline.setAdapter(locationsAdapter);
            rvHeadline.setItemAnimator(new DefaultItemAnimator());
            rvHeadline.setNestedScrollingEnabled(true);
        } else {
            locationsAdapter.notifyDataSetChanged();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public boolean isConnectingToInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private void fetchData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .cache(provideCache())
                .addNetworkInterceptor( provideCacheInterceptor() )
                .addInterceptor( provideOfflineCacheInterceptor() )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mocky.io/v2/5c261ccb3000004f0067f6ec/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        LocationsApi service = retrofit.create(LocationsApi.class);
        Call<User> call = service.getLocationsList();
        //calling the api
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                //if there is no error
                locationsList = (ArrayList<Location>) response.body().getLocations();
                textView.setText(getResources().getString(R.string.hello_msg,response.body().getCustName()));
                setupRecyclerView();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private Cache provideCache () {
        Cache cache = null;
        try {
            cache = new Cache( new File(this.getCacheDir(), "http-cache" ),
                    10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e) {
            Log.e( "Error", e.toString() );
        }
        return cache;
    }

    public Interceptor provideCacheInterceptor(){
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept (Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed( chain.request() );
                // re-write response header to force use of cache
                CacheControl cacheControl;

                if (isConnectingToInternet()) {
                    cacheControl = new CacheControl.Builder()
                            .maxAge(0, TimeUnit.SECONDS)
                            .build();
                } else {
                    cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();
                }
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", cacheControl.toString())
                        .build();
            }
        };
    }

    public Interceptor provideOfflineCacheInterceptor () {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept (Chain chain) throws IOException {
                Request request = chain.request();
                if (!isConnectingToInternet()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    @Override
    public void onItemClick(int po) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("place_name",locationsList.get(po).getPlace());
        intent.putExtra("date",locationsList.get(po).getDate());
        intent.putExtra("url",locationsList.get(po).getUrl());
        intent.putExtra("rate",Integer.toString(locationsList.get(po).getRate()));
        intent.putExtra("description",locationsList.get(po).getDescription());
        startActivity(intent);
    }
}
