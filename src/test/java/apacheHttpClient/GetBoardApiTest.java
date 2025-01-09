package apacheHttpClient;

import model.Board;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static utility.TestUtil.BOARD_NAME;

public class GetBoardApiTest extends ApacheHttpBaseTest {

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @BeforeMethod
    public void setUp() throws URISyntaxException, IOException {
        boardId = createBoard(BOARD_NAME);
        System.out.println("Board has been created.");
    }

    @Test
    public void when_getBoard_then_ValidRequest() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .build();

        HttpGet getRequest = new HttpGet(uri);

        Response<Board> response = sendRequest(getRequest, Board.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertNotNull(response.getResponseBody());
    }

    @Test
    public void when_getBoardWithInvalidId_then_badRequest() throws URISyntaxException, IOException {
        URI uri = baseURI().setPath("1/boards/" + "InvalidBoardId")
                .build();
        HttpGet getRequest = new HttpGet(uri);
        Response<String> getResponse = sendRequest(getRequest);
        assertThat(getResponse.getStatusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        AssertJUnit.assertEquals("invalid id", getResponse.getResponseBody());
    }


    @Test
    public void when_getBoardWithInvalidKey_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("key", "invalidKey")
                .build();
        HttpGet getRequest = new HttpGet(uri);
        Response<String> getResponse = sendRequest(getRequest);
        assertThat(getResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(getResponse.getResponseBody(), "invalid key");
    }

    @Test
    public void when_getBoardWithoutKey_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("key", "")
                .build();
        HttpGet getRequest = new HttpGet(uri);
        Response<String> getResponse = sendRequest(getRequest);
        assertThat(getResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(getResponse.getResponseBody(), "invalid app key");
    }

    @Test
    public void when_getBoardWithInvalidToken_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("token", "123")
                .build();
        HttpGet getRequest = new HttpGet(uri);
        Response<String> getResponse = sendRequest(getRequest);
        assertThat(getResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(getResponse.getResponseBody(), "invalid app token");
    }

    @Test
    public void when_getBoardWithoutToken_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("token", "")
                .build();
        HttpGet getRequest = new HttpGet(uri);
        Response<String> getResponse = sendRequest(getRequest);
        assertThat(getResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals(getResponse.getResponseBody(), "unauthorized permission requested");
    }
}
