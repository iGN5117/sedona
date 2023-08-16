## GeometryType

Introduction: Returns the type of the geometry as a string. Eg: 'LINESTRING', 'POLYGON', 'MULTIPOINT', etc. This function also indicates if the geometry is measured, by returning a string of the form 'POINTM'.

Format: `GeometryType (A:geometry)`

Since: `v1.5.0`

Example:

```sql
SELECT GeometryType(ST_GeomFromText('LINESTRING(77.29 29.07,77.42 29.26,77.27 29.31,77.29 29.07)'));
```

Result:

```
 geometrytype
--------------
 LINESTRING
```

```sql
SELECT GeometryType(ST_GeomFromText('POINTM(0 0 1)'));
```

Result:

```
 geometrytype
--------------
 POINTM
```

## ST_3DDistance

Introduction: Return the 3-dimensional minimum cartesian distance between A and B

Format: `ST_3DDistance (A:geometry, B:geometry)`

Since: `v1.2.0`

Spark SQL example:
```sql
SELECT ST_3DDistance(ST_GeomFromText("POINT Z (0 0 -5)"), 
                     ST_GeomFromText("POINT Z(1  1 -6"))
```
Output:
```
1.7320508075688772
```

## ST_AddPoint

Introduction: RETURN Linestring with additional point at the given index, if position is not available the point will be added at the end of line.

Format: `ST_AddPoint(geom: geometry, point: geometry, position: integer)`

Format: `ST_AddPoint(geom: geometry, point: geometry)`

Since: `v1.0.0`

Spark SQL example:
```sql
SELECT ST_AddPoint(ST_GeomFromText("LINESTRING(0 0, 1 1, 1 0)"), ST_GeomFromText("Point(21 52)"), 1)

SELECT ST_AddPoint(ST_GeomFromText("Linestring(0 0, 1 1, 1 0)"), ST_GeomFromText("Point(21 52)"))
```

Output:
```
LINESTRING(0 0, 21 52, 1 1, 1 0)
LINESTRING(0 0, 1 1, 1 0, 21 52)
```

## ST_Affine

Introduction: Apply an affine transformation to the given geometry.

ST_Affine has 2 overloaded signatures:

`ST_Affine(geometry, a, b, c, d, e, f, g, h, i, xOff, yOff, zOff)`

`ST_Affine(geometry, a, b, d, e, xOff, yOff)`


Based on the invoked function, the following transformation is applied:

`x = a * x + b * y + c * z + xOff OR x = a * x + b * y + xOff`

`y = d * x + e * y + f * z + yOff OR y = d * x + e * y + yOff`

`z = g * x + f * y + i * z + zOff OR z = g * x + f * y + zOff`

If the given geometry is empty, the result is also empty.

Format: `ST_Affine(geometry, a, b, c, d, e, f, g, h, i, xOff, yOff, zOff)`  
Format: `ST_Affine(geometry, a, b, d, e, xOff, yOff)`

```sql
ST_Affine(geometry, 1, 2, 4, 1, 1, 2, 3, 2, 5, 4, 8, 3)
```

Input: `LINESTRING EMPTY`

Output: `LINESTRING EMPTY`

Input: `POLYGON ((1 0 1, 1 1 1, 2 2 2, 1 0 1))`

Output: `POLYGON Z((9 11 11, 11 12 13, 18 16 23, 9 11 11))`

Input: `POLYGON ((1 0, 1 1, 2 1, 2 0, 1 0), (1 0.5, 1 0.75, 1.5 0.75, 1.5 0.5, 1 0.5))`

Output: `POLYGON((5 9, 7 10, 8 11, 6 10, 5 9), (6 9.5, 6.5 9.75, 7 10.25, 6.5 10, 6 9.5))`


```sql
ST_Affine(geometry, 1, 2, 1, 2, 1, 2)
```

Input: `POLYGON EMPTY`

Output: `POLYGON EMPTY`

Input: `GEOMETRYCOLLECTION (MULTIPOLYGON (((1 0, 1 1, 2 1, 2 0, 1 0), (1 0.5, 1 0.75, 1.5 0.75, 1.5 0.5, 1 0.5)), ((5 0, 5 5, 7 5, 7 0, 5 0))), POINT (10 10))`

Output: `GEOMETRYCOLLECTION (MULTIPOLYGON (((2 3, 4 5, 5 6, 3 4, 2 3), (3 4, 3.5 4.5, 4 5, 3.5 4.5, 3 4)), ((6 7, 16 17, 18 19, 8 9, 6 7))), POINT (31 32))`

Input: `POLYGON ((1 0 1, 1 1 1, 2 2 2, 1 0 1))`

Output: `POLYGON Z((2 3 1, 4 5 1, 7 8 2, 2 3 1))`

## ST_Angle

Introduction: Computes and returns the angle between two vectors represented by the provided points or linestrings.

There are three variants possible for ST_Angle:

`ST_Angle(Geometry point1, Geometry point2, Geometry point3, Geometry point4)`
Computes the angle formed by vectors represented by point1 - point2 and point3 - point4

`ST_Angle(Geometry point1, Geometry point2, Geometry point3)`
Computes the angle formed by vectors represented by point2 - point1 and point2 - point3

`ST_Angle(Geometry line1, Geometry line2)`
Computes the angle formed by vectors S1 - E1 and S2 - E2, where S and E denote start and end points respectively

!!!Note
    If any other geometry type is provided, ST_Angle throws an IllegalArgumentException.
    Additionally, if any of the provided geometry is empty, ST_Angle throws an IllegalArgumentException.

!!!Note
    If a 3D geometry is provided, ST_Angle computes the angle ignoring the z ordinate, equivalent to calling ST_Angle for corresponding 2D geometries.

