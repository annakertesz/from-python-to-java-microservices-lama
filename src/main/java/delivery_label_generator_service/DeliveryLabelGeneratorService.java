package delivery_label_generator_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * <h1>The API of the Label generator</h1>
 *
 * @since   2017-01-03
 */
public class DeliveryLabelGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryLabelGeneratorService.class);
    private DeliveryLabelGeneratorController controller;


    /**
     * The main method of the API
     * @param args String array of the given program arguments.
     * <b>Note:</b> Port must be given as the first argument.
     * <h3>paths</h3>
     *  <strong>/api/status</strong> - return the status of microservice
     *  <strong>/api/create-label</strong> - return the generated pdf file
     */
    public static void main(String[] args) {
        logger.debug("Starting " + DeliveryLabelGeneratorService.class.getName() + "...");

        setup(args);

        DeliveryLabelGeneratorService application = new DeliveryLabelGeneratorService();
        application.controller = new DeliveryLabelGeneratorController();

        get("/api/status", (application.controller::status));
        get("/api/create-label", (application.controller::getLabel));
    }


    /**
     * setup the server's port
     * @param args
     */
    private static void setup(String[] args) {
        if (args == null || args.length == 0) {
            logger.error("Port must be given as the first argument.");
            System.exit(-1);
        }

        try {
            port(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            logger.error("Invalid port given '{}', it should be number.", args[0]);
            System.exit(-1);
        }
    }
}
