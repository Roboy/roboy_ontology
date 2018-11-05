package org.roboy.ontology.constraints;

import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;

import java.util.Arrays;
import java.util.HashSet;

import static org.roboy.ontology.Neo4jLabel.*;
import static org.roboy.ontology.Neo4jRelationship.*;
import static org.roboy.ontology.Neo4jProperty.*;

public class InterlocutorConstraints {
    public static final HashSet<Neo4jLabel> legalLabels = new HashSet<>(Arrays.asList(
            Person));
    public static final HashSet<Neo4jRelationship> legalRelationships = new HashSet<> (Arrays.asList(
            EQUALS,
            FROM,
            HAS_HOBBY,
            LIVE_IN,
            STUDY_AT,
            OCCUPIED_AS,
            WORK_FOR,
            FRIEND_OF,
            MEMBER_OF,
            CHILD_OF,
            SIBLING_OF));
    public static final HashSet<Neo4jProperty> legalProperties = new HashSet<> (Arrays.asList(
            name,
            sex,
            full_name,
            birthdate,
            timestamp));
}
