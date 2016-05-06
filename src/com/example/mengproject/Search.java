package com.example.mengproject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		final EditText bookname = (EditText) findViewById(R.id.booksearch);
		
		Button search = (Button) findViewById(R.id.searchbook);
		
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = bookname.getText().toString();
				try {
					name = URLEncoder.encode(name, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String restURL = "http://arcane-sands-75613.herokuapp.com/v1/findrelated/" + name;
				new RestOperation_GetList().execute(restURL);
			}	
		});		
		
	}
	
	
	private class RestOperation_GetList extends AsyncTask<String, Void, Void> {

		final HttpClient client = new DefaultHttpClient(); 
		ProgressDialog progressDialog = new ProgressDialog(Search.this);
		String content;
		String error;
		ListView list = (ListView) findViewById(R.id.list);
		private List<String> data_list;
		private ArrayAdapter<String> arr_adapter;
		



		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog.setTitle("Please wait ...");
			progressDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			BufferedReader br = null;
			
			try {
				
				String url = new String(params[0]);
				Log.d("url", url);
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = client.execute(httpGet);
				HttpEntity responseEntity = response.getEntity();
				br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line=br.readLine())!=null){
					sb.append(line);
				}
				content = sb.toString();

			} catch (MalformedURLException e) {
				error = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				error = e.getMessage();
				e.printStackTrace();
			} finally {
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			if(error!=null) {
				Toast.makeText(Search.this, "Error" + error, Toast.LENGTH_LONG).show();
			} else {				
			
				JSONObject jsonResponse;
				
				try {
					jsonResponse = new JSONObject(content);					
					JSONArray data=jsonResponse.getJSONArray("title");
					if (data == null) return;
					data_list = new ArrayList<String>();
					for(int i=0; i< data.length(); i++){
						data_list.add(data.getString(i));
					}
					arr_adapter = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, data_list);
					list.setAdapter(arr_adapter);
					
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
							// TODO Auto-generated method stub

							String name = ((TextView) view).getText().toString();
							String restURL = "http://arcane-sands-75613.herokuapp.com/v1/getbooks/";
							new RestOperation_GetLocation().execute(restURL, name);
							
						}
					});
					
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
				
		}


	}
	
private class RestOperation_GetLocation extends AsyncTask<String, Void, Void>{
		

		final HttpClient client = new DefaultHttpClient(); 
		ProgressDialog progressDialog = new ProgressDialog(Search.this);
		String content;
		String error;
		List<BasicNameValuePair> dataList;




		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog.setTitle("Please wait ...");
			progressDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			BufferedReader br = null;
			
			try {
				dataList = new ArrayList<BasicNameValuePair>();
				dataList.add(new BasicNameValuePair("name", params[1]));
				String url = new String(params[0]);
				HttpPost httpPost = new HttpPost(url);
				HttpEntity entity = new UrlEncodedFormEntity(dataList, "UTF-8");
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				HttpEntity responseEntity = response.getEntity();
				br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line=br.readLine())!=null){
					sb.append(line);
				}
				content = sb.toString();
				Log.i("Content", content);

			} catch (MalformedURLException e) {
				error = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				error = e.getMessage();
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			if(error!=null) {
				Toast.makeText(Search.this, "Error" + error, Toast.LENGTH_LONG).show();
			} else {				
			
				JSONObject jsonResponse;
				
				try {
					jsonResponse = new JSONObject(content);					
					String loc = jsonResponse.getString("location");
					String t = jsonResponse.getString("title");
					String a = jsonResponse.getString("author");
					String e = jsonResponse.getString("edition");
					String p = jsonResponse.getString("publisher");
					
					Intent intent = new Intent(Search.this, Detail.class);
					intent.putExtra("location", loc);
					intent.putExtra("title", t);
					intent.putExtra("author", a);
					intent.putExtra("edition", e);
					intent.putExtra("publisher", p);
					startActivity(intent);
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
				
		}
	}
	
}
