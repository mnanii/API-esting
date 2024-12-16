package apacheHttpClient;

import model.Board;
import model.InvalidBoardResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateBoardApiTest extends ApacheHttpBaseTest{

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @AfterMethod
    public void tearDown() throws URISyntaxException, IOException {
        deleteBoard(boardId);
        System.out.println("Board has been deleted.");
    }

    @Test
    public void when_sendValidRequest_then_boardCreated() throws IOException, URISyntaxException {
        URI uri = baseURI().addParameter("name", "MyBoard")
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<Board> response = sendRequest(postRequest, Board.class);
        boardId = response.getResponseBody().getId();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertNotNull(response.getResponseBody());
        assertNotNull(response.getResponseBody().getId());
    }

    @Test
    public void when_sendRequestWithInvalidName_then_badRequest() throws URISyntaxException, IOException {
        URI uri = baseURI().addParameter("name", "")
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<InvalidBoardResponse> response = sendRequest(postRequest, InvalidBoardResponse.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        InvalidBoardResponse invalidBoardResponse = response.getResponseBody();
        assertEquals(invalidBoardResponse.getMessage(), "invalid value for name");
        assertEquals(invalidBoardResponse.getError(), "ERROR");
    }

    @Test
    public void when_createBoardWithInvalidKey_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().addParameter("name", "MyBoard")
                .setParameter("key", "invalidKey")
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<String> postResponse = sendRequest(postRequest);
        assertThat(postResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(postResponse.getResponseBody(), "invalid key");
    }

    @Test
    public void when_createBoardWithoutKey_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().addParameter("name", "MyBoard")
                .setParameter("key", "")
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<String> postResponse = sendRequest(postRequest);
        assertThat(postResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(postResponse.getResponseBody(), "invalid app key");
    }

    @Test
    public void when_createBoardWithInvalidToken_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().addParameter("name", "MyBoard")
                .setParameter("token", "invalidToken")
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<String> postResponse = sendRequest(postRequest);
        assertThat(postResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(postResponse.getResponseBody(), "invalid app token");
    }

    @Test
    public void when_createBoardWithoutToken_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().addParameter("name", "MyBoard")
                .setParameter("token", null)
                .build();
        HttpPost postRequest = new HttpPost(uri);
        Response<String> postResponse = sendRequest(postRequest);
        assertThat(postResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(postResponse.getResponseBody(), "unauthorized permission requested");
    }
}
