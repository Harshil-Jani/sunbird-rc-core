package io.opensaber.registry.model.state;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;

public class StateContext {

    JsonNode existingNode;
    String currentRole;
    JsonNode requestBody;
    ObjectNode result;

    public StateContext(JsonNode existingNode, String currentRole) {
        this.existingNode = existingNode;
        result = existingNode.deepCopy();
        this.currentRole = currentRole;
    }

    public StateContext(String currentRole, JsonNode requestBody) {
        this.currentRole = currentRole;
        result = requestBody.deepCopy();
        this.requestBody = requestBody;
    }

    public StateContext(JsonNode existingNode, JsonNode requestBody, String currentRole) {
        this.existingNode = existingNode;
        this.currentRole = currentRole;
        this.requestBody = requestBody;
        result = existingNode.deepCopy();
        copyTheRequestBody(requestBody);
    }

    private void copyTheRequestBody(JsonNode requestBody) {
        Iterator<Map.Entry<String, JsonNode>> fields = requestBody.fields();
        while(fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            if(result.has(next.getKey())) {
                result.put(next.getKey(), requestBody.get(next.getKey()));
            }
        }
    }

    public boolean isAttributesChanged() {
        Iterator<Map.Entry<String, JsonNode>> fields = existingNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            if(requestBody.has(next.getKey()) &&
                    !requestBody.get(next.getKey()).equals(existingNode.get(next.getKey()))){
                return true;
            }
        }
        return false;
    }

    public void setState(States destinationState) {
        if(existingNode != null) {
            result.put("state", destinationState.toString());
        } else {
            result.put("state", destinationState.toString());
        }
    }

    public JsonNode getResult() {
        return result;
    }

    public JsonNode getExistingNode() {
        return existingNode;
    }

    public void setExistingNode(JsonNode existingNode) {
        this.existingNode = existingNode;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public JsonNode getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(JsonNode requestBody) {
        this.requestBody = requestBody;
    }
}