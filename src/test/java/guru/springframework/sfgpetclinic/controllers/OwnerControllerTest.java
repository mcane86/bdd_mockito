package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    public static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    public static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Captor
    ArgumentCaptor<String> captor;
    @Mock
    BindingResult bindingResult;
    @Mock
    Model model;
    @Mock
    OwnerService ownerService;
    @InjectMocks
    OwnerController controller;

    @BeforeEach
    void setup() {
        // given
        given(ownerService.findAllByLastNameLike(captor.capture())).willAnswer(invocation -> {
            List<Owner> owners = new ArrayList<>();
            String name = invocation.getArgument(0);

            if (name.equals("%Bullet%")) {
                owners.add(new Owner(5L, "Bill", "Bullet"));
                return owners;
            } else if (name.equals("%DontFindMe%")) {
                return owners;
            } else if (name.equals("%FindMe%")) {
                owners.add(new Owner(4L, "Daniel", "DontFindMe"));
                owners.add(new Owner(3L, "Francis", "FindMe"));
                return owners;
            }
            throw new RuntimeException("Invalid Argument");
        });
    }

    @Test
    void processFindFormFromWildcards_IsEmpty() {
        // given
        Owner owner = new Owner(4L, "Daniel", "DontFindMe");
        // when
        String viewName = controller.processFindForm(owner, bindingResult, null);
        // then
        assertEquals("%DontFindMe%", captor.getValue());
        assertEquals("owners/findOwners", viewName);
    }

    @Test
    void processFindFormFromWildcards_SizeOfOne() {
        // given
        Owner owner = new Owner(5L, "Bill", "Bullet");
        // when
        String viewName = controller.processFindForm(owner, bindingResult, null);
        // then
        assertEquals("%Bullet%", captor.getValue());
        assertEquals("redirect:/owners/5", viewName);
    }

    @Test
    void processFindFormFromWildcards_MultipleFound() {
        // given
        Owner owner = new Owner(3L, "Francis", "FindMe");
        InOrder inOrder = inOrder(ownerService, model);
        // when
        String viewName = controller.processFindForm(owner, bindingResult, model);
        // then
        assertEquals("%FindMe%", captor.getValue());
        assertEquals("owners/ownersList", viewName);
        // inOrder verify
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
    }

    /*@Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(5L, "Bill", "Bullet");
        given(bindingResult.hasErrors()).willReturn(true);
        // when
        String viewName = controller.processCreationForm(owner, bindingResult);
        // then
        assertEquals(OWNERS_CREATE_OR_UPDATE_OWNER_FORM, viewName, "The views did not match.");
    }

    @Test
    void processCreationFormNoErrors() {
        // given
        Owner owner = new Owner(5L, "Bill", "Bullet");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any(Owner.class))).willReturn(owner);
        // when
        String viewName = controller.processCreationForm(owner, bindingResult);
        // then
        assertEquals(REDIRECT_OWNERS_5, viewName, "The views did not match.");
    }*/

}