package stdcrd;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String helloWorld(Model model) {
        // This passes data to our HTML page
        model.addAttribute("message", "Hello World!");

        // This tells Spring Boot to look for an HTML file named "index.html"
        return "index";
    }
}

// 2A3mUzU6cithYjXE