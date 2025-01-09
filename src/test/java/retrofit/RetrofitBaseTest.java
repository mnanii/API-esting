package retrofit;

import client.TrelloClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Board;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utility.TestUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

abstract class RetrofitBaseTest {
    protected String boardId;
    protected TrelloClient trelloClient;
    protected Properties properties;
    protected final ObjectMapper objectMapper = new ObjectMapper();


    protected void initializeResources() throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties.load(fis);
    }

    @AfterMethod
    public void tearDown() throws IOException {
        if (boardId != null) {
            deleteBoard(boardId);
            System.out.println("Board has been deleted.");
        }
    }

    protected String getKey() {
        return properties.getProperty(TestUtil.KEY);
    }

    protected String getToken() {
        return properties.getProperty(TestUtil.TOKEN);
    }

    protected Board createBoard(String name) throws IOException {

        String key = properties.getProperty(TestUtil.KEY);
        String token = properties.getProperty(TestUtil.TOKEN);

        Call<Board> board = trelloClient.createBoard(name, key, token);
        Response<Board> createBordResponse = board.execute();

        return createBordResponse.body();
    }

    protected TrelloClient buildTrelloClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.trello.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(TrelloClient.class);
    }

    protected void deleteBoard(String boardId) throws IOException {
        Call<Board> boardCall = trelloClient.deleteBoard(boardId, getKey(), getToken());
        boardCall.execute();
    }

    protected <T> T parseJsonBody(String jsonBody, Class<T> type){
        try {
            JSONObject errorBody = new JSONObject(jsonBody);
            return objectMapper.readValue(errorBody.toString(), type);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
