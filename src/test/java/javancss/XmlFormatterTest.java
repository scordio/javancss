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

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

        String sText = getXML( sw.toString(), getXslFile( "xmltest.xsl" ) );
        assertTrue( sText, sText.equals( "79" ) );

        javancss = measureTestFile( 117 );
        javancss.setXML( true );

        javancss.printStart( sw );
        javancss.printPackageNcss( sw );
        javancss.printObjectNcss( sw );
        javancss.printFunctionNcss( sw );
        javancss.printJavaNcss( sw );
        javancss.printEnd( sw );

        assertFalse( StringUtils.isEmpty( sw.toString() ) );

        javancss = measureTestFile( 118 );
        javancss.setXML( true );

        javancss.printStart( sw );
        javancss.printPackageNcss( sw );
        javancss.printObjectNcss( sw );
        javancss.printFunctionNcss( sw );
        javancss.printJavaNcss( sw );
        javancss.printEnd( sw );

        assertFalse( StringUtils.isEmpty( sw.toString() ) );
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

        String sText = getXML( sw.toString(), getXslFile( "javancss2text.xsl" ) );
        sText = sText.replaceAll( "(?:\r\n|\n\r)", "\n" );
        String sCompare = FileUtils.readFileToString( getTestFile( "Output32.txt" ), "ISO-8859-1" );
        assertTrue( sText, sText.equals( sCompare ) );
    }

    private File getXslFile( String filename )
    {
        return new File( getTestDir(), "../../xslt/" + filename );
    }

    private String getXML( String xmlContent, File xsltFile ) throws IOException, TransformerException
    {
        String xsltContent = FileUtils.readFileToString( xsltFile, "ISO-8859-1" );

        StreamSource xmlSource = new StreamSource( new StringReader( xmlContent ) );
        StreamSource styleSource = new StreamSource( new StringReader( xsltContent ) );

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer( styleSource );

        ByteArrayOutputStream output = new ByteArrayOutputStream( 1024 );
        StreamResult result = new StreamResult( output );
        transformer.transform( xmlSource, result );

        return output.toString();
    }
}
