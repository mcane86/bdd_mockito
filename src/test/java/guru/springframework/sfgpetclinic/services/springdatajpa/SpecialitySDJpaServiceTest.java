package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    Speciality speciality;

    @Mock(lenient = true)
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @BeforeEach
    void setup() {
        // given
        speciality = new Speciality();
    }

    @Test
    void testDeleteByObject() {
        // when
        service.delete(speciality);
        // then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    @DisplayName("Find By ID (BDD)")
    void findByIdTest() {
        // given
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));
        // when
        Speciality foundSpecialty = service.findById(1L);
        // then
        assertNotNull(foundSpecialty);
        then(specialtyRepository).should(timeout(100)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {
        // when
        service.deleteById(1L);
        service.deleteById(1L);
        // then
        then(specialtyRepository).should(timeout(100).times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeast() {
        // when
        service.deleteById(1L);
        service.deleteById(1L);
        // then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);
    }

    @Test
    void deleteByIdAtMost() {
        // when
        service.deleteById(1L);
        service.deleteById(1L);
        // then
        then(specialtyRepository).should(atMost(5)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {
        // when
        service.deleteById(1L);
        service.deleteById(1L);
        // then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);
        then(specialtyRepository).should(never()).deleteById(5L);
    }

    @Test
    void testDelete() {
        // when
        service.delete(new Speciality());
        // then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    @DisplayName("Test Throw RuntimeException when Deleting a Specialty")
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));
    }

    @Test
    void testFindByIDThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));
        assertThrows(RuntimeException.class, () -> service.findById(1L));
        then(specialtyRepository).should().findById(anyLong());
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        // given
        final String MATCH_ME = "MATCH_ME";
        Long expected = 1L;
        speciality.setDescription(MATCH_ME);
        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);
        // need mock to only return on match MATCH_ME string.
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);
        // when
        Speciality returnedSpeciality = service.save(speciality);
        // then
        assertEquals(expected, returnedSpeciality.getId());
    }

    @Test
    void testSaveLambdaNotAMatch() {
        // given
        final String MATCH_ME = "MATCH_ME";
        Long expected = 1L;
        speciality.setDescription("Not a match");
        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);
        // need mock to only return on match MATCH_ME string.
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);
        // when
        Speciality returnedSpeciality = service.save(speciality);
        // then
        assertNull(returnedSpeciality);
    }
}