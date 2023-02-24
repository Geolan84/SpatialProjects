import requests
import shutil
import urllib

#url = "https://services.sentinel-hub.com/ogc/wmts/d2c5e58d-2c32-44d2-941e-41e9f9a2b0ee?REQUEST=GetCapabilities"
# Example: Polygon((319581 6160457, 376044 6211481, 422925 6275715, 428882 6142326, 319581 6160457))
polygon = input().lower()
polygon = polygon.replace("(", '')
polygon = polygon.replace(")", '')
polygon = polygon.replace(",", '')
polygon = polygon.replace("polygon", '')
print(polygon)
numbers = polygon.split()
minFirst = 100000000.0
maxFirst = -100000000.0
minSecond = 100000000.0
maxSecond = -100000000.0
for i in range(len(numbers)):
    number = float(numbers[i])
    if i%2 == 0:
        if(number < minFirst):
            minFirst = number
        if(number > maxFirst):
            maxFirst = number
    else:
        if(number < minSecond):
            minSecond = number
        if(number > maxSecond):
            maxSecond = number
        
minFirst /= 1019069.126674
minSecond /= 1019069.126674
maxFirst /= 1019069.126674
maxSecond /= 1019069.126674
print(minFirst, minSecond, maxFirst, maxSecond)
# 1019069.126674
print("Enter time in format: 2020-06-01/2020-06-31")
time = input()
url = f"https://services.sentinel-hub.com/ogc/wmts/d2c5e58d-2c32-44d2-941e-41e9f9a2b0ee?REQUEST=GetTile&TILEMATRIXSET=UTM37N&FORMAT=image/tiff&LAYER=NATURAL-COLOR&TILEMATRIX=9&TILEROW=21&TILECOL=1&TIME={time}"
urllib.request.urlretrieve(url, "TestDonwloadPython.tiff")
#res = requests.get(url)
#print(res.content)
