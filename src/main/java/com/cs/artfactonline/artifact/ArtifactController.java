package com.cs.artfactonline.artifact;

import com.cs.artfactonline.artifact.converter.ArtifactDtoToArtifactConverter;
import com.cs.artfactonline.artifact.converter.ArtifactToArtifactDtoConverter;
import com.cs.artfactonline.artifact.dto.ArtifactDto;
import com.cs.artfactonline.system.Result;
import com.cs.artfactonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("api/v1/artifacts")
public class ArtifactController {
    private final ArtifactService artifactService;
    public final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    public final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;


    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable("artifactId") String artifactId)
    {
        Artifact foundArtifact = this.artifactService.findById(artifactId);

        return new Result(true, StatusCode.SUCCESS,"Find One Success",artifactToArtifactDtoConverter.convert(foundArtifact));
        //spring mvc s'occupe de transformer Result Object en json
    }
    @GetMapping
    public Result findAllArtifacts()
    {
        List<Artifact> foundArtifacts = this.artifactService.findAll();

        //Convert foundArtifacts to a list of foundArtifactDto
       List<ArtifactDto> artifactDtoList =  foundArtifacts.stream().map(artifact -> artifactToArtifactDtoConverter.convert(artifact)).toList();
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Find All Success",
                artifactDtoList
        );
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto)
    {
            return new Result(
                    true,
                    StatusCode.SUCCESS,
                    "Add Success",
                    this.artifactToArtifactDtoConverter.convert(artifactService.save(Objects.requireNonNull(artifactDtoToArtifactConverter.convert(artifactDto))))) ;
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId,@Valid @RequestBody ArtifactDto artifactDto)
    {
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId,update);
        ArtifactDto artifactDto1 = this.artifactToArtifactDtoConverter.convert(updatedArtifact);

        return new Result(true,StatusCode.SUCCESS,"Update Success",artifactDto1);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable("artifactId") String artifactId)
    {
        this.artifactService.delete(artifactId);
        return new Result(true,StatusCode.SUCCESS,"Delete Success");
    }



}
