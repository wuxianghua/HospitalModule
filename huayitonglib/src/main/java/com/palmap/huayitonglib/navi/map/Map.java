package com.palmap.huayitonglib.navi.map;

import android.graphics.Bitmap;

/**
 * Created by yibo.liu on 2017/12/21 15:36.
 */

public interface Map<T, K, G> {

    void setMapEngine(T mapEngine);

    /**
     * add or update if not added before
     *
     * @param source
     * @param sourceId
     * @param layerId
     * @param aboveId
     */
    void drawLine(K source, String sourceId, String layerId, String aboveId);

    void addImageSource(String imageName, Bitmap bitmap);

    /**
     * add or update if not added before
     *
     * @param source
     * @param sourceId
     * @param layerId
     * @param aboveId
     * @param imageName
     */
    void drawImage(G source, String sourceId, String layerId, String aboveId, String imageName);

    /**
     * add or update if not added before
     *
     * @param source
     * @param sourceId
     * @param layerId
     * @param aboveId
     * @param imageName
     */
    void drawImage(G source, String sourceId, String layerId, String aboveId, String imageName,String iconAnchor);
}
