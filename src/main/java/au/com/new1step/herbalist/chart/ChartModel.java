package au.com.new1step.herbalist.chart;

import lombok.Data;

import java.util.List;

@Data
public class ChartModel {

    private String hTitle;
    private String vTitle;
    private List<String> seriesLabels;
    private List<String> groupLabels;
    List<List<Double>> yValues;

}
