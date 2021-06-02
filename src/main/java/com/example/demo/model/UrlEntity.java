package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * url对象
 *
 * @author LiTing
 * @date 2021/2/26 11:18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlEntity {
    /**
     * 基础url
     */
    public String baseUrl;
    /**
     * url参数
     */
    public Map<String, Object> params = new HashMap<>();

    public String toUrl() {
        int i = 0;
        StringBuilder text = new StringBuilder(baseUrl);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (i == 0) {
                text = text.append("?" + entry.getKey() + "=" + entry.getValue());
            } else {
                text = text.append("&" + entry.getKey() + "=" + entry.getValue());
            }
            i++;
        }
        return text.toString();
    }

    public UrlEntity addParam(Map<String, Object> map) {
        if (map != null) {
            params.putAll(map);
        }
        return this;
    }
}
