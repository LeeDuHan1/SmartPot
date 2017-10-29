package com.bigpicture.lee_2.smartpot;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lee-2 on 2017-10-29.
 */

public class BackGroundTask extends AsyncTask<String, String, String> {
    private ProgressDialog progressDialog;
    private String pUrl;
    private String result;

    public BackGroundTask(ProgressDialog progressDialog, String url) {
        super();
        this.progressDialog = progressDialog;
        this.pUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog.show();
    }

    @Override
    protected String doInBackground(String... string) {
        GetParsingData gpd = new GetParsingData(pUrl);
        gpd.start();
        try {
            gpd.join();
            result = gpd.GetResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        this.progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
