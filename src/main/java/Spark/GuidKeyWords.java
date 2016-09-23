package Spark;

import java.util.Map;

/**
 * Created by pc on 09/09/2016.
 */
public class GuidKeyWords {
    private String guid;
    private String domain;
    private Map<String, Double> keywords;
    private Map<String, String> mapwords;

    public GuidKeyWords(String guid, String domain, Map<String, Double> keywords, Map<String, String> mapwords) {
        this.guid = guid;
        this.domain = domain;
        this.keywords = keywords;
        this.mapwords = mapwords;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Map<String, Double> getKeywords() {
        return keywords;
    }

    public void setKeywords(Map<String, Double> keywords) {
        this.keywords = keywords;
    }

    public Map<String, String> getMapwords() {
        return mapwords;
    }

    public void setMapwords(Map<String, String> mapwords) {
        this.mapwords = mapwords;
    }
}
