package liang.zhou.lane8.no5.my_business.login_register;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_network.Callback;

public class DefaultL_R extends Login_Register{


    @Override
    public String login(final String username, final String password,final User user) {
        my_poster.setServerURL(Constant.HOST+"LoginServlet");
        return getServerResult(username,password,user);
    }

    @Override
    protected String register(User user) {
        my_poster.setServerURL(Constant.HOST+"RegisterServlet");
        return getServerResult(user.getUsername(),user.getPassword(),null);
    }

    private String getServerResult(String username,String password,final User user){
        final boolean networkAccessed[]={false};
        final String message[]=new String[1];
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DefaultL_R","beforePostData");
        my_poster.posterData(jsonObject.toString().getBytes(), new Callback() {
            @Override
            public void onSuccess(byte[] result) {
                Log.d("DefaultL_R","onSuccess");
                String resultStr=new String(result);
                Log.d("DefaultL_R","onSuccess"+resultStr);
                try {
                    JSONObject resultJSON=new JSONObject(resultStr);
                    message[0]=resultJSON.getString("serverResult");
                    if(user!=null) {
                        user.setUserId(Integer.parseInt(resultJSON.getString("userId")));
                        user.setUsername(resultJSON.getString("username"));
                        user.setPassword(resultJSON.getString("password"));
                        user.setSex(resultJSON.getString("sex"));
                        user.setBirthday(resultJSON.getString("birthday"));
                        user.setAlwaysAppearance(resultJSON.getString("alwaysAppearance"));
                        user.setHomeAddress(resultJSON.getString("homeAddress"));
                        user.setMailBox(resultJSON.getString("mailBox"));
                        user.setMobile(resultJSON.getString("mobile"));
                        user.setPortrait(resultJSON.getString("portrait"));
                        user.setSubscribedFuncId(resultJSON.getString("subscribedFuncId"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                networkAccessed[0]=true;
            }

            @Override
            public void onFailure() {
                message[0]="服务器连接异常";
                networkAccessed[0]=true;
            }
        });
        while(!networkAccessed[0]){}
        return message[0];
    }
}
