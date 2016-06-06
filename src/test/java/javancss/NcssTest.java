/*
Copyright (C) 2000 Chr. Clemens Lee <clemens@kclee.com>.

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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class NcssTest extends AbstractTestCase
{
    private Javancss checkNcss( int testNumber, int expectedNcss )
    {
        Javancss javancss = measureTestFile( testNumber );
        assertEquals( "NCSS in Test" + testNumber + ".java", expectedNcss, javancss.getNcss() );
        return javancss;
    }

    private void checkNcss( int testNumber )
    {
        Javancss javancss = measureTestFile( testNumber );
        assertFalse( "NCSS in Test" + testNumber + ".java", javancss.getNcss() == 0 );
    }

    private void checkNcssAndLoc( int testNumber, int expectedNcss, int expectedLoc )
    {
        Javancss javancss = checkNcss( testNumber, expectedNcss );
        assertEquals( "LOC in Test" + testNumber + ".java", expectedLoc, javancss.getLOC() );
    }

    private void checkNcssAndLoc( int testNumber, int expectedNcssAndLoc )
    {
        checkNcssAndLoc( testNumber, expectedNcssAndLoc, expectedNcssAndLoc );
    }

    private Javancss checkNcssAndLoc( int testNumber )
    {
        Javancss javancss = measureTestFile( testNumber );
        int ncss = javancss.getNcss();
        int loc = javancss.getLOC();
        assertTrue( "NCSS != LOC in Test" + testNumber + ".java. NCSS is " + ncss + ", LOC is " + loc, ncss == loc );
        return javancss;
    }

    public void testNcss()
    {
        checkNcss( 2, 8 );
        checkNcss( 3, 69 );
        checkNcss( 4, 11 );
        checkNcss( 5, 16 );
        checkNcss( 7, 30 );
        checkNcss( 8, 30 );

        checkNcssAndLoc( 13 );
        checkNcssAndLoc( 14 );
        checkNcssAndLoc( 15 );

        checkNcss( 16, 4 );
        checkNcssAndLoc( 17 );
        checkNcssAndLoc( 18 );

        checkNcss( 20, 46 );
        checkNcss( 21, 67 );
        checkNcss( 22, 283 );
        checkNcss( 26, 47 );
        checkNcss( 27, 4 );
        checkNcss( 28, 465 );
        checkNcss( 29, 1 );
        checkNcss( 30, 3 );

        // ;; in java.sql.Connection
        try
        {
            checkNcss( 32, 26 );
        }
        catch ( Error eJavaSQLConnection )
        {
            fail( "java.sql.Connection double semicolon" );
        }
        // missing lf in last line/<EOF> not in single line
        try
        {
            checkNcss( 35, 1 );
        }
        catch ( Exception eEOF )
        {
            fail( "}<EOF>" );
        }
        try
        {
            checkNcss( 36, 1 );
        }
        catch ( Error eEOF )
        {
            fail( "//<EOF>" );
        }
        try
        {
            checkNcss( 37, 1 );
        }
        catch ( Error eCTRLZ )
        {
            fail( "//ctrl-Z" );
        }
        try
        {
            checkNcss( 38, 1 );
        }
        catch ( Error eCTRLZ )
        {
            fail( "0x0actrl-Z" );
        }
        // semicolons not allowed by JLS, but not counted anyway.
        try
        {
            checkNcss( 39, 5 );
        }
        catch ( Error eEmptyStatements )
        {
            fail( "Empty statments." );
        }

        // javancss parsed a file which it shouldn't
        Javancss javancss = measureTestFile( 42 );
        assertNotNull( "Test42 should be parsed *and* result in an exception.", javancss.getLastErrorMessage() );

        // file containing just ;
        checkNcss( 43, 0 );

        // Test if javancss continues after running across a parse error
        // Test42,java has an error, so use two other file and this and
        // take a look if it finishes with right result.
        javancss = measureTestFile( 1 );
        int ncss57 = javancss.getNcss();
        javancss = measureTestFile( 2 );
        ncss57 += javancss.getNcss();
        List<File> files = new ArrayList<File>();
        files.add( getTestFile( 1 ) );
        files.add( getTestFile( 42 ) );
        files.add( getTestFile( 2 ) );
        javancss = new Javancss( files );
        assertFalse( "ncss57: " + ncss57 + " javancss.getNcss(): " + javancss.getNcss(), javancss.getNcss() != ncss57 );

        checkNcss( 49, 3 );
        checkNcss( 51, 8 );
        checkNcss( 52, 12 );
        checkNcss( 53, 4 );
        checkNcss( 54, 9 );
        checkNcss( 55, 5 );
        checkNcss( 56 );
        checkNcss( 57 );
        checkNcss( 58, 37 );
        checkNcss( 59, 122 );
        checkNcss( 60, 35 );
        checkNcss( 61, 203 );
        checkNcss( 62, 616 );
        checkNcss( 63, 330 );
        checkNcss( 64, 70 );
        checkNcss( 65, 301 );
        checkNcss( 66, 3 );
        checkNcss( 67, 31 );

        // more comment counting
        checkNcss( 68, 3 );

        // zero methods one class javadoc comment, there should be no exception
        // because of divide by zero
        checkNcss( 69, 1 );

        /*
         * This method tries to reproduce a bug reported by
         * Chris Williamson. He reported problems with code
         * like this: F150MemoryMap f150Map = (F150MemoryMap) F150.super.memMap;
         */
        checkNcss( 70, 4 );

        // test for strictfp interface and static inner interface
        checkNcss( 73, 1 );
        checkNcss( 74, 2 );

        //
        // new Java 1.5 features
        //

        // @Deprecated annotation
        checkNcss( 75, 584 );
        // Class<?>
        checkNcss( 76, 404 );
        // Class<?,?>
        checkNcss( 77, 48 );
        // WeakHashMap<ImageInputStream, Object>
        checkNcss( 78, 35 );
        // Map<String, Map<String, Object>>
        checkNcss( 79, 1345 );
        // @Deprecated protected KeyStroke closeMenuKey;
        checkNcss( 80, 96 );
        // etc.
        checkNcss( 81, 92 );
        checkNcss( 82, 26 );
        checkNcss( 83, 2 );
        checkNcss( 84, 55 );
        checkNcss( 85, 242 );
        checkNcss( 86, 22 );
        checkNcss( 87, 8 );
        checkNcss( 88, 11 );
        checkNcss( 89, 65 );
        checkNcss( 90, 494 );
        checkNcss( 91, 30 );
        checkNcss( 92, 6 );
        checkNcss( 93, 38 );
        checkNcss( 94, 3 );
        checkNcss( 95, 10 );
        checkNcss( 96, 3 );
        checkNcss( 97, 3 );
        checkNcss( 98, 37 );
        checkNcss( 99, 243 );
        checkNcss( 100, 5 );
        checkNcss( 101, 256 );
        checkNcss( 102, 10 );
        checkNcss( 103, 3 );
        checkNcss( 104, 3 );
        checkNcss( 105, 5 );
        checkNcss( 106, 10 );
        checkNcss( 107, 9 );
        checkNcss( 108, 2 );
        checkNcss( 109, 2 );
        checkNcss( 110, 1 );
        checkNcss( 111, 4 );
        checkNcss( 112, 3 );
        checkNcss( 113, 13 );
        checkNcss( 114, 3 );
        checkNcss( 115, 11663 );
        checkNcss( 116, 12 );
        checkNcss( 117, 15 );
        checkNcss( 119, 2 );
        checkNcss( 120, 3 );
        checkNcss( 121, 5 );
        checkNcss( 123, 4 );
        checkNcss( 124, 7 );
        checkNcss( 125, 2 );
        checkNcss( 126, 13 );
        checkNcss( 127, 3 );
        checkNcss( 128, 3 );
        checkNcss( 129, 6 );
        checkNcss( 130, 5 );
        checkNcss( 131, 6 );
        checkNcss( 132, 12 );
        checkNcss( 134, 4 );
        checkNcss( 136, 2 );
        checkNcss( 138, 3 );
    }

    public void testNcssAndMore() throws IOException
    {
        final int ncss1 = 318;
        checkNcss( 1, ncss1 );

        final int ncss6 = 565;
        checkNcssAndLoc( 6, ncss6, 1254 );

        // Nr. 10
        Javancss javancss = measureTestFile( 9 );
        assertFalse( "LOC: " + javancss.getLOC(), ncss1 != javancss.getLOC() );

        checkNcssAndLoc( 10, ncss6 );
        checkNcssAndLoc( 11 );

        javancss = checkNcssAndLoc( 12 );
        List<FunctionMetric> vFunctions = javancss.getFunctionMetrics();
        String sFirstFunction = vFunctions.get( 0 ).name;
        assertNotNull( sFirstFunction );
        /* System.out.println( sFirstFunction ); */
        assertFalse( sFirstFunction, !sFirstFunction.equals( "Test12.readFile(URL)" ) );

        // Nr. 22
        javancss = measureTestFile( 19 );
        vFunctions = javancss.getFunctionMetrics();
        sFirstFunction = vFunctions.get( 0 ).name;
        assertFalse( sFirstFunction, !sFirstFunction.equals( "test.Test19.foo(String[],Controller)" ) );
        sFirstFunction = vFunctions.get( 3 ).name;
        assertFalse( !sFirstFunction.equals( "test.Test19.main(String[])" ) );

        javancss = checkNcss( 23, 10 );
        vFunctions = javancss.getFunctionMetrics();
        assertFalse( vFunctions.size() != 7 );
        assertFalse( measureTestFile( 24 ).getFunctionMetrics().size() != vFunctions.size() );

        // Nr. 30
        javancss = checkNcss( 25, 12 );
        assertFalse( javancss.getFunctionMetrics().size() != 9 );

        javancss = measureTestFile( 56 );
        StringWriter sw = new StringWriter();
        javancss.printPackageNcss( sw );
        sw.write( "\n" );
        javancss.printObjectNcss( sw );
        sw.write( "\n" );
        javancss.printFunctionNcss( sw );

        String sOutput56 = sw.toString().replaceAll( "\r\n", "\n" );
        String sCompare56 = FileUtils.readFileToString( getTestFile( "Output56.txt" ), "ISO-8859-1" );
        assertEquals( "File test/Output56.txt and javancss output differs", sOutput56, sCompare56 );

        // check that javadocs are counted correctly
        // after patches for additional comment counting
        javancss = measureTestFile( 32 );
        sw = new StringWriter();
        javancss.printPackageNcss( sw );
        sw.write( "\n" );
        javancss.printObjectNcss( sw );
        sw.write( "\n" );
        javancss.printFunctionNcss( sw );

        String sOutput32 = sw.toString().replaceAll( "\r\n", "\n" );
        String sCompare32 = FileUtils.readFileToString( getTestFile( "Output32.txt" ), "ISO-8859-1" );
        assertEquals( "File test/Output32.txt and javancss output differs", sOutput32, sCompare32 );
    }
}
