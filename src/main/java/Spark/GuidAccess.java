package Spark;

import java.util.List;

/**
 * Created by pc on 09/09/2016.
 */
public class GuidAccess {
    private String guid;
    private String domain;
    private List<String > newsIDs;

    public GuidAccess(){

    }

    public GuidAccess(String guid, String domain, List<String> newsIDs) {
        this.guid = guid;
        this.domain = domain;
        this.newsIDs = newsIDs;
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

    public List<String> getNewsIDs() {
        return newsIDs;
    }

    public void setNewsIDs(List<String> newsIDs) {
        this.newsIDs = newsIDs;
    }
}
