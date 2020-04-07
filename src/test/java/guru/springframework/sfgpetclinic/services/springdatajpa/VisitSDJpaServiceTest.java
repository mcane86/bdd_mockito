package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    private Visit visit;
    private Set<Visit> visits;

    @Mock
    VisitRepository visitRepository;
    @InjectMocks
    VisitSDJpaService service;

    @BeforeEach
    void setup() {
        visit = new Visit();
        visits = new HashSet<>();
        visits.add(visit);
    }

    @Test
    @DisplayName("Find All Visits")
    void findAll() {
        // given
        given(visitRepository.findAll()).willReturn(visits);
        // when
        Set<Visit> foundVisits = service.findAll();
        // then
        then(visitRepository).should().findAll();
        assertThat(foundVisits).hasSize(1);

    }

    @Test
    @DisplayName("Find Visit By ID")
    void findById() {
        //given
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));
        // when
        Visit foundVisit = service.findById(1L);
        // then
        then(visitRepository).should().findById(anyLong());
        assertThat(foundVisit).isNotNull();
    }

    @Test
    @DisplayName("Save Visit to Repository")
    void save() {
        // given
        given(visitRepository.save(any(Visit.class))).willReturn(visit);
        // when
        Visit savedVisit = service.save(new Visit());
        // then
        then(visitRepository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    @DisplayName("Delete Visit from Repository")
    void delete() {
        // when
        service.delete(visit);
        // then
        then(visitRepository).should().delete(any(Visit.class));
    }

    @Test
    @DisplayName("Delete from Repository, By ID")
    void deleteById() {
        // when
        service.deleteById(1L);
        // then
        then(visitRepository).should().deleteById(anyLong());
    }
}