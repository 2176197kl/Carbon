package com.carbon.DTO;

import java.util.Map;

public class Response {
    String result;
    Object data;
    String message;



    public Response success(Object data, String message){
        Response response=new Response();
        response.result="success";
        response.data=data;
        response.message=message;
        return response;
    }

    public Response fail(Object data, String message){
        Response response=new Response();
        response.result="fail";
        response.data=data;
        response.message=message;
        return response;
    }
}
