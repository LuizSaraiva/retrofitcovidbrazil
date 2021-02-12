package com.project.retrofitcovid;

import com.project.retrofitcovid.models.StateCatalog;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StateService {

    public static final String BASE_URL = "https://covid19-brazil-api.now.sh/api/report/";

    @GET("v{version}")
    Call<StateCatalog> data(@Path("version") int version);
}
