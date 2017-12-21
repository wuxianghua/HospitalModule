package com.palmap.huayitonglib.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by hengwei.guo on 2017/12/20 10:48.
 */

@Entity
public class MapPointInfoBean implements Serializable{

    /*
    {
    "poiId": "2696892",
    "floorName": "一层",
    "address": "住院楼A3区",
    "name": "抢救监护区",
    "point": "[11583992.192495503,3586234.7398485155]",
    "longitude": "11583992.19",
    "latitude": "3586234.74",
    "floorId": "2452754"
  }
     */
    @Id
    private Long id;
    private String poiId;
    private String name;
    private String address;
    private String floorName;
    private String point;
    private String longitude;
    private String latitude;
    private String floorId;

    public String getFloorId() {
        return this.floorId;
    }
    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getPoint() {
        return this.point;
    }
    public void setPoint(String point) {
        this.point = point;
    }
    public String getFloorName() {
        return this.floorName;
    }
    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPoiId() {
        return this.poiId;
    }
    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1954378153)
    public MapPointInfoBean(Long id, String poiId, String name, String address,
            String floorName, String point, String longitude, String latitude,
            String floorId) {
        this.id = id;
        this.poiId = poiId;
        this.name = name;
        this.address = address;
        this.floorName = floorName;
        this.point = point;
        this.longitude = longitude;
        this.latitude = latitude;
        this.floorId = floorId;
    }
    @Generated(hash = 549262624)
    public MapPointInfoBean() {
    }

}
