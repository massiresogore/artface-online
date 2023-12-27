package com.cs.artfactonline.artifact;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(String id)
    {
        super("Could not Find artifact with Id:"+id+ ":(");
    }
}
