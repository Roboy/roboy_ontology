package roboy.ontology;

/**
 * Contains the relations available in Neo4j database.
 * Respective questions should be added to the questions.json file
 * and used in the QuestionRandomizerState.
 */
public enum Neo4jLabel {
    City("City"),
    Company("Company"),
    Continent("Continent"),
    Country("Country"),
    Division("Division"),
    Hobby("Hobby"),
    Location("Location"),
    Object("Object"),
    Occupation("Occupation"),
    Organization("Organization"),
    Person("Person"),
    Robot("Robot"),
    University("University");

    public String type;

    Neo4jLabel(String type) {
        this.type=type;
    }
}

//enum Neo4jLocation {
//    City("City"),
//    Continent("Continent"),
//    Country("Country"),
//    Planet("Planet"),
//    Street("Street"),
//    Town("Town"),
//    Village("Village");
//
//    public String type;
//
//    Neo4jLocation(String type) {
//        this.type=type;
//    }
//}
