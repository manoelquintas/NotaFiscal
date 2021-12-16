package br.com.exaple.NotaFiscal.utils;


import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AnyCaptchaText {

    final String key = "pkg.301a720e211b4abca2d6531ead54";

    public String imagemToText(String base64) {
        // TODO Auto-generated method stub


        String body = "{" +
                "    \"clientKey\":\"" + key + "\"," +
                "    \"task\":" +
                "        {" +
                "            \"type\":\"ImageToTextTask\"," +
                "            \"body\":\"" + base64 + "\"}" ;

        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.post("https://api.anycaptcha.com/createTask")
                .body(body)
                .asJson();
        JSONParser parser = new JSONParser();
        String taskid = "";
        try {
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(jsonNodeHttpResponse.getBody().toString());
            taskid = json.get("taskId").toString();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(taskid);
        String getresult = "{" +
                "    \"clientKey\":\"" + key + "\"," +
                "    \"taskId\": " + taskid + "" +
                "}";

        System.out.println(getresult);

        //Thread.sleep(12000);

        HttpResponse<JsonNode> jsonNodeHttpResponse2 = Unirest.post("https://api.anycaptcha.com/getTaskResult")
                .body(getresult)
                .header("Content-Type", "text/plain")
                .asJson();
        String resultado = "";
        try {
            org.json.simple.JSONObject jsonResult = (org.json.simple.JSONObject) parser.parse(jsonNodeHttpResponse2.getBody().toString());
            resultado = jsonResult.get("status").toString();
            org.json.simple.JSONObject jsonResult2 = null;

            while (resultado.equalsIgnoreCase("processing")) {
                jsonNodeHttpResponse2 = Unirest.post("https://api.anycaptcha.com/getTaskResult")
                        .body(getresult)
                        .header("Content-Type", "text/plain")
                        .asJson();
                jsonResult2 = (org.json.simple.JSONObject) parser.parse(jsonNodeHttpResponse2.getBody().toString());

                resultado = jsonResult2.get("status").toString();
                System.out.println("processando");
                System.out.println(jsonNodeHttpResponse2.getBody());
                Thread.sleep(2000);

            }

            System.out.println(jsonResult2.get("solution").toString());
            JSONObject jsonObject = jsonNodeHttpResponse2.getBody().getObject();
            //JSONArray categories = jsonObject.getJSONArray("solution");
            org.json.simple.JSONObject json4 = (org.json.simple.JSONObject) parser.parse(jsonResult2.get("solution").toString());
            resultado = json4.get("text").toString();

        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (resultado.equalsIgnoreCase("processing") || resultado.equalsIgnoreCase("ready")) {
            System.out.println(resultado);
        }
        return resultado;


    }

}
