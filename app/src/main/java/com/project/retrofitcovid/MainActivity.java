package com.project.retrofitcovid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.retrofitcovid.models.State;
import com.project.retrofitcovid.models.StateCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String TAG = "infoCep";
    private RecyclerView rv;
    private RecyclerView.LayoutManager lm;
    private List<State> stateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_main);
        lm = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(lm);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StateService service = retrofit.create(StateService.class);
        Call<StateCatalog> request = service.data(1);

        request.enqueue(new Callback<StateCatalog>() {
            @Override
            public void onResponse(Call<StateCatalog> call, Response<StateCatalog> response) {

                if (response.code() != 200) {
                    Log.i(TAG, "ERRO: " + response.code());
                } else {

                    StateCatalog catalog = response.body();

                    for (State s : catalog.data) {

                        stateList.add(s);

                        StateAdapter adapter = new StateAdapter(stateList);
                        rv.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<StateCatalog> call, Throwable t) {
                Log.e(TAG, "Erro " + t.getMessage());
            }
        });

    }


    class StateAdapter extends RecyclerView.Adapter<stateViewHolder> {

        List<State> states;

        StateAdapter(List<State> list) {
            this.states = list;
        }

        @NonNull
        @Override
        public stateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new stateViewHolder(getLayoutInflater().inflate(R.layout.state_main, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull stateViewHolder holder, int position) {
            State state = stateList.get(position);

            String tState = state.uid + " - " + state.uf + " - " + state.state;
            String tCases = String.valueOf(state.cases);
            String tDeaths = String.valueOf(state.deaths);
            String tSuspects = String.valueOf(state.suspects);
            String tRefuses = String.valueOf(state.refuses);

            holder.txState.setText(tState);
            holder.txNumCases.setText(tCases);
            holder.txNumDeaths.setText(tDeaths);
            holder.txNumSuspects.setText(tSuspects);
            holder.txNumRefuses.setText(tRefuses);
        }

        @Override
        public int getItemCount() {
            return states.size();
        }
    }

    private static class stateViewHolder extends RecyclerView.ViewHolder {

        TextView txState;
        TextView txNumCases;
        TextView txNumDeaths;
        TextView txNumSuspects;
        TextView txNumRefuses;


        public stateViewHolder(@NonNull View itemView) {
            super(itemView);

            txState = itemView.findViewById(R.id.txState);
            txNumCases = itemView.findViewById(R.id.numCases);
            txNumDeaths = itemView.findViewById(R.id.numDeaths);
            txNumSuspects = itemView.findViewById(R.id.numSuspects);
            txNumRefuses = itemView.findViewById(R.id.numRefuses);

        }
    }
}
