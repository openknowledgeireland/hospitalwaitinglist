package ie.oki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Zoltan Toth
 */
@Controller
public class GeneralController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
