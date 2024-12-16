package client;

import model.Board;
import model.BoardUpdateRequest;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloClient {

    @POST("/1/boards")
    Call<Board> createBoard(@Query("name") String name, @Query("key") String key, @Query("token") String token);

    @GET("/1/boards/{id}")
    Call<Board> getBoardById(@Path("id") String id, @Query("key") String key, @Query("token") String token);

    @PUT("/1/boards/{id}")
    Call<Board> updateBoard(@Path("id") String id, @Query("key") String key, @Query("token") String token, @Body BoardUpdateRequest boardUpdateRequest);

    @DELETE("/1/boards/{id}")
    Call<Board> deleteBoard(@Path("id") String id, @Query("key") String key, @Query("token") String token);



}