!!!Tip
    ST_Angle returns the angle in radian between 0 and 2\Pi. To convert the angle to degrees, use [ST_Degrees](./#st_degrees).


Format: `ST_Angle(p1, p2, p3, p4) | ST_Angle(p1, p2, p3) | ST_Angle(line1, line2)`


Since: `1.5.0`

Example:

```sql
SELECT ST_Angle(ST_GeomFromWKT('POINT(0 0)'), ST_GeomFromWKT('POINT (1 1)'), ST_GeomFromWKT('POINT(1 0)'), ST_GeomFromWKT('POINT(6 2)'))
```

Output: 

```
0.4048917862850834
```

Example:

```sql
SELECT ST_Angle(ST_GeomFromWKT('POINT (1 1)'), ST_GeomFromWKT('POINT (0 0)'), ST_GeomFromWKT('POINT(3 2)'))
```

Output: 

```
0.19739555984988044
```

Example:

```sql
SELECT ST_Angle(ST_GeomFromWKT('LINESTRING (0 0, 1 1)'), ST_GeomFromWKT('LINESTRING (0 0, 3 2)'))
```

Output: 

```
0.19739555984988044
```

## ST_Area

Introduction: Return the area of A

Format: `ST_Area (A:geometry)`

Since: `v1.0.0`

Spark SQL example:
```sql
SELECT ST_Area(ST_GeomFromText("POLYGON(0 0, 0 10, 10 10, 0 10, 0 0)"))
```

Output:
```
10
```

## ST_AreaSpheroid

Introduction: Return the geodesic area of A using WGS84 spheroid. Unit is square meter. Works better for large geometries (country level) compared to `ST_Area` + `ST_Transform`. It is equivalent to PostGIS `ST_Area(geography, use_spheroid=true)` function and produces nearly identical results.

Geometry must be in EPSG:4326 (WGS84) projection and must be in ==lat/lon== order. You can use ==ST_FlipCoordinates== to swap lat and lon.

Format: `ST_AreaSpheroid (A:geometry)`

Since: `v1.4.1`

Spark SQL example:

```sql
SELECT ST_AreaSpheroid(ST_GeomFromWKT('Polygon ((35 34, 30 28, 34 25, 35 34))'))
```

Output: 

```
201824850811.76245
```

## ST_AsBinary

Introduction: Return the Well-Known Binary representation of a geometry

Format: `ST_AsBinary (A:geometry)`

Since: `v1.1.1`

Spark SQL example:

```sql
SELECT ST_AsBinary(ST_GeomFromWKT('POINT (1 1)'))
```

Output:

```
0101000000000000000000f87f000000000000f87f
```

## ST_AsEWKB

Introduction: Return the Extended Well-Known Binary representation of a geometry.
EWKB is an extended version of WKB which includes the SRID of the geometry.
The format originated in PostGIS but is supported by many GIS tools.
If the geometry is lacking SRID a WKB format is produced.
[Se ST_SetSRID](#ST_SetSRID)
It will ignore the M coordinate if present.

Format: `ST_AsEWKB (A:geometry)`

Since: `v1.1.1`

Spark SQL example:

```sql
SELECT ST_AsEWKB(ST_SetSrid(ST_GeomFromWKT('POINT (1 1)'), 3021))
```

Output:

```
0101000020cd0b0000000000000000f03f000000000000f03f
```

## ST_AsEWKT

Introduction: Return the Extended Well-Known Text representation of a geometry.
EWKT is an extended version of WKT which includes the SRID of the geometry.
The format originated in PostGIS but is supported by many GIS tools.
If the geometry is lacking SRID a WKT format is produced.
[See ST_SetSRID](#ST_SetSRID)
It will support M coodinate if present since v1.5.0.

Format: `ST_AsEWKT (A:geometry)`

Since: `v1.2.1`

Spark SQL example:

```sql
SELECT ST_AsEWKT(ST_SetSrid(ST_GeomFromWKT('POLYGON((0 0,0 1,1 1,1 0,0 0))'), 4326))
```

Output:

```
SRID=4326;POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))
```

Example:

```sql
SELECT ST_AsEWKT(ST_MakePointM(1.0, 1.0, 1.0))
```

Output:

```
POINT M(1 1 1)
```

Example:

```sql
SELECT ST_AsEWKT(ST_MakePoint(1.0, 1.0, 1.0, 1.0))
```

Output:

```
POINT ZM(1 1 1 1)
```

## ST_AsGeoJSON

Introduction: Return the [GeoJSON](https://geojson.org/) string representation of a geometry

Format: `ST_AsGeoJSON (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_AsGeoJSON(ST_GeomFromWKT('POLYGON((1 1, 8 1, 8 8, 1 8, 1 1))'))
```

Output:

```json
{
  "type":"Polygon",
  "coordinates":[
    [[1.0,1.0],
      [8.0,1.0],
      [8.0,8.0],
      [1.0,8.0],
      [1.0,1.0]]
  ]
}
```

## ST_AsGML

Introduction: Return the [GML](https://www.ogc.org/standards/gml) string representation of a geometry

Format: `ST_AsGML (A:geometry)`

Since: `v1.3.0`

Spark SQL example:

```sql
SELECT ST_AsGML(ST_GeomFromWKT('POLYGON((1 1, 8 1, 8 8, 1 8, 1 1))'))
```

Output:

```
1.0,1.0 8.0,1.0 8.0,8.0 1.0,8.0 1.0,1.0
```

## ST_AsKML

Introduction: Return the [KML](https://www.ogc.org/standards/kml) string representation of a geometry

Format: `ST_AsKML (A:geometry)`

Since: `v1.3.0`

Spark SQL example:

```sql
SELECT ST_AsKML(ST_GeomFromWKT('POLYGON((1 1, 8 1, 8 8, 1 8, 1 1))'))
```

Output:

```
1.0,1.0 8.0,1.0 8.0,8.0 1.0,8.0 1.0,1.0
```

## ST_AsText

Introduction: Return the Well-Known Text string representation of a geometry.
It will support M coodinate if present since v1.5.0.

Format: `ST_AsText (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_AsText(ST_SetSRID(ST_Point(1.0,1.0), 3021))
```

Output:

```
POINT (1 1)
```

Example:

```sql
SELECT ST_AsText(ST_MakePointM(1.0, 1.0, 1.0))
```

Output:

```
POINT M(1 1 1)
```

Example:

```sql
SELECT ST_AsText(ST_MakePoint(1.0, 1.0, 1.0, 1.0))
```

Output:

```
POINT ZM(1 1 1 1)
```

## ST_Azimuth

Introduction: Returns Azimuth for two given points in radians null otherwise.

Format: `ST_Azimuth(pointA: Point, pointB: Point)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Azimuth(ST_POINT(0.0, 25.0), ST_POINT(0.0, 0.0))
```

Output:

```
3.141592653589793
```

## ST_Boundary

Introduction: Returns the closure of the combinatorial boundary of this Geometry.

Format: `ST_Boundary(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Boundary(ST_GeomFromWKT('POLYGON((1 1,0 0, -1 1, 1 1))'))
```

Output:

```
LINESTRING (1 1, 0 0, -1 1, 1 1)
```

## ST_BoundingDiagonal

Introduction: Returns a linestring spanning minimum and maximum values of each dimension of the given geometry's coordinates as its start and end point respectively.
If an empty geometry is provided, the returned LineString is also empty.
If a single vertex (POINT) is provided, the returned LineString has both the start and end points same as the points coordinates

Format: `ST_BoundingDiagonal(geom: geometry)`

Since: `v1.5.0`

Spark SQL Example:
```sql
SELECT ST_BoundingDiagonal(ST_GeomFromWKT(geom))
```

Input: `POLYGON ((1 1 1, 3 3 3, 0 1 4, 4 4 0, 1 1 1))`

Output: `LINESTRING Z(0 1 1, 4 4 4)`

Input: `POINT (10 10)`

Output: `LINESTRING (10 10, 10 10)`

Input: `GEOMETRYCOLLECTION(POLYGON ((5 5 5, -1 2 3, -1 -1 0, 5 5 5)), POINT (10 3 3))`

Output: `LINESTRING Z(-1 -1 0, 10 5 5)`

## ST_Buffer

Introduction: Returns a geometry/geography that represents all points whose distance from this Geometry/geography is less than or equal to distance.

Format: `ST_Buffer (A:geometry, buffer: Double)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Buffer(ST_GeomFromWKT("POINT(0 0)"), 1)
```

Output:

```
POLYGON ((1 0, 0.9807852804032304 -0.1950903220161282, 0.9238795325112867 -0.3826834323650898, 0.8314696123025452 -0.5555702330196022, 0.7071067811865476 -0.7071067811865475, 0.5555702330196023 -0.8314696123025452, 0.3826834323650898 -0.9238795325112867, 0.1950903220161283 -0.9807852804032304, 0.0000000000000001 -1, -0.1950903220161282 -0.9807852804032304, -0.3826834323650897 -0.9238795325112867, -0.555570233019602 -0.8314696123025453, -0.7071067811865475 -0.7071067811865476, -0.8314696123025453 -0.5555702330196022, -0.9238795325112867 -0.3826834323650899, -0.9807852804032304 -0.1950903220161286, -1 -0.0000000000000001, -0.9807852804032304 0.1950903220161284, -0.9238795325112868 0.3826834323650897, -0.8314696123025455 0.555570233019602, -0.7071067811865477 0.7071067811865475, -0.5555702330196022 0.8314696123025452, -0.3826834323650903 0.9238795325112865, -0.1950903220161287 0.9807852804032303, -0.0000000000000002 1, 0.1950903220161283 0.9807852804032304, 0.38268343236509 0.9238795325112866, 0.5555702330196018 0.8314696123025455, 0.7071067811865474 0.7071067811865477, 0.8314696123025452 0.5555702330196022, 0.9238795325112865 0.3826834323650904, 0.9807852804032303 0.1950903220161287, 1 0))
```

## ST_BuildArea

Introduction: Returns the areal geometry formed by the constituent linework of the input geometry.

Format: `ST_BuildArea (A:geometry)`

Since: `v1.2.1`

Example:

```sql
SELECT ST_BuildArea(
    ST_GeomFromWKT('MULTILINESTRING((0 0, 20 0, 20 20, 0 20, 0 0),(2 2, 18 2, 18 18, 2 18, 2 2))')
) AS geom
```

Result:

```

+----------------------------------------------------------------------------+
|geom                                                                        |
+----------------------------------------------------------------------------+
|POLYGON((0 0,0 20,20 20,20 0,0 0),(2 2,18 2,18 18,2 18,2 2))                |
+----------------------------------------------------------------------------+
```

## ST_Centroid

Introduction: Return the centroid point of A

Format: `ST_Centroid (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Centroid(ST_GeomFromWKT('MULTIPOINT(-1  0, -1 2, 7 8, 9 8, 10 6)'))
```

Output:

```
POINT (4.8 4.8) 
```

## ST_ClosestPoint

Introduction: Returns the 2-dimensional point on geom1 that is closest to geom2. This is the first point of the shortest line between the geometries. If using 3D geometries, the Z coordinates will be ignored. If you have a 3D Geometry, you may prefer to use ST_3DClosestPoint.
It will throw an exception indicates illegal argument if one of the params is an empty geometry.

Format: `ST_ClosestPoint(g1: geomtry, g2: geometry)`

Since: `1.5.0`

Example1:
```sql
SELECT ST_AsText( ST_ClosestPoint(g1, g2)) As ptwkt;
```

Input: `g1: POINT (160 40), g2: LINESTRING (10 30, 50 50, 30 110, 70 90, 180 140, 130 190)`

Output: `POINT(160 40)`

Input: `g1: LINESTRING (10 30, 50 50, 30 110, 70 90, 180 140, 130 190), g2: POINT (160 40)`

Output: `POINT(125.75342465753425 115.34246575342466)`

Input: `g1: 'POLYGON ((190 150, 20 10, 160 70, 190 150))', g2: ST_Buffer('POINT(80 160)', 30)`

Output: `POINT(131.59149149528952 101.89887534906197)`

## ST_Collect

Introduction: Returns MultiGeometry object based on geometry column/s or array with geometries

Format

`ST_Collect(*geom: geometry)`

`ST_Collect(geom: array<geometry>)`

Since: `v1.2.0`

Example:

```sql
SELECT ST_Collect(
    ST_GeomFromText('POINT(21.427834 52.042576573)'),
    ST_GeomFromText('POINT(45.342524 56.342354355)')
) AS geom
```

Result:

```
+---------------------------------------------------------------+
|geom                                                           |
+---------------------------------------------------------------+
|MULTIPOINT ((21.427834 52.042576573), (45.342524 56.342354355))|
+---------------------------------------------------------------+
```

Example:

```sql
SELECT ST_Collect(
    Array(
        ST_GeomFromText('POINT(21.427834 52.042576573)'),
        ST_GeomFromText('POINT(45.342524 56.342354355)')
    )
) AS geom
```

Result:

```
+---------------------------------------------------------------+
|geom                                                           |
+---------------------------------------------------------------+
|MULTIPOINT ((21.427834 52.042576573), (45.342524 56.342354355))|
+---------------------------------------------------------------+
```

## ST_CollectionExtract

Introduction: Returns a homogeneous multi-geometry from a given geometry collection.

The type numbers are:
1. POINT
2. LINESTRING
3. POLYGON

If the type parameter is omitted a multi-geometry of the highest dimension is returned.

Format: `ST_CollectionExtract (A:geometry)`

Format: `ST_CollectionExtract (A:geometry, type:Int)`

Since: `v1.2.1`

Example:

```sql
WITH test_data as (
    ST_GeomFromText(
        'GEOMETRYCOLLECTION(POINT(40 10), POLYGON((0 0, 0 5, 5 5, 5 0, 0 0)))'
    ) as geom
)
SELECT ST_CollectionExtract(geom) as c1, ST_CollectionExtract(geom, 1) as c2
FROM test_data

```

Result:

```
+----------------------------------------------------------------------------+
|c1                                        |c2                               |
+----------------------------------------------------------------------------+
|MULTIPOLYGON(((0 0, 0 5, 5 5, 5 0, 0 0))) |MULTIPOINT(40 10)                |              |
+----------------------------------------------------------------------------+
```

## ST_ConcaveHull

Introduction: Return the Concave Hull of polgyon A, with alpha set to pctConvex[0, 1] in the Delaunay Triangulation method, the concave hull will not contain a hole unless allowHoles is set to true

Format: `ST_ConcaveHull (A:geometry, pctConvex:float)`

Format: `ST_ConcaveHull (A:geometry, pctConvex:float, allowHoles:Boolean)`

Since: `v1.4.0`

Spark SQL example:

```sql
SELECT ST_ConcaveHull(ST_GeomFromWKT('POLYGON((175 150, 20 40, 50 60, 125 100, 175 150))'), 1)
```

Output:

```
POLYGON ((125 100, 20 40, 50 60, 175 150, 125 100))  
```

## ST_ConvexHull

Introduction: Return the Convex Hull of polgyon A

Format: `ST_ConvexHull (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_ConvexHull(ST_GeomFromText('POLYGON((175 150, 20 40, 50 60, 125 100, 175 150))'))
```

Output:

```
POLYGON ((20 40, 175 150, 125 100, 20 40))
```

##  ST_CoordDim

Introduction: Returns the coordinate dimensions of the geometry. It is an alias of `ST_NDims`.

Format: `ST_CoordDim(geom: geometry)`

Since: `v1.5.0`

Example with x, y, z coordinate:

```sql
SELECT ST_CoordDim(ST_GeomFromText('POINT(1 1 2'))
```

Output: 

```
3
```

Example with x, y coordinate:

```sql
SELECT ST_CoordDim(ST_GeomFromWKT('POINT(3 7)'))
```

Output: 

```
2
```

## ST_Degrees

Introduction: Convert an angle in radian to degrees.

Format: `ST_Degrees(angleInRadian)`

Since: `v1.5.0`

Example:

```sql
SELECT ST_Degrees(0.19739555984988044)
```

Output: 

```
11.309932474020195
```

## ST_Difference

Introduction: Return the difference between geometry A and B (return part of geometry A that does not intersect geometry B)

Format: `ST_Difference (A:geometry, B:geometry)`

Since: `v1.2.0`

Example:

```sql
SELECT ST_Difference(ST_GeomFromWKT('POLYGON ((-3 -3, 3 -3, 3 3, -3 3, -3 -3))'), ST_GeomFromWKT('POLYGON ((0 -4, 4 -4, 4 4, 0 4, 0 -4))'))
```

Output:

```
POLYGON ((0 -3, -3 -3, -3 3, 0 3, 0 -3))
```

## ST_Dimension

Introduction: Return the topological dimension of this Geometry object, which must be less than or equal to the coordinate dimension. OGC SPEC s2.1.1.1 - returns 0 for POINT, 1 for LINESTRING, 2 for POLYGON, and the largest dimension of the components of a GEOMETRYCOLLECTION. If the dimension is unknown (e.g. for an empty GEOMETRYCOLLECTION) 0 is returned.

Format: `ST_Dimension (A:geometry), ST_Dimension (C:geometrycollection), `

Since: `v1.5.0`

Example:

```sql
SELECT ST_Dimension('GEOMETRYCOLLECTION(LINESTRING(1 1,0 0),POINT(0 0))');
```

Output:

```
1
```


## ST_Distance

Introduction: Return the Euclidean distance between A and B

Format: `ST_Distance (A:geometry, B:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Distance(ST_GeomFromText('POINT(72 42)'), ST_GeomFromText('LINESTRING(-72 -42, 82 92)'))
```

Output:

```
31.155515639003543
```

## ST_DistanceSphere

Introduction: Return the haversine / great-circle distance of A using a given earth radius (default radius: 6371008.0). Unit is meter. Compared to `ST_Distance` + `ST_Transform`, it works better for datasets that cover large regions such as continents or the entire planet. It is equivalent to PostGIS `ST_Distance(geography, use_spheroid=false)` and `ST_DistanceSphere` function and produces nearly identical results. It provides faster but less accurate result compared to `ST_DistanceSpheroid`.

Geometry must be in EPSG:4326 (WGS84) projection and must be in ==lat/lon== order. You can use ==ST_FlipCoordinates== to swap lat and lon. For non-point data, we first take the centroids of both geometries and then compute the distance.

Format: `ST_DistanceSphere (A:geometry)`

Since: `v1.4.1`

Spark SQL example 1:

```sql
SELECT ST_DistanceSphere(ST_GeomFromWKT('POINT (51.3168 -0.56)'), ST_GeomFromWKT('POINT (55.9533 -3.1883)'))
```

Output: 

```
543796.9506134904
```

Spark SQL example 2:

```sql
SELECT ST_DistanceSphere(ST_GeomFromWKT('POINT (51.3168 -0.56)'), ST_GeomFromWKT('POINT (55.9533 -3.1883)'), 6378137.0)
```

Output: 

```
544405.4459192449
```


## ST_DistanceSpheroid

Introduction: Return the geodesic distance of A using WGS84 spheroid. Unit is meter. Compared to `ST_Distance` + `ST_Transform`, it works better for datasets that cover large regions such as continents or the entire planet. It is equivalent to PostGIS `ST_Distance(geography, use_spheroid=true)` and `ST_DistanceSpheroid` function and produces nearly identical results. It provides slower but more accurate result compared to `ST_DistanceSphere`.

Geometry must be in EPSG:4326 (WGS84) projection and must be in ==lat/lon== order. You can use ==ST_FlipCoordinates== to swap lat and lon. For non-point data, we first take the centroids of both geometries and then compute the distance.

Format: `ST_DistanceSpheroid (A:geometry)`

Since: `v1.4.1`

Spark SQL example:

```sql
SELECT ST_DistanceSpheroid(ST_GeomFromWKT('POINT (51.3168 -0.56)'), ST_GeomFromWKT('POINT (55.9533 -3.1883)'))
```

Output: 

```
544430.9411996207
```

## ST_Dump

Introduction: It expands the geometries. If the geometry is simple (Point, Polygon Linestring etc.) it returns the geometry
itself, if the geometry is collection or multi it returns record for each of collection components.

Format: `ST_Dump(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:
```sql
SELECT ST_Dump(ST_GeomFromText('MULTIPOINT ((10 40), (40 30), (20 20), (30 10))'))
```

Output: 

```
[POINT (10 40), POINT (40 30), POINT (20 20), POINT (30 10)]
```

## ST_DumpPoints

Introduction: Returns list of Points which geometry consists of.

Format: `ST_DumpPoints(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_DumpPoints(ST_GeomFromText('LINESTRING (0 0, 1 1, 1 0)'))
```

Output: 

```
[POINT (0 0), POINT (0 1), POINT (1 1), POINT (1 0), POINT (0 0)]
```

## ST_EndPoint

Introduction: Returns last point of given linestring.

Format: `ST_EndPoint(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_EndPoint(ST_GeomFromText('LINESTRING(100 150,50 60, 70 80, 160 170)'))
```

Output: 

```
POINT(160 170)
```

## ST_Envelope

Introduction: Return the envelop boundary of A

Format: `ST_Envelope (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Envelope(ST_GeomFromWKT('LINESTRING(0 0, 1 3)'))
```

Output:

```
POLYGON ((0 0, 0 3, 1 3, 1 0, 0 0))
```

## ST_ExteriorRing

Introduction: Returns a line string representing the exterior ring of the POLYGON geometry. Return NULL if the geometry is not a polygon.

Format: `ST_ExteriorRing(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_ExteriorRing(ST_GeomFromText('POLYGON((0 0 1, 1 1 1, 1 2 1, 1 1 1, 0 0 1))'))
```

Output: 

```
LINESTRING (0 0, 1 1, 1 2, 1 1, 0 0)
```

## ST_FlipCoordinates

Introduction: Returns a version of the given geometry with X and Y axis flipped.

Format: `ST_FlipCoordinates(A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_FlipCoordinates(ST_GeomFromWKT("POINT (1 2)"))
```

Output: 

```
POINT (2 1)
```

## ST_Force_2D

Introduction: Forces the geometries into a "2-dimensional mode" so that all output representations will only have the X and Y coordinates

Format: `ST_Force_2D (A:geometry)`

Since: `v1.2.1`

Example:

```sql
SELECT ST_Force_2D(ST_GeomFromText('POLYGON((0 0 2,0 5 2,5 0 2,0 0 2),(1 1 2,3 1 2,1 3 2,1 1 2))'))
```

Output:

```
POLYGON((0 0,0 5,5 0,0 0),(1 1,3 1,1 3,1 1))
```

## ST_Force3D
Introduction: Forces the geometry into a 3-dimensional model so that all output representations will have X, Y and Z coordinates.
An optionally given zValue is tacked onto the geometry if the geometry is 2-dimensional. Default value of zValue is 0.0
If the given geometry is 3-dimensional, no change is performed on it.
If the given geometry is empty, no change is performed on it.

!!!Note
    Example output is after calling ST_AsText() on returned geometry, which adds Z for in the WKT for 3D geometries

Format: `ST_Force3D(geometry, zValue)`

Since: `1.4.1`

Spark SQL Example:

```sql
SELECT ST_AsText(ST_Force3D(ST_GeomFromText('POLYGON((0 0 2,0 5 2,5 0 2,0 0 2),(1 1 2,3 1 2,1 3 2,1 1 2))'), 2.3))
```

Output:

```
POLYGON Z((0 0 2, 0 5 2, 5 0 2, 0 0 2), (1 1 2, 3 1 2, 1 3 2, 1 1 2))
```

Spark SQL Example:

```sql
SELECT ST_AsText(ST_Force3D(ST_GeomFromText('LINESTRING(0 1,1 0,2 0)'), 2.3))
```

Output:

```
LINESTRING Z(0 1 2.3, 1 0 2.3, 2 0 2.3)
```

Spark SQL Example:

```sql
SELECT ST_AsText(ST_Force3D(ST_GeomFromText('LINESTRING EMPTY'), 3))
```

Output:

```
LINESTRING EMPTY
```

## ST_FrechetDistance

Introduction: Computes and returns discrete [Frechet Distance](https://en.wikipedia.org/wiki/Fr%C3%A9chet_distance) between the given two geometrie,
based on [Computing Discrete Frechet Distance](http://www.kr.tuwien.ac.at/staff/eiter/et-archive/cdtr9464.pdf)

If any of the geometries is empty, returns 0.0

Format: `ST_FrechetDistance(g1: geomtry, g2: geometry)`

Since: `1.5.0`

Example:

```sql
SELECT ST_FrechetDistance(ST_GeomFromWKT('POINT (0 1)'), ST_GeomFromWKT('LINESTRING (0 0, 1 0, 2 0, 3 0, 4 0, 5 0)'))
```

Output: 

```
5.0990195135927845
```

## ST_GeoHash

Introduction: Returns GeoHash of the geometry with given precision

Format: `ST_GeoHash(geom: geometry, precision: int)`

Since: `v1.1.1`

Example:

```sql
SELECT ST_GeoHash(ST_GeomFromText('POINT(21.427834 52.042576573)'), 5) AS geohash
```

Output:

```
u3r0p
```

## ST_GeometricMedian

Introduction: Computes the approximate geometric median of a MultiPoint geometry using the Weiszfeld algorithm. The geometric median provides a centrality measure that is less sensitive to outlier points than the centroid.

The algorithm will iterate until the distance change between successive iterations is less than the supplied `tolerance` parameter. If this condition has not been met after `maxIter` iterations, the function will produce an error and exit, unless `failIfNotConverged` is set to `false`.

If a `tolerance` value is not provided, a default `tolerance` value is `1e-6`.

Format: `ST_GeometricMedian(geom: geometry, tolerance: float, maxIter: integer, failIfNotConverged: boolean)`

Format: `ST_GeometricMedian(geom: geometry, tolerance: float, maxIter: integer)`

Format: `ST_GeometricMedian(geom: geometry, tolerance: float)`

Format: `ST_GeometricMedian(geom: geometry)`

Default parameters: `tolerance: 1e-6, maxIter: 1000, failIfNotConverged: false`

Since: `v1.4.1`

Example:
```sql
SELECT ST_GeometricMedian(ST_GeomFromWKT('MULTIPOINT((0 0), (1 1), (2 2), (200 200))'))
```

Output:
```
POINT (1.9761550281255005 1.9761550281255005)
```

## ST_GeometryN

Introduction: Return the 0-based Nth geometry if the geometry is a GEOMETRYCOLLECTION, (MULTI)POINT, (MULTI)LINESTRING, MULTICURVE or (MULTI)POLYGON. Otherwise, return null

Format: `ST_GeometryN(geom: geometry, n: Int)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_GeometryN(ST_GeomFromText('MULTIPOINT((1 2), (3 4), (5 6), (8 9))'), 1)
```

Output: 

```
POINT (3 4)
```

## ST_GeometryType

Introduction: Returns the type of the geometry as a string. EG: 'ST_Linestring', 'ST_Polygon' etc.

Format: `ST_GeometryType (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_GeometryType(ST_GeomFromText('LINESTRING(77.29 29.07,77.42 29.26,77.27 29.31,77.29 29.07)'))
```

Output:

```
ST_LINESTRING
```

## ST_HausdorffDistance

Introduction: Returns a discretized (and hence approximate) [Hausdorff distance](https://en.wikipedia.org/wiki/Hausdorff_distance) between the given 2 geometries.
Optionally, a densityFraction parameter can be specified, which gives more accurate results by densifying segments before computing hausdorff distance between them.
Each segment is broken down into equal-length subsegments whose ratio with segment length is closest to the given density fraction.

Hence, the lower the densityFrac value, the more accurate is the computed hausdorff distance, and the more time it takes to compute it.

If any of the geometry is empty, 0.0 is returned.

!!!Note
    Accepted range of densityFrac is (0.0, 1.0], if any other value is provided, ST_HausdorffDistance throws an IllegalArgumentException


!!!Note
    Even though the function accepts 3D geometry, the z ordinate is ignored and the computed hausdorff distance is equivalent to the geometries not having the z ordinate.

Format: `ST_HausdorffDistance(g1: geometry, g2: geometry, densityFrac)`

Since: `v1.5.0`

Example:

```sql
SELECT ST_HausdorffDistance(ST_GeomFromWKT('POINT (0.0 1.0)'), ST_GeomFromWKT('LINESTRING (0 0, 1 0, 2 0, 3 0, 4 0, 5 0)'), 0.1)
```

Output: 

```
5.0990195135927845
```

Example:

```sql
SELECT ST_HausdorffDistance(ST_GeomFromText('POLYGON Z((1 0 1, 1 1 2, 2 1 5, 2 0 1, 1 0 1))'), ST_GeomFromText('POLYGON Z((4 0 4, 6 1 4, 6 4 9, 6 1 3, 4 0 4))'))
```

Output: 

```
5.0
```

## ST_InteriorRingN

Introduction: Returns the Nth interior linestring ring of the polygon geometry. Returns NULL if the geometry is not a polygon or the given N is out of range

Format: `ST_InteriorRingN(geom: geometry, n: Int)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_InteriorRingN(ST_GeomFromText('POLYGON((0 0, 0 5, 5 5, 5 0, 0 0), (1 1, 2 1, 2 2, 1 2, 1 1), (1 3, 2 3, 2 4, 1 4, 1 3), (3 3, 4 3, 4 4, 3 4, 3 3))'), 0)
```

Output: 

```
LINESTRING (1 1, 2 1, 2 2, 1 2, 1 1)
```

## ST_Intersection

Introduction: Return the intersection geometry of A and B

Format: `ST_Intersection (A:geometry, B:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Intersection(
    ST_GeomFromWKT("POLYGON((1 1, 8 1, 8 8, 1 8, 1 1))"),
    ST_GeomFromWKT("POLYGON((2 2, 9 2, 9 9, 2 9, 2 2))")
    )
```

Output:

```
POLYGON ((2 8, 8 8, 8 2, 2 2, 2 8))
```

## ST_IsClosed

Introduction: RETURNS true if the LINESTRING start and end point are the same.

Format: `ST_IsClosed(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_IsClosed(ST_GeomFromText('LINESTRING(0 0, 1 1, 1 0)'))
```

Output: 

```
false
```

## ST_IsCollection

Introduction: Returns `TRUE` if the geometry type of the input is a geometry collection type.
Collection types are the following:

- GEOMETRYCOLLECTION
- MULTI{POINT, POLYGON, LINESTRING}

Format: `ST_IsCollection(geom: geometry)`

Since: `v1.5.0`

Example:

```sql
SELECT ST_IsCollection(ST_GeomFromText('MULTIPOINT(0 0), (6 6)'))
```

Output: 

```
true
```

Example:

```sql
SELECT ST_IsCollection(ST_GeomFromText('POINT(5 5)'))
```

Output: 

```
false
```

## ST_IsEmpty

Introduction: Test if a geometry is empty geometry

Format: `ST_IsEmpty (A:geometry)`

Since: `v1.2.1`

Spark SQL example:

```sql
SELECT ST_IsEmpty(ST_GeomFromWKT('POLYGON((0 0,0 1,1 1,1 0,0 0))'))
```

Output:

```
false
```

## ST_IsRing

Introduction: RETURN true if LINESTRING is ST_IsClosed and ST_IsSimple.

Format: `ST_IsRing(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_IsRing(ST_GeomFromText("LINESTRING(0 0, 0 1, 1 1, 1 0, 0 0)"))
```

Output: 

```
true
```

## ST_IsSimple

Introduction: Test if geometry's only self-intersections are at boundary points.

Format: `ST_IsSimple (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_IsSimple(ST_GeomFromWKT('POLYGON((1 1, 3 1, 3 3, 1 3, 1 1))'))
```

Output:

```
true
```

## ST_IsValid

Introduction: Test if a geometry is well formed

Format: `ST_IsValid (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_IsValid(ST_GeomFromWKT('POLYGON((0 0, 10 0, 10 10, 0 10, 0 0), (15 15, 15 20, 20 20, 20 15, 15 15))'))
```

Output:

```
false
```

## ST_Length

Introduction: Return the perimeter of A

Format: ST_Length (A:geometry)

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Length(ST_GeomFromWKT('LINESTRING(38 16,38 50,65 50,66 16,38 16)'))
```

Output:

```
123.0147027033899
```

## ST_LengthSpheroid

Introduction: Return the geodesic perimeter of A using WGS84 spheroid. Unit is meter. Works better for large geometries (country level) compared to `ST_Length` + `ST_Transform`. It is equivalent to PostGIS `ST_Length(geography, use_spheroid=true)` and `ST_LengthSpheroid` function and produces nearly identical results.

Geometry must be in EPSG:4326 (WGS84) projection and must be in ==lat/lon== order. You can use ==ST_FlipCoordinates== to swap lat and lon.

Format: `ST_LengthSpheroid (A:geometry)`

Since: `v1.4.1`

Spark SQL example:

```sql
SELECT ST_LengthSpheroid(ST_GeomFromWKT('Polygon ((0 0, 0 90, 0 0))'))
```

Output: 

```
20037508.342789244
```

## ST_LineFromMultiPoint

Introduction: Creates a LineString from a MultiPoint geometry.

Format: `ST_LineFromMultiPoint (A:geometry)`

Since: `v1.3.0`

Example:

```sql
SELECT ST_LineFromMultiPoint(ST_GeomFromText('MULTIPOINT((10 40), (40 30), (20 20), (30 10))'))
```

Output:

```
LINESTRING (10 40, 40 30, 20 20, 30 10)
```

## ST_LineInterpolatePoint

Introduction: Returns a point interpolated along a line. First argument must be a LINESTRING. Second argument is a Double between 0 and 1 representing fraction of total linestring length the point has to be located.

Format: `ST_LineInterpolatePoint (geom: geometry, fraction: Double)`

Since: `v1.0.1`

Spark SQL example:

```sql
SELECT ST_LineInterpolatePoint(ST_GeomFromWKT('LINESTRING(25 50, 100 125, 150 190)'), 0.2)
```

Output:
```
POINT (51.5974135047432 76.5974135047432)
```

## ST_LineMerge

Introduction: Returns a LineString formed by sewing together the constituent line work of a MULTILINESTRING.

!!!note
    Only works for MULTILINESTRING. Using other geometry will return a GEOMETRYCOLLECTION EMPTY. If the MultiLineString can't be merged, the original MULTILINESTRING is returned.

Format: `ST_LineMerge (A:geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_LineMerge(ST_GeomFromWKT('MULTILINESTRING ((-29 -27, -30 -29.7, -45 -33), (-45 -33, -46 -32))'))
```

Output:

```
LINESTRING (-29 -27, -30 -29.7, -45 -33, -46 -32)
```

## ST_LineSubstring

Introduction: Return a linestring being a substring of the input one starting and ending at the given fractions of total 2d length. Second and third arguments are Double values between 0 and 1. This only works with LINESTRINGs.

Format: `ST_LineSubstring (geom: geometry, startfraction: Double, endfraction: Double)`

Since: `v1.0.1`

Spark SQL example:

```sql
SELECT ST_LineSubstring(ST_GeomFromWKT('LINESTRING(25 50, 100 125, 150 190)'), 0.333, 0.666)
```

Output:

```
LINESTRING (69.28469348539744 94.28469348539744, 100 125, 111.70035626068274 140.21046313888758)
```

## ST_MakeLine

Introduction: Creates a LineString containing the points of Point, MultiPoint, or LineString geometries. Other geometry types cause an error.

Format: `ST_MakeLine(geom1: geometry, geom2: geometry)`

Format: `ST_MakeLine(geoms: array<geometry>)`

Since: `v1.5.0`

Example:

```sql
SELECT ST_AsText( ST_MakeLine(ST_Point(1,2), ST_Point(3,4)) );
```

Output:

```
LINESTRING(1 2,3 4)
```

Example:

```sql
SELECT ST_AsText( ST_MakeLine( 'LINESTRING(0 0, 1 1)', 'LINESTRING(2 2, 3 3)' ) );
```

Output:

```
 LINESTRING(0 0,1 1,2 2,3 3)
```

## ST_MakePolygon

Introduction: Function to convert closed linestring to polygon including holes

Format: `ST_MakePolygon(geom: geometry, holes: array<geometry>)`

Since: `v1.1.0`

Example:

```sql
SELECT ST_MakePolygon(
        ST_GeomFromText('LINESTRING(7 -1, 7 6, 9 6, 9 1, 7 -1)'),
        ARRAY(ST_GeomFromText('LINESTRING(6 2, 8 2, 8 1, 6 1, 6 2)'))
    ) 
```

Output:

```
POLYGON ((7 -1, 7 6, 9 6, 9 1, 7 -1), (6 2, 8 2, 8 1, 6 1, 6 2))
```

## ST_MakeValid

Introduction: Given an invalid geometry, create a valid representation of the geometry.

Collapsed geometries are either converted to empty (keepCollaped=true) or a valid geometry of lower dimension (keepCollapsed=false).
Default is keepCollapsed=false.

Format: `ST_MakeValid (A:geometry)`

Format: `ST_MakeValid (A:geometry, keepCollapsed:Boolean)`

Since: `v1.0.0`

Spark SQL example:

```sql
WITH linestring AS (
    SELECT ST_GeomFromWKT('LINESTRING(1 1, 1 1)') AS geom
) SELECT ST_MakeValid(geom), ST_MakeValid(geom, true) FROM linestring
```

Result:
```
+------------------+------------------------+
|st_makevalid(geom)|st_makevalid(geom, true)|
+------------------+------------------------+
|  LINESTRING EMPTY|             POINT (1 1)|
+------------------+------------------------+
```

!!!note
    In Sedona up to and including version 1.2 the behaviour of ST_MakeValid was different.
Be sure to check you code when upgrading. The previous implementation only worked for (multi)polygons and had a different interpretation of the second, boolean, argument.
It would also sometimes return multiple geometries for a single geometry input.

## ST_MinimumBoundingCircle

Introduction: Returns the smallest circle polygon that contains a geometry. The optional quadrantSegments parameter determines how many segments to use per quadrant and the default number of segments has been changed to 48 since v1.5.0. 

Format: `ST_MinimumBoundingCircle(geom: geometry, [Optional] quadrantSegments:int)`

Since: `v1.0.1`

Spark SQL example:

```sql
SELECT ST_MinimumBoundingCircle(ST_GeomFromWKT('LINESTRING(0 0, 0 1)'))
```

Output:

```
POLYGON ((0.5 0.5, 0.4997322937381828 0.4836404585891119, 0.4989294616193017 0.4672984353849285, 0.4975923633360985 0.4509914298352197, 0.4957224306869052 0.4347369038899742, 0.4933216660424395 0.4185522633027057, 0.4903926402016152 0.4024548389919359, 0.4869384896386668 0.3864618684828134, 0.4829629131445342 0.3705904774487396, 0.4784701678661044 0.3548576613727689, 0.4734650647475528 0.3392802673484192, 0.4679529633786629 0.3238749760393833, 0.4619397662556434 0.3086582838174551, 0.4554319124605879 0.2936464850978027, 0.4484363707663442 0.2788556548904993, 0.4409606321741775 0.2643016315870012, 0.4330127018922194 0.25, 0.4246010907632894 0.2359660746748161, 0.4157348061512726 0.2222148834901989, 0.4064233422958076 0.2087611515660989, 0.3966766701456176 0.1956192854956397, 0.3865052266813685 0.1828033579181773, 0.3759199037394887 0.1703270924499656, 0.3649320363489179 0.1582038489885644, 0.3535533905932738 0.1464466094067263, 0.3417961510114357 0.1350679636510822, 0.3296729075500345 0.1240800962605114, 0.3171966420818228 0.1134947733186316, 0.3043807145043603 0.1033233298543824, 0.2912388484339011 0.0935766577041924, 0.2777851165098012 0.0842651938487274, 0.264033925325184 0.0753989092367106, 0.2500000000000001 0.0669872981077807, 0.2356983684129989 0.0590393678258225, 0.2211443451095007 0.0515636292336559, 0.2063535149021975 0.0445680875394122, 0.1913417161825449 0.0380602337443566, 0.1761250239606168 0.0320470366213372, 0.1607197326515808 0.0265349352524472, 0.1451423386272312 0.0215298321338956, 0.1294095225512605 0.0170370868554659, 0.1135381315171867 0.0130615103613332, 0.0975451610080642 0.0096073597983848, 0.0814477366972944 0.0066783339575605, 0.0652630961100259 0.0042775693130948, 0.0490085701647804 0.0024076366639016, 0.0327015646150716 0.0010705383806983, 0.0163595414108882 0.0002677062618172, 0 0, -0.016359541410888 0.0002677062618172, -0.0327015646150715 0.0010705383806983, -0.0490085701647802 0.0024076366639015, -0.0652630961100257 0.0042775693130948, -0.0814477366972942 0.0066783339575605, -0.097545161008064 0.0096073597983847, -0.1135381315171866 0.0130615103613332, -0.1294095225512603 0.0170370868554658, -0.1451423386272311 0.0215298321338955, -0.1607197326515807 0.0265349352524472, -0.1761250239606166 0.0320470366213371, -0.1913417161825448 0.0380602337443566, -0.2063535149021973 0.044568087539412, -0.2211443451095006 0.0515636292336558, -0.2356983684129987 0.0590393678258224, -0.2499999999999999 0.0669872981077806, -0.264033925325184 0.0753989092367106, -0.277785116509801 0.0842651938487273, -0.291238848433901 0.0935766577041924, -0.3043807145043602 0.1033233298543823, -0.3171966420818227 0.1134947733186314, -0.3296729075500343 0.1240800962605111, -0.3417961510114356 0.1350679636510821, -0.3535533905932737 0.1464466094067262, -0.3649320363489177 0.1582038489885642, -0.3759199037394886 0.1703270924499655, -0.3865052266813683 0.1828033579181771, -0.3966766701456175 0.1956192854956396, -0.4064233422958076 0.2087611515660989, -0.4157348061512725 0.2222148834901987, -0.4246010907632894 0.235966074674816, -0.4330127018922192 0.2499999999999998, -0.4409606321741775 0.264301631587001, -0.4484363707663441 0.2788556548904991, -0.4554319124605878 0.2936464850978025, -0.4619397662556434 0.3086582838174551, -0.4679529633786628 0.3238749760393831, -0.4734650647475528 0.3392802673484191, -0.4784701678661044 0.3548576613727686, -0.4829629131445341 0.3705904774487395, -0.4869384896386668 0.3864618684828132, -0.4903926402016152 0.4024548389919357, -0.4933216660424395 0.4185522633027056, -0.4957224306869052 0.434736903889974, -0.4975923633360984 0.4509914298352196, -0.4989294616193017 0.4672984353849282, -0.4997322937381828 0.4836404585891118, -0.5 0.4999999999999999, -0.4997322937381828 0.5163595414108879, -0.4989294616193017 0.5327015646150715, -0.4975923633360985 0.5490085701647801, -0.4957224306869052 0.5652630961100257, -0.4933216660424395 0.5814477366972941, -0.4903926402016153 0.597545161008064, -0.4869384896386668 0.6135381315171865, -0.4829629131445342 0.6294095225512601, -0.4784701678661045 0.645142338627231, -0.4734650647475529 0.6607197326515806, -0.4679529633786629 0.6761250239606166, -0.4619397662556435 0.6913417161825446, -0.455431912460588 0.7063535149021972, -0.4484363707663442 0.7211443451095005, -0.4409606321741776 0.7356983684129986, -0.4330127018922194 0.7499999999999999, -0.4246010907632896 0.7640339253251838, -0.4157348061512727 0.777785116509801, -0.4064233422958078 0.7912388484339008, -0.3966766701456177 0.8043807145043602, -0.3865052266813686 0.8171966420818226, -0.3759199037394889 0.8296729075500342, -0.3649320363489179 0.8417961510114356, -0.353553390593274 0.8535533905932735, -0.3417961510114358 0.8649320363489177, -0.3296729075500345 0.8759199037394887, -0.317196642081823 0.8865052266813683, -0.3043807145043604 0.8966766701456175, -0.2912388484339011 0.9064233422958076, -0.2777851165098015 0.9157348061512725, -0.2640339253251843 0.9246010907632893, -0.2500000000000002 0.9330127018922192, -0.235698368412999 0.9409606321741775, -0.2211443451095007 0.9484363707663441, -0.2063535149021977 0.9554319124605877, -0.1913417161825452 0.9619397662556433, -0.176125023960617 0.9679529633786628, -0.1607197326515809 0.9734650647475528, -0.1451423386272312 0.9784701678661044, -0.1294095225512608 0.9829629131445341, -0.1135381315171869 0.9869384896386668, -0.0975451610080643 0.9903926402016152, -0.0814477366972945 0.9933216660424395, -0.0652630961100262 0.9957224306869051, -0.0490085701647807 0.9975923633360984, -0.0327015646150718 0.9989294616193017, -0.0163595414108883 0.9997322937381828, -0.0000000000000001 1, 0.0163595414108876 0.9997322937381828, 0.0327015646150712 0.9989294616193019, 0.04900857016478 0.9975923633360985, 0.0652630961100256 0.9957224306869052, 0.0814477366972943 0.9933216660424395, 0.0975451610080637 0.9903926402016153, 0.1135381315171863 0.9869384896386669, 0.1294095225512601 0.9829629131445342, 0.145142338627231 0.9784701678661045, 0.1607197326515807 0.9734650647475529, 0.1761250239606164 0.967952963378663, 0.1913417161825446 0.9619397662556435, 0.2063535149021972 0.955431912460588, 0.2211443451095005 0.9484363707663442, 0.2356983684129984 0.9409606321741777, 0.2499999999999997 0.9330127018922195, 0.2640339253251837 0.9246010907632896, 0.2777851165098009 0.9157348061512727, 0.291238848433901 0.9064233422958077, 0.3043807145043599 0.8966766701456179, 0.3171966420818225 0.8865052266813687, 0.3296729075500342 0.8759199037394889, 0.3417961510114355 0.8649320363489179, 0.3535533905932737 0.8535533905932738, 0.3649320363489175 0.841796151011436, 0.3759199037394885 0.8296729075500346, 0.3865052266813683 0.817196642081823, 0.3966766701456175 0.8043807145043604, 0.4064233422958076 0.7912388484339011, 0.4157348061512723 0.7777851165098015, 0.4246010907632893 0.7640339253251842, 0.4330127018922192 0.7500000000000002, 0.4409606321741774 0.735698368412999, 0.4484363707663439 0.7211443451095011, 0.4554319124605877 0.7063535149021978, 0.4619397662556433 0.6913417161825453, 0.4679529633786628 0.676125023960617, 0.4734650647475528 0.6607197326515809, 0.4784701678661043 0.6451423386272317, 0.482962913144534 0.6294095225512608, 0.4869384896386668 0.613538131517187, 0.4903926402016152 0.5975451610080643, 0.4933216660424395 0.5814477366972945, 0.4957224306869051 0.5652630961100262, 0.4975923633360984 0.5490085701647807, 0.4989294616193017 0.5327015646150718, 0.4997322937381828 0.5163595414108882, 0.5 0.5))
```

## ST_MinimumBoundingRadius

Introduction: Returns a struct containing the center point and radius of the smallest circle that contains a geometry.

Format: `ST_MinimumBoundingRadius(geom: geometry)`

Since: `v1.0.1`

Spark SQL example:

```sql
SELECT ST_MinimumBoundingRadius(ST_GeomFromText('POLYGON((1 1,0 0, -1 1, 1 1))'))
```

Output:

```
{POINT (0 1), 1.0}
```

## ST_Multi

Introduction: Returns a MultiGeometry object based on the geometry input.
ST_Multi is basically an alias for ST_Collect with one geometry.

Format

`ST_Multi(geom: geometry)`

Since: `v1.2.0`

Example:

```sql
SELECT ST_Multi(ST_GeomFromText('POINT(1 1)'))
```

Output:

```
MULTIPOINT (1 1)
```

## ST_NDims

Introduction: Returns the coordinate dimension of the geometry.

Format: `ST_NDims(geom: geometry)`

Since: `v1.3.1`

Spark SQL example with z coordinate:

```sql
SELECT ST_NDims(ST_GeomFromEWKT('POINT(1 1 2)'))
```

Output: 

```
3
```

Spark SQL example with x,y coordinate:

```sql
SELECT ST_NDims(ST_GeomFromText('POINT(1 1)'))
```

Output: 

```
2
```

## ST_Normalize

Introduction: Returns the input geometry in its normalized form.

Format

`ST_Normalize(geom: geometry)`

Since: `v1.3.0`

Example:

```sql
SELECT ST_AsEWKT(ST_Normalize(ST_GeomFromWKT('POLYGON((0 1, 1 1, 1 0, 0 0, 0 1))')))
```

Result:

```
POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))
```

## ST_NPoints

Introduction: Return points of the geometry

Format: `ST_NPoints (A:geometry)`

Since: `v1.0.0`

Example:

```sql
SELECT ST_NPoints(ST_GeomFromText('LINESTRING(77.29 29.07,77.42 29.26,77.27 29.31,77.29 29.07)'))
```

Output:

```
4
```

## ST_NRings

Introduction: Returns the number of rings in a Polygon or MultiPolygon. Contrary to ST_NumInteriorRings,
this function also takes into account the number of  exterior rings.

This function returns 0 for an empty Polygon or MultiPolygon.
If the geometry is not a Polygon or MultiPolygon, an IllegalArgument Exception is thrown.

Format: `ST_NRings(geom: geometry)`

Since: `1.4.1`


Examples:

Input: `POLYGON ((1 0, 1 1, 2 1, 2 0, 1 0))`

Output: `1`

Input: `'MULTIPOLYGON (((1 0, 1 6, 6 6, 6 0, 1 0), (2 1, 2 2, 3 2, 3 1, 2 1)), ((10 0, 10 6, 16 6, 16 0, 10 0), (12 1, 12 2, 13 2, 13 1, 12 1)))'`

Output: `4`

Input: `'POLYGON EMPTY'`

Output: `0`

Input: `'LINESTRING (1 0, 1 1, 2 1)'`

Output: `Unsupported geometry type: LineString, only Polygon or MultiPolygon geometries are supported.`

## ST_NumGeometries

Introduction: Returns the number of Geometries. If geometry is a GEOMETRYCOLLECTION (or MULTI*) return the number of geometries, for single geometries will return 1.

Format: `ST_NumGeometries (A:geometry)`

Since: `v1.0.0`

Example

```sql
SELECT ST_NumGeometries(ST_GeomFromWKT('LINESTRING (-29 -27, -30 -29.7, -45 -33)'))
```

Output:

```
1
```

## ST_NumInteriorRings

Introduction: RETURNS number of interior rings of polygon geometries.

Format: `ST_NumInteriorRings(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_NumInteriorRings(ST_GeomFromText('POLYGON ((0 0, 0 5, 5 5, 5 0, 0 0), (1 1, 2 1, 2 2, 1 2, 1 1))'))
```

Output: 

```
1
```

## ST_NumPoints
Introduction: Returns number of points in a LineString

!!!note
    If any other geometry is provided as an argument, an IllegalArgumentException is thrown.
    Example:
    `SELECT ST_NumPoints(ST_GeomFromWKT('MULTIPOINT ((0 0), (1 1), (0 1), (2 2))'))`

    Output: `IllegalArgumentException: Unsupported geometry type: MultiPoint, only LineString geometry is supported.`

Format: `ST_NumPoints(geom: geometry)`

Since: `v1.4.1`

Spark SQL example:

```sql
SELECT ST_NumPoints(ST_GeomFromText('LINESTRING(0 1, 1 0, 2 0)'))
```

Output: 

```
3
```

## ST_PointN

Introduction: Return the Nth point in a single linestring or circular linestring in the geometry. Negative values are counted backwards from the end of the LineString, so that -1 is the last point. Returns NULL if there is no linestring in the geometry.

Format: `ST_PointN(geom: geometry, n: integer)`

Since: `v1.2.1`

Spark SQL example:
```sql
SELECT ST_PointN(ST_GeomFromText("LINESTRING(0 0, 1 2, 2 4, 3 6)"), 2)
```

Result:

```
POINT (1 2)
```

## ST_PointOnSurface

Introduction: Returns a POINT guaranteed to lie on the surface.

Format: `ST_PointOnSurface(A:geometry)`

Since: `v1.2.1`

Examples:

```
SELECT ST_AsText(ST_PointOnSurface(ST_GeomFromText('POINT(0 5)')));
 st_astext
------------
 POINT(0 5)

SELECT ST_AsText(ST_PointOnSurface(ST_GeomFromText('LINESTRING(0 5, 0 10)')));
 st_astext
------------
 POINT(0 5)

SELECT ST_AsText(ST_PointOnSurface(ST_GeomFromText('POLYGON((0 0, 0 5, 5 5, 5 0, 0 0))')));
   st_astext
----------------
 POINT(2.5 2.5)

SELECT ST_AsText(ST_PointOnSurface(ST_GeomFromText('LINESTRING(0 5 1, 0 0 1, 0 10 2)')));
   st_astext
----------------
 POINT Z(0 0 1)

```

## ST_Polygon

Introduction: Function to create a polygon built from the given LineString and sets the spatial reference system from the srid

Format: `ST_Polygon(geom: geometry, srid: integer)`

Since: `v1.5.0`

Example:

```sql
SELECT ST_AsText( ST_Polygon(ST_GeomFromEWKT('LINESTRING(75 29 1, 77 29 2, 77 29 3, 75 29 1)'), 4326) );
```

Output:

```
POLYGON((75 29 1, 77 29 2, 77 29 3, 75 29 1))
```

## ST_ReducePrecision

Introduction: Reduce the decimals places in the coordinates of the geometry to the given number of decimal places. The last decimal place will be rounded. This function was called ST_PrecisionReduce in versions prior to v1.5.0.

Format: `ST_ReducePrecision (A:geometry, B:int)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_ReducePrecision(ST_GeomFromWKT('Point(0.1234567890123456789 0.1234567890123456789)')
    , 9)
```
The new coordinates will only have 9 decimal places.

Output:

```
POINT (0.123456789 0.123456789)
```

## ST_RemovePoint

Introduction: RETURN Line with removed point at given index, position can be omitted and then last one will be removed.

Format: `ST_RemovePoint(geom: geometry, position: integer)`

Format: `ST_RemovePoint(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_RemovePoint(ST_GeomFromText("LINESTRING(0 0, 1 1, 1 0)"), 1)
```

Output: 

```
LINESTRING(0 0, 1 0)
```

## ST_Reverse

Introduction: Return the geometry with vertex order reversed

Format: `ST_Reverse (A:geometry)`

Since: `v1.2.1`

Example:

```sql
SELECT ST_Reverse(ST_GeomFromWKT('LINESTRING(0 0, 1 2, 2 4, 3 6)'))
```

Output:

```
LINESTRING (3 6, 2 4, 1 2, 0 0)
```

## ST_S2CellIDs

Introduction: Cover the geometry with Google S2 Cells, return the corresponding cell IDs with the given level.
The level indicates the [size of cells](https://s2geometry.io/resources/s2cell_statistics.html). With a bigger level,
the cells will be smaller, the coverage will be more accurate, but the result size will be exponentially increasing.

Format: `ST_S2CellIDs(geom: geometry, level: Int)`

Since: `v1.4.0`

Spark SQL example:

```SQL
SELECT ST_S2CellIDs(ST_GeomFromText('LINESTRING(1 3 4, 5 6 7)'), 6)
```

Output:
```
[1159395429071192064, 1159958379024613376, 1160521328978034688, 1161084278931456000, 1170091478186196992, 1170654428139618304]
```

## ST_SetPoint

Introduction: Replace Nth point of linestring with given point. Index is 0-based. Negative index are counted backwards, e.g., -1 is last point.

Format: `ST_SetPoint (linestring: geometry, index: integer, point: geometry)`

Since: `v1.3.0`

Example:

```sql
SELECT ST_SetPoint(ST_GeomFromText('LINESTRING (0 0, 0 1, 1 1)'), 2, ST_GeomFromText('POINT (1 0)'))
```

Output:

```
LINESTRING (0 0, 0 1, 1 0)
```

## ST_SetSRID

Introduction: Sets the spatial reference system identifier (SRID) of the geometry.

Format: `ST_SetSRID (A:geometry, srid: Integer)`

Since: `v1.1.1`

Spark SQL example:

```sql
SELECT ST_AsEWKT(ST_SetSRID(ST_GeomFromWKT('POLYGON((1 1, 8 1, 8 8, 1 8, 1 1))'), 3021))
```

Output:

```
SRID=3021;POLYGON ((1 1, 8 1, 8 8, 1 8, 1 1))
```

## ST_SimplifyPreserveTopology

Introduction: Simplifies a geometry and ensures that the result is a valid geometry having the same dimension and number of components as the input,
and with the components having the same topological relationship.

Since: `v1.0.0`

Format: `ST_SimplifyPreserveTopology (A:geometry, distanceTolerance: Double)`

Example:

```sql
SELECT ST_SimplifyPreserveTopology(ST_GeomFromText('POLYGON((8 25, 28 22, 28 20, 15 11, 33 3, 56 30, 46 33,46 34, 47 44, 35 36, 45 33, 43 19, 29 21, 29 22,35 26, 24 39, 8 25))'), 10)
```

Output:

```
POLYGON ((8 25, 28 22, 15 11, 33 3, 56 30, 47 44, 35 36, 43 19, 24 39, 8 25))
```

## ST_Split

Introduction: Split an input geometry by another geometry (called the blade).
Linear (LineString or MultiLineString) geometry can be split by a Point, MultiPoint, LineString, MultiLineString, Polygon, or MultiPolygon.
Polygonal (Polygon or MultiPolygon) geometry can be split by a LineString, MultiLineString, Polygon, or MultiPolygon.
In either case, when a polygonal blade is used then the boundary of the blade is what is actually split by.
ST_Split will always return either a MultiLineString or MultiPolygon even if they only contain a single geometry.
Homogeneous GeometryCollections are treated as a multi-geometry of the type it contains.
For example, if a GeometryCollection of only Point geometries is passed as a blade it is the same as passing a MultiPoint of the same geometries.

Since: `v1.4.0`

Format: `ST_Split (input: geometry, blade: geometry)`

Spark SQL Example:

```sql
SELECT ST_Split(
    ST_GeomFromWKT('LINESTRING (0 0, 1.5 1.5, 2 2)'),
    ST_GeomFromWKT('MULTIPOINT (0.5 0.5, 1 1)'))
```

Output: 

```
MULTILINESTRING ((0 0, 0.5 0.5), (0.5 0.5, 1 1), (1 1, 1.5 1.5, 2 2))
```

## ST_SRID

Introduction: Return the spatial reference system identifier (SRID) of the geometry.

Format: `ST_SRID (A:geometry)`

Since: `v1.1.1`

Spark SQL example:

```sql
SELECT ST_SRID(ST_SetSRID(ST_GeomFromWKT('POLYGON((1 1, 8 1, 8 8, 1 8, 1 1))'), 3021))
```

Output:

```
3021
```

## ST_StartPoint

Introduction: Returns first point of given linestring.

Format: `ST_StartPoint(geom: geometry)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_StartPoint(ST_GeomFromText('LINESTRING(100 150,50 60, 70 80, 160 170)'))
```

Output: 

```
POINT(100 150)
```

## ST_SubDivide

Introduction: Returns list of geometries divided based of given maximum number of vertices.

Format: `ST_SubDivide(geom: geometry, maxVertices: int)`

Since: `v1.1.0`

Spark SQL example:
```sql
SELECT ST_SubDivide(ST_GeomFromText("POLYGON((35 10, 45 45, 15 40, 10 20, 35 10), (20 30, 35 35, 30 20, 20 30))"), 5)

```

Output:
```
[
    POLYGON((37.857142857142854 20, 35 10, 10 20, 37.857142857142854 20)),
    POLYGON((15 20, 10 20, 15 40, 15 20)),
    POLYGON((20 20, 15 20, 15 30, 20 30, 20 20)),
    POLYGON((26.428571428571427 20, 20 20, 20 30, 26.4285714 23.5714285, 26.4285714 20)),
    POLYGON((15 30, 15 40, 20 40, 20 30, 15 30)),
    POLYGON((20 40, 26.4285714 40, 26.4285714 32.1428571, 20 30, 20 40)),
    POLYGON((37.8571428 20, 30 20, 34.0476190 32.1428571, 37.8571428 32.1428571, 37.8571428 20)),
    POLYGON((34.0476190 34.6825396, 26.4285714 32.1428571, 26.4285714 40, 34.0476190 40, 34.0476190 34.6825396)),
    POLYGON((34.0476190 32.1428571, 35 35, 37.8571428 35, 37.8571428 32.1428571, 34.0476190 32.1428571)),
    POLYGON((35 35, 34.0476190 34.6825396, 34.0476190 35, 35 35)),
    POLYGON((34.0476190 35, 34.0476190 40, 37.8571428 40, 37.8571428 35, 34.0476190 35)),
    POLYGON((30 20, 26.4285714 20, 26.4285714 23.5714285, 30 20)),
    POLYGON((15 40, 37.8571428 43.8095238, 37.8571428 40, 15 40)),
    POLYGON((45 45, 37.8571428 20, 37.8571428 43.8095238, 45 45))
]
```

Spark SQL example:

```sql
SELECT ST_SubDivide(ST_GeomFromText("LINESTRING(0 0, 85 85, 100 100, 120 120, 21 21, 10 10, 5 5)"), 5)
```

Output:
```
[
    LINESTRING(0 0, 5 5)
    LINESTRING(5 5, 10 10)
    LINESTRING(10 10, 21 21)
    LINESTRING(21 21, 60 60)
    LINESTRING(60 60, 85 85)
    LINESTRING(85 85, 100 100)
    LINESTRING(100 100, 120 120)
]
```

## ST_SubDivideExplode

Introduction: It works the same as ST_SubDivide but returns new rows with geometries instead of list.

Format: `ST_SubDivideExplode(geom: geometry, maxVertices: int)`

Since: `v1.1.0`

Example:

Query:
```sql
SELECT ST_SubDivideExplode(ST_GeomFromText("LINESTRING(0 0, 85 85, 100 100, 120 120, 21 21, 10 10, 5 5)"), 5)
```

Result:

```
+-----------------------------+
|geom                         |
+-----------------------------+
|LINESTRING(0 0, 5 5)         |
|LINESTRING(5 5, 10 10)       |
|LINESTRING(10 10, 21 21)     |
|LINESTRING(21 21, 60 60)     |
|LINESTRING(60 60, 85 85)     |
|LINESTRING(85 85, 100 100)   |
|LINESTRING(100 100, 120 120) |
+-----------------------------+
```

Using Lateral View

Table:
```
+-------------------------------------------------------------+
|geometry                                                     |
+-------------------------------------------------------------+
|LINESTRING(0 0, 85 85, 100 100, 120 120, 21 21, 10 10, 5 5)  |
+-------------------------------------------------------------+
```

Query
```sql
select geom from geometries LATERAL VIEW ST_SubdivideExplode(geometry, 5) AS geom
```

Result:
```
+-----------------------------+
|geom                         |
+-----------------------------+
|LINESTRING(0 0, 5 5)         |
|LINESTRING(5 5, 10 10)       |
|LINESTRING(10 10, 21 21)     |
|LINESTRING(21 21, 60 60)     |
|LINESTRING(60 60, 85 85)     |
|LINESTRING(85 85, 100 100)   |
|LINESTRING(100 100, 120 120) |
+-----------------------------+
```

## ST_SymDifference

Introduction: Return the symmetrical difference between geometry A and B (return parts of geometries which are in either of the sets, but not in their intersection)


Format: `ST_SymDifference (A:geometry, B:geometry)`

Since: `v1.2.0`

Example:

```sql
SELECT ST_SymDifference(ST_GeomFromWKT('POLYGON ((-3 -3, 3 -3, 3 3, -3 3, -3 -3))'), ST_GeomFromWKT('POLYGON ((-2 -3, 4 -3, 4 3, -2 3, -2 -3))'))
```

Output:

```
MULTIPOLYGON (((-2 -3, -3 -3, -3 3, -2 3, -2 -3)), ((3 -3, 3 3, 4 3, 4 -3, 3 -3)))
```

## ST_Transform

Introduction:

Transform the Spatial Reference System / Coordinate Reference System of A, from SourceCRS to TargetCRS. For SourceCRS and TargetCRS, WKT format is also available since v1.3.1.

**Lon/Lat Order in the input geometry**

If the input geometry is in lat/lon order, it might throw an error such as `too close to pole`, `latitude or longitude exceeded limits`, or give unexpected results.
You need to make sure that the input geometry is in lon/lat order. If the input geometry is in lat/lon order, you can use ==ST_FlipCoordinates== to swap X and Y.

**Lon/Lat Order in the source and target CRS**

Sedona will make sure the source and target CRS to be in lon/lat order. If the source CRS or target CRS is in lat/lon order, these CRS will be swapped to lon/lat order.

**CRS code**

The CRS code is the code of the CRS in the official EPSG database (https://epsg.org/) in the format of `EPSG:XXXX`. A community tool [EPSG.io](https://epsg.io/) can help you quick identify a CRS code. For example, the code of WGS84 is `EPSG:4326`.

**WKT format**

You can also use OGC WKT v1 format to specify the source CRS and target CRS. An example OGC WKT v1 CRS of `EPGS:3857` is as follows:

```
PROJCS["WGS 84 / Pseudo-Mercator",
    GEOGCS["WGS 84",
        DATUM["WGS_1984",
            SPHEROID["WGS 84",6378137,298.257223563,
                AUTHORITY["EPSG","7030"]],
            AUTHORITY["EPSG","6326"]],
        PRIMEM["Greenwich",0,
            AUTHORITY["EPSG","8901"]],
        UNIT["degree",0.0174532925199433,
            AUTHORITY["EPSG","9122"]],
        AUTHORITY["EPSG","4326"]],
    PROJECTION["Mercator_1SP"],
    PARAMETER["central_meridian",0],
    PARAMETER["scale_factor",1],
    PARAMETER["false_easting",0],
    PARAMETER["false_northing",0],
    UNIT["metre",1,
        AUTHORITY["EPSG","9001"]],
    AXIS["Easting",EAST],
    AXIS["Northing",NORTH],
    EXTENSION["PROJ4","+proj=merc +a=6378137 +b=6378137 +lat_ts=0 +lon_0=0 +x_0=0 +y_0=0 +k=1 +units=m +nadgrids=@null +wktext +no_defs"],
    AUTHORITY["EPSG","3857"]]
```

!!!note
    By default, this function uses lon/lat order since `v1.5.0`. Before, it used lat/lon order.

!!!note
    By default, ==ST_Transform== follows the `lenient` mode which tries to fix issues by itself. You can append a boolean value at the end to enable the `strict` mode. In `strict` mode, ==ST_Transform== will throw an error if it finds any issue.

Format: `ST_Transform (A:geometry, SourceCRS:string, TargetCRS:string ,[Optional] lenientMode:bool)`

Since: `v1.2.0`

Example:

```sql
SELECT ST_AsText(ST_Transform(ST_GeomFromText('POLYGON((170 50,170 72,-130 72,-130 50,170 50))'),'EPSG:4326', 'EPSG:32649'))
```

```sql
SELECT ST_AsText(ST_Transform(ST_GeomFromText('POLYGON((170 50,170 72,-130 72,-130 50,170 50))'),'EPSG:4326', 'EPSG:32649', false))
```

Output:

```
POLYGON ((8766047.980342899 17809098.336766362, 5122546.516721856 18580261.912528664, 3240775.0740796793 -13688660.50985159, 4556241.924514083 -12463044.21488129, 8766047.980342899 17809098.336766362))
```


## ST_Translate
Introduction: Returns the input geometry with its X, Y and Z coordinates (if present in the geometry) translated by deltaX, deltaY and deltaZ (if specified)

If the geometry is 2D, and a deltaZ parameter is specified, no change is done to the Z coordinate of the geometry and the resultant geometry is also 2D.

If the geometry is empty, no change is done to it. 
If the given geometry contains sub-geometries (GEOMETRY COLLECTION, MULTI POLYGON/LINE/POINT), all underlying geometries are individually translated.

Format: `ST_Translate(geometry: geometry, deltaX: deltaX, deltaY: deltaY, deltaZ: deltaZ)`

Since: `1.4.1`

Example:

```sql
SELECT ST_Translate(ST_GeomFromText('GEOMETRYCOLLECTION(MULTIPOLYGON(((3 2,3 3,4 3,4 2,3 2)),((3 4,5 6,5 7,3 4))), POINT(1 1 1), LINESTRING EMPTY)'), 2, 2, 3)
```

Output:

```
GEOMETRYCOLLECTION (MULTIPOLYGON (((5 4, 5 5, 6 5, 6 4, 5 4)), ((5 6, 7 8, 7 9, 5 6))), POINT (3 3), LINESTRING EMPTY)
```

Example:

```sql
SELECT ST_Translate(ST_GeomFromText('POINT(-71.01 42.37)'),1,2)
```

Output:

```
POINT (-70.01 44.37)
```

## ST_Union

Introduction: Return the union of geometry A and B


Format: `ST_Union (A:geometry, B:geometry)`

Since: `v1.2.0`

Example:

```sql
SELECT ST_Union(ST_GeomFromWKT('POLYGON ((-3 -3, 3 -3, 3 3, -3 3, -3 -3))'), ST_GeomFromWKT('POLYGON ((1 -2, 5 0, 1 2, 1 -2))'))
```

Output:

```
POLYGON ((3 -1, 3 -3, -3 -3, -3 3, 3 3, 3 1, 5 0, 3 -1))
```

## ST_VoronoiPolygons

Introduction: Returns a two-dimensional Voronoi diagram from the vertices of the supplied geometry. The result is a GeometryCollection of Polygons that covers an envelope larger than the extent of the input vertices. Returns null if input geometry is null. Returns an empty geometry collection if the input geometry contains only one vertex. Returns an empty geometry collection if the extend_to envelope has zero area.


Format: `ST_VoronoiPolygons(g1: geometry, tolerance: float , extend_to: geometry)`

Optional parameters:
'tolerance' : The distance within which vertices will be considered equivalent. Robustness of the algorithm can be improved by supplying a nonzero tolerance distance. (default = 0.0)

'extend_to' : If a geometry is supplied as the "extend_to" parameter, the diagram will be extended to cover the envelope of the "extend_to" geometry, unless that envelope is smaller than the default envelope (default = NULL. By default, we extend the bounding box of the diagram by the max between bounding box's height and bounding box's width).

Since: `v1.5.0`

Example:

```sql
SELECT st_astext(ST_VoronoiPolygons(ST_GeomFromText('MULTIPOINT ((0 0), (1 1))')));
```

Output: 

```
GEOMETRYCOLLECTION(POLYGON((-1 2,2 -1,-1 -1,-1 2)),POLYGON((-1 2,2 2,2 -1,-1 2)))
```

## ST_X

Introduction: Returns X Coordinate of given Point null otherwise.

Format: `ST_X(pointA: Point)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_X(ST_POINT(0.0 25.0))
```

Output: 

```
0.0
```

## ST_XMax

Introduction: Returns the maximum X coordinate of a geometry

Format: `ST_XMax (A:geometry)`

Since: `v1.2.1`

Example:

```sql
SELECT ST_XMax(ST_GeomFromText('POLYGON ((-1 -11, 0 10, 1 11, 2 12, -1 -11))'))
```

Output:

```
2
```

## ST_XMin

Introduction: Returns the minimum X coordinate of a geometry

Format: `ST_XMin (A:geometry)`

Since: `v1.2.1`

Example:

```sql
SELECT ST_XMin(ST_GeomFromText('POLYGON ((-1 -11, 0 10, 1 11, 2 12, -1 -11))'))
```

Output: 

```
-1
```

## ST_Y

Introduction: Returns Y Coordinate of given Point, null otherwise.

Format: `ST_Y(pointA: Point)`

Since: `v1.0.0`

Spark SQL example:

```sql
SELECT ST_Y(ST_POINT(0.0 25.0))
```

Output: 

```
25.0
```

## ST_YMax

Introduction: Return the minimum Y coordinate of A

Format: `ST_YMax (A:geometry)`

Since: `v1.2.1`

Spark SQL example:
```sql
SELECT ST_YMax(ST_GeomFromText('POLYGON((0 0 1, 1 1 1, 1 2 1, 1 1 1, 0 0 1))'))
```

Output: 

```
2
```

## ST_YMin

Introduction: Return the minimum Y coordinate of A

Format: `ST_Y_Min (A:geometry)`

Since: `v1.2.1`

Spark SQL example:

```sql
SELECT ST_YMin(ST_GeomFromText('POLYGON((0 0 1, 1 1 1, 1 2 1, 1 1 1, 0 0 1))'))
```

Output: 

```
0
```

## ST_Z

Introduction: Returns Z Coordinate of given Point, null otherwise.

Format: `ST_Z(pointA: Point)`

Since: `v1.2.0`

Spark SQL example:

```sql
SELECT ST_Z(ST_POINT(0.0 25.0 11.0))
```

Output: 

```
11.0
```

## ST_ZMax

Introduction: Returns Z maxima of the given geometry or null if there is no Z coordinate.

Format: `ST_ZMax(geom: geometry)`

Since: `v1.3.1`

Spark SQL example:

```sql
SELECT ST_ZMax(ST_GeomFromText('POLYGON((0 0 1, 1 1 1, 1 2 1, 1 1 1, 0 0 1))'))
```

Output: 

```
1.0
```

## ST_ZMin

Introduction: Returns Z minima of the given geometry or null if there is no Z coordinate.

Format: `ST_ZMin(geom: geometry)`

Since: `v1.3.1`

Spark SQL example:

```sql
SELECT ST_ZMin(ST_GeomFromText('LINESTRING(1 3 4, 5 6 7)'))
```

Output: 

```
4.0
```

