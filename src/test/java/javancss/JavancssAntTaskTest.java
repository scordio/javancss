/*
Copyright (C) 2016 Emmanuel Bourg

This file is part of JavaNCSS
(http://www.kclee.de/clemens/java/javancss/).

JavaNCSS is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

JavaNCSS is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with JavaNCSS; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.  */

package javancss;

import junit.framework.TestCase;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Emmanuel Bourg
 */
public class JavancssAntTaskTest extends TestCase
{
    private Project project;

    protected void setUp() throws Exception
    {
        project = new Project();
        project.setCoreLoader( getClass().getClassLoader() );
        project.init();

        File buildFile = new File( "target/test-classes/testbuild.xml" );
        project.setBaseDir( buildFile.getParentFile() );

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        helper.parse( project, buildFile );

        redirectOutput( System.out );
    }

    /**
     * Redirects the Ant output to the specified stream.
     */
    private void redirectOutput( OutputStream out )
    {
        DefaultLogger logger = new DefaultLogger();
        logger.setOutputPrintStream( new PrintStream( out ) );
        logger.setMessageOutputLevel( Project.MSG_INFO );
        project.addBuildListener( logger );
    }

    public void testTask()
    {
        project.executeTarget( "run" );
    }
}
