// Copyright (C) 2025 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.poc.gerrit.plugins.aireview;


import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.api.changes.AiCodeReviewProvider;
import com.google.gerrit.extensions.common.AiCodeReviewInput;
import com.google.gerrit.extensions.common.AiResponse;
import com.google.gerrit.server.config.PluginConfig;
import com.google.gerrit.server.config.PluginConfigFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AiRestApiClient implements AiCodeReviewProvider {

  private static String apiKey = "";

  @Inject
  public AiRestApiClient(PluginConfigFactory cfgFactory, @PluginName String pluginName) {
    PluginConfig cfg = cfgFactory.getFromGerritConfig(pluginName);
    this.apiKey = cfg.getString("api-key", "");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException(
          "Missing 'api-key' in gerrit.config under [plugin \"" + pluginName + "\"]");
    }
  }

  @Override
  public AiResponse getAiReview(AiCodeReviewInput input) throws IOException {
    String res = callOpenRouterApi(input.prompt);
    AiResponse response = new AiResponse();
    response.response = res;
    response.status = "Success";
    return response;
  }


  public static String callOpenRouterApi(String prompt) {
    String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    Gson gson = new Gson();
    String escapedPrompt = gson.toJson(prompt);
    String jsonInput = String.format("""
        {
          "contents": [
            {
              "parts": [
                {
                  "text": %s
                }
              ]
            }
          ]
        }
        """, escapedPrompt);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(apiUrl))
        .header("X-goog-api-key", apiKey)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
        .build();

    try {
      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

      JsonArray candidates = jsonObject.getAsJsonArray("candidates");
      JsonObject candidate = candidates.get(0).getAsJsonObject();
      JsonObject content = candidate.getAsJsonObject("content");
      JsonArray parts = content.getAsJsonArray("parts");
      JsonObject part = parts.get(0).getAsJsonObject();

      return part.get("text").getAsString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
