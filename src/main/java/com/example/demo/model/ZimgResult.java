package com.example.demo.model;

import lombok.Data;

@Data
public class ZimgResult {
    private boolean ret;
    private ZimgResultInfo info;
    private ZimgResultError error;
}



