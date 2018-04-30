package roboy.ontology;

/**
 * Contains the relations available in Neo4j database.
 * Respective questions should be added to the questions.json file
 * and used in the QuestionRandomizerState.
 */
public enum Neo4jProperty {
    abilities("abilities"),
    age("age"),
    alias("alias"),
    birthdate("birthdate"),
    color("color"),
    full_name("full_name"),
    name("name"),
    sex("sex"),
    skills("skills"),
    last_name("last_name");

    public String type;

    Neo4jProperty(String type) {
        this.type=type;
    }
}
