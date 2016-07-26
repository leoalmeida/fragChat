package br.edu.ifspsaocarlos.sdm.fragchat.service;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by LeonardoAlmeida on 11/07/16.
 */
public class JsonUTF8Request extends JsonRequest<JSONObject> {

    private final Listener<JSONObject> mlistener;

    private String charset = null;

    public JsonUTF8Request(int method, String url,
                           JSONObject jsonRequest,
                           Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        super(method, url,
                (jsonRequest == null) ? null : jsonRequest.toString(),
                listener,
                errorListener);
        this.mlistener = listener;
    }

    public JsonUTF8Request(String url, JSONObject jsonRequest, Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, jsonRequest, listener, errorListener);
    }

    public JsonUTF8Request(String url, String charset, JSONObject jsonRequest, Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, jsonRequest, listener, errorListener);
        this.charset = charset;
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        mlistener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String parsed;

            if(charset != null) {
                parsed = new String(networkResponse.data, charset);
            } else {
                parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            }

            return Response.success(new JSONObject(parsed),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
