package com.coderfromscratch.simplehttpserver.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    public HttpRequest parseHttpRequest(InputStream input){
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.US_ASCII);

        HttpRequest req = new HttpRequest();
        parseRequestLine(reader, req);
        parseHeaders(reader, req);
        parseBody(reader, req);

        return req;
    }



    private void parseRequestLine(InputStreamReader input, HttpRequest req) {


    }



    private void parseHeaders(InputStreamReader input, HttpRequest req) {

    }

    private void parseBody(InputStreamReader input, HttpRequest req) {

    }


}
