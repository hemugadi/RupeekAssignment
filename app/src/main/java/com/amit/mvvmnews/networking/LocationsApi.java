package com.amit.mvvmnews.networking;

import com.amit.mvvmnews.model.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LocationsApi {
    @GET("/v2/5c261ccb3000004f0067f6ec/")
    Call<User> getLocationsList();
}
