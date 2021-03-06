/**
 * 
 */
package jp.atr.dni.bmi.desktop.neuroshareutils;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *
 * @author Keiji Harada [*1]</br>[*1] ATR Intl. Computational Neuroscience Labs, Decoding Group
 * @version 2011/04/22
 */
@XStreamAlias("NeuroshareXML")
public class NeuroshareFile {

    /**
     *
     */
    public static XStream xstream;

    static {
        xstream = new XStream(new DomDriver());
        xstream.processAnnotations(NeuroshareFile.class);
        xstream.processAnnotations(FileInfo.class);
        xstream.processAnnotations(Entity.class);
        xstream.processAnnotations(AnalogInfo.class);
        xstream.processAnnotations(EntityInfo.class);
        xstream.processAnnotations(EventInfo.class);
        xstream.processAnnotations(SegmentInfo.class);
        xstream.processAnnotations(NeuralInfo.class);
    }
    private String magicCode;
    private FileInfo fileInfo;
    @XStreamImplicit
    private ArrayList<Entity> entities;
    @XStreamOmitField
    private boolean lazyLoad;

    /**
     *
     */
    public NeuroshareFile() {
        super();
    }

    /**
     * @param magicCode
     * @param fileInfo
     * @param entities
     * @param lazyLoad
     */
    public NeuroshareFile(String magicCode, FileInfo fileInfo, ArrayList<Entity> entities, boolean lazyLoad) {
        super();
        this.magicCode = magicCode;
        this.fileInfo = fileInfo;
        this.entities = entities;
        this.lazyLoad = lazyLoad;
    }

    /**
     * @return the xstream
     */
    public static XStream getXstream() {
        return xstream;
    }

    /**
     * @param xstream the xstream to set
     */
    public static void setXstream(XStream xstream) {
        NeuroshareFile.xstream = xstream;
    }

    /**
     * @return the magicCode
     */
    public String getMagicCode() {
        return magicCode;
    }

    /**
     * @param magicCode the magicCode to set
     */
    public void setMagicCode(String magicCode) {
        this.magicCode = magicCode;
    }

    /**
     * @return the fileInfo
     */
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    /**
     * @param fileInfo the fileInfo to set
     */
    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    /**
     * @return the entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    /**
     * @return the lazyLoad
     */
    public boolean isLazyLoad() {
        return lazyLoad;
    }

    /**
     * @param lazyLoad the lazyLoad to set
     */
    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    /**
     * {@inheritDoc}
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return xstream.toXML(this);
    }
}
