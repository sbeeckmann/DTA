package entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entry.data.Entry;
import entry.data.EntryHolder;
import messaging.requests.*;
import messaging.response.BasicResponse;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@LocalBean
public class EntryBean {

    @PersistenceContext
    private EntityManager em;

    public String onCreateEntry(CreateEntryRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();
        try {
            EntryHolder entryHolder = this.em.find(EntryHolder.class, request.getEntryHolderId());
            if (entryHolder == null) {
                response.setError("Wrong id, holder not found"); // --> TODO enum
                return objectMapper.writeValueAsString(response);
            }

            Entry entry = new Entry();
            entry.setName(request.getName());
            entry.setStatus(Entry.Status.ACTIVE);
            entry.setCreationTime(System.currentTimeMillis());
            entry.setPriority(request.getPriority());
            entry.setActive(true);
            this.em.persist(entry);

            entryHolder.addEntry(entry);

            response.setResponseData(entry);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            //TODO error handling -> logging
        }

        return "";
    }

    public String onUpdateEntry(UpdateEntryRequest updateEntryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse basicResponse = new BasicResponse();
        try {
            Entry entry = this.em.find(Entry.class, updateEntryRequest.getEntryId());

            if (entry == null) {
                basicResponse.setError("Entry not found");
                return objectMapper.writeValueAsString(basicResponse);
            }

            entry.setPriority(updateEntryRequest.getPriority());
            entry.setName(updateEntryRequest.getName());
            basicResponse.setResponseData(entry);

            return objectMapper.writeValueAsString(basicResponse);
        } catch (JsonProcessingException ex) {
            // TODO error handling -> logging
        }

        return "";
    }

    public String onDeleteEntry(DeleteEntryRequest deleteEntryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();
        try {
            Entry entry = this.em.find(Entry.class, deleteEntryRequest.getEntryId());
            if (entry == null) {
                response.setError("Entry not found");
                return objectMapper.writeValueAsString(response);
            }

            EntryHolder entryHolder = this.em.find(EntryHolder.class, deleteEntryRequest.getEntryHolderId());
            if (entryHolder != null) {
                entryHolder.removeEntry(entry);
            }
            this.em.remove(entry);

            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            //TODO error handling -> logging
        }

        return "";
    }

    public String onGetEntries() {
        BasicResponse response = new BasicResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        List<EntryHolder> holders = this.em.createNamedQuery(EntryHolder.NQ_GET_ALL, EntryHolder.class).getResultList();
        try {
            response.setResponseData(holders);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            //TODO error handling -> logging
        }

        return "";
    }

    public String onCreateEntryHolder(CreateEntryHolderRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();
        try {
            EntryHolder entryHolder = new EntryHolder();
            entryHolder.setName(request.getName());

            this.em.persist(entryHolder);
            response.setResponseData(entryHolder);

            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            // TODO error handling -> logging
        }

        return "";
    }

    public String onDeleteEntryHolder(DeleteEntryHolderRequest deleteEntryHolderRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();
        try {
            EntryHolder entryHolder = this.em.find(EntryHolder.class, deleteEntryHolderRequest.getEntryHolderId());
            if (entryHolder == null) {
                response.setError("Entryholder not found");
                return objectMapper.writeValueAsString(response);
            }
            for (Entry entry : entryHolder.getEntries()) {
                this.em.remove(entry);
            }
            this.em.remove(entryHolder);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            //TODO error handling -> logging
        }

        return "";
    }

    public String onChangeEntryStatusRequest(ChangeEntryStatusRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();
        try {
            Entry entry = this.em.find(Entry.class, request.getEntryId());
            if (entry == null) {
                response.setError("Entry not found");
                return objectMapper.writeValueAsString(response);
            }
            entry.setActive(request.isActive());
            response.setResponseData(entry);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {

        }
        return "";
    }

    public String onEditEntryHolderRequest(EditEntryHolderRequest editEntryHolderRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();
        try {
            EntryHolder eh = this.em.find(EntryHolder.class, editEntryHolderRequest.getEntryHolderId());
            if (eh == null) {
                response.setError("EntryHolder not found");
                return objectMapper.writeValueAsString(response);
            }
            eh.setName(editEntryHolderRequest.getName());
            response.setResponseData(eh);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {

        }
        return "";
    }
}
