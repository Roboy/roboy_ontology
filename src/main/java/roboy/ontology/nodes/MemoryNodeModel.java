package roboy.ontology.nodes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import roboy.ontology.Neo4jMemoryInterface;
import roboy.ontology.Neo4jProperty;
import roboy.ontology.Neo4jRelationship;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a full node similarly to its representation in Memory.
 */
public class MemoryNodeModel {
    final static Logger LOGGER = LogManager.getLogger();
    protected Neo4jMemoryInterface memory;
    //Unique node IDs assigned by the memory.
    private int id;
    //"Person" etc.
    @Expose
    protected ArrayList<String> labels;
    //"Person" etc. Duplicate because Memory expects a single Label in CREATE queries, but
    // returns an array of labels inside GET responses.
    @Expose
    protected String label;
    //name, birthdate
    @Expose
    protected HashMap<String, Object> properties;
    //Relation: <name as String, ArrayList of IDs (nodes related to this node over this relation)>
    @Expose
    protected HashMap<String, ArrayList<Integer>> relationships;
    //If true, then fields with default values will be removed from JSON format.
    // Transient as stripping information is not a part of the node and not included in query.
    @Expose
    transient boolean stripQuery = false;

    boolean FAMILIAR = false;

    public ArrayList<Neo4jRelationship> Neo4jLegalRelationships;
    public ArrayList<Neo4jRelationship> Neo4jIllegalRelationships;
    public ArrayList<Neo4jProperty> Neo4jLegalProperties;
    public ArrayList<Neo4jProperty> Neo4jIllegalProperties;

    public MemoryNodeModel(Neo4jMemoryInterface memory){
        this.memory = memory;
        // TODO why id is assigned here?
        this.id = 0;
    }

