package org.roboy.ontology.nodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;
import org.roboy.ontology.NodeModel;

import java.util.Arrays;
import java.util.List;

import static org.roboy.ontology.Neo4jRelationship.*;
import static org.roboy.ontology.Neo4jProperty.*;

/**
 * Encapsulates a NodeModel and enables dialog states to easily store
 * and retrieve information about Roboy.
 */
public class Roboy extends NodeModel {
    private final Logger LOGGER = LogManager.getLogger();
    private final static List<Neo4jRelationship> legalRelationships = Arrays.asList(HAS_HOBBY);
    private final static List<Neo4jRelationship> illegalRelationships = Arrays.asList(OTHER);
    private final static List<Neo4jProperty> legalProperties = Arrays.asList(name);
    private final static List<Neo4jProperty> illegalProperties = Arrays.asList(color);

    /**
     * Initializer for the Roboy node
     */
    public Roboy() {
        super();
        setLabel(Neo4jLabel.Robot);
        setOntologyConditions();
    }

    /**
     * Initializer for the Roboy node
     */
    public Roboy(String name) {
        super();
        addName(name);
        setLabel(Neo4jLabel.Robot);
        setOntologyConditions();
    }

    private void setOntologyConditions() {
        this.Neo4jLegalRelationships = legalRelationships;
        this.Neo4jIllegalRelationships = illegalRelationships;
        this.Neo4jLegalProperties = legalProperties;
        this.Neo4jIllegalProperties = illegalProperties;
    }
}
