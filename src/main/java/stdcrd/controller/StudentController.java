package stdcrd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import stdcrd.model.Student;
import stdcrd.repository.UserRepository;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        // Sets the active navbar link highlight state to 'home'
        model.addAttribute("activePage", "home");

        return "index"; // Looks for templates/index.html
    }

    // 1. Display the Form
    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        // This empty student object holds the form data
        Student student = new Student();
        model.addAttribute("student", student);
        model.addAttribute("activePage", "add");
        return "student/create_student";
    }

    // 2. Handle Form Submission
    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student) {

        // Save to your database via repository
        userRepository.save(student);

        // Redirect back to the form with a success flag (or you can redirect to a list page later)
        return "redirect:/students/new?success";
    }

    @GetMapping("/students")
    public String listStudents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        // 1. Create a pageable configuration slice
        Pageable pageable = PageRequest.of(page, size);

        // 2. Fetch the paginated dataset out of your UserRepository
        Page<Student> studentPage = userRepository.findAll(pageable);

        // 3. Pass data properties to your template
        model.addAttribute("students", studentPage.getContent()); // The list of items on current page
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("pageSize", size); // Keeps track of chosen dropdown option
        model.addAttribute("activePage", "list");

        return "student/list_students";
    }

    @GetMapping("/students/{id}")
    public String viewStudentDetails(@PathVariable("id") Long id, Model model) {
        // Find student by ID or throw an error if not found
        Student student = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

        model.addAttribute("student", student);
        return "student/student_details";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        // Delete student from database using UserRepository
        userRepository.deleteById(id);

        return "redirect:/students";
    }

    @PostMapping("/students/{id}/update")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") Student student) {
        // Enforce the correct ID so Hibernate updates the existing row instead of inserting a new one
        student.setId(id);

        // Save automatically updates if the entity ID already exists in the DB
        userRepository.save(student);

        // Redirect back to the student list to see the changes reflected
        return "redirect:/students";
    }
}
