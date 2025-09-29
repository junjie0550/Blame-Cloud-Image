package com.junjie.blamepicturebackend.api.imagesearch.sub;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.junjie.blamepicturebackend.exception.BusinessException;
import com.junjie.blamepicturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 获取以图搜图页面地址（step 1）
 */
@Slf4j
public class GetImagePageUrlApi {

    /**
     * 可直接在此处粘贴从浏览器抓取到的 graph.baidu.com 的 Cookie。
     * 注意：Cookie 有有效期，过期后需要更新。
     */
    private static final String BAIDU_COOKIE = "BAIDUID=297BD4041ADD0E4CF303904C1CEEE74C:FG=1; BAIDUID_BFESS=297BD4041ADD0E4CF303904C1CEEE74C:FG=1; antispam_key_id=23; ab_sr=1.0.1_MWI4YmM0ZGZjOGRmMjNkMGIyZDI5Y2JhMzlmODFkYzRhMzkzOTE2OTg2NTVmYzNiNGNiMTMwYjBjZjgxOTM4ZmQyYzgyM2Q3M2M4Mjk2MTdjNjkzYWRkOWZiNDM0NzFmM2UzYzUwZDg0NDVkOTE4NDM2MGI1MDEyZTMyN2Q2ZGFhMDQ0MGNjN2VmNTNlNTJiODhlMjJlMTViM2JhMTU0Yw==; antispam_data=624149ef55e0b0bb3769f5e21525351c4ed7d8808c3c54f58885867523dea5f3473a3827e06830b0045345ff425e569832cb34cbeae70a8f29a90c4baeefc565f6b37ece5206a29e5d8f66a88b7f9f0c";

    // 复刻浏览器请求所需的 acs-token 与 boundary（注意：这些可能失效，需要更新）
    private static final String ACS_TOKEN = "1759119462010_1759136363438_wGu9QesCldyViLGrs+l8vl70YGm5dufVbrnZqjAixhpzoWmT6Nc/92caF9R44wFA6XbJALXqTPfURAsYV1xnSQtktQDd7DaREkhd69KMpugWLKV+DEr2IxoC6HOucF6262TF+KHCzmVDlzaEBz6EC6IGUoqyNWGBDtQkqoX+sf0zHjsRJ1ldlwNn5OT+eJl7mdXH89dkVy/7nVJXfkAYgHZR0+VZSc7+WkbUdGX8pc/foHyFlgHlyh0/aURmZ1YDpheY4wPg0L4wNS+Z16IOu7UTdEh7HZOUVko42qkFLwFSX1O5HtOsYQ1QLciVFWy2hKItE8gLRTg3djIhqQXkn7MIlyMGkKEuzO9ZjwLRWLm3SLWZlvgsA5gp8r3liGreBoX+dk+/qxYQbpcXfRc9ZPan5DKOsm4kYiYa+Pi/6kS52pYUiSPBvVy0EN3C6zldnqI4Gq5w52YmsjnzugGNjA==";
    private static final String MULTIPART_BOUNDARY = "WebKitFormBoundaryNX8HS9GgVEDe9wkn";

    /**
     * 获取以图搜图页面地址
     *
     * @param imageUrl
     * @return
     */
    public static String getImagePageUrl(String imageUrl) {
        long uptime = System.currentTimeMillis();
        String url = "https://graph.baidu.com/upload?uptime=" + uptime;
        try {
            String baiduCookie = StrUtil.isNotBlank(BAIDU_COOKIE) ? BAIDU_COOKIE : System.getenv("BAIDU_GRAPH_COOKIE");
            // 构造 multipart/raw，与浏览器抓包一致
            String encodedImage = URLUtil.encode(imageUrl, StandardCharsets.UTF_8);
            String sdkParamsJson = "{\"data\":\"624149ef55e0b0bb3769f5e21525351c4ed7d8808c3c54f58885867523dea5f3473a3827e06830b0045345ff425e5698acfb0133291496b6e5cf2d591e3d5112775927462234737e5310819d0b22a07a\",\"key_id\":\"23\",\"sign\":\"bf3614b0\"}";

            String CRLF = "\r\n";
            String delimiter = "--" + MULTIPART_BOUNDARY;
            String endDelimiter = delimiter + "--";
            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder.append(delimiter).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"image\"").append(CRLF).append(CRLF)
                    .append(encodedImage).append(CRLF)
                    .append(delimiter).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"tn\"").append(CRLF).append(CRLF)
                    .append("pc").append(CRLF)
                    .append(delimiter).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"from\"").append(CRLF).append(CRLF)
                    .append("pc").append(CRLF)
                    .append(delimiter).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"image_source\"").append(CRLF).append(CRLF)
                    .append("PC_UPLOAD_URL").append(CRLF)
                    .append(delimiter).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"sdkParams\"").append(CRLF).append(CRLF)
                    .append(sdkParamsJson).append(CRLF)
                    .append(endDelimiter).append(CRLF);

            String contentType = "multipart/form-data; boundary=" + MULTIPART_BOUNDARY;

            HttpRequest req = HttpRequest.post(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36")
                    .header("Accept", "*/*")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .header("Origin", "https://graph.baidu.com")
                    .header("Referer", "https://graph.baidu.com/pcpage/index?tpl_from=pc")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("sec-ch-ua", "\"Chromium\";v=\"140\", \"Not=A?Brand\";v=\"24\", \"Google Chrome\";v=\"140\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"Windows\"")
                    .header("acs-token", ACS_TOKEN)
                    .header("Content-Type", contentType)
                    .timeout(20000)
                    .body(bodyBuilder.toString());

            if (StrUtil.isNotBlank(baiduCookie)) {
                req.header("Cookie", baiduCookie);
            }

            HttpResponse httpResponse = req.execute();

            if (httpResponse.getStatus() != HttpStatus.HTTP_OK) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败，状态码：" + httpResponse.getStatus());
            }
            String body = httpResponse.body();
            if (StrUtil.isBlank(body) || StrUtil.startWithIgnoreCase(body.trim(), "<") || body.contains("<!DOCTYPE")) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口返回异常内容，疑似被风控");
            }
            JSONObject result = JSONUtil.parseObj(body);
            if (!result.containsKey("status") || result.getInt("status") != 0) {
                String msg = result.getStr("msg", "");
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败：" + msg);
            }
            JSONObject data = result.getJSONObject("data");
            String rawUrl = data.getStr("url");
            String searchResultUrl = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            if (StrUtil.isBlank(searchResultUrl)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "未返回有效的结果地址");
            }
            return searchResultUrl;
        } catch (BusinessException e) {
            log.error("调用百度以图搜图接口失败：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("调用百度以图搜图接口失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败，请稍后再试");
        }
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://blame-1319165062.cos.ap-chengdu.myqcloud.com//public/1/2025-09-23_q5NZHAvGdzlwbJj4.jpg";
//        String imageUrl = "https://www.codefather.cn/logo.png";
        String searchResultUrl = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + searchResultUrl);
    }
}
