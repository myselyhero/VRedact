package com.yongyong.vredact.entity;

import java.io.Serializable;

/**
 * @author yongyong
 *
 * desc:气泡实体类
 * 
 * @// TODO: 2020/9/11  
 */
public class VideoRecordCutBubbleEntity implements Serializable {

    //气泡id
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
}
