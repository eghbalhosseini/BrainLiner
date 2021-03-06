package jp.atr.dni.bmi.desktop.neuroshareutils.readers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import jp.atr.dni.bmi.desktop.neuroshareutils.AnalogData;
import jp.atr.dni.bmi.desktop.neuroshareutils.AnalogInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.Entity;
import jp.atr.dni.bmi.desktop.neuroshareutils.EntityInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.EntityType;
import jp.atr.dni.bmi.desktop.neuroshareutils.EventInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.EventType;
import jp.atr.dni.bmi.desktop.neuroshareutils.FileInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.NeuralInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.NeuroshareFile;
import jp.atr.dni.bmi.desktop.neuroshareutils.ReaderUtils;
import jp.atr.dni.bmi.desktop.neuroshareutils.SegmentInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.SegmentSourceInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.Tag;

/**
 *
 * @author Keiji Harada [*1]</br>[*1] ATR Intl. Conputational Neuroscience Labs, Decoding Group
 * @version 2011/04/22
 */
public class NSInfoReader {

    /**
     *
     * @param path
     * @param lazyLoad
     * @return
     */
    public NeuroshareFile readNSFile(String path, boolean lazyLoad) {

        FileInfo fileInfo = null;
        String magicCode = null;
        ArrayList<Entity> entities = new ArrayList<Entity>();
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(path, "r");
            file.seek(0);

            magicCode = "";
            for (int i = 0; i < 16; i++) {
                magicCode += (char) file.readByte();
            }

//         LOGGER.debug("magicCode: " + magicCode);
            if (!magicCode.equals("NSN ver00000010 ")) {
//            LOGGER.error("The file we are trying to read is not compatible with "
//                  + "the ver 1.0 format of the Neuroshare Native Datafile "
//                  + "Specificiation.\n\nSorry, but there is nothing else we can do now.\n\n" + "Exiting...");
                return null;
            }

            magicCode = magicCode.trim();

            // Now read in the file info
            fileInfo = readFileInfo(file);

            if (fileInfo.getEntityCount() < 1) {
//            LOGGER.error("The file has no entities to read in." + "Exiting...");
                return null;
            }

            // Now read in the entities
            for (int elemNum = 0; elemNum < fileInfo.getEntityCount(); elemNum++) {
                // First read in the element tag
                long elemType = ReaderUtils.readUnsignedInt(file);
//            LOGGER.debug("------------------------------");
//            LOGGER.debug("elemType: " + elemType);
                long elemLength = ReaderUtils.readUnsignedInt(file);
//            LOGGER.debug("elemLength: " + elemLength);
                Tag tag = new Tag(EntityType.getEntityType(elemType), elemLength);
                // Read in the entity information
                EntityInfo entityNFO = readEntityInfo(file);

                // Now that we have the tag, we should do different things, based on the type
                if (tag.getEntityType() == EntityType.ENTITY_EVENT) {

                    // Read in header data
                    EventInfo eventNFO = readEventInfo(entityNFO, tag, file);
                    entities.add(eventNFO);

                    // if (!lazyLoad) {
                    // Go through and get all the data. The type of data depends on the eventNFO's
                    // event type.
                    getEventData(entityNFO, eventNFO, tag, file);
                    // }
                } else if (tag.getEntityType() == EntityType.ENTITY_ANALOG) {
                    // Read in header data
                    AnalogInfo analogNFO = readAnalogInfo(entityNFO, tag, file);
                    entities.add(analogNFO);

                    // Get all the data for calculate statistics
                    ArrayList<AnalogData> data = getAnalogData(entityNFO, file);
                    analogNFO.setData(data);

                } else if (tag.getEntityType() == EntityType.ENTITY_SEGMENT) {
                    // Read in header data
                    SegmentInfo segmentInfo = readSegmentInfo(entityNFO, tag, file);

                    ArrayList<SegmentSourceInfo> segSourceInfos = new ArrayList<SegmentSourceInfo>();
                    // Get the segment source headers
                    for (int srcNDX = 0; srcNDX < segmentInfo.getSourceCount(); srcNDX++) {
//                  LOGGER.debug("Reading in segment source header------------------");
                        segSourceInfos.add(readSegmentSourceInfo(file));
                    }

                    segmentInfo.setSegSourceInfos(segSourceInfos);

                    entities.add(segmentInfo);

                    // Get the segment data now
                    getSegmentData(entityNFO, segmentInfo, file);

                } else if (tag.getEntityType() == EntityType.ENTITY_NEURAL) {
                    // Read in header data
                    NeuralInfo neuralInfo = readNeuralInfo(entityNFO, tag, file);
                    entities.add(neuralInfo);

                    // Get the neural data
                    getNeuralData(entityNFO, file);

                } else {
                    // We can't handle this, so just quit.
                    // NOTE: we would just be able to skip this tag and move on, but since the Neuroshare
                    // .nsn files are binary and not XML-based, we cannot just skip a tag and move on.
                    // This is one good reason not to use binary file formats like this.
//               LOGGER.error("An element tag has an unknown datatype, so we have to quit.");
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new NeuroshareFile(magicCode, fileInfo, entities, lazyLoad);
    }

    private AnalogInfo readAnalogInfo(EntityInfo entityNFO, Tag tag, RandomAccessFile file) throws IOException {

        double sampleRate;
        double minVal;
        double maxVal;
        String units = "";
        double resolution;
        double locationX;
        double locationY;
        double locationZ;
        double locationuser;
        double highFreqCorner;
        long highFreqOrder;
        String highFilterType = "";
        double lowFreqCorner;
        long lowFreqOrder;
        String lowFiltertype = "";
        String probeInfo = "";

        sampleRate = ReaderUtils.readDouble(file);
//      LOGGER.debug("sampleRate: " + sampleRate);

        minVal = ReaderUtils.readDouble(file);
//      LOGGER.debug("minVal: " + minVal);

        maxVal = ReaderUtils.readDouble(file);
//      LOGGER.debug("maxVal: " + maxVal);

        for (int i = 0; i < 16; i++) {
            units += (char) file.readByte();
        }
//      LOGGER.debug("units: " + units);

        resolution = ReaderUtils.readDouble(file);
//      LOGGER.debug("resolution: " + resolution);

        locationX = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationX: " + locationX);

        locationY = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationY: " + locationY);

        locationZ = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationZ: " + locationZ);

        locationuser = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationuser: " + locationuser);

        highFreqCorner = ReaderUtils.readDouble(file);
//      LOGGER.debug("highFreqCorner: " + highFreqCorner);

        highFreqOrder = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("highFreqOrder: " + highFreqOrder);

        for (int i = 0; i < 16; i++) {
            highFilterType += (char) file.readByte();
        }
//      LOGGER.debug("highFilterType: " + highFilterType);

        lowFreqCorner = ReaderUtils.readDouble(file);
//      LOGGER.debug("lowFreqCorner: " + lowFreqCorner);

        lowFreqOrder = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("lowFreqOrder: " + lowFreqOrder);

        for (int i = 0; i < 16; i++) {
            lowFiltertype += (char) file.readByte();
        }
//      LOGGER.debug("lowFiltertype: " + lowFiltertype);

        for (int i = 0; i < 128; i++) {
            probeInfo += (char) file.readByte();
        }
//      LOGGER.debug("probeInfo: " + probeInfo);

        return new AnalogInfo(tag, entityNFO, sampleRate, minVal, maxVal, units, resolution, locationX,
                locationY, locationY, locationuser, highFreqCorner, highFreqOrder, highFilterType, lowFreqCorner,
                lowFreqOrder, lowFiltertype, probeInfo);
    }

    private EntityInfo readEntityInfo(RandomAccessFile file) throws IOException {
        String entityLabel = "";
        for (int i = 0; i < 32; i++) {
            entityLabel += (char) file.readByte();
        }
//      LOGGER.debug("entityLabel: " + entityLabel);

        long entityType = ReaderUtils.readUnsignedInt(file);
        EntityType et = EntityType.getEntityType(entityType);
//      LOGGER.debug("entityType: " + entityType);

        long itemCount = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("itemCount: " + itemCount);

        return new EntityInfo(entityLabel, et, itemCount);
    }

    private EventInfo readEventInfo(EntityInfo entityInfo, Tag tag, RandomAccessFile file) throws IOException {
        // Now process the event information header
        long eventType = ReaderUtils.readUnsignedInt(file);
        EventType et = EventType.getEventType(eventType);
//      LOGGER.debug("eventType: " + eventType);

        long minDataLength = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("minDataLength: " + minDataLength);

        long maxDataLength = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("maxDataLength: " + maxDataLength);

        String csvDesc = "";

        for (int i = 0; i < 128; i++) {
            csvDesc += (char) file.readByte();
        }
//      LOGGER.debug("csvDesc: " + csvDesc);

        return new EventInfo(tag, entityInfo, et, minDataLength, maxDataLength, csvDesc);
    }

    private NeuralInfo readNeuralInfo(EntityInfo entityInfo, Tag tag, RandomAccessFile file) throws IOException {
        long sourceEntityID;
        long sourceUnitID;
        String probeInfo = "";

        sourceEntityID = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("sourceEntityID: " + sourceEntityID);

        sourceUnitID = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("sourceUnitID: " + sourceUnitID);

        for (int i = 0; i < 128; i++) {
            probeInfo += (char) file.readByte();
        }
//      LOGGER.debug("probeInfo: " + probeInfo);

        return new NeuralInfo(tag, entityInfo, sourceEntityID, sourceUnitID, probeInfo);
    }

    private SegmentInfo readSegmentInfo(EntityInfo entityInfo, Tag tag, RandomAccessFile file)
            throws IOException {
        long sourceCount;
        long minSampleCount;
        long maxSampleCount;
        double sampleRate;
        String units = "";

        sourceCount = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("sourceCount: " + sourceCount);

        minSampleCount = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("minSampleCount: " + minSampleCount);

        maxSampleCount = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("maxSampleCount: " + maxSampleCount);

        sampleRate = ReaderUtils.readDouble(file);
//      LOGGER.debug("sampleRate: " + sampleRate);

        for (int i = 0; i < 32; i++) {
            units += (char) file.readByte();
        }
//      LOGGER.debug("units: " + units);

        return new SegmentInfo(tag, entityInfo, sourceCount, minSampleCount, maxSampleCount, sampleRate, units);
    }

    private SegmentSourceInfo readSegmentSourceInfo(RandomAccessFile file) throws IOException {

        double minVal;
        double maxVal;
        double resolution;
        double subSampleShift;
        double locationX;
        double locationY;
        double locationZ;
        double locationUser;
        double highFreqCorner;
        long highFreqOrder;
        String highFilterType = "";
        double lowFreqCorner;
        long lowFreqOrder;
        String lowFilterType = "";
        String probeInfo = "";

        minVal = ReaderUtils.readDouble(file);
//      LOGGER.debug("minVal: " + minVal);

        maxVal = ReaderUtils.readDouble(file);
//      LOGGER.debug("maxVal: " + maxVal);

        resolution = ReaderUtils.readDouble(file);
//      LOGGER.debug("resolution: " + resolution);

        subSampleShift = ReaderUtils.readDouble(file);
//      LOGGER.debug("subSampleShift: " + subSampleShift);

        locationX = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationX" + locationX);

        locationY = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationY: " + locationY);

        locationZ = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationZ: " + locationZ);

        locationUser = ReaderUtils.readDouble(file);
//      LOGGER.debug("locationUser: " + locationUser);

        highFreqCorner = ReaderUtils.readDouble(file);
//      LOGGER.debug("highFreqCorner: " + highFreqCorner);

        highFreqOrder = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("highFreqOrder: " + highFreqOrder);

        for (int i = 0; i < 16; i++) {
            highFilterType += (char) file.readByte();
        }
//      LOGGER.debug("highFilterType: " + highFilterType);

        lowFreqCorner = ReaderUtils.readDouble(file);
//      LOGGER.debug("lowFreqCorner: " + lowFreqCorner);

        lowFreqOrder = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("lowFreqOrder: " + lowFreqOrder);

        for (int i = 0; i < 16; i++) {
            lowFilterType += (char) file.readByte();
        }
//      LOGGER.debug("lowFilterType: " + lowFilterType);

        for (int i = 0; i < 128; i++) {
            probeInfo += (char) file.readByte();
        }
//      LOGGER.debug("probeInfo: " + probeInfo);

        return new SegmentSourceInfo(minVal, maxVal, resolution, subSampleShift, locationX, locationY,
                locationZ, locationUser, highFreqCorner, highFreqOrder, highFilterType, lowFreqCorner,
                lowFreqOrder, lowFilterType, probeInfo);
    }

    private FileInfo readFileInfo(RandomAccessFile file) throws IOException {
        String fileType = "";
        long entityCount = 0;
        double timeStampResolution;
        double timeSpan;
        String appName = "";
        long year = 0;
        long month = 0;
        long dayOfWeek = 0;
        long dayOfMonth = 0;
        long hourOfDay = 0;
        long minOfDay = 0;
        long secOfDay = 0;
        long milliSecOfDay = 0;
        String comments = "";

        file.seek(16); // Seek to guarantee we are where we want to be

        // Read in fileType
        for (int i = 0; i < 32; i++) {
            fileType += (char) file.readByte();
        }
//      LOGGER.debug("fileType: " + fileType);

        // Read in entityCount
        entityCount = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("entityCount: " + Long.toString(entityCount & 0xffffffffL));
//      LOGGER.debug("entityCount: " + entityCount);

        // Read in timeStampeRes
        timeStampResolution = ReaderUtils.readDouble(file);
//      LOGGER.debug("timeStampeResolution: " + timeStampResolution);

        // Read in timespan
        timeSpan = ReaderUtils.readDouble(file);
//      LOGGER.debug("timeSpan: " + timeSpan);

        // Read in the appName
        for (int i = 0; i < 64; i++) {
            appName += (char) file.readByte();
        }
//      LOGGER.debug("appName: " + appName);

        // Read in the year
        year = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("year: " + year);

        // Read in the month
        month = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("month: " + month);

        // Read in the dayOfWeek
        dayOfWeek = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("dayOfWeek: " + dayOfWeek);

        dayOfMonth = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("dayOfMonth: " + dayOfMonth);

        // Read in the hourOfDay;
        hourOfDay = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("hourOfDay: " + hourOfDay);

        // Read in the minOfDay
        minOfDay = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("minOfDay: " + minOfDay);

        // Read in the secOfDay;
        secOfDay = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("secOfDay: " + secOfDay);

        // Read in the milliSecOfDay;
        milliSecOfDay = ReaderUtils.readUnsignedInt(file);
//      LOGGER.debug("milliSecOfDay: " + milliSecOfDay);

        // Read in the comments
        for (int i = 0; i < 256; i++) {
            comments += (char) file.readByte();
        }
//      LOGGER.debug("comments: " + comments);

        return new FileInfo(fileType, entityCount, timeStampResolution, timeSpan, appName, year, month,
                dayOfWeek, dayOfMonth, hourOfDay, minOfDay, secOfDay, milliSecOfDay, comments);
    }

    private ArrayList<AnalogData> getAnalogData(EntityInfo entityNFO, RandomAccessFile file) throws IOException {
        ArrayList<AnalogData> data = new ArrayList<AnalogData>();
        int count = 0;

        while (count < entityNFO.getItemCount()) {

            double timeStamp = ReaderUtils.readDouble(file);
//         LOGGER.debug("timeStamp: " + timeStamp);

            long dataCount = ReaderUtils.readUnsignedInt(file);
//         LOGGER.debug("dataCount: " + dataCount);

            ArrayList<Double> values = new ArrayList<Double>();

            for (int valNDX = 0; valNDX < dataCount; valNDX++) {
                values.add(ReaderUtils.readDouble(file));
                count++;
            }
            data.add(new AnalogData(timeStamp, dataCount, values));
//         LOGGER.debug("values: " + values);
        }

        return data;
    }

    private void getEventData(EntityInfo entityNFO, EventInfo eventNFO, Tag elem, RandomAccessFile file)
            throws IOException {

        for (int dataItemNum = 0; dataItemNum < entityNFO.getItemCount(); dataItemNum++) {

            double timeStamp = ReaderUtils.readDouble(file);
//         LOGGER.debug("timeStamp: " + timeStamp);
            long byteSize = ReaderUtils.readUnsignedInt(file);
//         LOGGER.debug("byteSize: " + byteSize);

            if (eventNFO.getEventType() == EventType.EVENT_TEXT) {
                file.skipBytes(((Long) elem.getElemLength()).intValue());
            } else if (eventNFO.getEventType() == EventType.EVENT_CSV) {
                // We are dealing with CSV. What do we do here?! TODO:XXX:FIXME:
                // XXX: this is not defined in the file format specification
            } else if (eventNFO.getEventType() == EventType.EVENT_BYTE) {
                file.skipBytes(1);
            } else if (eventNFO.getEventType() == EventType.EVENT_WORD) {
                file.skipBytes(2);
            } else if (eventNFO.getEventType() == EventType.EVENT_DWORD) {
                file.skipBytes(4);
            } else {
                // We can't handle it, so just quit.
//            LOGGER.error("An unexpected event type was encountered, so we have to quit.");
                return;
            }
        }
    }

    /**
     * Neural data is simply a list of timestamps that say in seconds, when spikes occurred in the
     * neuronal data.
     *
     * @param file
     * @return
     * @throws IOException
     */
    private void getNeuralData(EntityInfo entityNFO, RandomAccessFile file) throws IOException {
        file.skipBytes(8 * ((Long) entityNFO.getItemCount()).intValue());
    }

    private void getSegmentData(EntityInfo entityNFO, SegmentInfo segNFO, RandomAccessFile file)
            throws IOException {

        ArrayList<Long> sampleCounts = new ArrayList<Long>();
        ArrayList<Double> timeStamps = new ArrayList<Double>();
        ArrayList<Long> unitIDS = new ArrayList<Long>();

        for (int x = 0; x < segNFO.getSourceCount(); x++) {
            // NOTE: sample count is not defined in the spec because the spec documentation is wrong!
            long sampleCount = ReaderUtils.readUnsignedInt(file);
//         LOGGER.debug("sampleCount: " + sampleCount);
            sampleCounts.add(sampleCount);

            double timeStamp = ReaderUtils.readDouble(file);
//         LOGGER.debug("timeStamp: " + timeStamp);
            timeStamps.add(timeStamp);

            long unitID = ReaderUtils.readUnsignedInt(file);
//         LOGGER.debug("unitID: " + unitID);
            unitIDS.add(unitID);

            // The documentation might be wrong about this next line.
            file.skipBytes(8 * ((Long) segNFO.getMaxSampleCount()).intValue());
        }
    }
}
