package nz.flatline.flatline.api.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;

import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;


public class FlatlineRestClient {

    private final String API_URL;

    private Flatline api = null;

    public FlatlineRestClient(String API_URL) {
        this.API_URL = API_URL;
    }

    public interface Flatline {
        @GET("/flat/{id}/bills/")
        Observable<List<Bill>> bill(
                @Path("id") int id);

        @POST("/flat/{id}/post_access_tokens/")
        @FormUrlEncoded
        Observable<Okay> postAccessTokens(@Path("id" )int id, @Field("token") String token, @Field("secret") String secret);
    }

    public Flatline getFlatLineAPI() {
        if (api == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(User.class, new UserDeserializer())
                    .setDateFormat("yyyy-MM-dd")
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("FLATLINE"))
                    .setEndpoint(API_URL)
                    .build();

            // Create an instance of our GitHub API interface.
            api= restAdapter.create(Flatline.class);
        }

        return api;
    }

    class UserDeserializer implements JsonDeserializer<User>
    {
        @Override
        public User deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException
        {
            // Get the "user" element from the parsed JSON
            JsonElement user = je.getAsJsonObject().get("user");

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(user, User.class);

        }
    }
}
