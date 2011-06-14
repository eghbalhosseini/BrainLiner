/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.atr.dni.bmi.desktop.timeline;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Font;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import jp.atr.dni.bmi.desktop.model.Channel;
import jp.atr.dni.bmi.desktop.model.Workspace;
import jp.atr.dni.bmi.desktop.neuroshareutils.AnalogInfo;
import jp.atr.dni.bmi.desktop.neuroshareutils.ElemType;
import jp.atr.dni.bmi.desktop.neuroshareutils.Entity;
import jp.atr.dni.bmi.desktop.timeline.model.ViewerChannel;
import jp.atr.dni.bmi.desktop.workingfileutils.CSVReader;
import jp.atr.dni.bmi.desktop.workingfileutils.TSData;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Lookup;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//jp.atr.dni.bmi.desktop.timeline//timeline//EN",
autostore = false)
public final class TimelineTopComponent extends TopComponent implements PropertyChangeListener, GLEventListener/*implements LookupListener */ {

   private boolean SNAP_TO_GRID = false;
   private boolean SHOW_GRID;
   /** the x translation of the map */
   private double translationX = 0;
   /** the y translation of the map */
   private double translationY = 0;
   /** the scale of the map */
   private double scale = 1.0;
   /** the transform for virtual to screen coordinates */
   private AffineTransform transform = new AffineTransform();
   /** the transform for screen to virtual coordinates */
   private AffineTransform inverseTransform = new AffineTransform();
   private GLCanvas glCanvas;
   private GLUT glut;
   private GLU glu;
   private TextRenderer renderer;
   private double compression = 1;
   private static TimelineTopComponent instance;
   /** path to the icon used by the component and its open action */
   static final String ICON_PATH = "jp/atr/dni/bmi/desktop/timeline/graphPrev.png";
   private static final String PREFERRED_ID = "TimelineTopComponent";
   private DoubleBuffer[] colors;
   private Lookup.Result result = null;
   private ModeHandler handler;
   private ArrayList<ViewerChannel> viewerChannels;
   private ArrayList<Date> endTimes;
   private ArrayList<Channel> channels;
   public static final int SCROLLBAR_HEIGHT = 25;
   public static final double INCREMENT = .25;
   private double dataUpperX;
   private double dataUpperY;
   private double dataLowerX;
   private double dataLowerY;
   private Point2D dataLower;
   private Point2D dataUpper;
   private Date startTime;
   private Date endTime;
   private double timespan;
   private int numEntities;
   private double lastY;

   public TimelineTopComponent() {
      initGL();
      SHOW_GRID = true;
      setName(NbBundle.getMessage(TimelineTopComponent.class, "CTL_TimelineTopComponent"));
      setToolTipText(NbBundle.getMessage(TimelineTopComponent.class, "HINT_TimelineTopComponent"));
      setIcon(ImageUtilities.loadImage(ICON_PATH, true));
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {
		
      jLabel1 = new javax.swing.JLabel();
		
      org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(TimelineTopComponent.class, "TimelineTopComponent.jLabel1.text")); // NOI18N
		
      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
										  layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
														.addGap(730, 730, 730)
														.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
														.addContainerGap())
										  );
      layout.setVerticalGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
										);
   }// </editor-fold>//GEN-END:initComponents

   /**
    * code to initialize the openGL timeline
    */
   private void initGL() {
      GLProfile.initSingleton(false);
      GLProfile glp = GLProfile.getDefault();
      GLCapabilities caps = new GLCapabilities(glp);

      scale = .05;

      renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));

      setGlCanvas(new GLCanvas(caps));
      viewerChannels = new ArrayList<ViewerChannel>();

      getGlCanvas().addGLEventListener(this);

      handler = new ModeHandler(this);
      getGlCanvas().addMouseListener(handler);
      getGlCanvas().addMouseMotionListener(handler);
      getGlCanvas().addMouseWheelListener(handler);
      getGlCanvas().addKeyListener(handler);

