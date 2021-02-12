package com.project.retrofitcovid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.project.retrofitcovid.models.State;
import com.project.retrofitcovid.models.StateCatalog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String TAG = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StateService service = retrofit.create(StateService.class);
        Call<StateCatalog> request = service.data(1);

        request.enqueue(new Callback<StateCatalog>() {
            @Override
            public void onResponse(Call<StateCatalog> call, Response<StateCatalog> response) {

                if(response.code() != 200){
                    Log.i(TAG, "ERRO: "+response.code());
                }
                else{

                    StateCatalog catalog = response.body();

                    for(State s : catalog.data){
                        Log.i(TAG, s.uf+ " / "+s.cases);
                    }

                    Log.i(TAG,"------------------------");

                }
            }

            @Override
            public void onFailure(Call<StateCatalog> call, Throwable t) {
                Log.e(TAG, "Erro " + t.getMessage());
            }
        });



    }
}