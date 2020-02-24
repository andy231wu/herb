package au.com.new1step.herbalist.chart;

import au.com.new1step.herbalist.service.DoctorService;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChartDataProducer {

    private DoctorService doctorService;

    public ChartDataProducer(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    // this will be removed later
    public ChartModel fetchTestWeeklyChartData() {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorWeeklyChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorWeeklyPatientChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorWeeklyServiceFeeChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorMonthlyPatientChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return  model;
    }

    public ChartModel fetchDoctorMonthlyServiceFeeChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorYearlyChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorYearlyPatientChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    public ChartModel fetchDoctorYearlyServiceFeeChartData(String startDate) {
        ChartModel model = new ChartModel();

        /* omit */
        return model;
    }

    private List<Double> patientsForTheWeek(LocalDate firstDateOfWeek){
        List<Double> noOfPatientsByDay = doctorService.findDailyNoOfWeekPatients(firstDateOfWeek);
        return noOfPatientsByDay;
    }
    private List<Double> serviceFeesForTheWeek(LocalDate firstDateOfWeek){
        List<Double> serviceFeesByDay = doctorService.findDailyWeeklyFees(firstDateOfWeek);
        return serviceFeesByDay;
    }

    private List<Double> patientsForTheMonth(LocalDate start){
        return doctorService.findDailyNoOfPatientsForTheMonth(start);
    }

    private List<Double> feesForTheMonth(LocalDate start){
        return doctorService.findDailyFeesForTheMonth(start);
    }

    private List<Double> patientsForTheYear(LocalDate dayOneOfYear){
        return doctorService.findMonthlyNoOfPatientsForTheYear(dayOneOfYear);
    }

    private List<Double> feesForTheYear(LocalDate dayOneOfYear){
        return doctorService.findMonthlyFeesForTheYear(dayOneOfYear);
    }
}
