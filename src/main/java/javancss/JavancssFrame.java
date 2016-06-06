/*
Copyright (C) 2014 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS
(http://javancss.codehaus.org/).

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA*/

package javancss;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

import javax.help.*;
import javax.swing.*;
import javax.swing.border.*;

import ccl.swing.AboutDialog;
import ccl.swing.AnimationPanel;
import ccl.util.Init;

/**
 * Main class used to start JavaNCSS in GUI mode from other
 * java applications. To start JavaNCSS from the command line,
 * gui mode or not, class 'Main' is used.
 *
 * @author  <a href="http://www.kclee.de/clemens/">Chr. Clemens Lee</a> (<a href="mailto:clemens@kclee.com"><i>clemens@kclee.com</i></a>)
 * @version $Id$
 */
public class JavancssFrame extends JFrame {
    public static final String S_PACKAGES = "Packages";
    public static final String S_CLASSES = "Classes";
    public static final String S_METHODS = "Methods";

    private int _oldThreadPriority = -1;

    private AnimationPanel _pAnimationPanel = null;

    private JTextArea _txtPackage;
    private JTextArea _txtObject;
    private JTextArea _txtFunction;
    private JTextArea _txtError;

    private JTabbedPane _pTabbedPane = null;

    private Font pFont = new Font("Monospaced", Font.PLAIN, 12);

    private boolean _bNoError = true;

    private String _sProjectName = null;
    private String _sProjectPath = null;

    public void save() {
        File targetDirectory = new File(_sProjectPath);
        File packagesFile = new File( targetDirectory, _sProjectName.toLowerCase() + ".packages.txt" );
        File classesFile  = new File( targetDirectory, _sProjectName.toLowerCase() + ".classes.txt" );
        File methodsFile  = new File( targetDirectory, _sProjectName.toLowerCase() + ".methods.txt" );

        String sSuccessMessage = "Data appended successfully to the following files:";

        try {
            appendFile( packagesFile, _txtPackage.getText() );
            sSuccessMessage += "\n" + packagesFile;
        } catch(Exception e) {
            JOptionPane.showMessageDialog( this, "Could not append to file '" + classesFile + "'.\n" + e, "Error", JOptionPane.ERROR_MESSAGE );
        }

        try {
            appendFile( classesFile, _txtObject.getText() );
            sSuccessMessage += "\n" + classesFile;
        } catch(Exception e) {
            JOptionPane.showMessageDialog( this, "Could not append to file '" + classesFile + "'.\n" + e, "Error", JOptionPane.ERROR_MESSAGE );
        }

        try {
            appendFile( methodsFile, _txtFunction.getText() );
            sSuccessMessage += "\n" + methodsFile;
        } catch(Exception e) {
            JOptionPane.showMessageDialog( this, "Could not append to file '" + methodsFile + "'.\n" + e, "Error", JOptionPane.ERROR_MESSAGE );
        }

        JOptionPane.showMessageDialog( this, sSuccessMessage, "Message", JOptionPane.INFORMATION_MESSAGE );
    }

    public static void appendFile( File file, String content ) throws IOException
    {
        FileWriter writer = new FileWriter( file, true );
        writer.write( content );
        writer.close();
    }

    public JavancssFrame(Init pInit_) {
        super( "JavaNCSS: " + pInit_.getFileName() );

        super.setBackground( pInit_.getBackground() );

        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setIconImage( new ImageIcon( getClass().getClassLoader().getResource( "javancss/javancssframe.gif" ) ).getImage() );

        _sProjectName = pInit_.getFileName();
        _sProjectPath = pInit_.getFilePath();
        if ( _sProjectName == null || _sProjectName.trim().length() == 0 )
        {
            _sProjectName = pInit_.getApplicationName();
            _sProjectPath = pInit_.getApplicationPath();
        }

        createMenuBar();

        GridBagLayout layout = new GridBagLayout();

        getContentPane().setLayout(layout);

        Image image = Toolkit.getDefaultToolkit().getImage( AnimationPanel.class.getResource( "anim_recycle_brown.gif" ) );
        _pAnimationPanel = new AnimationPanel( image, 350 );

        JPanel pPanel = new JPanel();
        pPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        pPanel.add(_pAnimationPanel, BorderLayout.CENTER);

        getContentPane().add(pPanel);


        pack();
        setSize(640, 480);
        
        // center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( ( screenSize.width - getWidth() ) / 2, ( screenSize.height - getHeight() ) / 2 );
    }

    private void createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        menubar.add( createFileMenu() );
        menubar.add( createHelpMenu() );

