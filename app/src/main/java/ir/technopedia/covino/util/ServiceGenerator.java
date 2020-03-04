package ir.technopedia.covino.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by uTheLoneWolf on 5/11/2017.
 */

public class ServiceGenerator {

    //    public static final String API_BASE_URL = "http://ramsarfood.com/";
//    public static final String API_BASE_URL = "http://davidmathapp.ir/";
//    public static final String API_FILE_BASE = "https://technopedia.ir/public/";
    public static final String API_BASE_URL = "https://kamranhatami.ir/corona/index.php/";


//    public static CertificatePinner certificatePinner = new CertificatePinner.Builder()
//            .add("technopedia.ir", "sha256/jS9Oh3Sl9ZQrvgRkYr7MDZFWoktjjCv/KtDSxo8o7OM=")
//            .build();

//    public static TLSSocketFactory tlsSocketFactory;
//
//    {
//        try {
//            tlsSocketFactory = new TLSSocketFactory();
//        } catch (KeyManagementException | NoSuchAlgorithmException e) {
//            Log.d(TAG, "Failed to create Socket connection ");
//            e.printStackTrace();
//        }
//    }

//    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().certificatePinner(certificatePinner).connectTimeout(35, TimeUnit.SECONDS).writeTimeout(35, TimeUnit.SECONDS).readTimeout(35, TimeUnit.SECONDS).build();

//    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).connectTimeout(35, TimeUnit.SECONDS).writeTimeout(35, TimeUnit.SECONDS).readTimeout(35, TimeUnit.SECONDS).build();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(35, TimeUnit.SECONDS).writeTimeout(35, TimeUnit.SECONDS).readTimeout(35, TimeUnit.SECONDS).build();

    private static Retrofit.Builder builder = new Retrofit.Builder().client(okHttpClient).baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
