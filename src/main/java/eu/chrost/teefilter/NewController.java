package eu.chrost.teefilter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/new")
public class NewController {
    @PostMapping("/echo")
    public String echo(@RequestBody String input) {
        return input;
    }

    @GetMapping("/echo-get")
    public String echoGet(@RequestBody String input) {
        return input;
    }
}
