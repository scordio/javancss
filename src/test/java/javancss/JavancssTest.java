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

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;

/**
 * Test class for the JavaNCSS application.
 *
 *   $Id$
 *   3. 9. 1996
 */
public class JavancssTest extends AbstractTestCase
{
    private Javancss measureWithArgs( String[] args ) throws IOException
    {
        // turn stdout off
        PrintStream psStdout = System.out;

        try
        {
            System.setOut( new PrintStream( new ByteArrayOutputStream() ) );
            return new Javancss( args );
        }
        finally
        {
            // turn stdout back on
            System.setOut( psStdout );
        }
    }

    public void testCummulating() throws IOException
    {
        // Nr. 35
        String sTogether;
        String sTest11 = "";
        String sTest12 = "";
        try
        {
            sTest11 = FileUtils.readFileToString( getTestFile( 11 ), "ISO-8859-1" );
            sTest12 = FileUtils.readFileToString( getTestFile( 12 ), "ISO-8859-1" );
        }
        catch ( IOException e )
        {
            fail();
        }
        sTogether = sTest11 + sTest12;
        Javancss javancss = new Javancss( new StringReader( sTogether ) );
        List<FunctionMetric> vFunctions = javancss.getFunctionMetrics();
        String sFirstFunction = vFunctions.get( 0 ).name;
        assertFalse( !sFirstFunction.equals( "ccl.util.Test11.atoi(String)" ) );
        String sSomeFunction = vFunctions.get( 32 ).name;
        assertFalse( "Function: " + sSomeFunction, !sSomeFunction.equals( "Test12.readFile(URL)" ) );
        List<PackageMetric> vPackages = javancss.getPackageMetrics();
        assertFalse( vPackages.size() != 2 );
        int ncss38 = javancss.getNcss();

        String[] asArg = new String[3];
        asArg[0] = getTestFile( 11 ).getAbsolutePath();
        asArg[1] = asArg[0];
        asArg[2] = getTestFile( 12 ).getAbsolutePath();
        javancss = measureWithArgs( asArg );
        vPackages = javancss.getPackageMetrics();
        assertFalse( vPackages.size() != 2 );
        assertFalse( ncss38 == javancss.getNcss() );
    }

    public void testEncoding() throws IOException
    {
        String[] args = new String[] { "-encoding", "UTF-16", getTestFile( "TestEncoding.java" ).getAbsolutePath() };
        Javancss javancss = measureWithArgs( args );

        assertEquals( "NCSS in TestEncoding.java", 11, javancss.getNcss() );
    }

    public void testVersion() throws IOException
    {
        String[] args = new String[] { "-version" };
        measureWithArgs( args );
    }

    public void testRecursive() throws IOException
    {
        String[] args = new String[] { "-recursive", getTestFile( "../../../lib" ).getAbsolutePath() };
        measureWithArgs( args );
    }
}
