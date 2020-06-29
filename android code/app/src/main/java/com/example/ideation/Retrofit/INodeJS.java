package com.example.ideation.Retrofit;

import com.example.ideation.ChallengeGroup;
import com.example.ideation.Feedback;
import com.example.ideation.FeedbackChallenge;
import com.example.ideation.Group;
import com.example.ideation.Idea;
import com.example.ideation.IncreasedChallengeFeedback;
import com.example.ideation.Post;
import com.example.ideation.Reward;
import com.example.ideation.Transaction;
import com.example.ideation.User;
import com.example.ideation.UserChallenge;
import com.example.ideation.userGroups;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.Observable;
import okhttp3.Challenge;
import okhttp3.ResponseBody;
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
    @GET("userChallenges/{userID}")
    Call<List<UserChallenge>> getUserChallenges(@Path("userID")int userID);

    @GET("group/{groupID}")
    Call<List<Group>> getGroups(@Path("groupID") int groupID);
    @GET("userGroups/{userID}")
    Call<List<userGroups>> getUserGroups(@Path("userID") int userID);
    @GET("transactions/{userID}")
    Call<List<Transaction>> getTransactionByUser(@Path("userID") int userID);

    @GET("feedback/{groupId}")
    Call<List<Feedback>> getGroupFeedback(@Path("groupId") String groupId);

    @GET("feedback/challenge/{challengeId}")
    Call<List<FeedbackChallenge>> getChallengeFeedback(@Path("challengeId") int challengeId);

    @GET("feedback/challenge/rating/{challengeId}")
    Call<List<Integer>> getChallengeFeedbackRating(@Path("challengeId") int challengeId);

  @GET("user/{email}")
    Call<List<User>> getUserInfo(@Path("email") String email);
    @GET("users")
    Call<List<User>> getUsers();
    @GET("reward")
    Call<List<Reward>> getRewardInfo();

  @POST("group/new")
    Call<Group> createNewGroup(@Body Group group);

    @POST("feedback/new")
    Call<Feedback> createNewFeedback(@Body Feedback feedback);

    @POST("feedback/challenge/new")
    Call<FeedbackChallenge> createNewFeedbackChallenge(@Body FeedbackChallenge feedbackChallenge);

    @POST("challenge/new")
    Call<ChallengeGroup> createNewChallengeGroup(@Body ChallengeGroup challengeGroup);

   @POST("feedback/challenge/newRating")
    Call<IncreasedChallengeFeedback> challengeFeedbackIncreaseRating(@Body IncreasedChallengeFeedback increasedFeedbackChallenge);















}
