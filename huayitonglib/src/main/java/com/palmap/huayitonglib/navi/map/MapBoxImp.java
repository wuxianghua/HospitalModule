package com.palmap.huayitonglib.navi.map;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.Collections;

/**
 * Created by yibo.liu on 2017/12/21 15:39.
 */

public class MapBoxImp implements Map<MapboxMap, FeatureCollection, LatLng> {
    public static final String TAG = MapBoxImp.class.getSimpleName();

    private MapboxMap mMapboxMap;

    @Override
    public void setMapEngine(MapboxMap mapEngine) {
        mMapboxMap = mapEngine;
    }

    @Override
    public void drawLine(FeatureCollection source, String sourceId, String layerId, String aboveId) {
        try {
            if (mMapboxMap.getLayer(layerId) == null) {
                GeoJsonSource jsonSource;

                if (source == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    jsonSource = new GeoJsonSource(sourceId, featureCollection);
                } else {
                    jsonSource = new GeoJsonSource(sourceId, source);
                }

                if (mMapboxMap.getSource(sourceId) != null) {
                    mMapboxMap.removeSource(sourceId);
                }
                mMapboxMap.addSource(jsonSource);

                LineLayer startLayer = new LineLayer(layerId, sourceId);
                startLayer.setProperties(
                        //加虚线- - - - - - - - - - - -
                        PropertyFactory.lineDasharray(new Float[]{3f, 1f}),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineWidth(2.5f),
                        PropertyFactory.lineColor(Color.parseColor("#ff3333")));

                if (mMapboxMap.getLayer(aboveId) != null) {
                    mMapboxMap.addLayerAbove(startLayer, aboveId);
                } else {
                    mMapboxMap.addLayer(startLayer);
                }

            } else {
                GeoJsonSource geoJsonSource = (GeoJsonSource) mMapboxMap.getSourceAs(sourceId);
                if (source == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    geoJsonSource.setGeoJson(featureCollection);
                } else {
                    geoJsonSource.setGeoJson(source);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addImageSource(String imageName, Bitmap bitmap) {
        mMapboxMap.addImage(imageName, bitmap);
    }

    @Override
    public void drawImage(LatLng latLng, String sourceId, String layerId, String aboveId, String imageName) {
        drawImage(latLng, sourceId, layerId, aboveId, imageName, Property.ICON_ANCHOR_BOTTOM);
    }

    @Override
    public void drawImage(LatLng latLng, String sourceId, String layerId, String aboveId, String imageName, String
            iconAnchor) {
        try {
            if (mMapboxMap.getLayer(layerId) == null) {

                GeoJsonSource geoJsonSource;

                if (latLng == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    geoJsonSource = new GeoJsonSource(sourceId, featureCollection);
                } else {
                    Point point = Point.fromCoordinates(Position.fromCoordinates(latLng.getLongitude(), latLng
                            .getLatitude()));
                    geoJsonSource = new GeoJsonSource(sourceId, point);
                }

                if (mMapboxMap.getSource(sourceId) != null) {
                    mMapboxMap.removeSource(sourceId);
                }

                mMapboxMap.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer(layerId, sourceId);
                symbolLayer.setProperties(PropertyFactory.iconImage(imageName), PropertyFactory
                        .iconAnchor(iconAnchor));
                if (mMapboxMap.getLayer(aboveId) != null) {
                    mMapboxMap.addLayerAbove(symbolLayer, aboveId);
                } else {
                    mMapboxMap.addLayer(symbolLayer);
                }
            } else {

                GeoJsonSource geoJsonSource = (GeoJsonSource) mMapboxMap.getSourceAs(sourceId);
                if (latLng == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    geoJsonSource.setGeoJson(featureCollection);
                } else {
                    geoJsonSource.setGeoJson(Point.fromCoordinates(Position.fromCoordinates(latLng.getLongitude(),
                            latLng.getLatitude())));
                }
            }
        } catch (Exception e) {

        }
    }

}
