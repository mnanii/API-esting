package apacheHttpClient;

import model.DeleteBoardResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.testng.AssertJUnit;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.assertEquals;

public class DeleteBoardApiTest extends ApacheHttpBaseTest {

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
    public void when_deleteBoard_then_BordNotFound() throws IOException, URISyntaxException {
        URI uri = baseURI()
                .setPath("1/boards/" + boardId)
                .build();

        HttpDelete deleteRequest = new HttpDelete(uri);
        Response<DeleteBoardResponse> deleteResponse = sendRequest(deleteRequest, DeleteBoardResponse.class);
        assertThat(deleteResponse.getStatusCode(), equalTo(HttpStatus.SC_OK));

        HttpGet getRequest = new HttpGet(uri);
        Response<String> getResponse = sendRequest(getRequest);
        assertThat(getResponse.getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertEquals("The requested resource was not found.", getResponse.getResponseBody());
    }

    @Test
    public void when_createBoardWithInvalidId_then_badRequest() throws URISyntaxException, IOException {
        URI uri = baseURI()
                .setPath("1/boards/" + "123")
                .build();
        HttpDelete httpDelete = new HttpDelete(uri);
        Response<String> deleteResponse = sendRequest(httpDelete);
        assertThat(deleteResponse.getStatusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        AssertJUnit.assertEquals("invalid id", deleteResponse.getResponseBody());
    }

    @Test
    public void when_deleteBoardWithInvalidKey_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI()
                .setPath("1/boards/" + boardId)
                .setParameter("key", "aaaa")
                .build();
        HttpDelete httpDelete = new HttpDelete(uri);
        Response<String> deleteResponse = sendRequest(httpDelete);
        assertThat(deleteResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        AssertJUnit.assertEquals("invalid key", deleteResponse.getResponseBody());
    }

    @Test
    public void when_createBoardWithoutKey_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI()
                .setPath("1/boards/" + boardId)
                .setParameter("key", null)
                .build();
        HttpDelete httpDelete = new HttpDelete(uri);
        Response<String> deleteResponse = sendRequest(httpDelete);
        assertThat(deleteResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        AssertJUnit.assertEquals("invalid app key", deleteResponse.getResponseBody());
    }

    @Test
    public void when_createBoardWithInvalidToken_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI()
                .setPath("1/boards/" + boardId)
                .setParameter("token", "invalidToken")
                .build();
        HttpDelete httpDelete = new HttpDelete(uri);
        Response<String> deleteResponse = sendRequest(httpDelete);
        assertThat(deleteResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        AssertJUnit.assertEquals("invalid app token", deleteResponse.getResponseBody());
    }

    @Test
    public void when_createBoardWithoutToken_then_Unauthorized() throws URISyntaxException, IOException {
        URI uri = baseURI()
                .setPath("1/boards/" + boardId)
                .setParameter("token", null)
                .build();
        HttpDelete httpDelete = new HttpDelete(uri);
        Response<String> deleteResponse = sendRequest(httpDelete);
        assertThat(deleteResponse.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        AssertJUnit.assertEquals("unauthorized permission requested", deleteResponse.getResponseBody());
    }

}

