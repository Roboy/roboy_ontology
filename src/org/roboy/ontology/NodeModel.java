package org.roboy.ontology;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * This class represents a full node similarly to its representation in Ontology.
 */
public abstract class NodeModel {
    private final Logger LOGGER = LogManager.getLogger();
    //Unique node IDs assigned by the memory.
    private int id = 0;
    //"Person" etc.
    private HashSet<Neo4jLabel> labels;
    //"Person" etc. Duplicate because Memory expects a single Label in CREATE queries, but
    // returns an array of labels inside GET responses.
    private Neo4jLabel label;
    //name, birthdate
    private HashMap<Neo4jProperty, Object> properties;
    //Relation: <name as String, ArrayList of IDs (constraints related to this node over this relation)>
    private HashMap<Neo4jRelationship, ArrayList<Integer>> relationships;

    protected HashSet<Neo4jLabel> Neo4jLegalLabels;
    protected HashSet<Neo4jRelationship> Neo4jLegalRelationships;
    protected HashSet<Neo4jProperty> Neo4jLegalProperties;

    public NodeModel() { }

    public NodeModel(NodeModel node) {
        set(node);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Neo4jLabel getLabel() {
        return label;
    }

    public HashSet<Neo4jLabel> getLabels() {
        return labels;
    }

    public void setLabel(Neo4jLabel label) {
        if(this.labels == null) {
            this.labels = new HashSet<>();
        }
        labels.add(label);
        this.label = label;
    }

    public void setLabels(Neo4jLabel label) {
        if(this.labels == null) {
            this.labels = new HashSet<>();
        }
        labels.add(label);
    }

    public void setLabels(HashSet<Neo4jLabel> labels) {
        if(this.labels == null) {
            this.labels = new HashSet<>();
        }
        this.labels.addAll(labels);
    }

    public HashMap<Neo4jProperty, Object> getProperties() {
        return properties;
    }

    public Object getProperties(Neo4jProperty key) {
        return (properties != null ? properties.get(key) : null);
    }

    public void setProperties(HashMap<Neo4jProperty, Object> properties) {
        if(this.properties == null) {
            this.properties = new HashMap<>();
        }
        for (Neo4jProperty key : properties.keySet()) {
            setProperties(key, properties.get(key));
        }
    }

    public void setProperties(Neo4jProperty key, Object property) {
        if(this.properties == null) {
            this.properties = new HashMap<>();
        }
        properties.put(key, property);
    }

    public HashMap<Neo4jRelationship, ArrayList<Integer>> getRelationships() {
        return relationships;
    }

    public ArrayList<Integer> getRelationships(Neo4jRelationship key) {
        return (relationships != null ? relationships.get(key) : null);
    }

    public void setRelationships(HashMap<Neo4jRelationship, ArrayList<Integer>> relationships) {
        if(this.relationships == null) {
            this.relationships = new HashMap<>();
        }
        for (Neo4jRelationship key : relationships.keySet()) {
            setRelationships(key, relationships.get(key));
        }
    }

    public void setRelationships(Neo4jRelationship key, ArrayList<Integer> ids) {
        if(this.relationships == null) {
            this.relationships = new HashMap<>();
        }
        if(relationships.containsKey(key)) {
            relationships.get(key).addAll(ids);
        } else {
            relationships.put(key, ids);
        }
    }

    public void setRelationships(Neo4jRelationship key, Integer id) {
        if(this.relationships == null) {
            this.relationships = new HashMap<>();
        }
        if(relationships.containsKey(key)) {
            relationships.get(key).add(id);
        } else {
            ArrayList<Integer> idList = new ArrayList<>();
            idList.add(id);
            relationships.put(key, idList);
        }
    }

    public boolean hasRelationship(Neo4jRelationship relationship) {
        return !(getRelationships(relationship) == null) && (!getRelationships(relationship).isEmpty());
    }

    public void set(NodeModel node) {
        setId(node.getId());
        setRelationships(node.getRelationships() != null ? node.getRelationships() : new HashMap<>());
        setProperties(node.getProperties() != null ? node.getProperties() : new HashMap<>());
    }

    public String getName() {
        return (String) getProperties(Neo4jProperty.name);
    }

    public void addName(String name) {
        setProperties(Neo4jProperty.name, name);
    }

    public enum RelationshipAvailability {
        ALL_AVAILABLE, SOME_AVAILABLE, NONE_AVAILABLE
    }

    /**
     * Checks if predicates from the input array are available for this interlocutor.
     * @param relationships array of predicates to check
     * @return one of three: all, some or none available
     */
    public RelationshipAvailability checkRelationshipAvailability(Neo4jRelationship[] relationships) {
        boolean atLeastOneAvailable = false;
        boolean allAvailable = true;

        for (Neo4jRelationship predicate : relationships) {
            if (this.hasRelationship(predicate)) {
                atLeastOneAvailable = true;
            } else {
                allAvailable = false;
            }
        }
        if (allAvailable) return RelationshipAvailability.ALL_AVAILABLE;
        if (atLeastOneAvailable) return RelationshipAvailability.SOME_AVAILABLE;
        return RelationshipAvailability.NONE_AVAILABLE;
    }

    public HashMap<Boolean, ArrayList<Neo4jRelationship>> getPurityRelationships(Neo4jRelationship[] predicates) {
        HashMap<Boolean, ArrayList<Neo4jRelationship>> pureImpureValues = new HashMap<>();
        pureImpureValues.put(false, new ArrayList<>());
        pureImpureValues.put(true, new ArrayList<>());

        for (Neo4jRelationship predicate : predicates) {
            pureImpureValues.get(this.hasRelationship(predicate)).add(predicate);
        }

        return pureImpureValues;
    }

    public abstract boolean isLegal();

    public void resetId() {
        id = 0;
    }

    public void resetLabel() {
        label = null;
    }

    public void resetLabels(){
        labels = new HashSet<>();
    }

    public void resetProperties() {
        properties = new HashMap<>();
    }

    public void resetRelationships() {
        relationships = new HashMap<>();
    }

    public void resetNode() {
        resetId();
        resetLabel();
        resetLabels();
        resetProperties();
        resetRelationships();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NodeModel)) {
            return false;
        }

        NodeModel nodeModel = (NodeModel) obj;
        return getId() == nodeModel.getId() &&
                Objects.equals(getLabels(), nodeModel.getLabels()) &&
                getLabel() == nodeModel.getLabel() &&
                Objects.equals(getProperties(), nodeModel.getProperties()) &&
                Objects.equals(getRelationships(), nodeModel.getRelationships());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getLabels(), getLabel(), getProperties(), getRelationships());
    }

    @Override
    public String toString() {
        return "NodeModel{" +
                ", id=" + id +
                ", labels=" + labels +
                ", label=" + label +
                ", properties=" + properties +
                ", relationships=" + relationships +
                '}';
    }
}
