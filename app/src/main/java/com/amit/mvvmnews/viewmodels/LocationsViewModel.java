package com.amit.mvvmnews.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amit.mvvmnews.model.User;
import com.amit.mvvmnews.networking.LocationsRepository;

import okhttp3.Cache;

public class LocationsViewModel extends ViewModel {
    private MutableLiveData<User> mutableLiveData;
    private LocationsRepository locationsRepository;

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        locationsRepository = LocationsRepository.getInstance();
        mutableLiveData = locationsRepository.getUserLocations();

    }

    public LiveData<User> getNewsRepository() {
        return mutableLiveData;
    }
}
