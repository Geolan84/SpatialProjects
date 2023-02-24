SELECT ST_MakeLine(s.geom, p.geom) as geom
FROM public."Stations" s
INNER JOIN public."Points" p
    ON ST_Intersects(ST_Buffer(s.geom, 200), p.geom)
WHERE p.shop IS NOT NULL AND s.railway = 'tram_stop';