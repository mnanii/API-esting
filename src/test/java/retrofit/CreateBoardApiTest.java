package retrofit;

import client.TrelloClient;
import model.Board;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utility.TestUtil;

import java.io.IOException;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertNotNull;

public class CreateBoardApiTest {

    @Test
    public void createBoardTest() throws IOException {
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

        Call<Board> board = trelloClient.createBoard("name", TestUtil.KEY, TestUtil.TOKEN);
        Response<Board> createBordResponse = board.execute();

        System.out.println(createBordResponse);

        assertTrue(createBordResponse.isSuccessful());
        assertEquals(200, createBordResponse.code());
        assertNotNull(createBordResponse.body());
        assertNotNull(createBordResponse.body().getId());
        assertNotNull(createBordResponse.body().getName());
        assertNotNull(createBordResponse.body().getDesc());

    }
}
