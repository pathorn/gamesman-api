package edu.berkeley.gamesman.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Inject;

import edu.berkeley.gamesman.api.GameVariant.UnsupportedVersionException;
import edu.berkeley.gamesman.api.GameVariant.VersionNotFoundException;

@Path("{game}")
@Produces("text/plain")
public class GamesmanService {

	@PathParam("game")
    private String game;

    @PathParam("game")
    private PathSegment gameSegment;

    @Inject
    private ObjectMapper mapper;

    private final GamesmanApi api;
    
    @Context
    private HttpServletResponse resp;

    @Inject
    public GamesmanService(GamesmanApi api) {
        this.api = api;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("initial-position-value")
    public String getInitialPositionValue(@Context UriInfo info) {
    	GameVariant gameVariant;
    	try {
			gameVariant = getGameVariant();
		} catch (VersionNotFoundException e) {
			throw new RuntimeException(e);
			//ERROR(e);
		} catch (UnsupportedVersionException e) {
			throw new RuntimeException(e);
			//ERROR(e);
		}
        try {
        	resp.setHeader("Access-Control-Allow-Origin", "*");
			return mapper.writeValueAsString(
				api.getInitialPositionValue(gameVariant));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("position-values")
    public String getPositionValues(@Context UriInfo info) {
    	GameVariant gameVariant;
    	try {
			gameVariant = getGameVariant();
		} catch (VersionNotFoundException e) {
			throw new RuntimeException(e);
			//ERROR(e);
		} catch (UnsupportedVersionException e) {
			throw new RuntimeException(e);
			//ERROR(e);
		}
        MultivaluedMap<String, String> boardParameters =
            getMatrixParametersForSegment("position-values", info);
        try {
        	resp.setHeader("Access-Control-Allow-Origin", "*");
			return mapper.writeValueAsString(
				api.getPositionValues(gameVariant, boardParameters.get("position")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("next-position-values")
    public String getNextPositionValues(@Context UriInfo info) {
    	GameVariant gameVariant;
    	try {
			gameVariant = getGameVariant();
		} catch (VersionNotFoundException e) {
			throw new RuntimeException(e);
			//ERROR(e);
		} catch (UnsupportedVersionException e) {
			throw new RuntimeException(e);
			//ERROR(e);
		}
        MultivaluedMap<String, String> boardParameters =
            getMatrixParametersForSegment("next-position-values", info);
        try {
        	resp.setHeader("Access-Control-Allow-Origin", "*");
			return mapper.writeValueAsString(
				api.getNextPositionValues(gameVariant, boardParameters.getFirst("position")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	protected Map<String, String> getVariant() {
    	final Map<String, List<String>> multimap = getGameSegment().getMatrixParameters();
        return new Map<String, String>() {
			public int size() {
				return multimap.size();
			}
			public boolean isEmpty() {
				return multimap.isEmpty();
			}
			public boolean containsKey(Object key) {
				return multimap.containsKey(key);
			}
			public boolean containsValue(Object value) {
				throw new UnsupportedOperationException("containsValue");
			}
			public String get(Object key) {
				return multimap.get(key).get(0);
			}
			public String put(String key, String value) {
				throw new UnsupportedOperationException("put");
			}
			public String remove(Object key) {
				throw new UnsupportedOperationException("remove");
			}
			public void putAll(Map<? extends String, ? extends String> t) {
				throw new UnsupportedOperationException("putAll");
			}
			public void clear() {
				throw new UnsupportedOperationException("clear");
			}
			public Set<String> keySet() {
				// TODO Auto-generated method stub
				return multimap.keySet();
			}
			public Collection<String> values() {
				throw new UnsupportedOperationException("values");
			}
			public Set<java.util.Map.Entry<String, String>> entrySet() {
				throw new UnsupportedOperationException("entrySet");
			}
        };
    }

    protected String getGame() {
        return game;
    }
    
    protected GameVariant getGameVariant()
    		throws VersionNotFoundException, UnsupportedVersionException {
    	return GameVariant.makeVersioned(getGame(), getVariant());
    }

    protected PathSegment getGameSegment() {
        return gameSegment;
    }

    protected GamesmanApi getApi() {
        return api;
    }

    /**
     * Extracts the matrix parameters from the specified URI path segment.
     * Unfortunately, it is not possible to use {@code @PathParam} to extract
     * the path segment of an unnamed {@code @Path} (that is, a {@code @Path}
     * whose value is constant, not a variable).
     * <p>
     * If multiple path segments share the same value, it is not defined which
     * segment's matrix parameters are returned.
     * @param   path    the value of the path segment whose parameters to get
     * @param   info    context about the request URI
     * @return  the matrix parameters of the specified path segment
     * @throws  MissingPathSegmentException if there is no segment with the
     *          specified path in the given URI
     */
    private static MultivaluedMap<String, String> getMatrixParametersForSegment(
            String path, UriInfo info) {
        // Search through each path segment in the URI for the specified one.
        List<PathSegment> pathSegments = info.getPathSegments();
        for (PathSegment segment : pathSegments) {
            if (segment.getPath().equals(path)) {
                return segment.getMatrixParameters();
            }
        }

        String error = "no path segment for \"%s\" found in URI \"%s\"";
        error = String.format(error, path, info.getPath());
        throw new MissingPathSegmentException(error);
    }
}