    public MemoryNodeModel(boolean stripQuery, Neo4jMemoryInterface memory) {
        this.memory = memory;
        if(!stripQuery) {
            id = 0;
            labels = new ArrayList<>();
            properties = new HashMap<>();
            relationships = new HashMap<>();
        } else {
            id = 0;
            this.stripQuery = true;
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }
    public void setLabel(String label) {
        if(this.labels == null) {
            this.labels = new ArrayList<>();
        }
        labels.add(label);
        this.label = label;
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }
    public Object getProperty(String key) {
        return (properties != null ? properties.get(key) : null);
    }

    public void add(Neo4jProperty propertyName, String propertyValue) {
        if (Neo4jLegalProperties.contains(propertyName)) {
            LOGGER.info("The property " + propertyName.type + " is legal, proceed!");
            setProperty(propertyName.type, propertyValue);
        } else if (Neo4jIllegalProperties.contains(propertyName)) {
            LOGGER.error("The property " + propertyName.type + " is illegal, abort!");
        } else {
            LOGGER.warn("The property " + propertyName.type + " is undefined, abort!");
        }
    }

    public void add(HashMap<Neo4jProperty, String> properties) {
        for (Neo4jProperty propertyName : properties.keySet()) {
            add(propertyName, properties.get(propertyName));
        }
    }

    public void setProperties(HashMap<String, Object> properties) {
        if(this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.putAll(properties);
    }

    public void setProperty(String key, Object property) {
        if(this.properties == null) {
            this.properties = new HashMap<>();
        }
        properties.put(key, property);
    }

    public HashMap<String, ArrayList<Integer>> getRelationships() {
        return relationships;
    }
    public ArrayList<Integer> getRelationship(String key) {
        //TODO: Sort this shit out
        //return (relationships != null ? relationships.get(key.toLowerCase()) : null);
        return (relationships != null ? relationships.get(key) : null);
    }
    public void setRelationships(HashMap<String, ArrayList<Integer>> relationships) {
        if(this.relationships == null) {
            this.relationships = new HashMap<>();
        }
        this.relationships.putAll(relationships);
    }
    public void setRelationship(String key, Integer id) {
        if(this.relationships == null) {
            this.relationships = new HashMap<>();
        }
        if(relationships.containsKey(key)) {
            relationships.get(key).add(id);
        } else {
            ArrayList idList = new ArrayList();
            idList.add(id);
            relationships.put(key, idList);
        }
    }

    public void setStripQuery(boolean strip) {
        this.stripQuery = strip;
    }

    /**
     * This toString method returns the whole object, including empty variables.
     */
    public String toJSON(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json;
        // ID of 0 is default ID for an uncertain node.
        // If there exists a valid non-default ID value obtained from memory, it has to be included
        if (getId() != 0) {
            JsonElement jsonElement = gson.toJsonTree(this);
            jsonElement.getAsJsonObject().addProperty("id", getId());
            json = gson.toJson(jsonElement);
        } else {
            json = gson.toJson(this);
        }
        if(stripQuery) {
            //This is based on https://stackoverflow.com/questions/23920740/remove-empty-collections-from-a-json-with-gson
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String,Object> obj = gson.fromJson(json, type);
            for(Iterator<Map.Entry<String, Object>> it = obj.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Object> entry = it.next();
                if (entry.getValue() == null) {
                    it.remove();
                } else if (entry.getValue().getClass().equals(ArrayList.class)) {
                    if (((ArrayList<?>) entry.getValue()).size() == 0) {
                        it.remove();
                    }
                    //As ID is parsed into Double inside GSON, usng Double.class
                } else if (entry.getValue().getClass().equals(Double.class)) {
                    if (((Double) entry.getValue()) == 0) {
                        it.remove();
                    }
                } else if (entry.getValue().getClass().equals(HashMap.class)) {
                    if (((HashMap<?,?>) entry.getValue()).size() == 0) {
                        it.remove();
                    }
                } else if (entry.getValue().getClass().equals(String.class)) {
                    if (((String) entry.getValue()).equals("")) {
                        it.remove();
                    }
                }
            }
            json = gson.toJson(obj);
        }
        return json;
    }

    /**
     * After executing this method, the person field contains a node that
     * is in sync with memory and represents the interlocutor.
     *
     * Unless something goes wrong during querying, which would affect the
     * following communication severely.
     */
    public void addName(String name, String label) {
        setProperty("name", name);
        setLabel(label);

        ArrayList<Integer> ids = new ArrayList<>();
        // Query memory for matching persons.
        try {
            ids = memory.getByQuery(this);
        } catch (InterruptedException | IOException e) {
            LOGGER.info("Exception while querying memory, assuming person unknown.");
            e.printStackTrace();
        }
        // Pick first if matches found.
        if (ids != null && !ids.isEmpty()) {
            //TODO Change from using first id to specifying if multiple matches are found.
            try {
                MemoryNodeModel node = fromJSON(memory.getById(ids.get(0)), new Gson());
                setId(node.getId());
                setRelationships(node.getRelationships() != null ? node.getRelationships() : new HashMap<>());
                setProperties(node.getProperties() != null ? node.getProperties() : new HashMap<>());
                FAMILIAR = true;
            } catch (InterruptedException | IOException e) {
                LOGGER.warn("Unexpected memory error: provided ID not found upon querying.");
                e.printStackTrace();
            }
        }
        // Create new node if match is not found.
        else {
            try {
                int id = memory.create(this);
                // Need to retrieve the created node by the id returned by memory
                fromJSON(memory.getById(id), new Gson());
            } catch (InterruptedException | IOException e) {
                LOGGER.warn("Unexpected memory error: provided ID not found upon querying.");
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return (String) getProperty("name");
    }

    public boolean hasRelationship(Neo4jRelationship type) {
        return !(getRelationship(type.type) == null) && (!getRelationship(type.type).isEmpty());
    }

    public ArrayList<Integer> getRelationships(Neo4jRelationship type) {
        return getRelationship(type.type);
    }

    /**
     * Returns an instance of this class based on the given JSON.
     */
    public MemoryNodeModel fromJSON(String json, Gson gson) {
        return gson.fromJson(json, this.getClass());
    }

    public void add(Neo4jRelationship relationship, String name) {
        if (Neo4jLegalRelationships.contains(relationship)) {
            LOGGER.info("The relationship is legal, proceed!");
            addInformation(relationship, name);
        } else if (Neo4jIllegalRelationships.contains(relationship)) {
            LOGGER.error("The relationship is illegal, abort!");
        } else {
            LOGGER.warn("The relationship is undefined, abort!");
        }
    }

    public void add(HashMap<Neo4jRelationship, String> relationships) {
        for (Neo4jRelationship relationshipName : relationships.keySet()) {
            add(relationshipName, relationships.get(relationshipName));
        }
    }

    /**
     * Adds a new relation to the person node, updating memory.
     */
    private void addInformation(Neo4jRelationship relationship, String name) {
        ArrayList<Integer> ids = new ArrayList<>();
        // First check if node with given name exists by a matching query.
        MemoryNodeModel relatedNode = new MemoryNodeModel(true, memory);
        relatedNode.setProperty("name", name);
        //This adds a label type to the memory query depending on the relation.
        relatedNode.setLabel(Neo4jRelationship.determineNodeType(relationship.type));
        try {
            ids = memory.getByQuery(relatedNode);
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Exception while querying memory by template.");
            e.printStackTrace();
        }
        // Pick first from list if multiple matches found.
        if(ids != null && !ids.isEmpty()) {
            //TODO Change from using first id to specifying if multiple matches are found.
            setRelationship(relationship.type, ids.get(0));
        }
        // Create new node if match is not found.
        else {
            try {
                int id = memory.create(relatedNode);
                if(id != 0) { // 0 is default value, returned if Memory response was FAIL.
                    setRelationship(relationship.type, id);
                }
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Unexpected memory error: creating node for new relation failed.");
                e.printStackTrace();
            }
        }
        //Update the person node in memory.
        try{
            memory.save(this);
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Unexpected memory error: updating person information failed.");
            e.printStackTrace();
        }
    }

    public enum RelationshipAvailability {
        ALL_AVAILABLE, SOME_AVAILABLE, NONE_AVAILABLE
    }

    /**
     * Checks if predicates from the input array are available for this interlocutor.
     * @param rels array of predicates to check
     * @return one of three: all, some or none available
     */
    public Interlocutor.RelationshipAvailability checkRelationshipAvailability(Neo4jRelationship[] rels) {
        boolean atLeastOneAvailable = false;
        boolean allAvailable = true;

        for (Neo4jRelationship predicate : rels) {
            if (this.hasRelationship(predicate)) {
                atLeastOneAvailable = true;
            } else {
                allAvailable = false;
            }
        }
        if (allAvailable) return Interlocutor.RelationshipAvailability.ALL_AVAILABLE;
        if (atLeastOneAvailable) return Interlocutor.RelationshipAvailability.SOME_AVAILABLE;
        return Interlocutor.RelationshipAvailability.NONE_AVAILABLE;
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

    @Override
    public String toString() {
        return this.getClass().getName() + ": {" +
                "memory=" + memory +
                ", id=" + id +
                ", labels=" + labels +
                ", label='" + label + '\'' +
                ", properties=" + properties +
                ", relationships=" + relationships +
                ", stripQuery=" + stripQuery +
                ", FAMILIAR=" + FAMILIAR +
                "}";
    }
}
