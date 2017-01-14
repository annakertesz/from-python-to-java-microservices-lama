package delivery_label_generator_service;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @since   2017-01-03
 */
public class LabelFormatter {
    String beginOfCode;
    String beforeImg;
    String afterImg;
    String afterName;
    String afterCity;
    String afterAddress;
    String betweenRows;
    String endOfCode;


    /**
     * Constructor of the class - setup the necessary HTML tags
     */
    public LabelFormatter() {
        beginOfCode=" <html lang=\"en\" style=\"size: 21cm 29.7cm; margin: 0;\"><head></head><body>" +
                "<table style=\"size: 21cm 29.7cm; margin: 0;\"><tr>";
        beforeImg = "<td style=\"width: 70mm; border: solid black 1px\">" +
                "<div style=\"height: 44mm; width: 64mm; margin: 3mm;\">" +
                "<div style=\"float: right\">" +
                "<img src=\"";
        afterImg = "\" height=\"60\" width=\"60\"></img></div><div><p style=\"font-size: 7mm;" +
                " font-style: italic; margin-top: 10mm; text-align: left\"><strong>";
        afterName = "</strong></p><div style=\"margin: 15mm; \">" +
                "<p style=\"font-size: 6mm; font-style: italic;  margin:0; text-align: left\">" +
                "<strong>";
        afterCity = "</strong></p><p style=\"font-size: 5mm; font-style: italic;" +
                "margin:0; text-align: left\">";
        afterAddress = "</p></div></div></div></td>";
        betweenRows= "</tr><tr><td colspan=\"3\">&nbsp;</td></tr><tr>";
        endOfCode= " </tr></table></body></html>";
    }


    /**
     * Generate a formatted String with HTML tags for later PDF converting
     * @param orderList Get an ArrayList of deliveries' JSONObjects
     * @return the formatted String with the deliveries' details
     */
    public String createCode(ArrayList<JSONObject> orderList){
        String htmlCode = "";
        htmlCode += beginOfCode;
        for (int i=0; i<orderList.size(); i++) {
            try {
                htmlCode += beforeImg + new QrCodeGenerator(orderList.get(i).getString("id")).getUrlOfQr();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            htmlCode += afterImg + orderList.get(i).getString("name");
            htmlCode += afterName + orderList.get(i).getString("city");
            htmlCode += afterCity + orderList.get(i).getString("address");
            htmlCode += afterAddress;
            if ((i + 1) % 3 == 0) htmlCode += betweenRows;
        }
        htmlCode += endOfCode;
        return htmlCode;
    }
}
