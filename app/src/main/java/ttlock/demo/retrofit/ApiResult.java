package ttlock.demo.retrofit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;


/**
 *
 */
public class ApiResult<T> {

    private final static String TAG = ApiResult.class.getSimpleName();

    public static final int OK = 0;

    public Boolean success = true;
    private Integer code = OK;
    private String msg="";
    private T result;
    private String msgJson;
    private static Gson gson = new Gson();


    public ApiResult() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ApiResult(JSONObject jsonObject, TypeToken<T> typeToken) {
        super();
        try{
            if (jsonObject.has("errcode")){
                code = jsonObject.getInt("errcode");
                if (code != OK) {
                    if (jsonObject.has("errmsg")) {
                        msg = jsonObject.getString("errmsg");
                    }

                    success = false;

                }
            }else{

            }

            if (code == OK){
                try {
                    result = gson.fromJson(jsonObject.toString(), typeToken.getType());
                }catch (Exception ex){

                }

            }
        }catch (Exception e) {
            // TODO: handle exception
        }

    }
    private T testDefault(String msg){
        Gson gson = new Gson();
        result =  gson.fromJson(msg,new TypeToken<T>(){}.getType());
        return result;
    }
    private T test(String msg, TypeToken<T> typeToken){
        Gson gson = new Gson();
        result =  gson.fromJson(msg,typeToken.getType());
        return result;
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsgJson() {
        return msgJson;
    }
}
