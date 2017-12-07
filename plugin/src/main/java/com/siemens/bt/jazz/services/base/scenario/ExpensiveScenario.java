package com.siemens.bt.jazz.services.base.scenario;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class ExpensiveScenario implements AutoCloseable {
    private final Scenario scenario;
    private final CloseableHttpClient client = HttpClients.createDefault();

    public ExpensiveScenario(String name) throws IOException, ParserConfigurationException, SAXException {
        // post scenario name to start service
        HttpPost post = new HttpPost("https://jazz:9443/ccm/service/com.ibm.team.repository.service.serviceability.IScenarioRestService/scenarios/startscenario");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        ArrayList<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("scenarioName", name));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(body);
        post.setEntity(entity);

        // keep the response for closing the session again
        CloseableHttpResponse response = client.execute(post);
        HttpEntity result = response.getEntity();

        this.scenario = new Gson().fromJson(EntityUtils.toString(result), Scenario.class);

        // scenario should now contain all the details
        // close call can be ommited with try-with-resources
        response.close();
    }

    @Override
    public void close() throws Exception {
        // send stop to service
        HttpPost post = new HttpPost("https://jazz:9443/ccm/service/com.ibm.team.repository.service.serviceability.IScenarioRestService/scenarios/stopscenario");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        ArrayList<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("scenarioName", scenario.scenarioName));
        body.add(new BasicNameValuePair("scenarioHeaderValue", scenario.scenarioHeaderValue));
        body.add(new BasicNameValuePair("scenarioHeaderKey", scenario.scenarioHeaderKey));
        body.add(new BasicNameValuePair("scenarioInstanceId", scenario.scenarioInstanceId));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(body);
        post.setEntity(entity);

        try (CloseableHttpResponse result = client.execute(post)) {};
    }
}
