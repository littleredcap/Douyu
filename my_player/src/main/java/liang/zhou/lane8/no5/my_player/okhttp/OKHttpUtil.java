package liang.zhou.lane8.no5.my_player.okhttp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.MyApplication;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.common_utils.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OKHttpUtil {

    public static void uploadImage(int userId,String path,ServerResponse responseListener){
        File file=new File(path);
        String fileName=path.substring(path.lastIndexOf("/"),path.length());
        OkHttpClient client=new OkHttpClient();
        MultipartBody.Builder mBody_builder=new MultipartBody.Builder().
                setType(MultipartBody.FORM).
                addFormDataPart("img",fileName,
                        RequestBody.create(MediaType.parse("image/*"),file)).
                addFormDataPart("userId",String.valueOf(userId));
        RequestBody requestBody=mBody_builder.build();
        Request request=new Request.Builder().
                url(Constant.HOST+"ImageServlet").post(requestBody).build();
        try {
            Log.d("uploadImage",requestBody.contentLength()+"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHttpUtil","onFailure"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("portrait_url",response.body().string());
                responseListener.response(call,response);
            }
        });
    }



    public static void uploadJson(String url,int userId,String key, String value, ServerResponse responseListener){
        JSONObject jsonObject=getJson(userId,key,value);
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody= FormBody.create(MediaType.parse("application/json"),jsonObject.toString());
        Log.d("uploadJson",jsonObject.toString());
        Request request=new Request.Builder().
                url(url).
                post(requestBody).build();
        //"http://192.168.88.100:8080/Douyu4/SendEmailServlet"
        /*Request request=new Request.Builder().
                url("http://www.baidu.com").build();*/
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response){
                if(responseListener!=null) {
                    responseListener.response(call,response);
                }
            }
        });
    }

    public static void uploadJSONs(String url,String json,ServerResponse serverResponse){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=FormBody.create(MediaType.parse("application/json"),json);
        Request request=new Request.Builder().url(url).post(requestBody).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                serverResponse.response(call,response);
            }
        });
    }

    private static JSONObject getJson(int userId,String key, String value) {
        JSONObject object=new JSONObject();
        try {
            object.put("userId",userId);
            object.put(key,value);
            object.put("updatedColumn", StringUtils.upperCaseFirstChar(key));
            Log.d("getJson",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }



    public static void uploadKeyValue(String key,String value){
        OkHttpClient client=new OkHttpClient();
        //RequestBody requestBody= FormBody.create(MediaType.parse("application/json"),jsonObject.toString());
        MultipartBody.Builder builder=new MultipartBody.Builder().addFormDataPart(key,value);
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().
                url("http://192.168.88.101:8080/Douyu4/UpdatePersonalInfoServlet").
                post(requestBody).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("uploadKeyValue","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}
