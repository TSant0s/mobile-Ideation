package com.example.ideation.Retrofit;

import com.example.ideation.ChallengeGroup;
import com.example.ideation.Feedback;
import com.example.ideation.FeedbackChallenge;
import com.example.ideation.Group;
import com.example.ideation.Idea;
import com.example.ideation.Post;
import com.example.ideation.User;

import org.json.JSONObject;

import io.reactivex.Observable;
import okhttp3.Challenge;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import java.util.List;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Path;
import retrofit2.http.Url;


public interface INodeJS {

    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);



    @GET("challenge")
    Call<List<ChallengeGroup>> getChallenges();

    @GET("group")
    Call<List<Group>> getGroups();

    @GET("feedback/{groupId}")
    Call<List<Feedback>> getGroupFeedback(@Path("groupId") String grupoId);

    @GET("feedback/challenge/{challengeId}")
    Call<List<FeedbackChallenge>> getChallengeFeedback(@Path("challengeId") String challengeId);

  @GET("user/{email}")
    Call<List<User>> getUserInfo(@Path("email") String email);

  @POST("group/new")
    Call<Group> createNewGroup(@Body Group group);

    @POST("feedback/new")
    Call<Feedback> createNewFeedback(@Body Feedback feedback);

    @POST("feedback/challenge/new")
    Call<FeedbackChallenge> createNewFeedbackChallenge(@Body FeedbackChallenge feedbackChallenge);

    @POST("challenge/new")
    Call<ChallengeGroup> createNewChallengeGroup(@Body ChallengeGroup challengeGroup);










}
