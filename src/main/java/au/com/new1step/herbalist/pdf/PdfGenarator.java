package au.com.new1step.herbalist.pdf;

import au.com.new1step.herbalist.service.DoctorService;
import au.com.new1step.herbalist.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Component
public class PdfGenarator {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ShopService shopService;

    @Autowired
    private DoctorService doctorService;

    public void generatePdf(HttpServletRequest request, HttpServletResponse response, String pdfName, Long preID) throws ServletException, IOException {

        ByteArrayOutputStream bos = null;

        if("prescription".equalsIgnoreCase(pdfName)){
            bos = generatePrescription(preID);
        }else{
            return;
        }

        response.setContentType("application/pdf");
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(bos.toByteArray());
        out.flush();
        out.close();
    }

    // pdf file
    private ByteArrayOutputStream generatePrescription(Long preID) {
        PdfDataProducer dataProducer = new PdfDataProducer();
        dataProducer.setDoctorService(doctorService);
        dataProducer.setShopService(shopService);
        return createInMemoryPdf("/herbalist/pdf/prescription",dataProducer.fetchPrescriptionData(preID));
    }

    //=======Demo pdf  this will remove later ===============
    public void generateDemoPdf(HttpServletRequest request, HttpServletResponse response, String pdfName) throws ServletException, IOException {
        /* omit */
    }
    // pdf file
    private ByteArrayOutputStream generatePrescription() {
        PdfDataProducer dataProducer = new PdfDataProducer();
        return createInMemoryPdf("/herbalist/pdf/prescription",dataProducer.demoFetchPrescriptionData());
    }

    //--- below are private methods
    private ByteArrayOutputStream createInMemoryPdf(String templateName, PdfModel model)  {
        /* omit */
        return null;
    }

    private void createPhysicPdf(String templateName, PdfModel model) throws Exception {
         /* omit */
    }

    // data store in Map
    private void createPhysicPdf(String templateName, Map map) throws Exception {
        /* omit */
    }

}
