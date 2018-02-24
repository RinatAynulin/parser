package com.aynulin.vk.places.parser;

import com.google.gson.Gson;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.places.PlaceFull;
import com.vk.api.sdk.objects.places.responses.SearchResponse;

import java.io.*;

public class Parser {
    public static void main(String[] args) throws Exception {
        VKClient vkClient = new VKClient();
        parsePlaces(vkClient);
        parsePhotos(vkClient);
    }

    public static void parsePlaces(VKClient vkClient) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("places.txt"));
        Gson gson = new Gson();

        SearchResponse response = vkClient.getPlaces().count(1).execute();

        int size = response.getCount();
        int processed = 0;
        while (processed < size) {
            response = vkClient.getPlaces().offset(processed).execute();
            StringBuffer sb = new StringBuffer("");
            response.getItems().forEach(e -> sb.append(gson.toJson(e, PlaceFull.class)).append("\n"));
            writer.write(sb.toString());
            processed += response.getItems().size();
        }

        writer.close();
    }

    public static void parsePhotos(VKClient vkClient) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("places.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("photos.txt"));
        Gson gson = new Gson();
        String currentString;
        while ((currentString = reader.readLine()) != null) {
            Thread.sleep(300);
            PlaceFull placeFull = gson.fromJson(currentString, PlaceFull.class);
            System.out.println("processing: " + placeFull.getId());
            StringBuffer sb = new StringBuffer("");
            com.vk.api.sdk.objects.photos.responses.SearchResponse searchResponse =
                    vkClient.getPhotos().lat(placeFull.getLatitude()).lng(placeFull.getLongitude()).execute();
            searchResponse.getItems().forEach(e -> {
                sb.append(placeFull.getId());
                sb.append(":");
                sb.append(gson.toJson(e, Photo.class));
                sb.append("\n");
            });
            writer.write(sb.toString());
        }
        writer.close();
    }
}
