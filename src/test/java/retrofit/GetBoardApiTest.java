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

import java.io.IOException;
import static org.testng.AssertJUnit.*;
import static utility.TestUtil.KEY;
import static utility.TestUtil.TOKEN;

public class GetBoardApiTest extends RetrofitBaseTest {

    @Test
    public void getBoardTestApi() throws IOException {
        Board board = createBoard("MyBoard8");
        System.out.println(board);

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

        Call<Board> call = trelloClient.getBoardById(board.getId(), KEY, TOKEN);

        Response<Board> response = call.execute();
        System.out.println(response.code());


        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertNotNull(response.body().getId());
        assertNotNull(response.body().getName());
        assertNotNull(response.body().getDesc());



    }

}
