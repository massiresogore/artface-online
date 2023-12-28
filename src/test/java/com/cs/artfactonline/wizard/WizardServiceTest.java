package com.cs.artfactonline.wizard;

import com.cs.artfactonline.artifact.Artifact;
import com.cs.artfactonline.artifact.utils.IdWorker;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    @Mock
    IdWorker idWorker;
    List<Wizard> wizardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setName("Albus Dumbledore");
        this.wizardList.add(w1);

        Wizard w2 = new Wizard();
        w2.setName("Harry Potter");
        this.wizardList.add(w2);

        Wizard w3 = new Wizard();
        w3.setName("Harry Potter");
        this.wizardList.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(this.wizardRepository.findAll()).willReturn(this.wizardList);

        //When
        List<Wizard> actualWizards = wizardService.findAll();

        //Then
        assertThat(actualWizards.size()).isEqualTo(this.wizardList.size());
        verify(wizardRepository,times(1)).findAll();
    }

    @Test
    void testSaveWizardSuccess()
    {
        //Given
        Wizard wizard = new Wizard();
        wizard.setId(123456);
        wizard.setName("Albus Dumbledore");

        given(wizardRepository.save(wizard)).willReturn(wizard);

        //When
        Wizard savedWizard = wizardService.save(wizard);

        //then
        assertThat(savedWizard.getId()).isEqualTo(wizard.getId());
        assertThat(savedWizard.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository,times(1)).save(wizard);
    }

    @Test
    void findByIdSuccess()
    {
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904196");
        artifact.setName("Resurrection Stone");
        artifact.setImageUrl("ImageUrl6");
        artifact.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");

        Wizard wizard = new Wizard();
        wizard.setId(4);
        wizard.setName("Massire");
        wizard.addArtifact(artifact);

        given(wizardRepository.findById(4)).willReturn(Optional.of(wizard));

        //When
        Wizard returnWizard = wizardService.findById(4);

        //Then
        assertThat(returnWizard.getId()).isEqualTo(wizard.getId());
        assertThat(returnWizard.getName()).isEqualTo(wizard.getName());

        verify(wizardRepository, times(1)).findById(4);


    }

    @Test
    void testFindByIdNotFound()
    {
        //Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Wizard returnWizard = wizardService.findById(4);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not Find name with Id:4:(");
        verify(wizardRepository,Mockito.times(1)).findById(4);
    }

    @Test
    void testUpdateSuccess()
    {
        //Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Massire");
        oldWizard.addArtifact(oldArtifact);

        Wizard updateWizard = new Wizard();
        updateWizard.setId(1);
        updateWizard.setName("new Massire name");
        given(wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //When
        Wizard updatedWizard = wizardService.update(1,updateWizard);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(updateWizard.getId());
        assertThat(updatedWizard.getName()).isEqualTo(updateWizard.getName());
        verify(wizardRepository,times(1)).findById(1);
        verify(wizardRepository,times(1)).save(oldWizard);

    }

    @Test
    void  testDeleteWizardSuccess()
    {
        //Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Massire");

        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1);

        //When
        wizardService.delete(1);

        //Then
        verify(wizardRepository,times(1)).deleteById(1);
    }

    @Test
    void testDeleteWizardNotFound()
    {
        //Given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());


        //When
        assertThrows(ObjectNotFoundException.class,()->{
            wizardService.delete(1);
        });
        //Then
        verify(wizardRepository,times(1)).findById(1);
    }

}