package com.example.trivia.data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/"
            + "curiousily/simple-quiz/master/"
            + "script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        Question question = new Question();
                                        question.setAnswer( response.getJSONArray(i).get(0).toString());
                                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
//
                                        // Add question objects to list;
                                        questionArrayList.add(question);
//                                        Log.d("Hello", "onResponse: " + question.getAnswer());

//
//                                        Log.d("JSON", "onResponse: " + response.getJSONArray(i).get(0));
//                                        Log.d("JSON", "onResponse: " + response.getJSONArray(i).getBoolean(1))
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if (null != callBack) {
                                    callBack.processFinished(questionArrayList);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

                );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
