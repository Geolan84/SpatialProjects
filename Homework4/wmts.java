import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class wmts {

    public static void main(String[] args){
        String getCapabilities = "https://services.sentinel-hub.com/ogc/wmts/d2c5e58d-2c32-44d2-941e-41e9f9a2b0ee?REQUEST=GetCapabilities";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter polygon in WKT-format:");
        String polygon = scanner.nextLine().toLowerCase(Locale.ROOT);
        System.out.println(polygon);
        polygon = polygon.replace(",", "");
        polygon = polygon.replace("(", "");
        polygon = polygon.replace(")", "");
        polygon = polygon.replace("polygon", "");
        String[] points = polygon.split(" ");
        double minFirst = Double.MAX_VALUE;
        double maxFirst = Double.MIN_VALUE;
        double minSecond = Double.MAX_VALUE;
        double maxSecond = Double.MIN_VALUE;
        for(int index = 0; index < points.length; ++index){
            double result = Double.parseDouble(points[index]);
            if(index % 2 == 0){
                if(result < minFirst){
                    minFirst = result;
                }
                if(result > maxFirst){
                    maxFirst = result;
                }
            } else{
                if(result < minSecond){
                    minSecond = result;
                }
                if(result > maxSecond){
                    maxSecond = result;
                }
            }
        }
        System.out.println("Введите дату в формате: \"2020-08-01/2020-08-31\"");
        String time = scanner.nextLine();

        String link = "https://services.sentinel-hub.com/ogc/wmts/d2c5e58d-2c32-44d2-941e-41e9f9a2b0ee?REQUEST=GetTile&FORMAT=image/tiff&LAYER=NATURAL-COLOR&"+time;
        link += "&TILEMATRIX=14&TILEROW=350&TILECOL=250";

//        System.out.println(minFirst);
//        System.out.println(maxSecond);
//        System.out.println(minSecond);
//        System.out.println(maxSecond);
    }

}
