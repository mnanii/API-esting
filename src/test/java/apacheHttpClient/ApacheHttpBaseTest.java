package apacheHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Board;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import utility.TestUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;


abstract class ApacheHttpBaseTest {
    protected String boardId;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected Properties properties;

    public void initializeResources() throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties.load(fis);
    }

    @AfterMethod
    public void tearDown() throws URISyntaxException, IOException {
        deleteBoard(boardId);
        System.out.println("Board has been deleted." + boardId);
    }

    protected String createBoard(String boardName) throws URISyntaxException, IOException {
        URI uri = baseURI().addParameter("name", boardName)
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<Board> response = sendRequest(postRequest, Board.class);
        return response.getResponseBody().getId();
    }

    protected void deleteBoard(String boardId) throws URISyntaxException, IOException {
        URI uri = baseURI()
                .setPath("1/boards/" + boardId)
                .build();
        HttpDelete deleteRequest = new HttpDelete(uri);
        sendRequest(deleteRequest);
    }

    protected URIBuilder baseURI() throws URISyntaxException {
        return new URIBuilder("https://api.trello.com/1/boards/")
                .addParameter("key", properties.getProperty(TestUtil.KEY))
                .addParameter("token", properties.getProperty(TestUtil.TOKEN));
    }

    protected <T> Response<T> sendRequest(HttpUriRequest httpRequest, Class<T> returnType) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            T responseType = objectMapper.readValue(responseBody, returnType);
            return new Response<>(statusCode, responseType);
        }
    }

    protected Response<String> sendRequest(HttpUriRequest httpRequest) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            return new Response<>(statusCode, responseBody);
        }
    }
}
