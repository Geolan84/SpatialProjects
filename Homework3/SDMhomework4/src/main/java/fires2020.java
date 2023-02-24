import org.checkerframework.checker.units.qual.A;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.*;
import ucar.nc2.write.NetcdfFormatWriter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class fires2020 {

    private static Array formNewArray(){
        DecimalFormat df = new DecimalFormat("00");
        Array resultArray = Array.factory(DataType.FLOAT, new int[]{180, 360});
        for(int row = 0; row < 180; row++) {
            for (int column = 0; column < 360; column++) {
                resultArray.setDouble(row + column*180, -9999.0f);
            }
        }
        for(int tempDay=1; tempDay <= 31; ++tempDay){
            try (NetcdfFile ncfile = NetcdfFiles.open("MOPITT/CO/2010.08."+df.format(tempDay)+"/MOP03J-201008"+df.format(tempDay)+"-L3V3.1.3.hdf")) {
                Group sub_group = ncfile.findGroup("MOP03").getGroups().get(0);
                Variable co = sub_group.findVariableLocal("Retrieved_CO_Total_Column_Day");
                Array tempValues = co.read("0:179,0:359");
                for(int row = 0; row < 180; row++) {
                    for (int column = 0; column < 360; column++) {
                        Float newNumber = tempValues.getFloat(row + 180 * column);
                        Float oldNumber = resultArray.getFloat(row + 180 * column);
                        if (newNumber != -9999.0f) {
                            resultArray.setDouble(row + 180 * column, oldNumber + newNumber);
                        }
                    }
                }
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            } catch (NullPointerException nullE){
                System.out.println(nullE.getMessage());
            }
        }
        return resultArray;
    }

    public static void main(String[] args){
        Array newData = formNewArray();
        //System.out.println(newData);

        NetcdfFormatWriter.Builder builder = NetcdfFormatWriter.createNewNetcdf3("fires2020.nc");


        Dimension lonDim = builder.addDimension("nlon", 360);
        Dimension latDim = builder.addDimension("nlat", 180);

        List<Dimension> dims = new ArrayList<Dimension>();
        dims.add(latDim);
        dims.add(lonDim);

        List<Dimension> latDimList = new ArrayList<Dimension>();
        latDimList.add(latDim);

        List<Dimension> lonDimList = new ArrayList<Dimension>();
        lonDimList.add(lonDim);

        builder.addVariable("Latitude", DataType.FLOAT, latDimList);
        float startValue = -89.5f;
        Array latArray = Array.factory(DataType.FLOAT, new int[]{180});
        for(int i = 0; i < 180; ++i){
            latArray.setFloat(i, startValue);
            startValue += 1.0f;
        }

        startValue = -179.5f;
        builder.addVariable("Longitude", DataType.FLOAT, lonDimList);
        Array lonArray = Array.factory(DataType.FLOAT, new int[]{360});
        for(int i = 0; i < 360; ++i){
            lonArray.setFloat(i, startValue);
            startValue += 1.0f;
        }


        builder.addVariable("CO", DataType.FLOAT, dims)
                .addAttribute(new Attribute("units", "M/cm2"))
                .addAttribute(new Attribute("FillValue", -9999.0f));


        try (NetcdfFormatWriter writer = builder.build()) {
            writer.write("CO", newData);
            writer.write("Longitude", lonArray);
            writer.write("Latitude", latArray);

        } catch (IOException e) {
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }
    }

}
