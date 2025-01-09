package apacheHttpClient;

import model.Board;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.assertEquals;
import static utility.TestUtil.BOARD_NAME;

public class UpdateBoardApiTest extends ApacheHttpBaseTest {

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @BeforeMethod
    public void setUp() throws URISyntaxException, IOException {
        boardId = createBoard(BOARD_NAME);
        System.out.println("Board has been created." + boardId);
    }

    @Test
    public void when_updateBoardWithValidName_then_boardUpdated() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .addParameter("name", "MyBoard")
                .build();
        HttpPut putRequest = new HttpPut(uri);
        Response<Board> response = sendRequest(putRequest, Board.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_OK));

        HttpGet httpGet = new HttpGet(uri);
        Response<Board> getResponse = sendRequest(httpGet, Board.class);
        assertEquals("MyBoard", getResponse.getResponseBody().getName());
    }

    @Test
    public void when_updateBoardWithValidData_then_boardUpdated() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .build();
        HttpPut putRequest = new HttpPut(uri);

        Board board = new Board();
        board.setDesc("name");
        String requestJson = objectMapper.writeValueAsString(board);
        StringEntity requestBody = new StringEntity(requestJson, ContentType.APPLICATION_JSON);
        putRequest.setEntity(requestBody);

        Response<Board> response = sendRequest(putRequest, Board.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_OK));

        HttpGet httpGet = new HttpGet(uri);
        Response<Board> getResponse = sendRequest(httpGet, Board.class);
        assertEquals("name", getResponse.getResponseBody().getDesc());
    }


    @Test
    public void when_updateBoardWithInvalidName_then_badRequest() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .addParameter("name", null)
                .build();
        HttpPut putRequest = new HttpPut(uri);
        Response<Board> response = sendRequest(putRequest, Board.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        HttpGet httpGet = new HttpGet(uri);
        Response<Board> getResponse = sendRequest(httpGet, Board.class);
        assertEquals(BOARD_NAME, getResponse.getResponseBody().getName());
    }

    @Test
    public void when_updateBoardWithInvalidKey_then_Unauthorized() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("key", "123")
                .build();
        HttpPut putRequest = new HttpPut(uri);
        Response<String> putResponse = sendRequest(putRequest);
        assertThat(putResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals("invalid key", putResponse.getResponseBody());
    }

    @Test
    public void when_updateBoardWithoutKey_then_Unauthorized() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("key", null)
                .build();
        HttpPut putRequest = new HttpPut(uri);
        Response<String> putResponse = sendRequest(putRequest);
        assertThat(putResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals("invalid app key", putResponse.getResponseBody());
    }

    @Test
    public void when_updateBoardWitInvalidToken_then_Unauthorized() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("token", "123")
                .build();
        HttpPut putRequest = new HttpPut(uri);
        Response<String> putResponse = sendRequest(putRequest);
        assertThat(putResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals("invalid app token", putResponse.getResponseBody());
    }

    @Test
    public void when_updateBoardWithoutToken_then_Unauthorized() throws IOException, URISyntaxException {
        URI uri = baseURI().setPath("1/boards/" + boardId)
                .setParameter("token", null)
                .build();
        HttpPut putRequest = new HttpPut(uri);
        Response<String> putResponse = sendRequest(putRequest);
        assertThat(putResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertEquals("unauthorized permission requested", putResponse.getResponseBody());
    }
}
