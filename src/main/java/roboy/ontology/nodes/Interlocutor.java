package roboy.ontology.nodes;

import roboy.ontology.Neo4jLabel;
import roboy.ontology.Neo4jMemoryInterface;
import roboy.ontology.Neo4jProperty;
import roboy.ontology.Neo4jRelationship;

import java.util.ArrayList;

/**
 * Encapsulates a MemoryNodeModel and enables dialog states to easily store
 * and retrieve information about its current conversation partner.
 */
public class Interlocutor extends MemoryNodeModel {
    // private HashMap<UzupisIntents,String> uzupisInfo = new HashMap<>();

    private ArrayList<Neo4jRelationship> personLegalRelationships = new ArrayList<>();
    private ArrayList<Neo4jRelationship> personIllegalRelationships = new ArrayList<>();
    private ArrayList<Neo4jProperty> personLegalProperties = new ArrayList<>();
    private ArrayList<Neo4jProperty> personIllegalProperties = new ArrayList<>();

    public Interlocutor(Neo4jMemoryInterface memory) {
        super(memory);
        this.label = Neo4jLabel.Person.type;
        this.labels.add(this.label);
        this.Neo4jLegalRelationships = personLegalRelationships;
        this.Neo4jIllegalRelationships = personIllegalRelationships;
        this.Neo4jLegalProperties = personLegalProperties;
        this.Neo4jIllegalProperties = personIllegalProperties;
    }

//    public void saveUzupisProperty(UzupisIntents intent, String value) {
//        uzupisInfo.put(intent, value);
//    }

//    public HashMap<UzupisIntents, String> getUzupisInfo() {
//        return uzupisInfo;
//    }

}