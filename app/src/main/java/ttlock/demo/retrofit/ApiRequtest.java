package ttlock.demo.retrofit;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class ApiRequtest<T> {
    private static final String TAG = ApiRequtest.class.getSimpleName();

    private TypeToken<T> mResultType;

    public ApiRequtest (Call<ResponseBody> call, TypeToken<T> resultType, final ApiResponse.Listener<ApiResult<T>> listener, final ApiResponse.ErrorListener errorListener){
        mResultType = resultType;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    ResponseBody responseBody = response.body();
                    if(responseBody != null){
                        String jsonString = response.body().string();
                        final ApiResult<T> result = new ApiResult<>(new JSONObject(jsonString), mResultType);
                        listener.onResponse(result);
                    }

                }catch (IOException e){
                }catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorListener.onErrorResponse(t);
            }
        });
    }



}
