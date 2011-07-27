
/*
 * ChannelSelector.java
 *
 * Created on 2011/02/23, 11:17:50
 */
package jp.atr.dni.bmi.desktop.explorereditor;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import jp.atr.dni.bmi.desktop.model.FileType;
import jp.atr.dni.bmi.desktop.model.GeneralFileInfo;
import jp.atr.dni.bmi.desktop.model.api.data.APIList;
import jp.atr.dni.bmi.desktop.model.api.AnalogChannel;
import jp.atr.dni.bmi.desktop.model.api.Channel;
import jp.atr.dni.bmi.desktop.model.api.ChannelType;
import jp.atr.dni.bmi.desktop.model.api.EventChannel;
import jp.atr.dni.bmi.desktop.model.api.NeuralSpikeChannel;
import jp.atr.dni.bmi.desktop.model.api.SegmentChannel;
import jp.atr.dni.bmi.desktop.model.api.Workspace;
import jp.atr.dni.bmi.desktop.model.api.data.NSNSegmentSource;
import jp.atr.dni.bmi.desktop.neuroshareutils.AnalogInfo;

import jp.atr.dni.bmi.desktop.neuroshareutils.CSVReader;
import jp.atr.dni.bmi.desktop.neuroshareutils.Entity;
import jp.atr.dni.bmi.desktop.neuroshareutils.EntityType;
import jp.atr.dni.bmi.desktop.neuroshareutils.EventInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.NeuralInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.readers.NSReader;
import jp.atr.dni.bmi.desktop.neuroshareutils.NeuroshareFile;
import jp.atr.dni.bmi.desktop.neuroshareutils.readers.NevReader;
import jp.atr.dni.bmi.desktop.neuroshareutils.readers.NSXReader;
import jp.atr.dni.bmi.desktop.neuroshareutils.readers.PlxReader;
import jp.atr.dni.bmi.desktop.neuroshareutils.SegmentInfo;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author Keiji Harada [*1]</br>[*1] ATR Intl. Computational Neuroscience Labs, Decoding Group
 * @version 2011/04/22
 */
public class ChannelSelector extends javax.swing.JPanel implements ActionListener {

   // Define Lists.
   private DefaultListModel unAvailableChannelList;
   private DefaultListModel availableChannelList;
   private DefaultListModel selectedChannelList;
   private DialogDescriptor dialogDescriptor;
   private Dialog dialog;
   private GeneralFileInfo generalFileInfo;
   private boolean dataFileFlag = false;
   private Workspace workspace;

