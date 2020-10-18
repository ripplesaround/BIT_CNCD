package server.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

//处理器
public class WebHandler extends DefaultHandler {
    private List<Entity> entities = new ArrayList<Entity>();
    private List<Mapping> mappings = new ArrayList<Mapping>();

    private Entity entity;
    private Mapping mapping;
    private String tag;
    private boolean isMapping = false;

    public WebHandler() {
    }
//    public WebHandler(List<Entity> entities, List<Mapping> mappings) {
//        this.entities = entities;
//        this.mappings = mappings;
//    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(null!=qName){
            tag = qName;
            if(tag.equals("servlet")){
                entity = new Entity();
                isMapping = false;
            }else if(tag.equals("servlet-mapping")){
                mapping = new Mapping();
                isMapping = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(null!=qName){
            if(qName.equals("servlet")){
                entities.add(entity);
//                System.out.println("end qname-->"+qName);
            }else if(qName.equals("servlet-mapping")){
//                System.out.println("hello");
                mappings.add(mapping);
//                System.out.println("end qname-->"+qName);
            }
        }
        tag = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String contents = new String(ch,start,length).trim();
        if(contents.length()<1){
            return;
        }
//        System.out.println("content-->"+contents+" isMapping-->"+isMapping);
        if(null!=tag){
             if(isMapping){
                 if(tag.equals("servlet-name")){
                     mapping.setName(contents);
                 }
                 else if(tag.equals("url-pattern")){
                     mapping.addPattern(contents);
                 }
             }
             else {
                 if(tag.equals("servlet-name")){
                    entity.setName(contents);
                 }
                 else if(tag.equals("servlet-class")){
                    entity.setClz(contents);
                 }
             }
        }
    }
}
