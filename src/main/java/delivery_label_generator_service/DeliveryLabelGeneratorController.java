package delivery_label_generator_service;

import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 *
 * @since   2017-01-03
 */
public class DeliveryLabelGeneratorController {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryLabelGeneratorController.class);


    /**
     * Creating a list with the deliveries' JSONObjects
     * @param jsonString A json formatted string with the necessary delivery parameters (check the README.md for more info)
     * @return An array with the deliveries' JSONObjects
     */
    /* private -> testing */ ArrayList<JSONObject> createListOfJSONObjects(String jsonString) {
        ArrayList<JSONObject> deliveryLabels = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (Object object: jsonArray) {
            deliveryLabels.add(new JSONObject(object.toString()));
        }
        return deliveryLabels;
    }


    /**
     * Convert a formatted String into a PDF
     * @param orders Get an ArrayList of JSONObjects
     * @return a byteArray of the PDF
     */
    /* private -> testing */ byte[] createPDF(ArrayList<JSONObject> orders){
        LabelFormatter formatter = new LabelFormatter();
        String htmlcode = formatter.createCode(orders);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlcode));
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }


    /**
     *
     * @param request spark.Request class' object for server request
     * @param response spark.Response class' object for server response
     * @return 200 if the the outputStream didn't run on IOException
     * @throws IOException when the outputStream towards the Spark api fail
     */
    public int getLabel(Request request, Response response) throws IOException {

        ArrayList<JSONObject> orders = createListOfJSONObjects(request.queryParams("orders"));
        response.header("Content-Type", "application/pdf");
        ServletOutputStream outputStream = response.raw().getOutputStream();
        outputStream.write(createPDF(orders));
        outputStream.flush();
        outputStream.close();

        return 200;
    }


    /**
     * Check the microservice's status
     * @param request spark.Request class' object for server request
     * @param response spark.Response class' object for server response
     * @return OK when the server are running
     */
    public String status(Request request, Response response) {
        return "OK";
    }
}
