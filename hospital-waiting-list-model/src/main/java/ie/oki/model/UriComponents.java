package ie.oki.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Zoltan Toth
 */
@Data
public class UriComponents implements Serializable {

    private static final long serialVersionUID = -4235144710517215808L;

    /** http/https */
    private String scheme;

    /** domain */
    private String host;

    /** rest of the url */
    private String path;
}
