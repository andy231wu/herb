package au.com.new1step.herbalist.servlet;

import au.com.new1step.herbalist.jpa.model.Herb;
import au.com.new1step.herbalist.jpa.model.HerbClass;
import au.com.new1step.herbalist.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/doctor/searchHerbsByHerbClass")
public class HerbNameServlet extends HttpServlet {

    @Autowired
    private ShopService shopService;

    @Override
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        String herbClassName = req.getParameter("param1");
        HerbClass herbClass = shopService.findHerbClassByName(herbClassName);
        List<Herb> herbs = shopService.findAllHerbsByHerbClass(herbClass );
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < herbs.size(); i++){
            builder.append(herbs.get(i).getChineseName());
            if (i < (herbs.size() - 1)) {
                builder.append("|");
            }
        }

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(builder.toString());

    }//doGet

    public void doPost(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        doGet(req,resp);
    }
 
}