//      glCanvas.addMouseListener(new MouseListener() {
//
//         @Override
//         public void mouseClicked(MouseEvent me) {
//         }
//
//         @Override
//         public void mousePressed(MouseEvent me) {
//            // update the virtual points
//            screenCurrentPoint = me.getPoint();
//            screenPickedPoint = screenCurrentPoint;
//            currentPoint = getVirtualCoordinates(me.getX(), me.getY());
//            pickedPoint = currentPoint;
//            if (previousPoint == null) {
//               screenPreviousPoint = screenCurrentPoint;
//               previousPoint = currentPoint;
//            }
//         }
//
//         @Override
//         public void mouseReleased(MouseEvent me) {
//         }
//
//         @Override
//         public void mouseEntered(MouseEvent me) {
//         }
//
//         @Override
//         public void mouseExited(MouseEvent me) {
//         }
//      });

//      glCanvas.addMouseMotionListener(new MouseMotionListener() {
//
//         @Override
//         public void mouseDragged(MouseEvent me) {
//            Point2D currentPoint = me.getPoint();// getVirtualCoordinates(arg0.getX(),
//				// arg0.getY());
//            // getVirtualCoordinates(arg0.getX(), arg0.getY());
//
//            // left button performs an action or drags the canvas
//            if ((me.getModifiers() & MouseEvent.BUTTON1_MASK) > 0) {
//               double dx = currentPoint.getX() - previousPoint.getX();
//               double dy = previousPoint.getY() - currentPoint.getY();
//					//            System.out.println("dx: " + dx + "\tdy: " + dy);
//               translationX += dx;
//               translationY += dy;
//
//               previousPoint = me.getPoint();
//
//               buildTransforms();
//            }
//
//            else if ((me.getModifiers() & MouseEvent.BUTTON3_MASK) > 0) {
//
//            }
//         }
//
//         @Override
//         public void mouseMoved(MouseEvent me) {
//            previousPoint = me.getPoint();// getVirtualCoordinates(arg0.getX(), arg0.getY());
//         }
//      });

//      glCanvas.addMouseWheelListener(new MouseWheelListener() {
//
//         @Override
//         public void mouseWheelMoved(MouseWheelEvent mwe) {
//            System.out.println("wheel moved");
//            if (mwe.getWheelRotation() < 0) {
//               setScale(getScale()*SCALE_AMOUNT);
//            }
//            else if (mwe.getWheelRotation() > 0) {
//               setScale(getScale()/SCALE_AMOUNT);
//            }
//         }
//      });

      Animator animator = new Animator(getGlCanvas());
      animator.add(getGlCanvas());
      animator.start();

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(getGlCanvas(), javax.swing.GroupLayout.DEFAULT_SIZE, 1377, Short.MAX_VALUE).addContainerGap()));
      layout.setVerticalGroup(
              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(getGlCanvas(), javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE));

      //add a resize listener
