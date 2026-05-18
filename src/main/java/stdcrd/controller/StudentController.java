package stdcrd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import stdcrd.model.Student;
import stdcrd.repository.UserRepository;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private UserRepository userRepository;

    // 1. Display the Form
    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        // This empty student object holds the form data
        Student student = new Student();
        model.addAttribute("student", student);
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
    public String listStudents(Model model) {
        // Fetch all students from Supabase using your UserRepository
        List<Student> students = userRepository.findAll();

        // Pass the list to the Thymeleaf HTML view
        model.addAttribute("students", students);

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

        System.out.println("========================================");
        System.out.println("UPDATING STUDENT ID " + id + ": " + student.getName());
        System.out.println("========================================");

        // Save automatically updates if the entity ID already exists in the DB
        userRepository.save(student);

        // Redirect back to the student list to see the changes reflected
        return "redirect:/students";
    }
}
