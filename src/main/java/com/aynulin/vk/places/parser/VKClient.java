package com.aynulin.vk.places.parser;

import com.google.gson.Gson;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.photos.PhotosGetQuery;
import com.vk.api.sdk.queries.photos.PhotosSearchQuery;
import com.vk.api.sdk.queries.places.PlacesSearchQuery;

public class VKClient {
    private VkApiClient vk;
    private UserActor userActor;
    private ServiceActor serviceActor;

    public VKClient() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        this.vk = new VkApiClient(transportClient, new Gson(), 5);
        this.userActor = new UserActor(userId, "accessToken"); // fixme
        this.serviceActor = new ServiceActor(appId, "accessToken"); // fixme
    }


    public UserActor getUserActor() {
        return userActor;
    }

    public VkApiClient getVk() {
        return vk;
    }

    public PlacesSearchQuery getPlaces() {
        return vk.places().search(userActor, 54.3181598f, 48.3837914f).city(149).radius(4).count(1000); // Ulyanovsk data
    }

    public PhotosSearchQuery getPhotos() {
        return vk.photos().search(serviceActor).radius(100);
    }



}
