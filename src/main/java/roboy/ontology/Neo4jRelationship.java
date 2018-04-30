package roboy.ontology;

import static roboy.ontology.Neo4jLabel.*;

/**
 * Contains the relations available in Neo4j database.
 * Respective questions should be added to the questions.json file
 * and used in the QuestionRandomizerState.
 */
public enum Neo4jRelationship {
    CHILD_OF("CHILD_OF"),
    FRIEND_OF("FRIEND_OF"),
    FROM("FROM"),
    HAS_HOBBY("HAS_HOBBY"),
    IS("IS"),
    IS_IN("IS_IN"),
    KNOW("KNOW"),
    LIVE_IN("LIVE_IN"),
    MEMBER_OF("MEMBER_OF"),
    OCCUPIED_AS("OCCUPIED_AS"),
    PART_OF("PART_OF"),
    SIBLING_OF("SIBLING_OF"),
    STUDY_AT("STUDY_AT"),
    WORK_FOR("WORK_FOR"),
    UNDEFINED("UNDEFINED");

    public String type;

    Neo4jRelationship(String type) {
        this.type=type;
    }

    public static String determineNodeType(String relationship) {
        // TODO expand list as new Node types are added.
        if(relationship.equals(HAS_HOBBY.type)) return Hobby.type;
        if(relationship.equals(FROM.type)) return Location.type;
        if(relationship.equals(WORK_FOR.type) ||
                relationship.equals(STUDY_AT.type)) return Organization.type;
        if(relationship.equals(OCCUPIED_AS.type)) return Occupation.type;
        if(relationship.equals(UNDEFINED.type)) return "Undefined";
        else return "";
    }
}
