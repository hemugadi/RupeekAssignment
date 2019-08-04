package com.amit.mvvmnews.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amit.mvvmnews.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsRepository {
    private static LocationsRepository locationsRepository;

    public static LocationsRepository getInstance(){
        if (locationsRepository == null){
            locationsRepository = new LocationsRepository();
        }
        return locationsRepository;
    }

    private LocationsApi locationsApi;

    public LocationsRepository(){
        locationsApi = RetrofitService.cteateService(LocationsApi.class);
    }

    public MutableLiveData<User> getUserLocations(){
        MutableLiveData<User> locationsData = new MutableLiveData<>();
        locationsApi.getLocationsList().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call,
                                   Response<User> response) {
                if (response.isSuccessful()){
                    locationsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("hemanth","error ===== "+t.toString());
                locationsData.setValue(null);
            }
        });
        return locationsData;
    }
}
