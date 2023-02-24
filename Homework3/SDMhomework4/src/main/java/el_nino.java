import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.ArrayList;


public class el_nino {

    private static Variable sst;

    private static double calculateONIByMonth(int year, int month) {
        //(5N-5S, 170W-120W)
        try {
            Array data = sst.read((year-1854)*12+month +",41:47,95:120");
            double sum = 0.0;
            double tempNumber;
            int count = 0;
            while(data.hasNext()){
                tempNumber = data.nextDouble();
                if(tempNumber != -9.96921E36f){
                    sum += tempNumber;
                    count++;
                }
            }
            return sum/(double)count;

        } catch (InvalidRangeException e) {
            System.out.println(e.getMessage());
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
        return 0.0;
    }

    private static ArrayList<Double> calculateONISequence() {
        ArrayList<Double> answer = new ArrayList<Double>();
        for (int year = 1950; year <= 1980; year++) {
            for(int month = 0; month < 10; month++){
                double a1 = calculateONIByMonth(year, month);
                double a2 = calculateONIByMonth(year, month+1);
                double a3 = calculateONIByMonth(year, month+2);
                answer.add((a1+a2+a3)/3.0);
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        try (NetcdfFile ncfile = NetcdfFiles.open("sst.mon.ltm.nc")) {
            sst = ncfile.findVariable("sst");
            ArrayList<Double> result = calculateONISequence();
            double sum = 0.0;
            for (double val : result){
                sum += val;
            }
            double mExpectation = sum/(double)result.size();
            for (int i = 0; i < result.size(); ++i) {
                System.out.format("%.3f",result.get(i)-mExpectation);
                System.out.println();
            }

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
