package roboy.ontology;

/**
 * Contains the relations available in Neo4j database.
 * Respective questions should be added to the questions.json file
 * and used in the QuestionRandomizerState.
 */
public enum Neo4jLabels {
    Person("Person"),
    Robot("Robot");

    public String type;

    Neo4jLabels(String type) {
        this.type=type;
    }
}
