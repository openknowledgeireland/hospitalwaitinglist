package ie.oki.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class holds the custom properties that are necessary for the application.
 *
 * @author Zoltan Toth
 */
@Data
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppProperties {

    private final Csv csv = new Csv();
    private final AutoDownload autoDownload = new AutoDownload();

    @Data
    public static class Csv {

        /** Host. */
        private String host;

        /** The path that's shared between the CSV files. */
        private String baseUrl;

        /** Name of the OP file. */
        private String opFileName;

        /** Name of the IPDC file. */
        private String ipdcFileName;
    }

    @Data
    public static class AutoDownload {

        /** The cron expression for the auto download service. */
        private String cron;
    }
}
