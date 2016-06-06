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

import java.io.File;
import java.io.StringWriter;

import ccl.util.FileUtil;
import ccl.util.Util;
import ccl.xml.XMLUtil;

/**
 * This test class checks that the xml output feature is
 * working properly.
 *
 * @author Chr. Clemens Lee
 * @version $Id$
 */
public class XmlFormatterTest extends AbstractTestCase
{
    /**
     * Is it at least possible to properly parse generated xml code?
     */
    public void testParsing() throws Exception
    {
        Javancss javancss = measureTestFile( 57 );
        javancss.setXML( true );

        StringWriter sw = new StringWriter();

        javancss.printStart( sw );
        javancss.printPackageNcss( sw );
        javancss.printObjectNcss( sw );
        javancss.printFunctionNcss( sw );
        javancss.printJavaNcss( sw );
        javancss.printEnd( sw );

        String sText = XMLUtil.getXML( sw.toString(), getXslFile( "xmltest.xsl" ) );
        assertTrue( sText, sText.equals( "79" ) );

        javancss = measureTestFile( 117 );
        javancss.setXML( true );

        javancss.printStart( sw );
        javancss.printPackageNcss( sw );
        javancss.printObjectNcss( sw );
        javancss.printFunctionNcss( sw );
        javancss.printJavaNcss( sw );
        javancss.printEnd( sw );

        assertFalse( Util.isEmpty( sw.toString() ) );

        javancss = measureTestFile( 118 );
        javancss.setXML( true );

        javancss.printStart( sw );
        javancss.printPackageNcss( sw );
        javancss.printObjectNcss( sw );
        javancss.printFunctionNcss( sw );
        javancss.printJavaNcss( sw );
        javancss.printEnd( sw );

        assertFalse( Util.isEmpty( sw.toString() ) );
    }

    /**
     * Is the transformed XML output identical to the standard ASCI output?
     */
    public void testXML2Text() throws Exception
    {
        Javancss javancss = measureTestFile( 32 );
        javancss.setXML( true );

        StringWriter sw = new StringWriter();

        javancss.printStart( sw );
        javancss.printPackageNcss( sw );
        javancss.printObjectNcss( sw );
        javancss.printFunctionNcss( sw );
        javancss.printJavaNcss( sw );
        javancss.printEnd( sw );

        String sText = XMLUtil.getXML( sw.toString(), getXslFile( "javancss2text.xsl" ) );
        sText = sText.replaceAll( "(?:\r\n|\n\r)", "\n" );
        String sCompare = FileUtil.readFile( getTestFile( "Output32.txt" ).getAbsolutePath() );
        assertTrue( sText, sText.equals( sCompare ) );
    }

    private File getXslFile( String filename )
    {
        return new File( getTestDir(), "../../xslt/" + filename );
    }
}
