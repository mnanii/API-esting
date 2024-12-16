package retrofit;

import client.TrelloClient;
import model.Board;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utility.TestUtil;

import java.io.IOException;

public class RetrofitBaseTest {

    public Board createBoard(String name) throws IOException {

        String key = TestUtil.KEY;
        String token = TestUtil.TOKEN;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.trello.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        TrelloClient trelloClient = retrofit.create(TrelloClient.class);

        Call<Board> board = trelloClient.createBoard(name, key, token);
        Response<Board> createBordResponse = board.execute();

        return createBordResponse.body();

    }
}
