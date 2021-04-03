package com.example.canvasui.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.canvasui.R;
import com.example.canvasui.api.models.Course;
import com.example.canvasui.api.models.UpcomingEvent;
import com.example.canvasui.api.models.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class USUCanvasAPI {
    public interface OnRequestCompleteListener<T> {
        public void onComplete(T[] result, String errorMessage);
    }

    public class InvalidTokenException extends RuntimeException {
        public InvalidTokenException() {
            super("Error: Canvas API token was invalid. Did you forgot to include you token in strings.xml");
        }
    }

    private RequestQueue queue;
    private String token;
    private static USUCanvasAPI instance;

    public static USUCanvasAPI getInstance(Context context) {
        if (instance == null) {
            instance = new USUCanvasAPI(context, context.getResources().getString(R.string.canvas_token));
        }
        return instance;
    }

    private USUCanvasAPI(Context context, String token) {
        if (token.equals("") || token == null) {
            throw new InvalidTokenException();
        }
        this.queue = Volley.newRequestQueue(context);
        this.token = token;
    }

    public void getUser(OnRequestCompleteListener<User> onRequestCompleteListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://usu.instructure.com/api/v1/users/self?access_token=" + token,
                null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                User[] users = new User[1];
                User user = new User();
                try {
                    user.id = (int)response.get("id");
                    user.name = response.getString("name");
                } catch (JSONException e) {
                    onRequestCompleteListener.onComplete(null, e.toString());
                    e.printStackTrace();
                }
                System.out.println("RESULT: " + response.toString());
                users[0] = user;
                onRequestCompleteListener.onComplete(users, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                onRequestCompleteListener.onComplete(null, error.toString());
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void getUpcomingEvents(OnRequestCompleteListener<UpcomingEvent> onRequestCompleteListener) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "https://usu.instructure.com/api/v1/users/self/upcoming_events?per_page=1&access_token=" + token,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        UpcomingEvent[] events = new UpcomingEvent[response.length()];
                        for(int i=0; i<response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                UpcomingEvent event = new UpcomingEvent();
                                event.title = obj.getString("title");
                                events[i] = event;
                            } catch (JSONException e) {
                                onRequestCompleteListener.onComplete(null, e.toString());
                                e.printStackTrace();
                            }
                        }
                        onRequestCompleteListener.onComplete(events, null);
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                onRequestCompleteListener.onComplete(null, error.toString());
                error.printStackTrace();

            }
        });

        queue.add(jsonObjectRequest);
    }

    public void getCourses(OnRequestCompleteListener<Course> onRequestCompleteListener) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "https://usu.instructure.com/api/v1/courses?enrollment_state=active&access_token=" + token,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Course[] courses = new Course[response.length()];
                        for(int i=0; i<response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Course course = new Course();
                                course.id = obj.getInt("id");
                                course.name = obj.getString("name");
                                courses[i] = course;
                            } catch (JSONException e) {
                                onRequestCompleteListener.onComplete(null, e.toString());
                                e.printStackTrace();
                            }
                        }
                        onRequestCompleteListener.onComplete(courses, null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                error.printStackTrace();

            }
        });

        queue.add(jsonObjectRequest);
    }


}