   /** Creates new form ChannelSelector
    * @param gfi
    */
   public ChannelSelector(GeneralFileInfo gfi) {

      beforeInitComponents(gfi);
      initComponents();
      afterInitComponents();
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {
      java.awt.GridBagConstraints gridBagConstraints;

      jScrollPane3 = new javax.swing.JScrollPane();
      jList3 = new javax.swing.JList();
      jScrollPane4 = new javax.swing.JScrollPane();
      jList4 = new javax.swing.JList();
      jScrollPane5 = new javax.swing.JScrollPane();
      jList5 = new javax.swing.JList();
      jPanel1 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jTextField1 = new javax.swing.JTextField();
      jPanel4 = new javax.swing.JPanel();
      jPanel7 = new javax.swing.JPanel();
      jScrollPane2 = new javax.swing.JScrollPane();
      jList2 = new javax.swing.JList();
      jPanel8 = new javax.swing.JPanel();
      jButton4 = new javax.swing.JButton();
      jButton3 = new javax.swing.JButton();
      jPanel3 = new javax.swing.JPanel();
      jPanel5 = new javax.swing.JPanel();
      jButton1 = new javax.swing.JButton();
      jButton2 = new javax.swing.JButton();
      jPanel6 = new javax.swing.JPanel();
      jScrollPane6 = new javax.swing.JScrollPane();
      jList6 = new javax.swing.JList();
      jScrollPane1 = new javax.swing.JScrollPane();
      jList1 = new javax.swing.JList();
      jPanel2 = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();

      jList3.setModel(new javax.swing.AbstractListModel() {
         String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
         public int getSize() { return strings.length; }
         public Object getElementAt(int i) { return strings[i]; }
      });
      jScrollPane3.setViewportView(jList3);

      jList4.setModel(new javax.swing.AbstractListModel() {
         String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
         public int getSize() { return strings.length; }
         public Object getElementAt(int i) { return strings[i]; }
      });
      jScrollPane4.setViewportView(jList4);

      jList5.setModel(new javax.swing.AbstractListModel() {
         String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
         public int getSize() { return strings.length; }
         public Object getElementAt(int i) { return strings[i]; }
      });
      jScrollPane5.setViewportView(jList5);

      setLayout(new java.awt.GridBagLayout());

      jLabel1.setText(org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jLabel1.text")); // NOI18N

      jTextField1.setText(this.generalFileInfo.getFilePath());

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
            .addContainerGap())
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel1))
            .addContainerGap(47, Short.MAX_VALUE))
      );

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 2.0;
      gridBagConstraints.weighty = 1.0;
      add(jPanel1, gridBagConstraints);

      jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jPanel4.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP)); // NOI18N
      jPanel4.setLayout(new java.awt.GridBagLayout());

      jPanel7.setLayout(new java.awt.GridBagLayout());

      jList2.setModel(selectedChannelList);
      jScrollPane2.setViewportView(jList2);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      jPanel7.add(jScrollPane2, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 40.0;
      jPanel4.add(jPanel7, gridBagConstraints);

      jButton4.setText(org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jButton4.text")); // NOI18N
      jButton4.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton4ActionPerformed(evt);
         }
      });

      jButton3.setText(org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jButton3.text")); // NOI18N
      jButton3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
      jPanel8.setLayout(jPanel8Layout);
      jPanel8Layout.setHorizontalGroup(
         jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel8Layout.createSequentialGroup()
            .addComponent(jButton3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton4)
            .addContainerGap(114, Short.MAX_VALUE))
      );
      jPanel8Layout.setVerticalGroup(
         jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jButton3)
            .addComponent(jButton4))
      );

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      jPanel4.add(jPanel8, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 9.0;
      add(jPanel4, gridBagConstraints);

      jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jPanel3.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP)); // NOI18N
      jPanel3.setLayout(new java.awt.GridBagLayout());

      jButton1.setText(org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jButton1.text")); // NOI18N
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
         }
      });

      jButton2.setText(org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jButton2.text")); // NOI18N
      jButton2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
            .addContainerGap(114, Short.MAX_VALUE)
            .addComponent(jButton1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton2))
      );
      jPanel5Layout.setVerticalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jButton2)
            .addComponent(jButton1))
      );

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      jPanel3.add(jPanel5, gridBagConstraints);

      jPanel6.setLayout(new java.awt.GridBagLayout());

      jList6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jList6.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP)); // NOI18N
      jList6.setForeground(new java.awt.Color(153, 153, 153));
      jList6.setModel(unAvailableChannelList);
      jList6.setValueIsAdjusting(true);
      jList6.setVisibleRowCount(3);
      jScrollPane6.setViewportView(jList6);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      jPanel6.add(jScrollPane6, gridBagConstraints);

      jList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jList1.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP)); // NOI18N
      jList1.setModel(availableChannelList);
      jList1.setValueIsAdjusting(true);
      jList1.setVisibleRowCount(13);
      jScrollPane1.setViewportView(jList1);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      jPanel6.add(jScrollPane1, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 40.0;
      jPanel3.add(jPanel6, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 9.0;
      add(jPanel3, gridBagConstraints);

      jLabel2.setForeground(new java.awt.Color(0, 102, 255));
      jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel2.setText(org.openide.util.NbBundle.getMessage(ChannelSelector.class, "ChannelSelector.jLabel2.text")); // NOI18N

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
      );

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 2.0;
      gridBagConstraints.weighty = 1.0;
      add(jPanel2, gridBagConstraints);
   }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       // Set all channels as Selected.
       if (this.availableChannelList.isEmpty()) {
          return;
       }

       ArrayList<Channel> tmpChannelList = new ArrayList<Channel>();
       for (int i = 0; i < this.availableChannelList.getSize(); i++) {
          tmpChannelList.add((Channel) this.availableChannelList.getElementAt(i));
       }

       for (int j = 0; j < tmpChannelList.size(); j++) {
          this.moveChannelToSelected(tmpChannelList.get(j));
       }

       jList1.repaint();
       jList2.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       // Set chosen channel as Selected.
       Object[] chosen = jList1.getSelectedValues();
       if (chosen == null) {
          // Any channel is not chosen.
          return;
       }

       for (int i = 0; i < chosen.length; i++) {
          Channel ch = (Channel) chosen[i];
          this.moveChannelToSelected(ch);
       }

       jList1.repaint();
       jList2.repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       // Set chosen channel as Unselected.
       Object[] chosen = jList2.getSelectedValues();
       if (chosen == null) {
          // Any channel is not chosen.
          return;
       }

       for (int i = 0; i < chosen.length; i++) {
          Channel ch = (Channel) chosen[i];
          this.moveChannelToUnSelected(ch);
       }

       jList1.repaint();
       jList2.repaint();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       // Set all channels as Unselected.
       if (this.selectedChannelList.isEmpty()) {
          return;
       }

       ArrayList<Channel> tmpChannelList = new ArrayList<Channel>();
       for (int i = 0; i < this.selectedChannelList.getSize(); i++) {
          tmpChannelList.add((Channel) this.selectedChannelList.getElementAt(i));
       }

       for (int j = 0; j < tmpChannelList.size(); j++) {
          this.moveChannelToUnSelected(tmpChannelList.get(j));
       }

       jList1.repaint();
       jList2.repaint();
    }//GEN-LAST:event_jButton4ActionPerformed
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JButton jButton2;
   private javax.swing.JButton jButton3;
   private javax.swing.JButton jButton4;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JList jList1;
   private javax.swing.JList jList2;
   private javax.swing.JList jList3;
   private javax.swing.JList jList4;
   private javax.swing.JList jList5;
   private javax.swing.JList jList6;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel5;
   private javax.swing.JPanel jPanel6;
   private javax.swing.JPanel jPanel7;
   private javax.swing.JPanel jPanel8;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JScrollPane jScrollPane4;
   private javax.swing.JScrollPane jScrollPane5;
   private javax.swing.JScrollPane jScrollPane6;
   private javax.swing.JTextField jTextField1;
   // End of variables declaration//GEN-END:variables

   /**
    *
    * @param e
    */
   @Override
   public void actionPerformed(ActionEvent e) {

      if (e.getSource() == DialogDescriptor.OK_OPTION && this.selectedChannelList.size() > 0) {
         Channel[] channels = new Channel[selectedChannelList.size()];
         // Add Channels to the workspace.
         for (int i = 0; i < this.selectedChannelList.size(); i++) {
            Object obj = this.selectedChannelList.get(i);
            Channel ch = (Channel) obj;


            // Delete multiple SegSourceInfos.(i.e. Application retains only first SegSourceInfo.)
            if (ch.getType() == ChannelType.SEGMENT) {//TI
               // Segment Entity.
               SegmentChannel segChannel = (SegmentChannel) ch;
//               APIList<NSNSegmentSource> segSourceInfos = segChannel.getSegmentSources();
               int size = (int) segChannel.numSegSources();
               if (size >= 2) {
                  for (int j = 1; j < size; j++) {
                     segChannel.removeSegmentSource(j);
//                     segSourceInfos.remove(segSourceInfos.size() - 1);
                  }
//                        segChannel.setSegmentSources(segSourceInfos);//does nothing since the list has already been modified

//                  ch.setEntity((Entity) si);

                  JOptionPane.showMessageDialog(null, "Channel: " + ch.getLabel() + " - Extra ns_SegSourceInfos were skipped!");
               }
//               segChannel.setSegmentSources(segSourceInfos);
            }
            channels[i] = ch;
         }
         if (workspace == null) {
            workspace = Lookup.getDefault().lookup(Workspace.class);
         }
         workspace.addChannels(channels);
      }
   }

   private void beforeInitComponents(GeneralFileInfo generalFileInfo) {
      // Define settings about components.
      this.generalFileInfo = generalFileInfo;
      this.unAvailableChannelList = new DefaultListModel();
      this.availableChannelList = new DefaultListModel();
      this.selectedChannelList = new DefaultListModel();
   }

   private void afterInitComponents() {
      // Set values about components.
      this.dialogDescriptor = new DialogDescriptor(this, "Channel Selecter", true, this);
      // Case : Neuroshare(nsn), Plexon(plx), BlackRockMicroSystems(nev,ns1-9)
      FileType fileType = this.generalFileInfo.getFileType();
      if (FileType.isDataFile(fileType)) {
         this.dataFileFlag = true;

         // Get nsObj to set channel list.
         NeuroshareFile nsf = this.generalFileInfo.getNsObj();
         if (nsf == null) {
            if (fileType == FileType.NSN) {
               // Get nsObj if unload.
               NSReader nsr = new NSReader();
               nsf = nsr.readNSFileOnlyInfo(this.generalFileInfo.getFilePath());
               this.generalFileInfo.setNsObj(nsf);
            } else if (fileType == FileType.PLX) {
               // Get nsObj from plx file.
               PlxReader plr = new PlxReader();
               nsf = plr.readPlxFileOnlyInfo(this.generalFileInfo.getFilePath());
               this.generalFileInfo.setNsObj(nsf);
            } else if (fileType == FileType.NEV) {
               // Get nsObj from nev file.
               NevReader nevr = new NevReader();
               nsf = nevr.readNevFileOnlyInfo(this.generalFileInfo.getFilePath());
               this.generalFileInfo.setNsObj(nsf);
            } else if (fileType == FileType.NSX) {
               // Get nsObj from ns1-ns9 file.
               NSXReader nsxr = new NSXReader();
               nsf = nsxr.readNsxFileAllData(this.generalFileInfo.getFilePath());
               this.generalFileInfo.setNsObj(nsf);
            } else if (fileType == FileType.CSV) {
               // Get nsObj from csv file.
               CSVReader csvr = new CSVReader();
               nsf = csvr.readCsvFileAllData(this.generalFileInfo.getFilePath());
               this.generalFileInfo.setNsObj(nsf);
            }
         }

         this.unAvailableChannelList.removeAllElements();
         this.availableChannelList.removeAllElements();
         this.selectedChannelList.removeAllElements();

         if (nsf != null) {
            // Add channels to availableChannelList.
            for (int i = 0; i < nsf.getEntities().size(); i++) {

               // If record did not exist, then it will not register the entity as channel.
               Entity e = nsf.getEntities().get(i);
               EntityType eType = e.getTag().getEntityType();

               Channel ch = null;
               if (eType == EntityType.ENTITY_ANALOG) {
                  ch = new AnalogChannel(i, (AnalogInfo) e);
               } else if (eType == EntityType.ENTITY_EVENT) {
                  ch = new EventChannel(i, (EventInfo) e);
               } else if (eType == EntityType.ENTITY_NEURAL) {
                  ch = new NeuralSpikeChannel(i, (NeuralInfo) e);
               } else if (eType == EntityType.ENTITY_SEGMENT) {
                  ch = new SegmentChannel(i, (SegmentInfo) e);
               }

               if (e.getEntityInfo().getItemCount() <= 0) {
                  this.unAvailableChannelList.addElement(ch);
                  continue;
               }
               if (ch != null) {
                  this.availableChannelList.addElement(ch);
               }
            }
         }

      } else {
         this.dataFileFlag = false;
         JOptionPane.showMessageDialog(null, "It is not a data file.");
      }
   }

   /**
    *
    */
   public void showDialog() {
      if (this.dataFileFlag) {
         this.dialog = DialogDisplayer.getDefault().createDialog(this.dialogDescriptor);
         this.dialog.setModal(true);
         this.dialog.pack();
         this.dialog.setVisible(true);
      }
   }

   private void moveChannelToSelected(Channel ch) {
      if (this.selectedChannelList.isEmpty()) {
         // Add to First.
         this.selectedChannelList.addElement(ch);
      } else {
         int selectedChannelListSize = this.selectedChannelList.getSize();
         for (int ii = 0; ii < selectedChannelListSize; ii++) {
            Channel search = (Channel) this.selectedChannelList.getElementAt(ii);
            int diff = ch.getId() - search.getId();

            if (diff < 0) {
               // Insert at ii.
               this.selectedChannelList.insertElementAt(ch, ii);
               break;
            }
            if (selectedChannelListSize <= ii + 1) {
               // Add to Last.
               this.selectedChannelList.addElement(ch);
               break;
            }
         }
      }
      this.availableChannelList.removeElement(ch);
   }

   private void moveChannelToUnSelected(Channel ch) {
      if (this.availableChannelList.isEmpty()) {
         // Add to First.
         this.availableChannelList.addElement(ch);
      } else {
         int unSelectedChannelListSize = this.availableChannelList.getSize();
         for (int i = 0; i < unSelectedChannelListSize; i++) {
            Channel search = (Channel) this.availableChannelList.getElementAt(i);
            int diff = ch.getId() - search.getId();

            if (diff < 0) {
               // Insert at ii.
               this.availableChannelList.insertElementAt(ch, i);
               break;
            }
            if (unSelectedChannelListSize <= i + 1) {
               // Add to Last.
               this.availableChannelList.addElement(ch);
               break;
            }
         }
      }
      this.selectedChannelList.removeElement(ch);
   }
}
