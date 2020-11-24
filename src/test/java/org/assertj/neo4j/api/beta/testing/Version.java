package org.assertj.neo4j.api.beta.testing;

public enum Version {

    NEO4J_4_11_1("neo4j:4.1.1"),
    ;

    private final String dockerImage;

    Version(final String dockerImage) {
        this.dockerImage = dockerImage;
    }

    public String dockerImage() {
        return this.dockerImage;
    }
}
