package com.khopan.api.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import androidx.annotation.NonNull;

import com.khopan.api.common.R;
import com.khopan.api.common.activity.IFragmentActivity;
import com.khopan.api.common.fragment.CenterButtonTextFragment;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NetworkUtils {
	private NetworkUtils() {}

	public static boolean isInternalAvailable(@NonNull Context context) {
		ConnectivityManager manager = context.getSystemService(ConnectivityManager.class);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return !(info == null || !info.isConnected());
	}

	public static InetAddress[] getLocalIpAddress(@NonNull Context context) {
		WifiManager wifiManager = context.getSystemService(WifiManager.class);
		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		int ipAddress = connectionInfo.getIpAddress();
		String ipString = Formatter.formatIpAddress(ipAddress);
		String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
		List<InetAddress> addressList = new ArrayList<>();
		List<Thread> threadList = new ArrayList<>();

		for(int i = 0; i < 255; i++) {
			String host = prefix + i;
			Thread thread = new Thread(() -> {
				try {
					InetAddress address = InetAddress.getByName(host);

					if(address.isReachable(1000)) {
						addressList.add(address);
					}
				} catch(Throwable Errors) {
					throw new RuntimeException("Error while getting local IP addresses", Errors);
				}
			});

			threadList.add(thread);
			thread.start();
		}

		for(Thread thread : threadList) {
			try {
				thread.join();
			} catch(Throwable Errors) {
				throw new RuntimeException("Error while joining worker threads", Errors);
			}
		}

		addressList.sort(Comparator.comparingInt(x -> Byte.toUnsignedInt(x.getAddress()[3])));
		return addressList.toArray(new InetAddress[0]);
	}

	public static <T extends Context & IFragmentActivity> void assertInternet(T activity, Runnable action) {
		new Object() {
			{ this.tryConnect(); }

			private void tryConnect() {
				if(!NetworkUtils.isInternalAvailable(activity)) {
					activity.setFragment(new CenterButtonTextFragment(activity.getString(R.string.noInternetConnection), activity.getString(R.string.retry), this :: tryConnect));
				} else {
					action.run();
				}
			}
		};
	}
}
