package au.com.new1step.herbalist.chart;

import java.io.File;

/**
 * Created by awu on 16/05/2018.
 */
public class Bar3DChartGeneratorDemo {

    public static void main(String[] args){
        File chartDir = new File( "/data/chart");
        if(!chartDir.exists()){
            chartDir.mkdir();
        }
        String chartPath = chartDir + "/weeklyChart2.jpeg";

        ChartDataProducer dataProducer = new ChartDataProducer(null);
        ChartModel model = dataProducer.fetchTestWeeklyChartData();

        Integer width = 480;
        Integer height = 240;
        Bar3DChartGenerator.generate(chartPath, width, height, model);

    }

}
