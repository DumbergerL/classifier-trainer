package org.SirTobiSwobi.c3.classifiertrainer.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.classifiertrainer.api.TCRelationship;
import org.SirTobiSwobi.c3.classifiertrainer.db.CategoryManager;
import org.SirTobiSwobi.c3.classifiertrainer.db.Relationship;
import org.SirTobiSwobi.c3.classifiertrainer.db.RelationshipType;

import com.codahale.metrics.annotation.Timed;

@Path("/relationships/{rel}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipResource {
	
	private CategoryManager catMan;
	public RelationshipResource(CategoryManager catMan) {
		this.catMan = catMan;
	}
	
	@GET
    @Timed
	public Response getRelationship(@PathParam("rel") long rel){
		if(!catMan.containsRelationship(rel)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		Relationship relationship = catMan.getRelationshipByAddress(rel);
		TCRelationship output = new TCRelationship(relationship.getId(),
				relationship.getFrom().getId(), relationship.getTo().getId(), relationship.getType().toString());
		return Response.ok(output).build();
	}
	
	@PUT
	public Response setRelationship(@PathParam("rel") long rel, @NotNull @Valid TCRelationship relationship){
		if(relationship.getId()!=rel){
			Response response = Response.status(400).build();
			return response;
		}
		
		RelationshipType type;
		if(relationship.getType().equals("Sub")){
			type = RelationshipType.Sub;
		}else if(relationship.getType().equals("Equality")){
			type = RelationshipType.Equality;
		}else{
			Response response = Response.status(400).build();
			return response;
		}
		
		catMan.setRelationship(relationship.getId(), relationship.getFromId(), relationship.getToId(), type);

		Response response = Response.ok().build();
		return response;
	}
	
	@DELETE
	public Response deleteRelationship(@PathParam("rel") long rel){
		catMan.deleteRelationship(rel);
		Response response = Response.ok().build();
		return response;
	}

}