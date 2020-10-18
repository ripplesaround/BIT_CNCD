import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Webcontext {
    private List<Entity> entities = null;
    private List<Mapping> mappings = null;

    private Map<String,String> entityMap = new HashMap<String,String>();
    private Map<String,String> mappingMap = new HashMap<String,String>();

    public Webcontext(List<Entity> entities, List<Mapping> mappings) {
        this.entities = entities;
        this.mappings = mappings;

        //将entity的list转成map
        for(Entity entity:entities){
            entityMap.put(entity.getName(),entity.getClz());
        }
        //将map的list转成map
        for(Mapping mapping:mappings){
            for(String pattern:mapping.getPatterns()){
                mappingMap.put(pattern,mapping.getName());
            }

        }

    }
    public String getClz(String pattern){
        String name = mappingMap.get(pattern);
        return entityMap.get(name);
    }

}
