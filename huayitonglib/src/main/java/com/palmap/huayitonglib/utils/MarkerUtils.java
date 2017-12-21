package com.palmap.huayitonglib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;
import com.palmap.huayitonglib.R;

import java.util.Collections;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;

/**
 * Created by yibo.liu on 2017/11/01 19:56.
 */

public class MarkerUtils {
    public static final String TAG = MarkerUtils.class.getSimpleName();

    private boolean mHasStartMark = false;

    public void addStartMark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(START_MARKER_LAYERID) == null) {
            if (position == null) {
                startsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                startsource = new GeoJsonSource(
                        START_MARKER_SOURCEID,
                        startsb
                );
            } else {
                startsource = new GeoJsonSource(
                        START_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(START_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(START_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(startsource);
            startlayer = new SymbolLayer(START_MARKER_LAYERID, START_MARKER_SOURCEID);
//            startlayer.setProperties(
//                    PropertyFactory.iconImage(START_IMAGE_LOCATION), PropertyFactory.iconSize(0.5f)
//            ,PropertyFactory.fillExtrusionHeight(50f));


            startlayer.setProperties(
                    PropertyFactory.iconImage(START_IMAGE_LOCATION),
                    PropertyFactory.fillExtrusionHeight(50f),
                    PropertyFactory.iconAnchor(ICON_ANCHOR_BOTTOM) // 设置锚点在icon底部
            );
            mMapboxMap.addLayerAbove(startlayer, aboveLayerid);
        } else {
            startsource = (GeoJsonSource) mMapboxMap.getSource(START_MARKER_SOURCEID);
            if (position == null) {
                startsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                startsource.setGeoJson(startsb);
            } else {
                startsource.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
        mHasStartMark = true;
    }


    public void addFlow01Mark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource = new GeoJsonSource(
                        FLOW_MARKER_SOURCEID,
                        flowsb
                );
            } else {
                flowsource = new GeoJsonSource(
                        FLOW_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource);
            flowlayer = new SymbolLayer(FLOW_MARKER_LAYERID, FLOW_MARKER_SOURCEID);
            flowlayer.setProperties(
                    PropertyFactory.iconImage(FLOW_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer, aboveLayerid);
        } else {
            flowsource = (GeoJsonSource) mMapboxMap.getSource(FLOW_MARKER_SOURCEID);
            if (position == null) {
                flowsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource.setGeoJson(flowsb);
            } else {
                flowsource.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }

    }


    public void addFlow02Mark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW02_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb02 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource02 = new GeoJsonSource(
                        FLOW02_MARKER_SOURCEID,
                        flowsb02
                );
            } else {
                flowsource02 = new GeoJsonSource(
                        FLOW02_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW02_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW02_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource02);
            flowlayer02 = new SymbolLayer(FLOW02_MARKER_LAYERID, FLOW02_MARKER_SOURCEID);
            flowlayer02.setProperties(
                    PropertyFactory.iconImage(FLOW02_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer02, aboveLayerid);
        } else {
            flowsource02 = (GeoJsonSource) mMapboxMap.getSource(FLOW02_MARKER_SOURCEID);
            if (position == null) {
                flowsb02 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource02.setGeoJson(flowsb02);
            } else {
                flowsource02.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }

    }

    public void addFlow03Mark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW03_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb03 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource03 = new GeoJsonSource(
                        FLOW03_MARKER_SOURCEID,
                        flowsb03
                );
            } else {
                flowsource03 = new GeoJsonSource(
                        FLOW03_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW03_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW03_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource03);
            flowlayer03 = new SymbolLayer(FLOW03_MARKER_LAYERID, FLOW03_MARKER_SOURCEID);
            flowlayer03.setProperties(
                    PropertyFactory.iconImage(FLOW03_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer03, aboveLayerid);
        } else {
            flowsource03 = (GeoJsonSource) mMapboxMap.getSource(FLOW03_MARKER_SOURCEID);
            if (position == null) {
                flowsb03 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource03.setGeoJson(flowsb03);
            } else {
                flowsource03.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }

    }

    public void addFlow04Mark(LatLng position, String aboveLayerid) {
//        mMapboxMap.addImage(FLOW04_IMAGE_LOCATION, decodeSampledbitmapFromResource(
//                mContext.getResources(),
//                icon,
//                100,
//                100
//        ));
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW04_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb04 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource04 = new GeoJsonSource(
                        FLOW04_MARKER_SOURCEID,
                        flowsb04
                );
            } else {
                flowsource04 = new GeoJsonSource(
                        FLOW04_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW04_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW04_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource04);
            flowlayer04 = new SymbolLayer(FLOW04_MARKER_LAYERID, FLOW04_MARKER_SOURCEID);
            flowlayer04.setProperties(
                    PropertyFactory.iconImage(FLOW04_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer04, aboveLayerid);
        } else {
            flowsource04 = (GeoJsonSource) mMapboxMap.getSource(FLOW04_MARKER_SOURCEID);
            if (position == null) {
                flowsb04 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource04.setGeoJson(flowsb04);
            } else {
                flowsource04.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }


    public void addFlow05Mark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW05_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb05 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource05 = new GeoJsonSource(
                        FLOW05_MARKER_SOURCEID,
                        flowsb05
                );
            } else {
                flowsource05 = new GeoJsonSource(
                        FLOW05_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW05_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW05_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource05);
            flowlayer05 = new SymbolLayer(FLOW05_MARKER_LAYERID, FLOW05_MARKER_SOURCEID);
            flowlayer05.setProperties(
                    PropertyFactory.iconImage(FLOW05_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer05, aboveLayerid);
        } else {
            flowsource05 = (GeoJsonSource) mMapboxMap.getSource(FLOW05_MARKER_SOURCEID);
            if (position == null) {
                flowsb05 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource05.setGeoJson(flowsb05);
            } else {
                flowsource05.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }

    public void addFlow06Mark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW06_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb06 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource06 = new GeoJsonSource(
                        FLOW06_MARKER_SOURCEID,
                        flowsb06
                );
            } else {
                flowsource06 = new GeoJsonSource(
                        FLOW06_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW06_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW06_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource06);
            flowlayer06 = new SymbolLayer(FLOW06_MARKER_LAYERID, FLOW06_MARKER_SOURCEID);
            flowlayer06.setProperties(
                    PropertyFactory.iconImage(FLOW06_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer06, aboveLayerid);
        } else {
            flowsource06 = (GeoJsonSource) mMapboxMap.getSource(FLOW06_MARKER_SOURCEID);
            if (position == null) {
                flowsb06 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource06.setGeoJson(flowsb06);
            } else {
                flowsource06.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }
    public void addFlow07Mark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(FLOW07_MARKER_LAYERID) == null) {
            if (position == null) {
                flowsb07 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource07 = new GeoJsonSource(
                        FLOW07_MARKER_SOURCEID,
                        flowsb07
                );
            } else {
                flowsource07 = new GeoJsonSource(
                        FLOW07_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FLOW07_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FLOW07_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(flowsource07);
            flowlayer07 = new SymbolLayer(FLOW07_MARKER_LAYERID, FLOW07_MARKER_SOURCEID);
            flowlayer07.setProperties(
                    PropertyFactory.iconImage(FLOW07_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(flowlayer07, aboveLayerid);
        } else {
            flowsource07 = (GeoJsonSource) mMapboxMap.getSource(FLOW07_MARKER_SOURCEID);
            if (position == null) {
                flowsb07 = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                flowsource07.setGeoJson(flowsb07);
            } else {
                flowsource07.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }

    public void addDianTiMark(LatLng position, String aboveLayerid) {

        mMapboxMap.addImage(DIANTI_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                R.mipmap.ic_map_marker_zhiti,
                100,
                100
        ));

        Log.d(TAG, "addStartMark: " + position.toString());
        if (mMapboxMap.getLayer(DIANTI_MARKER_LAYERID) == null) {
            if (position == null) {
                diantisb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                diantisource = new GeoJsonSource(
                        DIANTI_MARKER_SOURCEID,
                        diantisb
                );
            } else {
                diantisource = new GeoJsonSource(
                        DIANTI_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(DIANTI_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(DIANTI_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(diantisource);
            diantilayer = new SymbolLayer(DIANTI_MARKER_LAYERID, DIANTI_MARKER_SOURCEID);
            diantilayer.setProperties(
                    PropertyFactory.iconImage(DIANTI_IMAGE_LOCATION)
                    , PropertyFactory.fillExtrusionHeight(50f));
            mMapboxMap.addLayerAbove(diantilayer, aboveLayerid);
        } else {
            diantisource = (GeoJsonSource) mMapboxMap.getSource(DIANTI_MARKER_SOURCEID);
            if (position == null) {
                diantisb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                diantisource.setGeoJson(diantisb);
            } else {
                diantisource.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }



    public void removeStartMark() {
        Log.d("lyb", "removeStartMark: ");
        if (mMapboxMap.getLayer(START_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(START_MARKER_LAYERID);
        }
        mHasStartMark = false;
    }

    public void addEndMark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "setEndMark: ");
        if (mMapboxMap.getLayer(END_MARKER_LAYERID) == null) {
            if (position == null) {
                endsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                endsource = new GeoJsonSource(
                        END_MARKER_SOURCEID,
                        endsb
                );
            } else {
                endsource = new GeoJsonSource(
                        END_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(END_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(END_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(endsource);
            endlayer = new SymbolLayer(END_MARKER_LAYERID, END_MARKER_SOURCEID);
            endlayer.setProperties(
                    PropertyFactory.iconImage(END_IMAGE_LOCATION),
                    PropertyFactory.iconAnchor(ICON_ANCHOR_BOTTOM));
            mMapboxMap.addLayerAbove(endlayer, aboveLayerid);
        } else {
            endsource = (GeoJsonSource) mMapboxMap.getSource(END_MARKER_SOURCEID);
            if (position == null) {
                endsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                endsource.setGeoJson(endsb);
            } else {
                endsource.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }

    public void addFindMark(LatLng position, String aboveLayerid) {
        Log.d(TAG, "setfindMark: ");
        if (mMapboxMap.getLayer(FIND_MARKER_LAYERID) == null) {
            if (position == null) {
                findsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                findsource = new GeoJsonSource(
                        FIND_MARKER_SOURCEID,
                        findsb
                );
            } else {
                findsource = new GeoJsonSource(
                        FIND_MARKER_SOURCEID,
                        com.mapbox.services.commons.geojson.Point.fromCoordinates(Position.fromCoordinates(position
                                .getLongitude(), position.getLatitude()))
                );
            }
            if (mMapboxMap.getSource(FIND_MARKER_SOURCEID) != null) {
                mMapboxMap.removeSource(FIND_MARKER_SOURCEID);
            }
            mMapboxMap.addSource(findsource);
            findlayer = new SymbolLayer(FIND_MARKER_LAYERID, FIND_MARKER_SOURCEID);
            findlayer.setProperties(
                    PropertyFactory.iconImage(FIND_IMAGE_LOCATION),
                    PropertyFactory.iconAnchor(ICON_ANCHOR_BOTTOM)
            );
            mMapboxMap.addLayerAbove(findlayer, aboveLayerid);
        } else {
            findsource = (GeoJsonSource) mMapboxMap.getSource(FIND_MARKER_SOURCEID);
            if (position == null) {
                findsb = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                findsource.setGeoJson(findsb);
            } else {
                findsource.setGeoJson(com.mapbox.services.commons.geojson.Point.fromCoordinates(Position
                        .fromCoordinates
                                (position.getLongitude(), position.getLatitude())));
            }
        }
    }

    public void removeEndMark() {
        Log.d("lyb", "removeEndMark: ");
        if (mMapboxMap.getLayer(END_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(END_MARKER_LAYERID);
        }
    }

    public void removeFindMark() {
        Log.d("lyb", "removeEndMark: ");
        if (mMapboxMap.getLayer(FIND_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FIND_MARKER_LAYERID);
        }
    }

    public void removeDianTiMark() {
        Log.d("lyb", "removeDianTiMark: ");
        if (mMapboxMap.getLayer(DIANTI_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(DIANTI_MARKER_LAYERID);
        }
    }
    public void removeAllFlowMark() {
        Log.d("lyb", "removeEndMark: ");
        if (mMapboxMap.getLayer(FLOW_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW_MARKER_LAYERID);
        }
        if (mMapboxMap.getLayer(FLOW02_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW02_MARKER_LAYERID);
        }
        if (mMapboxMap.getLayer(FLOW03_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW03_MARKER_LAYERID);
        }
        if (mMapboxMap.getLayer(FLOW04_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW04_MARKER_LAYERID);
        }
        if (mMapboxMap.getLayer(FLOW05_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW05_MARKER_LAYERID);
        }
        if (mMapboxMap.getLayer(FLOW06_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW06_MARKER_LAYERID);
        }
        if (mMapboxMap.getLayer(FLOW07_MARKER_LAYERID) != null) {
            mMapboxMap.removeLayer(FLOW07_MARKER_LAYERID);
        }
    }

    public boolean hasStartMark() {
        return mHasStartMark;
    }

    public void setVisibility() {

    }

//    public MarkerUtils(MapboxMap mapboxMap, Context context, int findsourceid, int startsourceid, int endsourceid,
//                       int flowsourceid, int flow02, int flow03, int flow04, int flow05, int flow06) {
//        mMapboxMap = mapboxMap;
//        mContext = context;
//        init(findsourceid, startsourceid, endsourceid, flowsourceid, flow02, flow03, flow04, flow05, flow06);
//    }
    public MarkerUtils(MapboxMap mapboxMap, Context context,int startsourceid, int endsourceid) {
        mMapboxMap = mapboxMap;
        mContext = context;
        init(startsourceid, endsourceid);
    }

    private void init(int startsourceid, int endsourceid) {
        mMapboxMap.addImage(START_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                startsourceid,
                100,
                100
        ));
        mMapboxMap.addImage(END_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                endsourceid,
                100,
                100
        ));
    }

    public void init(int findsourceid, int startsourceid, int endsourceid, int flowsourceid, int flow02, int flow03,
                     int flow04, int flow05, int flow06) {
        //查找点位信息的图标
        mMapboxMap.addImage(FIND_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                findsourceid,
                100,
                100
        ));
        mMapboxMap.addImage(START_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                startsourceid,
                100,
                100
        ));

        mMapboxMap.addImage(END_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                endsourceid,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flowsourceid,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW02_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow02,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW03_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow03,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW04_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow04,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW05_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow05,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW06_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow06,
                100,
                100
        ));

    }

    public void initFlowIcon(int flowsourceid, int flow02, int flow03, int flow04, int flow05, int flow06) {
        mMapboxMap.addImage(FLOW_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flowsourceid,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW02_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow02,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW03_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow03,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW04_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow04,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW05_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow05,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW06_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow06,
                100,
                100
        ));
    }

    public void initFlowIcon_F4(int flowsourceid, int flow02, int flow03, int flow04, int flow05, int flow06,int flow07) {
        mMapboxMap.addImage(FLOW_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flowsourceid,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW02_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow02,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW03_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow03,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW04_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow04,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW05_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow05,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW06_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow06,
                100,
                100
        ));
        mMapboxMap.addImage(FLOW07_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                flow07,
                100,
                100
        ));
    }
    private void setLocationMark(int sourceId) {
        mMapboxMap.addImage(START_IMAGE_LOCATION, decodeSampledbitmapFromResource(
                mContext.getResources(),
                sourceId,
                100,
                100
        ));
    }

    private BitmapFactory.Options option;

    private Bitmap decodeSampledbitmapFromResource(Resources resources, int resID, int reqWidth, int reqHeight) {
        option = new BitmapFactory.Options();
        //设置inJustDecodeBounds为：ture，预先加载Bitmap的宽高参数
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resID, option);
        //计算图片的采样率
        option.inSampleSize = calcuteInSapmleSize(option, reqWidth, reqHeight);
        //根据图片采样率加载图片
        option.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resID, option);
    }

    private int height;
    private int width;
    private int inSample;
    private int halfHeight;
    private int halfWidth;

    private int calcuteInSapmleSize(BitmapFactory.Options option, int reqWidth, int reqHeight) {
        height = option.outHeight;
        width = option.outWidth;
        inSample = 1;
        if (height > reqHeight || width > reqWidth) {
            halfHeight = height / 2;
            halfWidth = width / 2;
            while (halfHeight / inSample >= reqHeight && halfWidth / inSample >= reqWidth) {
                inSample *= 2;
            }
        }
        return inSample;
    }

    public static final String START_MARKER_LAYERID = "start_marker_layerid_huaxi";
    public static final String START_MARKER_SOURCEID = "start_marker_sourceid_huaxi";
    private FeatureCollection startsb;
    private GeoJsonSource startsource;
    private SymbolLayer startlayer;

    public static final String END_MARKER_LAYERID = "marker_layerid_huaxi";
    public static final String END_MARKER_SOURCEID = "marker_sourceid_huaxi";
    private FeatureCollection endsb;
    private GeoJsonSource endsource;
    private SymbolLayer endlayer;

    public static final String FIND_MARKER_LAYERID = "find_marker_layerid_huaxi";
    public static final String FIND_MARKER_SOURCEID = "find_marker_sourceid_huaxi";
    private FeatureCollection findsb;
    private GeoJsonSource findsource;
    private SymbolLayer findlayer;


    public static final String FLOW_MARKER_LAYERID = "flow_marker_layerid_huaxi";
    public static final String FLOW_MARKER_SOURCEID = "flow_marker_sourceid_huaxi";
    private FeatureCollection flowsb;
    private GeoJsonSource flowsource;
    private SymbolLayer flowlayer;



    public static final String FLOW02_MARKER_LAYERID = "flow02_marker_layerid_huaxi";
    public static final String FLOW02_MARKER_SOURCEID = "flow02_marker_sourceid_huaxi";
    private FeatureCollection flowsb02;
    private GeoJsonSource flowsource02;
    private SymbolLayer flowlayer02;


    public static final String FLOW03_MARKER_LAYERID = "flow03_marker_layerid_huaxi";
    public static final String FLOW03_MARKER_SOURCEID = "flow03_marker_sourceid_huaxi";
    private FeatureCollection flowsb03;
    private GeoJsonSource flowsource03;
    private SymbolLayer flowlayer03;

    public static final String FLOW04_MARKER_LAYERID = "flow04_marker_layerid_huaxi";
    public static final String FLOW04_MARKER_SOURCEID = "flow04_marker_sourceid_huaxi";
    private FeatureCollection flowsb04;
    private GeoJsonSource flowsource04;
    private SymbolLayer flowlayer04;

    public static final String FLOW05_MARKER_LAYERID = "flow05_marker_layerid_huaxi";
    public static final String FLOW05_MARKER_SOURCEID = "flow05_marker_sourceid_huaxi";
    private FeatureCollection flowsb05;
    private GeoJsonSource flowsource05;
    private SymbolLayer flowlayer05;

    public static final String FLOW06_MARKER_LAYERID = "flow06_marker_layerid_huaxi";
    public static final String FLOW06_MARKER_SOURCEID = "flow06_marker_sourceid_huaxi";
    private FeatureCollection flowsb06;
    private GeoJsonSource flowsource06;
    private SymbolLayer flowlayer06;

    public static final String FLOW07_MARKER_LAYERID = "flow07marker_layerid_huaxi";
    public static final String FLOW07_MARKER_SOURCEID = "flow07_marker_sourceid_huaxi";
    private FeatureCollection flowsb07;
    private GeoJsonSource flowsource07;
    private SymbolLayer flowlayer07;



    public static final String DIANTI_MARKER_LAYERID = "dianti_marker_layerid_huaxi";
    public static final String DIANTI_MARKER_SOURCEID = "dianti_marker_sourceid_huaxi";
    private FeatureCollection diantisb;
    private GeoJsonSource diantisource;
    private SymbolLayer diantilayer;



    private String START_IMAGE_LOCATION = "start_image_location";
    private String END_IMAGE_LOCATION = "end_image_location";
    private String FIND_IMAGE_LOCATION = "find_image_location";
    private String FLOW_IMAGE_LOCATION = "flow_image_location";
    private String FLOW02_IMAGE_LOCATION = "flow02_image_location";
    private String FLOW03_IMAGE_LOCATION = "flow03_image_location";
    private String FLOW04_IMAGE_LOCATION = "flow04_image_location";
    private String FLOW05_IMAGE_LOCATION = "flow05_image_location";
    private String FLOW06_IMAGE_LOCATION = "flow06_image_location";
    private String FLOW07_IMAGE_LOCATION = "flow07_image_location";
    private String DIANTI_IMAGE_LOCATION = "dianti_image_location";

    public MapboxMap mMapboxMap;
    private final Context mContext;


}
