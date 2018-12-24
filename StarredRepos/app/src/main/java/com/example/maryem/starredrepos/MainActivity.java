package com.example.maryem.starredrepos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class MainActivity extends AppCompatActivity {

    private String gitAPIurl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc&page=";
    private static final String GIT_API_URL="https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private java.util.List<Repo> repoList;
    private RecyclerView.Adapter adapter;
    private boolean isScrolling;
    private int currentItems,totalItems,scrolledOutItems, page=1;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = findViewById(R.id.main_list);

        repoList = new ArrayList<>();
        adapter = new RepoAdapter(getApplicationContext(),repoList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getData();

        //Begin : setting an "OnScroll Listener" to implement the "Endless Scroll" concept -Pagination-
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems =linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && ((currentItems + scrolledOutItems) == totalItems))
                {
                    isScrolling=false;
                    page++;
                    loadMoreRepositoriesData(page);
                }

            }
        });
        //End
    }


    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            SSLEngine engine = sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GIT_API_URL,
                new Response.Listener<String>() {
                    //On success, the code inside this method will be executed;
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("items");

                            for (int i=0; i<array.length(); i++)
                            {
                                JSONObject repoObject = array.getJSONObject(i);
                                JSONObject repoOwnerObject = repoObject.getJSONObject("owner");
                                Repo repo = new Repo(repoObject.getString("name"),
                                        repoObject.getString("description"),
                                        repoOwnerObject.getString("login"),
                                        repoObject.getString("stargazers_count"));

                                repoList.add(repo);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                },
                //On failure, this code will be executed instead.
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //                     progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadMoreRepositoriesData(int pageFunc)
    {
        // progressBar.setVisibility(View.VISIBLE);

        gitAPIurl = GIT_API_URL+"&page="+String.valueOf(pageFunc);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, gitAPIurl,
                new Response.Listener<String>() {
                    //On success, the code inside this method will be executed;
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("items");

                            for (int i=0; i<array.length(); i++)
                            {
                                JSONObject repoObject = array.getJSONObject(i);
                                JSONObject repoOwnerObject = repoObject.getJSONObject("owner");
                                Repo repo = new Repo(repoObject.getString("name"),
                                        repoObject.getString("description"),
                                        repoOwnerObject.getString("login"),
                                        repoObject.getString("stargazers_count"));

                                repoList.add(repo);
                            }

                            // progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                //On failure, this code will be executed instead.
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
