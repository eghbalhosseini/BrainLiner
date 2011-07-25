/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.atr.dni.bmi.desktop.model.api.data;

/**
 *
 * @author Makoto Takemiya - [武宮 誠] <br />
 * <a href="http://www.atr.jp">ATR - [株式会社・国際電氣通信基礎技術研究所]</a>
 *
 * @version 2011/07/15
 */
public final class NSNNeuralSpikeData implements APIData {

    private APIList<Double> timeStamps;;

    /**
     * @return the timeStamps
     */
    public APIList<Double> getTimeStamps() {
        return timeStamps;
    }

    /**
     * @param timeStamps the timeStamps to set
     */
    public void setTimeStamps(APIList<Double> timeStamps) {
        this.timeStamps = timeStamps;
    }
}
