package com.yb.service;

import com.yb.domain.jpa.Student;
import com.yb.dto.request.StudentRequestDto;
import com.yb.dto.response.StudentResponseDto;
import com.yb.repository.jpa.StudentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

  @InjectMocks
  StudentService service;

  @Mock
  StudentRepository studentRepository;

//  @BeforeEach
//  void init() {
//    service = new StudentService(studentRepository);
//  }

  @Test
  void shouldGetStudents() {
    Mockito.when(studentRepository.findById(0L)).thenReturn(Optional.of(new Student("Jogn", 25)));
    StudentResponseDto student = service.findStudentById(0L);
    Assertions.assertNotEquals(null, student);
  }

  @Test
  void shouldCreateStudent() {
    StudentRequestDto studentRequestDto = new StudentRequestDto(1L, "John", 25);
    Mockito.when(studentRepository.save(ArgumentMatchers.any(Student.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    StudentResponseDto studentResponseDto = service.createStudent(studentRequestDto);
    Assertions.assertEquals("John", studentResponseDto.getName());
  }

  @Test
  void shouldRemoveStudent() {
    Student student = new Student("John", 15);
    Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    Mockito.doNothing().when(studentRepository).delete(student);
    service.removeStudent(1L);
    Mockito.verify(studentRepository).delete(student);
  }
}