        setJMenuBar( menubar );
    }

    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu( "File" );

        JMenuItem item = new JMenuItem( "Save" );
        item.setAccelerator( KeyStroke.getKeyStroke( 'S', InputEvent.CTRL_MASK ) );
        item.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent event )
            {
                save();
            }
        } );
        menu.add( item );

        item = new JMenuItem( "Exit" );
        item.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent event )
            {
                dispose();
            }
        } );

        menu.add( item );

        return menu;
    }

    private JMenu createHelpMenu()
    {
        JMenu menu = new JMenu( "Help" );

        JMenuItem item = new JMenuItem( "Contents..." , 'C');
        try
        {
            HelpBroker broker = new HelpSet( null, getClass().getClassLoader().getResource( "javancss/javancss.hs" ) ).createHelpBroker();
            item.addActionListener( new CSH.DisplayHelpFromSource( broker ) );
        }
        catch ( HelpSetException e )
        {
            e.printStackTrace();
        }
        
        menu.add( item );
        menu.addSeparator();
        
        item = new JMenuItem( "About..." );
        item.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                AboutDialog dlgAbout = new AboutDialog( JavancssFrame.this, "Chr. Clemens Lee", javancss.Main.S_RCS_HEADER );
                dlgAbout.dispose();
                requestFocus();
            }
        } );
        
        menu.add( item );
        
        return menu;
    }

    public void showJavancss(Javancss pJavancss_) throws IOException {
        if (_oldThreadPriority != -1) {
            Thread.currentThread().setPriority(_oldThreadPriority);
            _pAnimationPanel.stop();
        }
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        _bNoError = true;
        if (pJavancss_.getLastErrorMessage() != null && pJavancss_.getNcss() <= 0) {
            _bNoError = false;
            JTextArea txtError = new JTextArea();
            String sError = "Error in Javancss: " +
                   pJavancss_.getLastErrorMessage();
            txtError.setText(sError);
            JScrollPane jspError = new JScrollPane(txtError);
            getContentPane().add(jspError, BorderLayout.CENTER);
        } else {
            JPanel pPanel = new JPanel(true);
            pPanel.setLayout(new BorderLayout());
            _pTabbedPane = new JTabbedPane();
            _pTabbedPane.setDoubleBuffered(true);

            _txtPackage = new JTextArea();
            _txtPackage.setFont(pFont);
            JScrollPane jspPackage = new JScrollPane(_txtPackage);
            int inset = 5;
            jspPackage.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) );
            _pTabbedPane.addTab("Packages", null, jspPackage);

            _txtObject = new JTextArea();
            _txtObject.setFont(pFont);
            JScrollPane jspObject = new JScrollPane(_txtObject);
            jspObject.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) );
            _pTabbedPane.addTab("Classes", null, jspObject);

            _txtFunction = new JTextArea();
            _txtFunction.setFont(pFont);
            JScrollPane jspFunction = new JScrollPane(_txtFunction);
            jspFunction.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) );
            _pTabbedPane.addTab("Methods", null, jspFunction);

            // date and time
            String sTimeZoneID = System.getProperty("user.timezone");
            if (sTimeZoneID.equals("CET")) {
                sTimeZoneID = "ECT";
            }
            TimeZone pTimeZone = TimeZone.getTimeZone(sTimeZoneID);

            SimpleDateFormat pSimpleDateFormat
                   = new SimpleDateFormat("EEE, MMM dd, yyyy  HH:mm:ss");//"yyyy.mm.dd e 'at' hh:mm:ss a z");
            pSimpleDateFormat.setTimeZone(pTimeZone);
            String sDate = pSimpleDateFormat.format(new Date()) + " " + pTimeZone.getID();

            StringWriter sw=new StringWriter();
            pJavancss_.printPackageNcss(sw);

            _txtPackage.setText(sDate + "\n\n" + sw.toString());
            
            sw=new StringWriter();
            pJavancss_.printObjectNcss(sw);
            
            _txtObject.setText(sDate + "\n\n" + sw.toString());

            sw=new StringWriter();
            pJavancss_.printFunctionNcss(sw);
            _txtFunction.setText(sDate + "\n\n" + sw.toString());

            if (pJavancss_.getLastErrorMessage() != null) {
                _txtError = new JTextArea();
                String sError = "Errors in Javancss:\n\n" +
                       pJavancss_.getLastErrorMessage();
                _txtError.setText(sError);
                JScrollPane jspError = new JScrollPane(_txtError);
                jspError.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) );
                getContentPane().add(jspError, BorderLayout.CENTER);
                _pTabbedPane.addTab("Errors", null, jspError);
            }

            pPanel.add(_pTabbedPane, BorderLayout.CENTER);
            getContentPane().add(pPanel, BorderLayout.CENTER);
        }

        validate();
        repaint();
    }

    @Override
    public void setVisible(boolean bVisible_) {
        if (bVisible_) {
            _oldThreadPriority = Thread.currentThread().getPriority();
            _pAnimationPanel.start();
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        } else {
            _pAnimationPanel.stop();
        }

        super.setVisible(bVisible_);
    }

    public void setSelectedTab(String sTab_) {
        if (sTab_ == null || sTab_.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        if (!_bNoError) {
            return;
        }
        if (sTab_.equals(S_METHODS)) {
            /*_pTabbedPane.setSelectedComponent(_txtFunction);*/
            _pTabbedPane.setSelectedIndex(2);
        } else if (sTab_.equals(S_CLASSES)) {
            /*_pTabbedPane.setSelectedComponent(_txtObject);*/
            _pTabbedPane.setSelectedIndex(1);
        } else {
            /*_pTabbedPane.setSelectedComponent(_txtPackage);*/
            _pTabbedPane.setSelectedIndex(0);
        }
    }
}
