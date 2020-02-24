package au.com.new1step.herbalist.servlet;

import au.com.new1step.herbalist.chart.Bar3DChartGenerator;
import au.com.new1step.herbalist.chart.ChartDataProducer;
import au.com.new1step.herbalist.chart.ChartModel;
import au.com.new1step.herbalist.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by awu on 18/05/2018.
 */

@WebServlet("/herbalist/chart/*")
public class ChartServlet extends HttpServlet {

    @Autowired
    private DoctorService doctorService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestedFile = request.getPathInfo();
        String startDate = request.getParameter("startDate");

        if(startDate == null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate today = LocalDate.now();

            if(requestedFile.contains("yearly")){
                LocalDate dayOneOfYear = LocalDate.of(today.getYear(), 1, 1);
                startDate = dayOneOfYear.format(formatter);
            }else if(requestedFile.contains("monthly")){
                LocalDate dayOneOfMonth = today.withDayOfMonth(1);
                startDate = dayOneOfMonth.format(formatter);
            }else{ // weekly
                startDate = today.format(formatter);
            }
        }
      //  System.out.println("ANDY-PATH: " + requestedFile + " Start Date: " + startDate);
        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        requestedFile = URLDecoder.decode(requestedFile, "UTF-8");

        // support multiple bar charts
        ByteArrayOutputStream bos = null;
        switch(requestedFile){
            case "/doctor/weekly.png":
                bos = generateDoctorWeeklyChart(startDate);
                break;
            case "/doctor/weeklyPatient.png":
                bos = generateDoctorWeeklyPatientChart(startDate);
                break;
            case "/doctor/weeklyServiceFee.png":
                bos = generateDoctorWeeklyServiceFeeChart(startDate);
                break;
            case "/doctor/monthlyPatient.png":
                bos = generateDoctorMonthlyPatientChart(startDate);
                break;
            case "/doctor/monthlyServiceFee.png":
                bos = generateDoctorMonthlyServiceFeeChart(startDate);
                break;
            case "/doctor/yearly.png":
                bos = generateDoctorYearlyChart(startDate);
                break;
            case "/doctor/yearlyPatient.png":
                bos = generateDoctorYearlyPatientChart(startDate);
                break;
            case "/doctor/yearlyServiceFee.png":
                bos = generateDoctorYearlyServiceFeeChart(startDate);
                break;
            default:
                return;
        }

        response.setContentType("image/png");
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(bos.toByteArray());
        out.flush();
        out.close();
    }

    /* doctor report */
    private ByteArrayOutputStream generateDoctorWeeklyChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorWeeklyChartData(startDate);

        Integer width = 480;
        Integer height = 240;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorWeeklyPatientChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorWeeklyPatientChartData(startDate);

        Integer width = 480;
        Integer height = 240;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorWeeklyServiceFeeChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorWeeklyServiceFeeChartData(startDate);

        Integer width = 480;
        Integer height = 240;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorMonthlyPatientChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorMonthlyPatientChartData(startDate);

        Integer width = 750;
        Integer height = 300;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorMonthlyServiceFeeChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorMonthlyServiceFeeChartData(startDate);

        Integer width = 750;
        Integer height = 300;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorYearlyChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorYearlyChartData(startDate);

        Integer width = 750;
        Integer height = 300;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorYearlyPatientChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorYearlyPatientChartData(startDate);

        Integer width = 750;
        Integer height = 300;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    private ByteArrayOutputStream generateDoctorYearlyServiceFeeChart(String startDate) {
        ChartDataProducer dataProducer = new ChartDataProducer(doctorService);
        ChartModel model = dataProducer.fetchDoctorYearlyServiceFeeChartData(startDate);

        Integer width = 750;
        Integer height = 300;
        return Bar3DChartGenerator.generate(width, height, model);
    }

    /* shop report here */
}
