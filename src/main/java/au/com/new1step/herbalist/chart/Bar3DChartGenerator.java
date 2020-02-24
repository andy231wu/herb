package au.com.new1step.herbalist.chart;

import org.jfree.data.category.DefaultCategoryDataset;

import java.io.ByteArrayOutputStream;

/**
 * Created by awu on 16/05/2018.
 */
public class Bar3DChartGenerator {
    // memory chart
    public static  ByteArrayOutputStream generate(Integer width, Integer height, ChartModel model){
        DefaultCategoryDataset dataset = setupChartDataset(model);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        draw(bos, width, height, model.getHTitle(), model.getVTitle(), dataset );
        return bos;
    }

    // physic chart
    public static String generate(String path, Integer width, Integer height, ChartModel model){
        DefaultCategoryDataset dataset = setupChartDataset(model);

        draw(path, width, height, model.getHTitle(),model.getVTitle(), dataset );
        return path;
    }

    private static DefaultCategoryDataset setupChartDataset(ChartModel model){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        /* omit */

        return dataset;
    }

    // draw memory chart
    private static void draw(ByteArrayOutputStream bos, Integer width, Integer height, String hTitle, String vTitle, DefaultCategoryDataset dataset ) {
      /* omit */
    }

    // draw physic chart
    private static void draw(String path, Integer width, Integer height, String hTitle, String vTitle, DefaultCategoryDataset dataset ) {
        /* omit */
    }
}
