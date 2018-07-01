package ru.job4j.carplace.controller;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.carplace.model.dao.*;
import ru.job4j.carplace.model.entity.Car;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Base64;

/**
 * The user Create servlet.
 */
@WebServlet(
        name = "AddController",
        description = "Add car",
        urlPatterns = {"/add_car", "/update_car"}
)
@MultipartConfig
public final class AddUpdateController extends HttpServlet {
    /**
     * Logger instance.
     */
    private Logger log;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("makesList", MakeDAO.getInstance().findAll());
        request.setAttribute("enginesList", EngineDAO.getInstance().findAll());
        request.setAttribute("bodiesList", BodyDAO.getInstance().findAll());
        request.setAttribute("transmissionsList", TransmissionDAO.getInstance().findAll());
        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            Car car = CarDAO.getInstance().findById(id);
            request.setAttribute("car", car);
            request.setAttribute("modelsList", ModelDAO.getInstance().getModelsByMakeId(car.getMake().getId()));
        } else {
            request.setAttribute("car", null);
            request.setAttribute("modelsList", null);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/add_car.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String carId = request.getParameter("carId");
        boolean sold = (request.getParameter("sold") != null
                && request.getParameter("sold").equals("on"));

        String carName = request.getParameter("name");
        int makeId = Integer.parseInt(request.getParameter("make"));
        int modelId = Integer.parseInt(request.getParameter("model"));
        int engineId = Integer.parseInt(request.getParameter("engine"));
        int bodyId = Integer.parseInt(request.getParameter("body"));
        int transmissionId = Integer.parseInt(request.getParameter("transmission"));
        String login = (String) request.getSession().getAttribute("login");

        Car car = new Car();
        car.setName(carName);
        car.setSold(sold);
        car.setMake(MakeDAO.getInstance().findById(makeId));
        car.setModel(ModelDAO.getInstance().findById(modelId));
        car.setBody(BodyDAO.getInstance().findById(bodyId));
        car.setEngine(EngineDAO.getInstance().findById(engineId));
        car.setTransmission(TransmissionDAO.getInstance().findById(transmissionId));
        car.setOwner(UserDAO.getInstance().findByLogin(login));
        car.setImage(getFileFromRequest(request));
        car.setBase64imageFile(Base64.getEncoder().encodeToString(car.getImage()));
        if (!carId.isEmpty()) {
            car.setId(Integer.parseInt(carId));
            CarDAO.getInstance().update(car);
        } else {
            try {
                int id = CarDAO.getInstance().create(car);
                car.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("cars");
    }

    @Override
    public void init() {
        this.log = LogManager.getLogger(this.getClass());
    }

    @Override
    public void destroy() {
    }

    /**
     * Gets file from request.
     * @param request request
     * @return file data
     */
    private byte[] getFileFromRequest(HttpServletRequest request) {
        byte[] image = null;
        try {
            Part filePart = request.getPart("file");
            InputStream fileContent = filePart.getInputStream();
            image = IOUtils.toByteArray(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return image;
    }
}
