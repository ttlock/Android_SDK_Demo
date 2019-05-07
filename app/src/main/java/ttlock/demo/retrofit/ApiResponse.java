package ttlock.demo.retrofit;

public class ApiResponse<T> {

    /**
     * network request error response
     */
    public interface ErrorListener {
        void onErrorResponse(Throwable requestError);
    }

    /**
     * network request success response result
     * @param <T> data result
     */
    public interface Listener<T> {
        void onResponse(T result);
    }
}
