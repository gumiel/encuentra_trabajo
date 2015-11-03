package com.gumiel.perez.tareadiplomado;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gumiel.perez.tareadiplomado.data.JobPostDbContract;
import com.gumiel.perez.tareadiplomado.data.JobPostDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void synDataTask(MenuItem item) {
        GetDataAsyncTask asyncTask = new GetDataAsyncTask();
        asyncTask.execute();
    }

    //private class GetDataAsyncTask extends AsyncTask<Void, Void, String[]> {
    private class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        //protected String[] doInBackground(Void... params) {
        protected Void doInBackground(Void... params) {

            // The URL To connect:
            // http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon().appendPath("work_posts.json").build();

            try {
                URL url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                String line;
                while((line = reader.readLine()) != null ){
                    buffer.append(line).append("\n");
                }

                String clientInfoJSON = buffer.toString();
                Log.d(LOG_TAG, "JSON: " + clientInfoJSON);
                JSONArray jsonArray = new JSONArray(clientInfoJSON);
                int length = jsonArray.length();
                //String result[] = new String[length];
                JobPostDbHelper dbHelper = new JobPostDbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                for(int i = 0; i < length; i++){

                    JSONObject element = jsonArray.getJSONObject(i);
                    int id = element.getInt("id");
                    String title = element.getString("title");
                    String date = element.getString("posted_date");
                    String description = element.getString("description");

                    //result[i] = title + "--" + date;

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(JobPostDbContract.JobPost._ID,id);
                    contentValues.put(JobPostDbContract.JobPost.TITLE_COLUMN,title);
                    contentValues.put(JobPostDbContract.JobPost.POSTED_DATE_COLUMN,date);
                    contentValues.put(JobPostDbContract.JobPost.DESCRIPTION_COLUMN,description);

                    db.insert(JobPostDbContract.JobPost.TABLE_NAME,null,contentValues);

                }

                //return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
