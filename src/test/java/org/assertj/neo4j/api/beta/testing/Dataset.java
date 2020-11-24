package org.assertj.neo4j.api.beta.testing;

public enum Dataset {

    GITHUB_LANGUAGE("samples/language.cypher"),
    EUROPEAN_CITIES("samples/paris-subway.cypher"),
    ;

    private final String resource;

    Dataset(final String resource) {
        this.resource = resource;
    }

    public String resource() {
        return this.resource;
    }
}
