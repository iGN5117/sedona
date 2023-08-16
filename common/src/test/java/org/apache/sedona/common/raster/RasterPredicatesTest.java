/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sedona.common.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.awt.image.DataBuffer;
import java.io.IOException;


public class RasterPredicatesTest extends RasterTestBase {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    @Test
    public void testIntersectsNoCrs() {
        Geometry queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 10, 0, 10));
        GridCoverage2D raster = createRandomRaster(DataBuffer.TYPE_BYTE, 100, 100, 0, 100, 1, 1, null);
        boolean result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertTrue(result);
        queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(1000, 1010, 1000, 1010));
        result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertFalse(result);
    }

    @Test
    public void testIntersectsQueryWindowNoCrs() {
        Geometry queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 10, 0, 10));
        GridCoverage2D raster = createRandomRaster(DataBuffer.TYPE_BYTE, 100, 100, 0, 100, 1, 1, "EPSG:4326");
        boolean result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertTrue(result);
        queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(1000, 1010, 1000, 1010));
        result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertFalse(result);
    }

    @Test
    public void testIntersectsRasterNoCrs() {
        Geometry queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 10, 0, 10));
        queryWindow.setSRID(3857);
        GridCoverage2D raster = createRandomRaster(DataBuffer.TYPE_BYTE, 100, 100, 0, 100, 1, 1, null);
        boolean result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertTrue(result);
        queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(1000, 1010, 1000, 1010));
        queryWindow.setSRID(3857);
        result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertFalse(result);
    }

    @Test
    public void testIntersectsSameCrs() {
        Geometry queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 10, 0, 10));
        queryWindow.setSRID(3857);
        GridCoverage2D raster = createRandomRaster(DataBuffer.TYPE_BYTE, 100, 100, 0, 100, 1, 1, "EPSG:3857");
        boolean result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertTrue(result);
        queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(10, 20, 10, 20));
        queryWindow.setSRID(3857);
        result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertTrue(result);
        queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(1000, 1010, 1000, 1010));
        queryWindow.setSRID(3857);
        result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertFalse(result);
    }

    @Test
    public void testIntersectsWithTransformations() {
        Geometry queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 10, 0, 10));
        queryWindow.setSRID(4326);
        GridCoverage2D raster = createRandomRaster(DataBuffer.TYPE_BYTE, 100, 100, 0, 100, 1, 1, "EPSG:3857");
        boolean result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertTrue(result);
        queryWindow = GEOMETRY_FACTORY.toGeometry(new Envelope(10, 20, 10, 20));
        queryWindow.setSRID(4326);
        result = RasterPredicates.rsIntersects(raster, queryWindow);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsNoCrs() throws FactoryException {
        Geometry geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(5, 10, 5, 10));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1);
        boolean result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(2, 22, 2, 22));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //geometry protruding out of the raster envelope
        geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(2, 20, 2, 25));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsGeomNoCrs() throws FactoryException {
        Geometry geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(5, 10, 5, 10));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1, -1, 0, 0, 4326);
        boolean result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(2, 22, 2, 22));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //geometry protruding out of the raster envelope
        geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(2, 20, 2, 25));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsRasterNoCrs() throws FactoryException, ParseException, IOException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Geometry geometry = geometryFactory.toGeometry(new Envelope(5, 10, 5, 10));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1);
        boolean result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        geometry = geometryFactory.toGeometry(new Envelope(2, 22, 2, 22));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //geometry protruding out of the raster envelope
        geometry = geometryFactory.toGeometry(new Envelope(2, 20, 2, 25));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsSameCrs() throws FactoryException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Geometry geometry = geometryFactory.toGeometry(new Envelope(5, 10, 5, 10));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1, -1, 0, 0, 4326);
        boolean result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        geometry = geometryFactory.toGeometry(new Envelope(2, 22, 2, 22));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //geometry protruding out of the raster envelope
        geometry = geometryFactory.toGeometry(new Envelope(2, 20, 2, 25));
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsDifferentCrs() throws FactoryException, TransformException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 3857);
        Geometry geometry = geometryFactory.toGeometry(new Envelope(5, 10, 5, 10));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1, -1, 0, 0, 3857);
        boolean result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertTrue(result);

        //geometry protruding out of the raster envelope
        geometry = geometryFactory.toGeometry(new Envelope(2, 20, 2, 25));
        geometry = JTS.transform(geometry, CRS.findMathTransform(raster.getCoordinateReferenceSystem(), CRS.decode("EPSG:4326", true)));
        geometry.setSRID(4326);
        result = RasterPredicates.rsContains(raster, geometry);
        Assert.assertFalse(result);
    }


    @Test
    public void testWithinNoCrs() throws FactoryException {
        Geometry geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 100, 0, 50));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1);
        boolean result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        raster = RasterConstructors.makeEmptyRaster(1, 100, 50, 0, 50, 1);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //raster protruding out of the geometry
        raster = RasterConstructors.makeEmptyRaster(1, 100, 100, 0, 50, 1);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testWithinGeomNoCrs() throws FactoryException {
        Geometry geometry = GEOMETRY_FACTORY.toGeometry(new Envelope(0, 100, 0, 50));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1, -1, 0, 0, 3857);
        boolean result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        raster = RasterConstructors.makeEmptyRaster(1, 100, 50, 0, 50, 1, -1, 0, 0, 3857);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //raster protruding out of the geometry
        raster = RasterConstructors.makeEmptyRaster(1, 100, 100, 0, 50, 1, -1, 0, 0, 3857);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testWithinRasterNoCrs() throws FactoryException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Geometry geometry = geometryFactory.toGeometry(new Envelope(0, 100, 0, 50));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1);
        boolean result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        raster = RasterConstructors.makeEmptyRaster(1, 100, 50, 0, 50, 1);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //raster protruding out of the geometry
        raster = RasterConstructors.makeEmptyRaster(1, 100, 100, 0, 50, 1);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testWithinSameCrs() throws FactoryException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Geometry geometry = geometryFactory.toGeometry(new Envelope(0, 100, 0, 50));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 2, 22, 1, -1, 0, 0, 4326);
        boolean result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //overlapping raster and geometry;
        raster = RasterConstructors.makeEmptyRaster(1, 100, 50, 0, 50, 1, -1, 0, 0, 4326);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);

        //raster protruding out of the geometry
        raster = RasterConstructors.makeEmptyRaster(1, 100, 100, 0, 50, 1, -1, 0, 0, 4326);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertFalse(result);
    }

    @Test
    public void testWithinDifferentCrs() throws FactoryException, TransformException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Geometry geometry = geometryFactory.toGeometry(new Envelope(30, 60, 10, 50));
        GridCoverage2D raster = RasterConstructors.makeEmptyRaster(1, 20, 20, 32, 35, 1, -1, 0, 0, 4326);
        geometry = JTS.transform(geometry, CRS.findMathTransform(raster.getCoordinateReferenceSystem(), CRS.decode("EPSG:3857", true)));
        geometry.setSRID(3857);
        boolean result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertTrue(result);


        //raster protruding out of the geometry
        raster = RasterConstructors.makeEmptyRaster(1, 100, 100, 0, 50, 1, -1, 0, 0, 4326);
        result = RasterPredicates.rsWithin(raster, geometry);
        Assert.assertFalse(result);
    }
}
