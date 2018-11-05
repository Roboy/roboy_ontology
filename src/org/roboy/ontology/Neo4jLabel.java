package org.roboy.ontology;

import com.google.common.collect.Maps;
import org.roboy.ontology.constraints.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

/**
 * Contains the relations available in Neo4j database.
 * Respective questions should be added to the questions.json file
 * and used in the QuestionRandomizerState.
 */
public enum Neo4jLabel {
    Person("Person"),
    TelegramPerson("TelegramPerson"),
    FacebookPerson("FacebookPerson"),
    SlackPerson("SlackPerson"),
    WhatsAppPerson("WhatsAppPerson"),
    LinePerson("LinePerson"),
    Robot("Robot"),
    Company("Company"),
    University("University"),
    City("City"),
    Country("Country"),
    Hobby("Hobby"),
    Occupation("Occupation"),
    Object("Object"),
    Location("Location"),
    Organization("Organization"),
    Other("Other"),
    None("");

    public String type;

    Neo4jLabel(String type) {
        this.type = type;
    }

    private static final Map<String, Neo4jLabel> typeIndex =
            Maps.newHashMapWithExpectedSize(Neo4jLabel.values().length);

    static {
        for (Neo4jLabel label : Neo4jLabel.values()) {
            typeIndex.put(label.type, label);
        }
    }

    public static Neo4jLabel lookupByType(String type) {
        return typeIndex.get(type);
    }

    public static boolean contains(String type){
        return typeIndex.containsKey(type);
    }

    public HashSet<Neo4jRelationship> getLegalRelationships() {
        switch (this) {
            case Person:
                return InterlocutorConstraints.legalRelationships;
            case TelegramPerson:
                return SocialPersonConstraints.legalRelationships;
            case FacebookPerson:
                return SocialPersonConstraints.legalRelationships;
            case SlackPerson:
                return SocialPersonConstraints.legalRelationships;
            case WhatsAppPerson:
                return SocialPersonConstraints.legalRelationships;
            case LinePerson:
                return SocialPersonConstraints.legalRelationships;
            case Robot:
                return RoboyConstraints.legalRelationships;
            case Company:
                return OrganisationConstraints.legalRelationships;
            case University:
                return OrganisationConstraints.legalRelationships;
            case City:
                return LocationContraints.legalRelationships;
            case Country:
                return LocationContraints.legalRelationships;
            case Hobby:
                return null;
            case Occupation:
                return null;
            case Object:
                return null;
            case Location:
                return LocationContraints.legalRelationships;
            case Organization:
                return OrganisationConstraints.legalRelationships;
            case Other:
                return new HashSet<>(Arrays.asList(Neo4jRelationship.values()));
            case None:
                return new HashSet<>(Arrays.asList(Neo4jRelationship.values()));
        }
        throw new AssertionError("Unknown error on enum entry: " + this);
    }

    public HashSet<Neo4jProperty> getLegalProperties() {
        switch (this) {
            case Person:
                return InterlocutorConstraints.legalProperties;
            case TelegramPerson:
                return SocialPersonConstraints.legalProperties;
            case FacebookPerson:
                return SocialPersonConstraints.legalProperties;
            case SlackPerson:
                return SocialPersonConstraints.legalProperties;
            case WhatsAppPerson:
                return SocialPersonConstraints.legalProperties;
            case LinePerson:
                return SocialPersonConstraints.legalProperties;
            case Robot:
                return RoboyConstraints.legalProperties;
            case Company:
                return OrganisationConstraints.legalProperties;
            case University:
                return OrganisationConstraints.legalProperties;
            case City:
                return LocationContraints.legalProperties;
            case Country:
                return LocationContraints.legalProperties;
            case Hobby:
                return null;
            case Occupation:
                return null;
            case Object:
                return null;
            case Location:
                return LocationContraints.legalProperties;
            case Organization:
                return OrganisationConstraints.legalProperties;
            case Other:
                return new HashSet<>(Arrays.asList(Neo4jProperty.values()));
            case None:
                return new HashSet<>(Arrays.asList(Neo4jProperty.values()));
        }
        throw new AssertionError("Unknown error on enum entry: " + this);
    }
}
