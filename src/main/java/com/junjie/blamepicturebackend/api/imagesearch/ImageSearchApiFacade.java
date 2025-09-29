package com.junjie.blamepicturebackend.api.imagesearch;

import com.junjie.blamepicturebackend.api.imagesearch.model.ImageSearchResult;
import com.junjie.blamepicturebackend.api.imagesearch.sub.GetImageFirstUrlApi;
import com.junjie.blamepicturebackend.api.imagesearch.sub.GetImageListApi;
import com.junjie.blamepicturebackend.api.imagesearch.sub.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ImageSearchApiFacade {

    /**
     * 搜索图片
     * @param imageUrl
     * @return
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;
    }

    public static void main(String[] args) {
        List<ImageSearchResult> imageList = searchImage("https://blame-1319165062.cos.ap-chengdu.myqcloud.com//public/1/2025-09-23_q5NZHAvGdzlwbJj4.jpg");
        System.out.println("结果列表" + imageList);
    }
}
