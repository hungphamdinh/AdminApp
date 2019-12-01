package com.example.adminapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Common {
//  public static User currentUser;
  public static final String UPDATE = "Thay đổi";
  public static final String DELETE= "Xóa";
  public static final int PICK_IMAGE_REQUEST=71;
  public static final String USER_KEY = "User";
  public static final String PWD_KEY= "Password";
  public static boolean isConnectedToInternet(Context context){
    ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(connectivityManager!=null){
      NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
      if(infos!=null){
        for (int i=0;i<infos.length;i++){
          if (infos[i].getState()== NetworkInfo.State.CONNECTED)
            return true;
        }
      }
    }
    return false;
  }

}
