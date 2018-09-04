package org.roboy.ontology.constraints;

import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;

import java.util.Arrays;
import java.util.HashSet;

import static org.roboy.ontology.Neo4jLabel.Location;
import static org.roboy.ontology.Neo4jProperty.*;
import static org.roboy.ontology.Neo4jRelationship.*;

public class LocationContraints {
    public static final HashSet<Neo4jLabel> legalLabels = new HashSet<>(Arrays.asList(
            Location));
    public static final HashSet<Neo4jRelationship> legalRelationships = new HashSet<> (Arrays.asList(
            IS_IN));
    public static final HashSet<Neo4jProperty> legalProperties = new HashSet<> (Arrays.asList(
            name,
            size,
            timestamp));
}
