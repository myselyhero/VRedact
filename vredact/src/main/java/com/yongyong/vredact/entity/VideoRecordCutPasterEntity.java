package com.yongyong.vredact.entity;

import java.io.Serializable;

/**
 * @author yongyong
 * 
 * desc:贴纸
 * 
 * @// TODO: 2020/9/11
 */
public class VideoRecordCutPasterEntity implements Serializable {

    //贴纸id
    private long id;
    //文本
    private String text;
    //x坐标
    private float x;
    //y坐标
    private float y;
    //角度
    private float degree;
    //缩放值
    private float scaling;
    //气泡顺序
    private int order;
    //水平镜像 1镜像 2未镜像
    private int horizonMirror;
    //贴纸PNG URL
    private String stickerURL;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getScaling() {
        return scaling;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getHorizonMirror() {
        return horizonMirror;
    }

    public void setHorizonMirror(int horizonMirror) {
        this.horizonMirror = horizonMirror;
    }

    public String getStickerURL() {
        return stickerURL;
    }

    public void setStickerURL(String stickerURL) {
        this.stickerURL = stickerURL;
    }
}
