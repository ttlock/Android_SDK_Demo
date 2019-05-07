package ttlock.demo.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by
 */
public interface ApiService {


    /**
     * development environment
     */
    public static final String CLIENT_ID = "439063e312444f1f85050a52efcecd2e";
    public static final String CLIENT_SECRET = "0ef1c49b70c02ae6314bde603d4e9b05";
    public static final String REDIRECT_URI = "http://open.ttlock.com.cn";

    @POST("/lockRecords/fromLock")
    @FormUrlEncoded
    Call<Error> uploadRecords(@Field("lockId") int lockId, @Field("records") String records);

    @POST("/room/registerNb")
    @FormUrlEncoded
    Call<Error> registerNb(@Field("lockId") int lockId, @Field("nbNodeId") String nbNodeId, @Field("nbCardNumber") String nbCardNumber, @Field("nbRssi") int nbRssi, @Field("nbOperator") String nbOperator);

    @POST("/oauth2/token")
    @FormUrlEncoded
    Call<String> auth(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType, @Field("username") String username, @Field("password") String password, @Field("redirect_uri") String redirectUri);

    @POST("/v3/lock/list")
    @FormUrlEncoded
    Call<String> getLockList(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize, @Field("date") long date);

    @POST("/v3/lock/initialize")
    @FormUrlEncoded
    Call<ResponseBody> lockInit(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockData") String lockData, @Field("lockAlias") String alias, @Field("date") long date);

    @GET("/v3/key/list")
    Call<ResponseBody> getUserKeyList(@QueryMap Map<String, String> params);

    @POST("/v3/lock/resetKey")
    @FormUrlEncoded
    Call<ResponseBody> restEkey(@Field("clientId") String clientId, @Field("accessToken") String accessToken,@Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/lock/delete")
    @FormUrlEncoded
    Call<ResponseBody> deleteLock(@Field("clientId") String clientId, @Field("accessToken") String accessToken,@Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/lock/resetKeyboardPwd")
    @FormUrlEncoded
    Call<ResponseBody> resetPasscode(@Field("clientId") String clientId, @Field("accessToken") String accessToken,@Field("lockId") int lockId,  @Field("pwdInfo") String pwdInfo,@Field("timestamp") long timestamp, @Field("date") long date);

    @POST("/v3/lock/changeAdminKeyboardPwd")
    @FormUrlEncoded
    Call<ResponseBody> changeAdminPasscode(@FieldMap Map<String, String> params);

    @POST("/v3/fingerprint/add")
    @FormUrlEncoded
    Call<ResponseBody> addFingerprint(@FieldMap Map<String, String> params);

    @GET("/v3/fingerprint/list")
    Call<ResponseBody> getUserFingerprintList(@QueryMap Map<String, String> params);

    @POST("/v3/fingerprint/delete")
    @FormUrlEncoded
    Call<ResponseBody> deleteFingerprint(@FieldMap Map<String, String> params);

    @POST("/v3/fingerprint/clear")
    @FormUrlEncoded
    Call<ResponseBody> clearFingerprints(@Field("clientId") String clientId, @Field("accessToken") String accessToken,@Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/identityCard/add")
    @FormUrlEncoded
    Call<ResponseBody> addICCard(@FieldMap Map<String, String> params);

    @GET("/v3/identityCard/list")
    Call<ResponseBody> getUserICCardList(@QueryMap Map<String, String> params);

    @POST("/v3/identityCard/delete")
    @FormUrlEncoded
    Call<ResponseBody> deleteICCard(@FieldMap Map<String, String> params);

    @POST("/v3/identityCard/changePeriod ")
    @FormUrlEncoded
    Call<ResponseBody> modifyICCardPeriod(@FieldMap Map<String, String> params);

    @POST("/v3/identityCard/clear")
    @FormUrlEncoded
    Call<ResponseBody> clearICCards(@Field("clientId") String clientId, @Field("accessToken") String accessToken,@Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/gateway/upgradeCheck")
    @FormUrlEncoded
    Call<String> gatewayUpgradeCheck(@Field("clientId") String clientId, @Field("accessToken") String accessToken,@Field("gatewayId") int gatewayId, @Field("date") long date);

    @POST("/v3/gateway/isInitSuccess")
    @FormUrlEncoded
    Call<String> gatewayIsInitSuccess(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("gatewayNetMac") String gatewayNetMac, @Field("date") long date);

    @POST("/v3/gateway/list")
    @FormUrlEncoded
    Call<String> getGatewayList(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize, @Field("date") long date);

    @POST("/v3/lock/upgradeCheck")
    @FormUrlEncoded
    Call<String> lockUpgradeCheck(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/lock/upgradeRecheck")
    @FormUrlEncoded
    Call<String> lockUpgradeCheckAgain(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("firmwareInfo") String firmwareInfo, @Field("lockId") int lockId, @Field("date") long date);
}
