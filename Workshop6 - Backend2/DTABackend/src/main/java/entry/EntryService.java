package entry;

import messaging.requests.*;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/entry")
public class EntryService {

    @EJB
    private EntryBean entryBean;

    @POST
    @Path("deleteEntry")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEntry(DeleteEntryRequest request) {
        return this.entryBean.onDeleteEntry(request);
    }

    @POST
    @Path("updateEntry")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEntry(UpdateEntryRequest request) {
        return this.entryBean.onUpdateEntry(request);
    }

    @POST
    @Path("changeEntryStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeEntryStatus(ChangeEntryStatusRequest request) {
        return this.entryBean.onChangeEntryStatusRequest(request);
    }

    @POST
    @Path("createEntry")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEntry(CreateEntryRequest request) {
        return this.entryBean.onCreateEntry(request);
    }

    @POST
    @Path("createEntryHolder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEntryHolder(CreateEntryHolderRequest request) {
        return this.entryBean.onCreateEntryHolder(request);
    }

    @POST
    @Path("deleteEntryHolder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEntryHolder(DeleteEntryHolderRequest request) {
        return this.entryBean.onDeleteEntryHolder(request);
    }

    @POST
    @Path("editEntryHolder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editEntryHolder(EditEntryHolderRequest request) {
        return this.entryBean.onEditEntryHolderRequest(request);
    }

    @GET
    @Path("getEntries")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntries() {
        return this.entryBean.onGetEntries();
    }
}
