package eu.chrost.teefilter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/old")
public class OldController {
    @PostMapping("/echo")
    public String echo(@RequestBody String input) {
        return input;
    }
}
