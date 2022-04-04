package com.coderfromscratch.simplehttpserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private static final int SP = 0x20; //32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpRequest parseHttpRequest(InputStream input) throws IOException, HttpParsingException {
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.US_ASCII);

        HttpRequest req = new HttpRequest();


        parseRequestLine(reader, req);
        parseHeaders(reader, req);
        parseBody(reader, req);

        return req;
    }



    private void parseRequestLine(InputStreamReader input, HttpRequest req) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();
        int _byte;

        boolean methodParsed = false;
        boolean requestTargetParsed =false;

        while ((_byte = input.read()) >= 0){
            if (_byte== CR){
                _byte = input.read();
                if (_byte == LF){
                    if (!methodParsed || !requestTargetParsed)
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if (_byte== SP){
                if (!methodParsed){
                    methodParsed = true;
                } else if (!requestTargetParsed){
                    requestTargetParsed = true;
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char) _byte);
            }
        }

    }



    private void parseHeaders(InputStreamReader input, HttpRequest req) {

    }

    private void parseBody(InputStreamReader input, HttpRequest req) {

    }


}
