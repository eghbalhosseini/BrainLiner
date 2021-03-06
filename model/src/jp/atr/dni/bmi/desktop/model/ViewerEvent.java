/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.atr.dni.bmi.desktop.model;

import javax.swing.event.EventListenerList;

/**
 *
 * @author Keiji Harada [*1]</br>[*1] ATR Intl. Conputational Neuroscience Labs, Decoding Group
 * @version 2011/04/22
 */
public class ViewerEvent {

    /**
     *
     */
    protected EventListenerList eventListenerList = new EventListenerList();

    /**
     * 
     * @param listener
     */
    public void addViewerEventListener(ViewerEventListener listener) {
        eventListenerList.add(ViewerEventListener.class, listener);
    }

    /**
     * 
     * @param listener
     */
    public void removeViewerEventListener(ViewerEventListener listener) {
        eventListenerList.remove(ViewerEventListener.class, listener);
    }

    void fireViewerEvent(ViewerEventObject veo) {
        Object[] listeners = eventListenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ViewerEventListener.class) {
                ((ViewerEventListener) listeners[i + 1]).valueChanged(veo);
            }
        }
    }
}
