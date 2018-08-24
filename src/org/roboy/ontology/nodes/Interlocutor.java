package org.roboy.ontology.nodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;
import org.roboy.ontology.NodeModel;
import org.roboy.ontology.util.Uuid;
import org.roboy.ontology.util.UuidType;
import org.roboy.ontology.util.UzupisIntents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.roboy.ontology.Neo4jProperty.*;
import static org.roboy.ontology.Neo4jRelationship.*;

/**
 * Encapsulates a NodeModel and enables dialog states to easily store
 * and retrieve information about its current conversation partner.
 */
public class Interlocutor extends NodeModel {
    private final Logger LOGGER = LogManager.getLogger();
    private final static List<Neo4jRelationship> legalRelationships = Arrays.asList(HAS_HOBBY);
    private final static List<Neo4jRelationship> illegalRelationships = Arrays.asList(OTHER);
    private final static List<Neo4jProperty> legalProperties = Arrays.asList(name);
    private final static List<Neo4jProperty> illegalProperties = Arrays.asList(color);

    private HashMap<UzupisIntents, String> uzupisInfo = new HashMap<>();

    public Interlocutor() {
        super();
        setLabel(Neo4jLabel.Person);
        setOntologyConditions();
    }

    public Interlocutor(String name) {
        super();
        addName(name);
        setLabel(Neo4jLabel.Person);
        setOntologyConditions();
    }

    public Interlocutor(Uuid uuid) {
        super();
        addUuid(uuid);
        setLabel(uuid.getType().toNeo4jLabel());
        setOntologyConditions();
    }

    private void setOntologyConditions() {
        this.Neo4jLegalRelationships = legalRelationships;
        this.Neo4jIllegalRelationships = illegalRelationships;
        this.Neo4jLegalProperties = legalProperties;
        this.Neo4jIllegalProperties = illegalProperties;
    }

    public void addUuid(Uuid uuid) {
        setProperties(uuid.getType().toNeo4jProperty(), uuid);
    }

    public Uuid getUuid(UuidType type) {
        return new Uuid(type, (String) getProperties(type.toNeo4jProperty()));
    }

    public void saveUzupisProperty(UzupisIntents intent, String value) {
        uzupisInfo.put(intent, value);
    }

    public HashMap<UzupisIntents, String> getUzupisInfo() {
        return uzupisInfo;
    }




}