//		      this.addComponentListener(new ComponentAdapter() {
//					public void componentResized(ComponentEvent arg0) {
//
//						// resize about the center of the scene
//						if (size != null) {
//							Dimension newSize = Canvas.this.getSize();
//							translationX += (newSize.width - size.width)/2.0;
//							translationY += (newSize.height - size.height)/2.0;
//						}
//
//						// update the view transforms when the canvas is resized
//						buildTransforms();
//						size = Canvas.this.getSize();
//					}
//				});


   }
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JLabel jLabel1;
   // End of variables declaration//GEN-END:variables

   /**
    * Gets default instance. Do not use directly: reserved for *.settings files only,
    * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
    * To obtain the singleton instance, use {@link #findInstance}.
    */
   public static synchronized TimelineTopComponent getDefault() {
      if (instance == null) {
         instance = new TimelineTopComponent();
      }
      return instance;
   }

   /**
    * Obtain the TimelineTopComponent instance. Never call {@link #getDefault} directly!
    */
   public static synchronized TimelineTopComponent findInstance() {
      TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
      if (win == null) {
         Logger.getLogger(TimelineTopComponent.class.getName()).warning(
                 "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
         return getDefault();
      }
      if (win instanceof TimelineTopComponent) {
         return (TimelineTopComponent) win;
      }
      Logger.getLogger(TimelineTopComponent.class.getName()).warning(
              "There seem to be multiple components with the '" + PREFERRED_ID
              + "' ID. That is a potential source of errors and unexpected behavior.");
      return getDefault();
   }

   @Override
   public int getPersistenceType() {
      return TopComponent.PERSISTENCE_ALWAYS;
   }
   private Lookup.Result fileInfos = null;

   @Override
   public void componentOpened() {
      /*fileInfos = Utilities.actionsGlobalContext().lookupResult(GeneralFileInfo.class);
      //      fileInfos.allItems();  // This means something. THIS IS IMPORTANT.
      fileInfos.addLookupListener(this);*/

      Workspace.addPropertyChangeListener(this);
   }

   @Override
   public void componentClosed() {
      Workspace.removePropertyChangeListener(this);
//      fileInfos.removeLookupListener(this);
//      fileInfos = null;
      //TODO: stop animator
   }

   void writeProperties(java.util.Properties p) {
      // better to version settings since initial version as advocated at
      // http://wiki.apidesign.org/wiki/PropertyFiles
      p.setProperty("version", "1.0");
      // TODO store your settings
   }

   Object readProperties(java.util.Properties p) {
      if (instance == null) {
         instance = this;
      }
      instance.readPropertiesImpl(p);
      return instance;
   }

   private void readPropertiesImpl(java.util.Properties p) {
      String version = p.getProperty("version");
      // TODO read your settings according to their version
   }

   @Override
   protected String preferredID() {
      return PREFERRED_ID;
   }

   @Override
   public void display(GLAutoDrawable drawable) {
//      update();
      render(drawable);
   }

   @Override
   public void dispose(GLAutoDrawable arg0) {
      // TODO Auto-generated method stub
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init function");
      GL2 gl = (GL2) drawable.getGL();
      glu = new GLU();
      glut = new GLUT();

      // set the drawing parameters
      gl.glClearColor(0f, 0f, 0f, 1.0f);
      gl.glPointSize(3.0f);
      gl.glEnable(GL2.GL_LINE_SMOOTH);
      gl.glEnable(GL2.GL_BLEND);
      gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_DONT_CARE);
      gl.glLineWidth(1.5f);
      drawable.getGL().setSwapInterval(1);

      gl.glMatrixMode(GL2.GL_PROJECTION);
      gl.glLoadIdentity();
      buildTransforms();
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = (GL2) drawable.getGL();
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL2.GL_PROJECTION);
      gl.glLoadIdentity();
      glu.gluOrtho2D(0.0, width, height, 0);
   }

   /**
    * Converts screen coordinates to virtual coordinates.
    *
    * @param x - the x component of the screen coordinate
    * @param y - the y component of the screen coordinate
    * @return - the point in virtual coordinates.
    */
   public Point2D getVirtualCoordinates(double x, double y) {
      return inverseTransform.transform(new Point2D.Double(x, y), null);
   }

   /**
    * Converts virtual coordinates to screen coordinates.
    *
    * @param x - the x component of the virtual coordinate
    * @param y - the y component of the virtual coordinate
    * @return - the point in screen coordinates.
    */
   public Point2D getScreenCoordinates(double x, double y) {
      return transform.transform(new Point2D.Double(x, -y), null);
   }

   /**
    * Rebuilds the view transform and the inverse view transforms.
    */
   private void buildTransforms() {

      double width = getWidth();
      double height = getHeight();
      transform = new AffineTransform(1, 0, 0, 1, 0, 0);
      transform.translate(0.5 * width, 0.5 * height);
      transform.scale(scale, scale);
      transform.translate(translationX - width / 2.0, translationY - height / 2.0);

      try {
         inverseTransform = transform.createInverse();
      } catch (Exception e) {
      }
   }

   /**
    * Order to draw:
    * 1) grid
    * 2) data
    * 3) labels
    * 4) timeline
    *
    * @param drawable
    */
   private void render(GLAutoDrawable drawable) {
      int width = getWidth();
      int height = getHeight();

      GL2 gl = drawable.getGL().getGL2();
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

      if (channels == null || endTime == null) {
         return;
      }

      gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glOrtho(0, 640, 480, 0, -1, 1);
//        gl.glMatrixMode(GL2.GL_MODELVIEW);

//      if (SHOW_GRID){
//         gl.glLoadIdentity();
//      }
//      gl.glClearColor( .9f, .9f, .9f, 1.0f );

//      gl.glLoadIdentity();
//      gl.glTranslated(translationX / (width*.5), translationY / (height*.5), 0);
//      gl.glScaled(scale, scale, 0);
//      gl.glLoadIdentity();
//glu.gluOrtho2D (0,
//                 getWidth(),
//                0,
//                getHeight());

//      gl.glViewport(0, 0, width, height);//TODO: look into this some more

      int timeMult = 1000;


      gl.glColor3d(.6, .1, .5);

      numEntities = channels.size();

      //Draw data
      gl.glLineWidth(1);

      gl.glBegin(GL.GL_LINES);

      double yOffset = 0;

      double maxX = 0;

      Point2D minPoint = getVirtualCoordinates(0, 0);
      Point2D maxPoint = getVirtualCoordinates(getWidth(), getHeight());

      //draw data
      for (Channel c : channels) {
         Entity e = c.getEntity();
         if (e.getTag().getElemType() == ElemType.ENTITY_ANALOG) {
            AnalogInfo ai = (AnalogInfo) e;

            if (ai == null) {
               continue;
            }

            double timeIncrement = (1.0 / (ai.getSampleRate()) * timeMult);

            double normalizer = Math.max(Math.abs(ai.getMaxVal()), Math.abs(ai.getMinVal()));
            double subtractor = 0;
            if (ai.getMinVal() > 0) {
               subtractor = ai.getMinVal();
               normalizer -= subtractor;
            } else if (ai.getMaxVal() < 0) {
               subtractor = -ai.getMaxVal();
               normalizer -= subtractor;
            }

            double xVal = 0;

            CSVReader cr = new CSVReader();

            if (c.getChannelType().equals("TS")) {

               // Get TSData from the WorkingFile to display.
               TSData tSData = cr.getTSData(c.getWorkingFilePath());

               ArrayList<Double> vals = tSData.getAllValues().get(0);
               double entityTime = vals.size() / timeIncrement;

               double lastX = 0;
               lastY = ((vals.get(0) - subtractor) / normalizer) - yOffset * 5;

               for (int i = 0; i < vals.size(); i++) {
                  if (i % 2 == 0) {
                     Point2D p = getScreenCoordinates(lastX, lastY);
                     gl.glVertex2d(p.getX(), p.getY());
                  } else {
                     lastY = ((vals.get(i) - subtractor) / normalizer) - yOffset * 5;
                     Point2D p = getScreenCoordinates(xVal, lastY);
                     gl.glVertex2d(p.getX(), p.getY());
                  }
                  lastX = xVal;
                  xVal += timeIncrement;
               }
               if (xVal > maxX) {
                  maxX = xVal;
               }
               yOffset++;
            }


         }
      }
      gl.glEnd();

      lastY = (yOffset - 1) * 5 - 1;

      endTime.setTime(endTime.getTime() + (long) maxX);

      boolean showMin = timespan > 60000, showHour = timespan > 360000;

      width -= SCROLLBAR_HEIGHT;
      height -= SCROLLBAR_HEIGHT;

      //Draw Y-axis labels
      for (int i = 0; i < numEntities; i++) {
         //Draw label
         Point2D p = getScreenCoordinates(0, -5 * i);
         float tSize = (float) (getScale() + (ERROR) * 0.0125f);
         if (tSize > .15) {
            tSize = .15f;
         }
         Entity e = channels.get(i).getEntity();
         String probeInfo = "";
         if (e instanceof AnalogInfo) {

            AnalogInfo aE = (AnalogInfo) e;
            probeInfo = aE.getProbeInfo();
         }
         drawTextUnscaled(gl, e.getEntityInfo().getEntityLabel() + "-" + probeInfo, SCROLLBAR_HEIGHT, p.getY(), tSize, 2.0f);
      }

      //Calculate screen area for data
      dataLower = getScreenCoordinates(0, 0);
      dataUpper = getScreenCoordinates(timespan, -lastY);

      double diffX = dataUpper.getX() - dataLower.getX();
      double diffY = dataUpper.getY() - dataLower.getY();

      double lowerDiffX = Math.abs(SCROLLBAR_HEIGHT - dataLower.getX());
      double upperDiffX = Math.abs(dataUpper.getX() - width);

      dataLowerX = dataLower.getX() < 0 ? lowerDiffX * (width / diffX) + 0 : 0;
      dataUpperX = dataUpper.getX() > width ? width - (upperDiffX * (width / diffX)) : width;

      double lowerDiffY = Math.abs(0 - dataLower.getY());
      double upperDiffY = Math.abs(dataUpper.getY() - height);

      dataLowerY = dataLower.getY() < 0 ? lowerDiffY * (height / diffY) : 0;
      dataUpperY = dataUpper.getY() > height ? height - dataLowerY - upperDiffY * (height / diffY) : height - dataLowerY;

      double incr = Math.abs(getScreenCoordinates(200, 0).getX() - dataLower.getX());
      incr = (200 / incr) * 200;
      if (incr < 1) {
         incr = 1;
      }

      //Draw X-axis labels
      for (int i = 0; i < timespan; i += incr) {
         Point2D p = getScreenCoordinates(i, 0);
         Date currDate = new Date(startTime.getTime() + i);
         String ms = currDate.getTime() + "";
         ms = ms.substring(ms.length() - 3);
         drawTextUnscaled(gl, (showHour ? currDate.getHours() + ":" : "") + (showMin ? currDate.getMinutes() + ":" : "") + currDate.getSeconds() + ":" + ms, p.getX(), height, 0.15f, 2f);
         drawVerticalLine(gl, p.getX());

         if (i + incr >= timespan) {
            i += incr;
            p = getScreenCoordinates(i, 0);
            currDate = new Date(startTime.getTime() + i);
            ms = currDate.getTime() + "";
            ms = ms.substring(ms.length() - 3);
            drawTextUnscaled(gl, (showHour ? currDate.getHours() + ":" : "") + (showMin ? currDate.getMinutes() + ":" : "") + currDate.getSeconds() + ":" + ms, p.getX(), height, 0.15f, 2f);
            drawVerticalLine(gl, p.getX());
            break;
         }
      }

      //Draw bottom time scroller
      drawHorizontalTimelineScroller(gl, dataLowerX + SCROLLBAR_HEIGHT, height, dataUpperX - dataLowerX);
      drawVerticalTimelineScroller(gl, SCROLLBAR_HEIGHT, dataLowerY, dataUpperY);
   }

   public void drawVerticalLine(GL2 gl, double x) {
      gl.glPushMatrix();
      gl.glLineWidth(1);
      gl.glTranslated(x, 0, 0);
      gl.glColor4f(1, 0, 0, .6f);
      gl.glBegin(GL2.GL_LINES);
      gl.glVertex2d(0, 0);
      gl.glVertex2d(0, getHeight());
      gl.glEnd();
      gl.glPopMatrix();
   }

   public void drawTextUnscaled(GL2 gl, String text, double x, double y, float size, float width) {
      gl.glPushMatrix();
      gl.glTranslated(x, y, 0);
      gl.glScalef(size, -size, 0.0f);
      gl.glLineWidth((float) (width));
      glut.glutStrokeString(GLUT.STROKE_ROMAN, text);
      gl.glPopMatrix();
   }

   /**
    * Utility function for drawing text
    *
    * @param gl - the JOGL context
    * @param text 0 the text to draw
    * @param x - the x position
    * @param y - the y position
    * @param size - the size of the text
    * @param width - the width of the letters
    */
   public void drawText(GL2 gl, String text, double x, double y, float size, float width) {
//      gl.glPushMatrix();
//gl.glTranslated(x, y, 0);
//        gl.glScalef(size, size, 0.0f);
//      renderer.beginRendering(glCanvas.getWidth(), glCanvas.getHeight());
//    // optionally set the color
//    renderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
//    renderer.draw("Text to draw", x, y);
//    // ... more draw commands, color changes, etc.
//    renderer.endRendering();
//    gl.glPopMatrix();

      gl.glPushMatrix();
      gl.glTranslated(x, y, 0);
      size = (float) (2f * (scale * size));
//      gl.glRotatef(angle, 0, 0, 1);
      gl.glScalef(size, -size, 0.0f);
      gl.glLineWidth(width);
      glut.glutStrokeString(GLUT.STROKE_ROMAN, text);
      gl.glPopMatrix();
   }

   public void drawHorizontalTimelineScroller(GL2 gl, double x, double y, double x2) {
      gl.glPushMatrix();
      gl.glTranslated(x, y, 0);
      gl.glColor4f(1, 0, 0, .6f);
      gl.glBegin(GL2.GL_QUADS);
      gl.glVertex3d(0, SCROLLBAR_HEIGHT, 0);
      gl.glVertex3d(0, -1, 0);
      gl.glVertex3d(x2, -1, 0);
      gl.glVertex3d(x2, SCROLLBAR_HEIGHT, 0);
      gl.glEnd();
      gl.glPopMatrix();
   }

   public void drawVerticalTimelineScroller(GL2 gl, double x, double y, double y2) {
      gl.glPushMatrix();
      gl.glTranslated(x, y, 0);
      gl.glColor4f(1, 0, 0, .6f);
      gl.glBegin(GL2.GL_QUADS);
      gl.glVertex3d(-SCROLLBAR_HEIGHT, 0, 0);
      gl.glVertex3d(-1, 0, 0);
      gl.glVertex3d(-1, y2, 0);
      gl.glVertex3d(-SCROLLBAR_HEIGHT, y2, 0);
      gl.glEnd();
      gl.glPopMatrix();
   }

   /**
    * Zooms to the boundries of the current map.
    */
   public void zoomAll() {

      // reset the view transform
      translationX = 0;
      translationY = 0;
      scale = 0.5;

      // zoom to the current map
      boolean zoom = false;
      double minX = Double.MAX_VALUE;
      double maxX = -Double.MAX_VALUE;
      double minY = Double.MAX_VALUE;
      double maxY = -Double.MAX_VALUE;

      for (ViewerChannel object : viewerChannels) {
//			if (object.getGraphics() != null) {
//				float[] coords = object.getGraphics().getCoordinates();
//				for (int i=0; i<coords.length; i+=2) {
//					if (coords[i] < minX) minX = coords[i];
//					if (coords[i] > maxX) maxX = coords[i];
//					if (coords[i + 1] < minY) minY = coords[i + 1];
//					if (coords[i + 1] > maxY) maxY = coords[i + 1];
//				}
//
//				zoom = true;
//			}
      }

      if (zoom) {
         translationX = -(maxX + minX) / 2 + getWidth() / 2;
         translationY = -(maxY + minY) / 2 + getHeight() / 2;

         double dx = maxX - minX;
         dx = Math.max(dx, maxY - minY);
         scale = getHeight() / dx;
      }

      buildTransforms();
   }

   /**
    * Returns the object at the specified virtual coordiantes or null if no
    * object is located at the specified coordinates. If two objects are
    * located at the specified point, then the first object is always
    * returned.
    *
    * The method first checks for objects using the shape interface. However,
    * the interface does not allow for the selection of lines. If no object
    * is found, then the closest object within a small radius is returned.
    *
    * @param x - the x value in virutal coordinates
    * @param y - the y value in virtual coordinates
    * @return - the object or null if no object is picked
    */
   public ViewerChannel getPickedObject(double x, double y, boolean toggleSelected) {

      // sort the visible objects from highest depth to lowest depth
      TreeSet<ViewerChannel> objects = new TreeSet<ViewerChannel>(new Comparator<ViewerChannel>() {

         public int compare(ViewerChannel arg0, ViewerChannel arg1) {
            int depth1 = arg0.getDepth();
            int depth2 = arg1.getDepth();

            if (depth2 != depth1) {
               return depth2 - depth1;
            } else {
               return arg1.hashCode() - arg0.hashCode();
            }
         }
      });

      for (ViewerChannel object : viewerChannels) {
         objects.add(object);
      }

      // check for selection using shapes
      ViewerChannel selected = null;
      for (ViewerChannel object : objects) {
//			if (object.getGraphics().getBounds2D().contains(x, y)) {
//				if (object.getGraphics().getFillColor() != null && object.getGraphics().contains(x, y)) {
//					if (object.equals(modeHandler.getSelectedObject())) {
//						selected = object;
//						if (toggleSelected == false) {
//							return selected;
//						}
//					}
//					else {
//						return object;
//					}
//				}
//			}
      }

      if (selected != null) {
         return selected;
      }

      // find the closest line
      double pickTolerance = 10.0 / scale;
      double closest = pickTolerance;
      selected = null;

//		for (ViewerChannel object : viewerChannels) {
//			Rectangle2D bounds = object.getGraphics().getBounds2D();
//			if (x > (bounds.getMinX() - pickTolerance)
//					&& x < (bounds.getMaxX() + pickTolerance)
//					&& y > (bounds.getMinY() - pickTolerance)
//					&& y < (bounds.getMaxY() + pickTolerance)) {
//
//				float[] coords = object.getGraphics().getCoordinates();
//				float px = coords[0];
//				float py = coords[1];
//					for (int index=2; index<coords.length; index += 2) {
//					float nx = coords[index%coords.length];
//					float ny = coords[(index + 1)%coords.length];
//
//					Line2D line = new Line2D.Float(px, py, nx, ny);
//					double dist = line.ptSegDist(new Point2D.Double(x, y));
//					if (dist < closest) {
//						closest = dist;
//						selected = object;
//					}
//
//					px = nx;
//					py = ny;
//				}
//			}
//		}

      return selected;
   }

   /**
    * Gets the tranlation in the x direction.
    */
   public double getTranslationX() {
      return translationX;
   }

   /**
    * Sets the translation in the x direction and rebuilds the transforms.
    */
   public void setTranslationX(double translationX) {
      this.translationX = translationX;
      buildTransforms();
   }

   /**
    * Gets the tranlation in the y direction.
    */
   public double getTranslationY() {
      return translationY;
   }

   /**
    * Sets the translation in the y direction and rebuilds the transforms.
    */
   public void setTranslationY(double translationY) {
      this.translationY = translationY;
      buildTransforms();
   }

   /**
    * Gets the scale scale.
    */
   public double getScale() {
      return scale;
   }

   /**
    * Sets the scale and rebuilds the affine transforms.
    */
   public void setScale(double scale) {
//		if (scale < 0.008 || scale > .2) {
//			return;
//		}

      this.scale = scale;
      buildTransforms();
   }

   /* @Override
   public void resultChanged(LookupEvent le) {
   //     System.out.println("change");
   
   GeneralFileInfo obj = Utilities.actionsGlobalContext().lookup(GeneralFileInfo.class);
   
   if (obj != null && obj.getFileExtention().equals("nsn")) {
   //               System.out.println("adding data");
   fileInfo = obj;
   NSReader reader = new NSReader();
   NeuroshareFile nsn = reader.readNSFileAllData(fileInfo.getFilePath());
   fileInfo.setNsObj(nsn);
   }
   }*/
   /**
    * @return the glCanvas
    */
   public GLCanvas getGlCanvas() {
      return glCanvas;
   }

   /**
    * @param glCanvas the glCanvas to set
    */
   public void setGlCanvas(GLCanvas glCanvas) {
      this.glCanvas = glCanvas;
   }

   /**
    * @return the dataUpperX
    */
   public double getDataUpperX() {
      return dataUpperX;
   }

   /**
    * @param dataUpperX the dataUpperX to set
    */
   public void setDataUpperX(double dataUpperX) {
      this.dataUpperX = dataUpperX;
   }

   /**
    * @return the dataUpperY
    */
   public double getDataUpperY() {
      return dataUpperY;
   }

   /**
    * @param dataUpperY the dataUpperY to set
    */
   public void setDataUpperY(double dataUpperY) {
      this.dataUpperY = dataUpperY;
   }

   /**
    * @return the dataLowerX
    */
   public double getDataLowerX() {
      return dataLowerX;
   }

   /**
    * @param dataLowerX the dataLowerX to set
    */
   public void setDataLowerX(double dataLowerX) {
      this.dataLowerX = dataLowerX;
   }

   /**
    * @return the dataLowerY
    */
   public double getDataLowerY() {
      return dataLowerY;
   }

   /**
    * @param dataLowerY the dataLowerY to set
    */
   public void setDataLowerY(double dataLowerY) {
      this.dataLowerY = dataLowerY;
   }

   /**
    * @return the dataLower
    */
   public Point2D getDataLower() {
      return dataLower;
   }

   /**
    * @param dataLower the dataLower to set
    */
   public void setDataLower(Point2D dataLower) {
      this.dataLower = dataLower;
   }

   /**
    * @return the dataUpper
    */
   public Point2D getDataUpper() {
      return dataUpper;
   }

   /**
    * @param dataUpper the dataUpper to set
    */
   public void setDataUpper(Point2D dataUpper) {
      this.dataUpper = dataUpper;
   }

   @Override
   public void propertyChange(PropertyChangeEvent pce) {
      channels = Workspace.getChannels();
      endTimes = new ArrayList<Date>();

      numEntities = channels.size();

      //XXX: because the workspace channels are static, we will get a concurrent modification error 
      //here if something changes. The API needs to be changed to prevent this.
      for (Channel c : channels) {
         Date end = new Date((long) c.getTSHeader().getSamplingRate_Hz() * c.getEntity().getEntityInfo().getItemCount());
         endTimes.add(end);
      }

      for (Date e : endTimes) {
         if (endTime == null) {
            endTime = e;
         } else if (e.getTime() > endTime.getTime()) {
            endTime = e;
         }
      }

      startTime = new Date(0);
//      startTime.setYear((int) nsfi.getYear());
//      startTime.setMonth((int) (nsfi.getMonth() - 1));
//      startTime.setDate((int) nsfi.getDayOfMonth());
//      startTime.setHours((int) nsfi.getHourOfDay());
//      startTime.setMinutes((int) nsfi.getMinOfDay());
//      startTime.setSeconds((int) nsfi.getSecOfDay());
//      startTime.setTime(startTime.getTime() + nsfi.getMilliSecOfDay());

      endTime.setTime(startTime.getTime());
      timespan = endTime.getTime() - startTime.getTime();

      lastY = 0;
   }
}