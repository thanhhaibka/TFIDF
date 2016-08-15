package config;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;

/**
 * Created by pc on 05/08/2016.
 */
public class TFVector {
    private int[] vector;
    private WVTDocumentInfo wvtDocumentInfo;

    public TFVector(int[] vector, WVTDocumentInfo wvtDocumentInfo) {
        this.vector = vector;
        this.wvtDocumentInfo = wvtDocumentInfo;
    }

    public int[] getVector() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }

    public WVTDocumentInfo getWvtDocumentInfo() {
        return wvtDocumentInfo;
    }

    public void setWvtDocumentInfo(WVTDocumentInfo wvtDocumentInfo) {
        this.wvtDocumentInfo = wvtDocumentInfo;
    }
}
