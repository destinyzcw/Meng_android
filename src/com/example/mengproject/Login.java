package com.example.mengproject;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		Button getData = (Button) findViewById(R.id.getservicedata);
		getData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String restURL = "http://arcane-sands-75613.herokuapp.com/v1/login";
				new RestOperation_Signin().execute(restURL);

			}
		});
		
		Button signup = (Button) findViewById(R.id.signup);
		signup.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("please enter username and password");
                View view = LayoutInflater.from(Login.this).inflate(R.layout.register, null);
                builder.setView(view);
                
                final EditText username = (EditText)view.findViewById(R.id.username_re);
                final EditText password = (EditText)view.findViewById(R.id.password_re);
                final EditText email = (EditText)view.findViewById(R.id.email_re);
                final EditText confirm = (EditText)view.findViewById(R.id.confirmpassword_re);
                
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    	String confirmStr = confirm.getText().toString().trim();
                        String passwordStr = password.getText().toString().trim();
                        String emailStr = email.getText().toString().trim();
                        String usernameStr = username.getText().toString().trim();
                    
                        if(confirmStr.equals(passwordStr)){
                        	String restURL = "http://arcane-sands-75613.herokuapp.com/v1/register";
            				new RestOperation_Signup().execute(restURL, usernameStr, emailStr, passwordStr);
                        }
                        else{
                        	Toast.makeText(Login.this, "password not match", Toast.LENGTH_LONG).show();
                        }
                    } 
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    	
                    }
                });
                builder.show();
            }
        });
	}

	private class RestOperation_Signup extends AsyncTask<String, Void, Void> {

		final HttpClient client = new DefaultHttpClient(); 
		ProgressDialog progressDialog = new ProgressDialog(Login.this);

//		TextView showParsedJSON = (TextView)findViewById(R.id.showParsedJSON);
		List<BasicNameValuePair> dataList;
		String content;
		String error;


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
					dataList.add(new BasicNameValuePair("email", params[2]));
					dataList.add(new BasicNameValuePair("password", params[3]));
					
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
				} catch (ClientProtocolException e) {
					error = e.getMessage();
					e.printStackTrace();
				} catch (IOException e) {
					error = e.getMessage();
					e.printStackTrace();
				}finally{
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
				Toast.makeText(Login.this, "Error" + error, Toast.LENGTH_LONG).show();
			} else {				
			
				JSONObject jsonResponse;
				
				try {
					jsonResponse = new JSONObject(content);
					String success = jsonResponse.getString("message");
//					output = success + System.getProperty("line.separator");					
//					showParsedJSON.setText(output);	
					Toast.makeText(Login.this, success, Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		
	}	
	
	private class RestOperation_Signin extends AsyncTask<String, Void, Void> {

		final HttpClient client = new DefaultHttpClient(); 
		ProgressDialog progressDialog = new ProgressDialog(Login.this);

		EditText email = (EditText)findViewById(R.id.email);
		EditText password = (EditText)findViewById(R.id.password);
		List<BasicNameValuePair> dataList;
		String content;
		String error;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog.setTitle("Please wait ...");
			progressDialog.show();

			dataList = new ArrayList<BasicNameValuePair>();
			dataList.add(new BasicNameValuePair("email", email.getText().toString()));
			dataList.add(new BasicNameValuePair("password", password.getText().toString()));
		}

		@Override
		protected Void doInBackground(String... params) {
			BufferedReader br = null;
			
			try {
				
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
				Toast.makeText(Login.this, "Error" + error, Toast.LENGTH_LONG).show();
			} else {				
			
				JSONObject jsonResponse;
				
				try {
					jsonResponse = new JSONObject(content);
					String success = jsonResponse.getString("error");
					if(success.equals("false")){
						Toast.makeText(Login.this, "login success", Toast.LENGTH_LONG).show();
				        Intent intent = new Intent();
				        intent.setClass(Login.this, Search.class);
				        Login.this.startActivity(intent);
				        Login.this.finish();
					}
					else{
						Toast.makeText(Login.this, "invalid email or password", Toast.LENGTH_LONG).show();
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
				
		}


	}
}
