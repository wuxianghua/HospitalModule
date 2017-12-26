package com.palmap.huayitonglib.navi.route;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.google.gson.Gson;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.palmap.huayitonglib.navi.astar.model.PoiInfo;
import com.palmap.huayitonglib.navi.astar.model.path.TreatedRoadNet;
import com.palmap.huayitonglib.navi.astar.navi.AStar;
import com.palmap.huayitonglib.navi.astar.navi.AStarPath;
import com.palmap.huayitonglib.navi.astar.navi.AStarVertex;
import com.palmap.huayitonglib.navi.astar.navi.DefaultG;
import com.palmap.huayitonglib.navi.astar.navi.DefaultH;
import com.palmap.huayitonglib.navi.astar.navi.VertexLoader;
import com.palmap.huayitonglib.navi.route.bean.Door;
import com.palmap.huayitonglib.navi.route.bean.Doors;
import com.palmap.huayitonglib.utils.Utils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wtm on 2017/10/9.
 */

public class MapBoxNavigateManager implements INavigateManager<FeatureCollection> {

    private static GeometryFactory geometryFactory = new GeometryFactory();

    private AStar aStar;

    private HandlerThread handlerThread;

    private Handler routeHandler;

    private Context context;

    private Doors doors;

    private Gson gson;

    private Listener<FeatureCollection> listener = DEFAULT_LISTENER;

    private static Listener<FeatureCollection> DEFAULT_LISTENER = new Listener<FeatureCollection>() {

        @Override
        public void onNavigateComplete(NavigateState state, List<AStarPath> routes, double totalDistance, double
                fromDistance, double toDistance, double fromX, double fromY, long fromPlanargraph, FeatureCollection
                                               from, double fromConX, double fromConY, double toX, double toY, long
                                               toPlanargraph, FeatureCollection to, double toConX, double toConY) {

        }
    };

