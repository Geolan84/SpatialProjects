SELECT name, geom, osm_id
FROM public."Buildings"
WHERE ST_Intersects(ST_Buffer(ST_GeomFromText('MultiLineString((415688.17796501616248861 6179603.57537323795258999, 415709.07612204371253029 6179691.91758249141275883, 415721.89999112882651389 6179759.36163471639156342, 415734.72386021394049749 6179834.40501676965504885, 415742.32319004210876301 6179898.52436219528317451, 415745.41041778476210311 6179976.41749293357133865, 415744.46050155622651801 6180107.98089058510959148, 415739.23596229933900759 6180234.7947070924565196, 415735.43629738519666716 6180332.16112051624804735, 415731.6366324711125344 6180379.65693194232881069, 415717.38788904325338081 6180455.17527211084961891, 415677.49140744522446766 6180609.06170113198459148, 415670.36703573126578704 6180624.2603607876226306))', 32637), 5), public."Buildings".geom)
UNION
SELECT name, geom, osm_id
FROM public."Stations"
WHERE ST_Intersects(ST_Buffer(ST_GeomFromText('MultiLineString((415688.17796501616248861 6179603.57537323795258999, 415709.07612204371253029 6179691.91758249141275883, 415721.89999112882651389 6179759.36163471639156342, 415734.72386021394049749 6179834.40501676965504885, 415742.32319004210876301 6179898.52436219528317451, 415745.41041778476210311 6179976.41749293357133865, 415744.46050155622651801 6180107.98089058510959148, 415739.23596229933900759 6180234.7947070924565196, 415735.43629738519666716 6180332.16112051624804735, 415731.6366324711125344 6180379.65693194232881069, 415717.38788904325338081 6180455.17527211084961891, 415677.49140744522446766 6180609.06170113198459148, 415670.36703573126578704 6180624.2603607876226306))', 32637), 5), public."Stations".geom)
