package com.example.ddlist;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MainActivity extends Activity {
	
	String url = "http://12.7.8.1/XMQQMobile/service";
	PullToRefreshListView list;
	Button btn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mian);

		list = (PullToRefreshListView) findViewById(R.id.MyListView);
		btn = (Button) findViewById(R.id.btn);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doRequset();
			}
		});

		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemTitle", "This is Title.....");
			map.put("ItemText", "This is text.....");
			mylist.add(map);
		}

		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.list,

				new String[] { "ItemTitle", "ItemText" },

				new int[] { R.id.ItemTitle, R.id.ItemText });
		

		list.setMode(Mode.BOTH);
		list.setAdapter(mSchedule);
		list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				doRequset();
				
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				doRequset();
				
			}
		});

	}
	
	protected void doRequset() {
		String json = 
				"{\"head\": {\"serviceCode\":\"QuerySiMuProductList\"," +
				"\"token\":\"da9c5889-5447-429f-9d9c-f365146f31f4\"," +
				"\"functionCode\":\"\"," +
				"\"channel\":\"android\"},\"request\":{\"pageNum\": 1}}";
		
		Log.d("JOSn", json);
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("MESSAGE", json);
		Log.d("params", params.getHeaders() + "");
		
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				url,
				params,
				new RequestCallBack<String>() {
			@Override
			public void onStart() {
				Toast.makeText(MainActivity.this, "conn...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				Toast.makeText(MainActivity.this, current + "/" + total, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Toast.makeText(MainActivity.this, "upload response:" + responseInfo.result, Toast.LENGTH_SHORT).show();
				list.onRefreshComplete();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				list.onRefreshComplete();
				Toast.makeText(MainActivity.this, "!!! + " + msg, Toast.LENGTH_SHORT).show();
			}
		});

	}
}
