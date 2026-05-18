package stdcrd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stdcrd.model.Student;

@Repository
public interface UserRepository extends JpaRepository<Student, Long> {
}