    public MapBoxNavigateManager(Context context, final String routeDataPath) {
        this.context = context;
        gson = new Gson();
        handlerThread = new HandlerThread("mapBoxNavigateManager");
        handlerThread.start();
        routeHandler = new Handler(handlerThread.getLooper());
        routeHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    String pathJsonStr = loadFromAsset(MapBoxNavigateManager.this.context, routeDataPath);
                    JSONObject pathObject = new JSONObject(pathJsonStr);
                    TreatedRoadNet treatedRoadNet = new TreatedRoadNet(pathObject.optLong("mapId"), pathObject
                            .optJSONArray("vertexes"), pathObject.optJSONObject("paths"), pathObject.optJSONObject
                            ("connections"));
                    aStar = new AStar(new DefaultG(), new DefaultH(), new VertexLoader(treatedRoadNet));

                    if (doors == null) {
                        String doorIds = loadFromAsset(MapBoxNavigateManager.this.context, "doorIds.json");
                        doors = gson.fromJson(doorIds, Doors.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String loadFromAsset(Context context, String filename) {
        InputStream is = null;
        try {
            AssetManager am = context.getAssets();
            is = am.open(filename);
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder buffer = new StringBuilder("");
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void setNavigateListener(Listener<FeatureCollection> listener) {
        this.listener = listener == null ? DEFAULT_LISTENER : listener;
    }

    @Override
    public void switchPlanarGraph(long id) {

    }

    @Override
    public void navigation(double fromX, double fromY, long fromPlanargraph, Feature fromFeature, Feature toFeature, double toX, double toY, long
            toPlanargraph) {
        if (!precondition()) {
            return;
        }
        PoiInfo from = getPoiInfo(fromFeature);
        PoiInfo to = getPoiInfo(toFeature);
        List<AStarPath> routes = aStar.astar(
                geometryFactory.createPoint(new Coordinate(fromX, fromY)),
                fromPlanargraph,
                from,
                geometryFactory.createPoint(new Coordinate(toX, toY)),
                toPlanargraph,
                to,
                0
        );
        if (routes == null || routes.size() == 0) {
            this.listener.onNavigateComplete(NavigateState.NAVIGATE_REQUEST_ERROR, null, 0, 0, 0, fromX, fromY,
                    fromPlanargraph,
                    null, 0, 0, toX, toY, toPlanargraph, null, 0, 0);
            return;
        }

        Point shape = null;
        Point endShape = null;

        List<Feature> features = new ArrayList<>();
        List<Feature> otherFeatures = new ArrayList<>();

        Double totalDistance = 0d;
        Double fromDistance = 0d;
        Double toDistance = 0d;

        for (AStarPath aStarPath : routes) {
            AStarVertex fromVertex = aStarPath.getFrom();
            AStarVertex toVertex = aStarPath.getTo();
            if (fromPlanargraph == fromVertex.getVertex().getPlanarGraphId()) {
                Point startPoint = (Point) fromVertex.getVertex().getShape();
                double[] startPosition = webMercator2LatLng(startPoint.getX(), startPoint
                        .getY());
                Point endPoint = (Point) toVertex.getVertex().getShape();
                double[] endPosition = webMercator2LatLng(endPoint.getX(), endPoint.getY());
                List<Position> positionList = new ArrayList<>();
                positionList.add(Position.fromCoordinates(startPosition[1], startPosition[0]));
                positionList.add(Position.fromCoordinates(endPosition[1], endPosition[0]));
                LineString lineString = LineString.fromCoordinates(positionList);
                features.add(Feature.fromGeometry(lineString));
                // TODO: 2017/12/12/012 这个点设置的是否准确  需要验证
                shape = (Point) toVertex.getVertex().getShape();
                totalDistance += DistanceOp.distance(startPoint, endPoint);
                fromDistance += DistanceOp.distance(startPoint, endPoint);
            } else if (toPlanargraph == fromVertex.getVertex().getPlanarGraphId()) {
                if (endShape == null) {
                    endShape = (Point) fromVertex.getVertex().getShape();
                }

                Point startPoint = (Point) fromVertex.getVertex().getShape();
                double[] startPosition = webMercator2LatLng(startPoint.getX(), startPoint
                        .getY());
                Point endPoint = (Point) toVertex.getVertex().getShape();
                double[] endPosition = webMercator2LatLng(endPoint.getX(), endPoint.getY());
                List<Position> positionList = new ArrayList<>();
                positionList.add(Position.fromCoordinates(startPosition[1], startPosition[0]));
                positionList.add(Position.fromCoordinates(endPosition[1], endPosition[0]));
                LineString lineString = LineString.fromCoordinates(positionList);
                otherFeatures.add(Feature.fromGeometry(lineString));
                totalDistance += DistanceOp.distance(startPoint, endPoint);
                toDistance += DistanceOp.distance(startPoint, endPoint);
            }
        }

        if (endShape == null) {
            shape = null;
        }

        FeatureCollection routeFeatureCollection = FeatureCollection.fromFeatures(features);
        FeatureCollection otherRouteFeatureCollection = FeatureCollection.fromFeatures(otherFeatures);

        if (endShape != null && shape != null) {
            this.listener.onNavigateComplete(NavigateState.OK, routes, totalDistance, fromDistance, toDistance,
                    fromX, fromY,
                    fromPlanargraph,
                    routeFeatureCollection, shape.getX(), shape.getY(), toX, toY, toPlanargraph,
                    otherRouteFeatureCollection, endShape.getX(), endShape.getY());
        } else {
            this.listener.onNavigateComplete(NavigateState.OK, routes, totalDistance, fromDistance, toDistance,
                    fromX, fromY,
                    fromPlanargraph,
                    routeFeatureCollection, 0, 0, toX, toY, toPlanargraph,
                    otherRouteFeatureCollection, 0, 0);
        }
    }

    private PoiInfo getPoiInfo(Feature feature) {
        PoiInfo frompoiInfo = new PoiInfo();
        if (feature != null) {
            long id = Long.valueOf(feature.getId());
            for (Door poi : doors.pois) {
                if (poi.id == id) {
                    frompoiInfo.doorIds = poi.doorIds;
                }
            }
            Geometry featureShape;
            try {
                featureShape = Utils.getFeatureShape(feature);
                frompoiInfo.shape = featureShape;
                frompoiInfo.planarGraphId = feature.getNumberProperty("planar_graph").longValue();
            } catch (com.vividsolutions.jts.io.ParseException e) {
                e.printStackTrace();
            }
            return frompoiInfo;
        } else {
            return null;
        }
    }

    /**
     * 墨卡托投影转经纬度坐标
     */
    public static double[] webMercator2LatLng(double x, double y) {
        double x1 = x / 20037508.34 * 180.0;
        double y1 = y / 20037508.34 * 180.0;
        y1 = 180 / Math.PI * (2 * Math.atan(Math.exp(y1 * Math.PI / 180)) - Math.PI / 2);
        return new double[]{y1, x1};
    }

    @Override
    public void navigation(double fromX, double fromY, long fromPlanargraph, double toX, double toY, long
            toPlanargraph, long currentPlanargraph) {
        if (!precondition()) {
            return;
        }
    }

    private boolean precondition() {
        if (aStar == null) {
            this.listener.onNavigateComplete(NavigateState.NAVIGATE_REQUEST_ERROR, null, 0, 0, 0, 0, 0, 0,
                    null, 0, 0, 0, 0, 0, null, 0, 0);
            return false;
        }
        return true;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public long[] getKeys() {
        return new long[0];
    }

    @Override
    public double getMinDistanceByPoint(Coordinate coordinate) {
        return 0;
    }

    @Override
    public void clipFeatureCollectionByCoordinate(Coordinate coordinate) {

    }

    @Override
    public void clipFeatureCollectionByCoordinate(Coordinate coordinate, long planarGraph) {

    }

    @Override
    public Coordinate getPointOfIntersectioanByPoint(Coordinate coordinate) {
        return null;
    }

    @Override
    public long[] getAllPlanarGraphId() {
        return new long[0];
    }

    @Override
    public double getEachLineLength(long id, int index) {
        return 0;
    }

    @Override
    public double getFloorLineLength(long id) {
        return 0;
    }

    @Override
    public double getTotalLineLength() {
        return 0;
    }

    @Override
    public void destructor() {
        if (routeHandler != null && handlerThread != null) {
            routeHandler.removeCallbacksAndMessages(null);
            routeHandler.post(new Runnable() {
                @Override
                public void run() {
                    handlerThread.quit();
                    routeHandler = null;
                    handlerThread = null;
                }
            });
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destructor();
    }
}
