package com.siemens.bt.jazz.services.base.scenario;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ExpensiveScenario implements AutoCloseable {
    CloseableHttpClient client = HttpClients.createDefault();

    public ExpensiveScenario(String name) throws IOException {
        // post scenario name to start service
        HttpPost post = new HttpPost("https://localhost:9443/ccm/service/com.ibm.team.repository.service.serviceability.IScenarioRestService/scenarios/startscenario");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        ArrayList<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("scenarioName", name));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(body);
        post.setEntity(entity);

        // keep the response for closing the session again
        CloseableHttpResponse execute = client.execute(post);
        HttpEntity result = execute.getEntity();
    }

    @Override
    public void close() throws Exception {
        // send stop to service
    }
}
